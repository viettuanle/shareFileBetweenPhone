package npu.s2p.activity;

import java.util.ArrayList;

import npu.database.SQLiteAdapter;
import npu.database.SingleData;
import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Button;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;


public class View_Business_contact extends Activity {
	private SQLiteAdapter mySQLiteAdapter;
	TextView txtViewBusinessName,txtViewBusinessTitle, txtViewBusinessCompany,txtViewBusinessPhone, 
	txtViewBusinessEmail, txtViewBusinessWebsite, txtViewBusinessSlogan, txtViewBusinessAddress;
	Button btnSave, btnCancel;
	private SingleData single = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_business_contact);
		mySQLiteAdapter = new SQLiteAdapter(this);
		
		txtViewBusinessName = (TextView) findViewById(R.id.txtView_Business_Name);
		txtViewBusinessTitle = (TextView)findViewById(R.id.txtView_Business_Title);
		txtViewBusinessCompany= (TextView)findViewById(R.id.txtView_Business_Company);
		txtViewBusinessPhone= (TextView)findViewById(R.id.txtView_Business_Phone);
		txtViewBusinessEmail= (TextView)findViewById(R.id.txtView_Business_Email);
		txtViewBusinessWebsite= (TextView)findViewById(R.id.txtView_Business_Website);
		txtViewBusinessSlogan = (TextView)findViewById(R.id.txtView_Business_Sologan);
		txtViewBusinessAddress= (TextView)findViewById(R.id.txtView_Business_Address);
		btnSave = (Button) findViewById(R.id.btnView_Business_Save);
		btnCancel = (Button) findViewById(R.id.btnView_Business_Cancel);
		
		// Read the values which class View_contact.java passed to this Child
				// Activity (View_Business_contact.java)
				String row_id = getIntent().getStringExtra(
						"view_business_contact_base_on_row_id");
				// fill in data_row into some edit_text
	
				mySQLiteAdapter.openToRead();
				Cursor cursor = mySQLiteAdapter.queryRowId(Long.parseLong(row_id));
				startManagingCursor(cursor);
				cursor.moveToFirst();
				if (!cursor.isAfterLast()) // if has same phone # then update
				{
					single = new SingleData(cursor);

					txtViewBusinessName.setText(single.getGivenname().toString()+ " " + single.getMiddlename().toString()+ " "+ single.getFamilyname().toString());
					txtViewBusinessTitle.setText(single.getTypes().toString());
					txtViewBusinessPhone.setText(single.getPhone().toString());
					txtViewBusinessEmail.setText(single.getEmail().toString());
					txtViewBusinessCompany.setText(single.getOrg1().toString());
					txtViewBusinessWebsite.setText(single.getOrg2().toString());
					txtViewBusinessSlogan.setText(single.getSpinorg1().toString()); //slogan
					txtViewBusinessAddress.setText(single.getPobox().toString());

					

				}
				cursor.close();// let java know that you are through with the cursor.
				
				//manage two buttons
				 btnSave.setOnClickListener(new OnClickListener(){
			        	@Override
			      	   public void onClick(View v) {
			      		     // import to Android database
			        		
			        		 String str_contact_id = checkPhoneexist(single.getPhone());
		            		 if(str_contact_id.equals("notfound")){
		            			 //insert into android contact
		            			 createContact(single);
		            		 }else{
		            			 //update
		            			 deleteContact(str_contact_id);
		            			 createContact(single);
		            		 }
			      		     finish();
			      	   }
			      	  });// end btnSave listener
				 
				 btnCancel.setOnClickListener(new OnClickListener(){
			        	@Override
			      	   public void onClick(View v) {			      		 
			      		     finish();
			      	   }
			      	  });// end btnCancel listener

	}//end onCreate
	
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
          .withValue(ContactsContract.CommonDataKinds.Organization.DEPARTMENT, singledata.getSpinorg1())
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

}//end class View_Business_contact
