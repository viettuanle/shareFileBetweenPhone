package npu.s2p.activity;

//import static npu.database.Constant.KEY_ROWID;
//import static npu.database.Constant.KEY_GIVENNAME;
//import static npu.database.Constant.KEY_PHONE;
//import static npu.database.Constant.KEY_EMAIL;


import java.util.ArrayList;



import android.app.Activity;
import android.app.Dialog;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.RawContacts;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;

import npu.database.SQLiteAdapter;
import npu.database.SingleData;
import npu.support.adapter.listviewAdapter;

/**
 * 
 * @author Le Tuan Use: The main purpose of class View_contact is load and
 *         display basic information in database to listview. This class also
 *         handle temperature data when the peer receive (compare old then
 *         update or insert new data)
 */
public class View_contact extends Activity implements OnClickListener{
	private final int MANUAL_EDIT_CONTACT_ACTIVITY_CODE = 3;
//	private final int VIEW_BUSINESS_CARD_CONTACT_ACTIVITY_CODE = 4;
	private Button btnImportViewContact, btnCancelViewContact;
	 
	listviewAdapter adapter;
	ListView ltvDisplay;
	private SQLiteAdapter mySQLiteAdapter;
	private ArrayList<SingleData> list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_contact);
		mySQLiteAdapter = new SQLiteAdapter(this);

		btnImportViewContact = (Button) findViewById(R.id.btnImportViewContact);
		btnCancelViewContact = (Button) findViewById(R.id.btnCancelViewContact);
		btnImportViewContact.setOnClickListener(this);
		btnCancelViewContact.setOnClickListener(this);
		// handle listview
		ltvDisplay = (ListView) findViewById(R.id.ltvViewContact);
		// if datalist is not empty then update it to database, at version 2:
		// need to auto
		// update right after receive new message.
		if (!PeerActivity.peer.CheckDataListEmpty()) {
			UpdateDatabase();
		} else {
			Toast.makeText(this, "There is no update contact!",
					Toast.LENGTH_LONG).show();
		}

		loadtolistview();
		displayList();
		
		// create contextmenu for listview
		registerForContextMenu(ltvDisplay);
		

	} // end onCreate
	 public void onClick(View v){
	        if(v instanceof Button){
	             switch(v.getId()){
	             case R.id.btnImportViewContact:{
	            	 // if new contact --> insert, if old contact --> update
	            	 // come back previous activity (finish)
	            	 for(int i=0;i<list.size();i++){
	            		 String str_contact_id = checkPhoneexist(list.get(i).getPhone());
	            		 if(str_contact_id.equals("notfound")){
	            			 //insert into android contact
	            			 createContact(list.get(i));
	            		 }else{
	            			 //update
	            			 deleteContact(str_contact_id);
	            			 createContact(list.get(i));
	            		 }
	            	 }//end for
	            	 finish();
	                  break;
	             }
	             case R.id.btnCancelViewContact:{
	            	 // come back previous activity (finish)
	            	 finish();
	                  break;
	             }
	             }
	        }//end if
	 }// end onClick
	private void displayList() {
		this.adapter = new listviewAdapter(this,
				android.R.layout.simple_list_item_1, list);
		refreshListView();// refresh listview
	}// end displayList
	
	public void refreshListView() {// before call this method, should update
		// content of list
				adapter.notifyDataSetChanged();// refresh listview
				ltvDisplay.setAdapter(adapter);
				ltvDisplay.invalidateViews();
}
	
	/**
	 * check new phone number, if it is not in database --> insert, otherwise
	 * update, + data_filde is empty --> copy + data_field is not empty
	 * (conflict content): Alert Dialog to let user make decision. then empty
	 * datalist before ending
	 */
	private void UpdateDatabase() {
		//
		int same = 0;
		int newdata = 0;
		// check new phone number
		ArrayList<SingleData> newdatalist = new ArrayList<SingleData>();
		newdatalist.addAll(PeerActivity.peer.getDataList());

		int c = PeerActivity.peer.getsizedata();

		mySQLiteAdapter.openToWrite();
//		ArrayList<String> column_name = mySQLiteAdapter.getColumns(SQLiteAdapter.DATABASE_TABLE);
		for (int i = 0; i < c; i++) {// traversal to each row, check

			Cursor cursor = mySQLiteAdapter.checkphone(newdatalist.get(i)
					.getPhone());
			startManagingCursor(cursor);
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) // if has same phone # then update
			{
//				String temp_time = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAdapter.KEY_TIME));
				SingleData oldSingleData = new SingleData(cursor);
				SingleData tempNewSingleData = new SingleData(
						newdatalist.get(i));
				
				if(oldSingleData.getTime().equals(tempNewSingleData.getTime())){
					same++; // count number similar phone number update
				}else{						
					
					do {
					// check each field here(except phone number, if data
					// conflict then pop up dialog, default get new data
//						Toast.makeText(getBaseContext(), "conflic data", Toast.LENGTH_SHORT).show();
						show_userchoose_custom_dialog(oldSingleData,tempNewSingleData);
						//do I need update timestamp here? --> Yes, mean anytime A-->B, then A-->B again, ==>pop up. (B-->A, not pop up)
					// No, mean any time A-->B, then A-->B again, not pop up if B not manual change data (B--> A, also pop up)
//					
					} while (cursor.moveToNext());
				}
			} else {
				newdata++;
				// insert if new data
				mySQLiteAdapter.insert(newdatalist.get(i));
			}// end else
			cursor.close();// let java know that you are through with the
							// cursor.

		}// end for

		// empty datalist
		mySQLiteAdapter.close();
		PeerActivity.peer.ClearDataList();
		Toast.makeText(this,
				"You have: " + same + " update and " + newdata + " new data",
				Toast.LENGTH_LONG).show();

	}// end UpdateDatabase

	/**
	 * Load database into listview
	 */
	public void show_userchoose_custom_dialog(final SingleData olddata,final SingleData newdata) {
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.user_choose_data_dialog);
		dialog.setTitle("Data Conflict !");
		dialog.setCancelable(false);
		
		
		
		RadioGroup rdg_lastname  = (RadioGroup)dialog.findViewById(R.id.rdg_lastname_UserChooseData);
		final RadioButton rdb_lastnameold= (RadioButton)dialog.findViewById(R.id.rdb_lastnameold_UserChooseData);
		final RadioButton rdb_middlenameold= (RadioButton)dialog.findViewById(R.id.rdb_middlenameold_UserChooseData);
		final RadioButton rdb_firstnameold= (RadioButton)dialog.findViewById(R.id.rdb_firstnameold_UserChooseData);
		final RadioButton rdb_genderold= (RadioButton)dialog.findViewById(R.id.rdb_genderold_UserChooseData);
		final RadioButton rdb_phoneold= (RadioButton)dialog.findViewById(R.id.rdb_phoneold_UserChooseData);
		final RadioButton rdb_emailold= (RadioButton)dialog.findViewById(R.id.rdb_emailold_UserChooseData);
		final RadioButton rdb_poboxold= (RadioButton)dialog.findViewById(R.id.rdb_postalold_UserChooseData);
		
		final TextView txt_firstname= (TextView)dialog.findViewById(R.id.txt_firstname_UserChooseData);
		final TextView txt_middlename= (TextView)dialog.findViewById(R.id.txt_middlename_UserChooseData);
		final TextView txt_lastname= (TextView)dialog.findViewById(R.id.txt_lasttname_UserChooseData);
		final TextView txt_gender= (TextView)dialog.findViewById(R.id.txt_gender_UserChooseData);
		final TextView txt_phone= (TextView)dialog.findViewById(R.id.txt_phonenumber_UserChooseData);
		final TextView txt_email= (TextView)dialog.findViewById(R.id.txt_email_UserChooseData);
		final TextView txt_pobox= (TextView)dialog.findViewById(R.id.txt_postal_UserChooseData);
		
		
		if (!olddata.getFamilyname().equals(newdata.getFamilyname())){

			rdg_lastname.setVisibility(View.VISIBLE);
			txt_lastname.setVisibility(View.VISIBLE);

			rdg_lastname.check(R.id.rdb_lastnamenew_UserChooseData);//default choose new data

			rdb_lastnameold.setText(olddata.getFamilyname());			
			final RadioButton rdb_lastnamenew  = (RadioButton)dialog.findViewById(R.id.rdb_lastnamenew_UserChooseData);
			rdb_lastnamenew.setText(newdata.getFamilyname());

		}
		if (!olddata.getMiddlename().equals(newdata.getMiddlename())){
			final RadioGroup rdg_middlename = (RadioGroup)dialog.findViewById(R.id.rdg_middlename_UserChooseData);
			rdg_middlename.setVisibility(View.VISIBLE);
			txt_middlename.setVisibility(View.VISIBLE);
			rdg_middlename.check(R.id.rdb_middlenamenew_UserChooseData);//default choose new data

			rdb_middlenameold.setText(olddata.getMiddlename());
			
			final RadioButton rdb_middlenamenew  = (RadioButton)dialog.findViewById(R.id.rdb_middlenamenew_UserChooseData);
			rdb_middlenamenew.setText(newdata.getMiddlename());
		}
		if (!olddata.getGivenname().equals(newdata.getGivenname())){
			final RadioGroup rdg_firstname = (RadioGroup)dialog.findViewById(R.id.rdg_firstname_UserChooseData);
			rdg_firstname.setVisibility(View.VISIBLE);
			txt_firstname.setVisibility(View.VISIBLE);
			rdg_firstname.check(R.id.rdb_firstnamenew_UserChooseData);//default choose new data

			rdb_firstnameold.setText(olddata.getGivenname());
			
			final RadioButton rdb_firstnamenew  = (RadioButton)dialog.findViewById(R.id.rdb_firstnamenew_UserChooseData);
			rdb_firstnamenew.setText(newdata.getGivenname());
		}
		if (!olddata.getGender().equals(newdata.getGender())){
			final RadioGroup rdg_gender = (RadioGroup)dialog.findViewById(R.id.rdg_gender_UserChooseData);
			rdg_gender.setVisibility(View.VISIBLE);
			txt_gender.setVisibility(View.VISIBLE);
			rdg_gender.check(R.id.rdb_gendernew_UserChooseData);//default choose new data
		
			rdb_genderold.setText(olddata.getGender());
			
			final RadioButton rdb_gendernew  = (RadioButton)dialog.findViewById(R.id.rdb_gendernew_UserChooseData);
			rdb_gendernew.setText(newdata.getGender());
		}
		if (!olddata.getPhone().equals(newdata.getPhone())){
			final RadioGroup rdg_phone = (RadioGroup)dialog.findViewById(R.id.rdg_phone_UserChooseData);
			rdg_phone.setVisibility(View.VISIBLE);
			txt_phone.setVisibility(View.VISIBLE);
			rdg_phone.check(R.id.rdb_phonenew_UserChooseData);//default choose new data

			rdb_phoneold.setText(olddata.getPhone());
			
			final RadioButton rdb_phonenew  = (RadioButton)dialog.findViewById(R.id.rdb_phonenew_UserChooseData);	
			rdb_phonenew.setText(newdata.getPhone());
		}
		if (!olddata.getEmail().equals(newdata.getEmail())){
			final RadioGroup rdg_email = (RadioGroup)dialog.findViewById(R.id.rdg_email_UserChooseData);
			rdg_email.setVisibility(View.VISIBLE);
			txt_email.setVisibility(View.VISIBLE);
			rdg_email.check(R.id.rdb_emailnew_UserChooseData);//default choose new data

			rdb_emailold.setText(olddata.getEmail());
			
			final RadioButton rdb_emailnew  = (RadioButton)dialog.findViewById(R.id.rdb_emailnew_UserChooseData);
			rdb_emailnew.setText(newdata.getEmail());
		}
		if (!olddata.getPobox().equals(newdata.getPobox())){
			final RadioGroup rdg_pobox = (RadioGroup)dialog.findViewById(R.id.rdg_postal_UserChooseData);
			rdg_pobox.setVisibility(View.VISIBLE);
			txt_pobox.setVisibility(View.VISIBLE);
			rdg_pobox.check(R.id.rdb_postalnew_UserChooseData);//default choose new data

			rdb_poboxold.setText(olddata.getPobox());
			
			final RadioButton rdb_poboxnew  = (RadioButton)dialog.findViewById(R.id.rdb_postalnew_UserChooseData);
			rdb_poboxnew.setText(newdata.getPobox());
		}
		

		Button btnOK_NewOld = (Button) dialog
				.findViewById(R.id.btnOK_Update_UserChooseData);
		btnOK_NewOld.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				// check samename, check exist name, otherwise rename
//				if(rdg_lastname.getCheckedRadioButtonId()==rdb_lastnameold.getId()){}
				if (rdb_lastnameold.isChecked()) {
					newdata.setFamilyname(olddata.getFamilyname());
					} 
				if (rdb_middlenameold.isChecked()) {
					newdata.setMiddlename(olddata.getMiddlename());
					}
				if (rdb_firstnameold.isChecked()) {
					newdata.setGivenname(olddata.getGivenname());
					}				
				if (rdb_genderold.isChecked()) {
					newdata.setGender(olddata.getGender());
					}
				if (rdb_phoneold.isChecked()) {
					newdata.setPhone(olddata.getPhone());
					}
				if (rdb_emailold.isChecked()) {
					newdata.setEmail(olddata.getEmail());
					}
				if (rdb_poboxold.isChecked()) {
					newdata.setPobox(olddata.getPobox());
					}
				mySQLiteAdapter.openToWrite();
				mySQLiteAdapter.updatephonenumber(newdata);
				mySQLiteAdapter.close();
//				Toast.makeText(getBaseContext(), tempdata.getFamilyname(), Toast.LENGTH_LONG).show();
				// Display new data update
				loadtolistview();
				displayList();
				
				dialog.dismiss();
			}
		});

		Button btnCancel_Update = (Button) dialog
				.findViewById(R.id.btnCancel_Update_UserChooseData);
		btnCancel_Update.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				dialog.dismiss();
			}
		});

		dialog.show();
		
	}
	
	
	public void createContact(SingleData singledata) {
    	ContentResolver cr = getContentResolver();
    	
    	Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        
    	    	
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = ops.size();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
        		 .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                 .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                 .build());
        //name
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, singledata.getGivenname()+" "+singledata.getFamilyname())
                .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, singledata.getFamilyname())
                .withValue(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME, singledata.getMiddlename())
                .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, singledata.getGivenname())
                .build());
        //phone
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, singledata.getPhone())                           
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, singledata.getSpinphone())           
                .build());
        //email
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
        	    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
        	    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
        	    .withValue(ContactsContract.CommonDataKinds.Email.DATA, singledata.getEmail())
        	    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
        	    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, singledata.getSpinemail())
        	    .build());

      //Postal Address        
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
          .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
          .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
          .withValue(ContactsContract.CommonDataKinds.StructuredPostal.POBOX, singledata.getPobox())
          
          .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
          .withValue(ContactsContract.CommonDataKinds.StructuredPostal.STREET, singledata.getStreet())
          
          .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
          .withValue(ContactsContract.CommonDataKinds.StructuredPostal.CITY, singledata.getCity())
          
          .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
          .withValue(ContactsContract.CommonDataKinds.StructuredPostal.REGION, singledata.getState() )
          
         .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
         .withValue(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE, singledata.getZipcode())
          
          .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
          .withValue(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY, singledata.getCountry())
          
          .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
          .withValue(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, singledata.getSpinaddr())          
          
          .build());
        
        //IM details
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
          .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
          .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
          .withValue(ContactsContract.CommonDataKinds.Im.DATA, singledata.getIm())
          .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE )
          .withValue(ContactsContract.CommonDataKinds.Im.DATA5, singledata.getSpinim())
           .build());
        //Organization details
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
          .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
          .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE )
          .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, singledata.getOrg1())
          .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE )
          .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, singledata.getTypes())
          .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE )
          .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, singledata.getSpinorg1())
           .build());
        
        try {
			cr.applyBatch(ContactsContract.AUTHORITY, ops);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OperationApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        	
    }
	public void deleteContact(String contact_id) {

    	ContentResolver cr = getContentResolver();
    	String where = ContactsContract.Data.CONTACT_ID + " = ? ";
    	String[] params = new String[] {contact_id};
    
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI)
    	        .withSelection(where, params)
    	        .build());
        try {
			cr.applyBatch(ContactsContract.AUTHORITY, ops);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OperationApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	  
    }
	
	/*
	 * 	 if phone number exist then return CONTACT_ID otherwise return "notfound"
	 */
	public String checkPhoneexist(String number) {
	    ContentResolver cr = getContentResolver();

	    String [] projection = new String []{
	        ContactsContract.CommonDataKinds.Phone._ID,
	        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
	        ContactsContract.CommonDataKinds.Phone.NUMBER,
	        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
	    };
	   
	    String selection = ContactsContract.CommonDataKinds.Phone.NUMBER + " LIKE ? ";;
	    String[] selectionArgs = new String[]{"%"+number+ "%"};

	    Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
	        projection,
	        selection,
	        selectionArgs,
	        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

	   if(cursor.isAfterLast()){
		   return "notfound";
		  
	   }else{
			cursor.moveToFirst();
			String str_return;
			
				do {
					str_return = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
					
				} while (cursor.moveToNext());

			cursor.close();
		   return 	str_return;	 
	   }
	    
	}

	
	//////////////////////////////////
	/**
	 * Copy database into ArrayList<SingleData>, similar method fullDatalist in class Send 
	 */
	private void loadtolistview() {
		list = new ArrayList<SingleData>();
		
		mySQLiteAdapter.openToRead();
//		Cursor cursor = mySQLiteAdapter.queueAll(); //ok, but not sort
		Cursor cursor = mySQLiteAdapter.queueAll_SortBy_GivenName();
		startManagingCursor(cursor);
		cursor.moveToFirst();
								
		if (!cursor.isAfterLast()) {
			do {
				 SingleData single = new SingleData(cursor);
				 				
				list.add(single);

			} while (cursor.moveToNext());

		}

		// let java know that you are through with the cursor.
		cursor.close();
		mySQLiteAdapter.close();

	}// end loadtolistview()

	// create context menu
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		if (v.getId() == R.id.ltvViewContact) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

			// menu.setHeaderTitle("Edit contact  ~ name of current selected");
			menu.setHeaderTitle("Edit contact "
					+ (String) list.get(info.position).getFamilyname() + " "
					+ (String) list.get(info.position).getMiddlename() + " "
					+ (String) list.get(info.position).getGivenname());
																			
			menu.add(0, v.getId(), 0, "Edit");
			menu.add(0, v.getId(), 0, "View Business Card");
			menu.add(0, v.getId(), 0, "Delete");
			menu.add(0, v.getId(), 0, "Cancel");

		}

	}

	// handle action with each select on context menu
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
	
		if (item.getTitle() == "Edit") {
			EditItem(list.get(info.position).getRowid());

		}else if (item.getTitle() == "View Business Card") {
			ViewBusinessItem(list.get(info.position).getRowid());

		} else if (item.getTitle() == "Delete") {
			DeleteItem(list.get(info.position).getGivenname()+" "+
					list.get(info.position).getMiddlename() + " "+ 
					list.get(info.position).getFamilyname()
					,
					list.get(info.position).getRowid());
		} else {
			return false;
		}
		return true;
	} // end onContextItemSelected

	public void ViewBusinessItem(String id){
		Intent intent = new Intent(getBaseContext(), View_Business_contact.class);
		intent.putExtra("view_business_contact_base_on_row_id", id);
		startActivity(intent);

	}
	public void DeleteItem(String str, String id) {
		Toast.makeText(this, "You  Delete " + str + " " + id, Toast.LENGTH_LONG)
				.show();
		mySQLiteAdapter.openToRead();
		mySQLiteAdapter.deleteRow(Long.parseLong(id));
		mySQLiteAdapter.close();

		// update listview
		loadtolistview(); // store date into list
		displayList();
	}

	/*
	 * @ allow edit information of current name selected process: call class for
	 * handle edit (callForResult), save data after click Done.
	 */
	public void EditItem(String id) {
		// transfer row_id of selected
		Intent intent = new Intent(getBaseContext(), Manual_edit_contact.class);
		intent.putExtra("edit_contact_base_on_row_id", id);
		startActivity(intent);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mySQLiteAdapter != null)
			mySQLiteAdapter.close();
	}

	/**
	 * This method is used because we call ActivityForResult at the
	 * Manual_edit_contact just show update success or not
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MANUAL_EDIT_CONTACT_ACTIVITY_CODE
				&& resultCode == RESULT_OK) {
			// Display new data update
			loadtolistview();
			displayList();
			Toast.makeText(View_contact.this, "Update successed! ",
					Toast.LENGTH_LONG).show();
		}
		if (requestCode == MANUAL_EDIT_CONTACT_ACTIVITY_CODE
				&& resultCode == RESULT_CANCELED) {
			// Display total peers has ask request update
			Toast.makeText(View_contact.this, "Don't update! ",
					Toast.LENGTH_LONG).show();
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
}// end class

