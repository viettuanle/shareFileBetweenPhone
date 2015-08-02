package npu.s2p.activity;

import java.util.ArrayList;
import java.util.Arrays; 

import npu.support.utility.Validate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
//import android.content.Intent;
//import android.database.Cursor;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.CheckBox;
import android.widget.CompoundButton;
//import android.widget.TextView;
//import android.widget.TextView;
import npu.s2p.activity.Send;
import android.widget.Toast;
//import android.widget.RadioButton;
import npu.database.*;


/**
 * 
 * @author Le Tuan 
 * This class allows add new contact information manually into database.
 * User can add phone type and email type as default list, and user also add custom type too.
 * Each contact belongs to one group (default friend).
 * This activity also help validate email, phone 
 */

public class Manual_add_contact extends Activity implements OnClickListener,OnFocusChangeListener,
		RadioGroup.OnCheckedChangeListener {

	private SQLiteAdapter mySQLiteAdapter;

	Button btnAddContactDone, btnAddContactRevert;
	EditText edtAddContactFirstName, edtAddContactLastName,
			edtAddContactMiddleName, edtAddContactCompanyName, edtAddContactCompanyWebsite,edtAddContactCompanySlogan, edtAddContactPhoneNumber,
			edtAddContactEmailAddress, edtAddContactPostalAddress;
	Spinner spnAddContactGroup, spnAddContactPhoneNumber, spnAddContactEmailAddress;
	RadioGroup male_female;
	CheckBox chbAddContactYourCard;
	private String ismale = "male";
	private String isyourcard = "no";
	private ArrayList<String> array_list_group ;
	private ArrayList<String> array_list_phone ;
	private ArrayList<String> array_list_email ;
//	private String spinner_phone_selected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.manual_add_contact);
		mySQLiteAdapter = new SQLiteAdapter(this);
		mySQLiteAdapter.openToRead();
		btnAddContactDone = (Button) findViewById(R.id.btnAddContactDone);

		btnAddContactRevert = (Button) findViewById(R.id.btnAddContactRevert);

		edtAddContactFirstName = (EditText) findViewById(R.id.edtAddContactFirstName);
		edtAddContactLastName = (EditText) findViewById(R.id.edtAddContactLastName);
		edtAddContactMiddleName = (EditText) findViewById(R.id.edtAddContactMiddleName);
		male_female = (RadioGroup) findViewById(R.id.rdgAddContactMaleFemale);
		
		spnAddContactGroup = (Spinner) findViewById(R.id.spnAddContactGroup);// for title
		
		edtAddContactCompanyName = (EditText) findViewById(R.id.edtAddContactCompanyName);
		edtAddContactCompanyWebsite = (EditText) findViewById(R.id.edtAddContactCompanyWebsite);
		edtAddContactCompanySlogan= (EditText) findViewById(R.id.edtAddContactCompanySlogan);
		
		edtAddContactPhoneNumber = (EditText) findViewById(R.id.edtAddContactPhoneNumber);
		edtAddContactPhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
		edtAddContactPhoneNumber.setOnFocusChangeListener(this);
		
		spnAddContactPhoneNumber = (Spinner) findViewById(R.id.spnAddContactPhoneNumber);

		edtAddContactEmailAddress = (EditText) findViewById(R.id.edtAddContactEmailAddress);
		edtAddContactEmailAddress.setOnFocusChangeListener(this);
		
		spnAddContactEmailAddress = (Spinner) findViewById(R.id.spnAddContactEmailAddress);

		edtAddContactPostalAddress = (EditText) findViewById(R.id.edtAddContactPostalAddress);  
		chbAddContactYourCard = (CheckBox) findViewById(R.id.chbAddContactYourCard);  // your card?
		
		chbAddContactYourCard.setOnCheckedChangeListener(new  CompoundButton.OnCheckedChangeListener(){
			     // @Override
			        public void  onCheckedChanged(CompoundButton  buttonView,boolean isChecked) {
			        // TODO Auto-generated method stub
			        	 toglecheckbox(chbAddContactYourCard);

			        }
			        });

		
		
		
		Resources res = getResources();
		array_list_group = new ArrayList<String>(Arrays.asList(res
				.getStringArray(R.array.spinner_group)));
		array_list_phone = new ArrayList<String>(Arrays.asList(res
				.getStringArray(R.array.spinner_phone)));
		array_list_email = new ArrayList<String>(Arrays.asList(res
				.getStringArray(R.array.spinner_email)));
		
		if (PeerActivity.peer != null) {
			btnAddContactDone.setOnClickListener(this);
			btnAddContactRevert.setOnClickListener(this);
			male_female.setOnCheckedChangeListener(this);
			male_female.check(R.id.radiobuttonmale);// default male is checked
			refreshAndDisplaySpinner(spnAddContactGroup,array_list_group);
			spnAddContactGroup
			.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent,
						View v, int pos, long id) {
					String spinner_group_selected=parent.getItemAtPosition(pos).toString();
					if (spinner_group_selected.equalsIgnoreCase("Custom")) {// Dialog													
						show_custom_dialog("Custom Group",spnAddContactGroup);
					}//end if
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {			}
			});
			
			refreshAndDisplaySpinner(spnAddContactPhoneNumber,array_list_phone);
			
			spnAddContactPhoneNumber
					.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent,
								View v, int pos, long id) {
							String spinner_phone_selected=parent.getItemAtPosition(pos).toString();
							if (spinner_phone_selected.equalsIgnoreCase("Custom")) {// Dialog													
								show_custom_dialog("Custom Phone Type",spnAddContactPhoneNumber);
							}//end if
						}
						@Override
						public void onNothingSelected(AdapterView<?> arg0) {			}
					});

			refreshAndDisplaySpinner(spnAddContactEmailAddress,array_list_email);
			spnAddContactEmailAddress
			.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent,
						View v, int pos, long id) {
					String spinner_email_selected=parent.getItemAtPosition(pos).toString();
					if (spinner_email_selected.equalsIgnoreCase("Custom")) {// Dialog														
						show_custom_dialog("Custom Email Type",spnAddContactEmailAddress);
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {	}
			});
		}// end PeerActivity.peer!=null

	}// end onCreate

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.RadioGroup.OnCheckedChangeListener#onCheckedChanged(android
	 * .widget.RadioGroup, int)
	 */
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (group == male_female) {
			if (checkedId == R.id.radiobuttonmale) {
				ismale = "male";
			} else {
				ismale = "female";
			}
		}

	}// end onCheckChanged

	private void refreshAndDisplaySpinner(Spinner spn,ArrayList<String> arraylist){	
		ArrayAdapter<String> spnAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, arraylist);
		spnAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		spn.setAdapter(spnAdapter);
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onBackPressed() click return key, close
	 * application
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if(mySQLiteAdapter != null)mySQLiteAdapter.close();
		this.finish();
	}

	public void onFocusChange(View v, boolean hasFocus) {

		if (v instanceof EditText) {
			// handle phone, email focus
			switch (v.getId()) {
			case R.id.edtAddContactPhoneNumber: {
				if (!hasFocus
						&& edtAddContactPhoneNumber.getText().toString()
								.length() == 0) {
					call_dialog_one_buttion("Please input number",edtAddContactPhoneNumber);
				}
				break;
			}
			case R.id.edtAddContactEmailAddress: {
				if (!hasFocus) {					
					if(!CheckEmailInput()){						
							call_dialog_one_buttion("Invalidate email",edtAddContactEmailAddress);					
				}
					}
				break;
			}
			}// end switch

		}// end if
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View) When
	 * button Update click: if there is no date then Toast for user knows. + If
	 * data has already, check and update + If new data then insert to database.
	 * 
	 * When button Revert click: Close activity
	 */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnAddContactDone: {// save to Database
			String temp = edtAddContactFirstName.getText().toString()
					+ edtAddContactLastName.getText().toString()
					+ edtAddContactMiddleName.getText().toString();
			// alert if don't input one of first, middle or last name
			if (temp.equals("")) {
				call_dialog_one_buttion("Input name please!",edtAddContactFirstName);
			} else {
				// update if phone number exist
				String phonenumber = Validate.validatePhoneNumber(edtAddContactPhoneNumber.getText()
						.toString());// cut off blank space
				if (phonenumber.equals("")) {
					call_dialog_one_buttion("Input number please!",edtAddContactPhoneNumber);
					// move focus to phone
				} else {
				try{
				mySQLiteAdapter.openToRead();
							
					Cursor cursor = mySQLiteAdapter.checkphone(phonenumber);
					startManagingCursor(cursor);
					cursor.moveToFirst();
					if (!cursor.isAfterLast()) // if has same phone # then
												//ask update, cancel (rewrite)
					{
						Toast.makeText(getBaseContext(), "Phone number has existed, update new", Toast.LENGTH_LONG).show();
						 SingleData single = new SingleData(cursor);
						 	single.setTypes(spnAddContactGroup.getSelectedItem().toString());// title
							single.setGivenname(Validate.validateName(edtAddContactFirstName.getText().toString()));
							single.setMiddlename(Validate.validateName(edtAddContactMiddleName.getText().toString()));
							single.setFamilyname(Validate.validateName(edtAddContactLastName.getText().toString()));
							single.setGender(ismale);
							single.setOrg1(Validate.validateName(edtAddContactCompanyName.getText().toString()));//company name
							single.setOrg2(Validate.validateName(edtAddContactCompanyWebsite.getText().toString()));// company website
							single.setSpinorg1(Validate.validateName(edtAddContactCompanySlogan.getText().toString())); // slogan  - Spinorg1
							single.setSpinphone(spnAddContactPhoneNumber.getSelectedItem().toString());
							single.setPhone(phonenumber);
							single.setSpinemail(spnAddContactEmailAddress.getSelectedItem().toString());
							single.setEmail(edtAddContactEmailAddress.getText()
									.toString().replaceAll("\\s", ""));
							single.setNotes(isyourcard);
							single.setPobox(Validate.validateName(edtAddContactPostalAddress.getText().toString()));
							mySQLiteAdapter.updatephonenumber(single);
							
//							PeerActivity.peer.addBusinessCardList(single); //add my info
						
					} 
					else {
						// validate input data and insert if new data						
							mySQLiteAdapter.openToWrite();

							mySQLiteAdapter.insert(
									spnAddContactGroup.getSelectedItem().toString(), // type, // title
									Validate.validateName(edtAddContactFirstName.getText().toString()),
									Validate.validateName(edtAddContactMiddleName.getText().toString()),
									Validate.validateName(edtAddContactLastName.getText().toString()),
									ismale,
									spnAddContactPhoneNumber.getSelectedItem().toString(), // spinphone
									phonenumber, 
									spnAddContactEmailAddress.getSelectedItem().toString(), // spinemail
									edtAddContactEmailAddress.getText().toString()
											.replaceAll("\\s", ""), 											
									"",// spinim
									"", // im
									"", // spinaddr
									"", // street
									Validate.validateName(edtAddContactPostalAddress.getText().toString()),
									"", // city
									"", // state
									"", // zipcode
									"", // country
									"", // spinsns
									"", // sns
									Validate.validateName(edtAddContactCompanySlogan.getText().toString()), // spinorg1 - slogan
									Validate.validateName(edtAddContactCompanyName.getText().toString()), // org1  - company name
									"", // spinorg2
									Validate.validateName(edtAddContactCompanyWebsite.getText().toString()), // org2 - company website
									isyourcard , // notes --> is your card, value yes-no STRING
									PeerActivity.getcurrentTime(), // time
																	// (use
																	// as
																	// timestamp)
									"" // photo
							);
							
							
							SingleData single = new SingleData();
							single.setTypes(spnAddContactGroup.getSelectedItem().toString()); // title
							single.setGivenname(Validate.validateName(edtAddContactFirstName.getText().toString()));
							single.setMiddlename(Validate.validateName(edtAddContactMiddleName.getText().toString()));
							single.setFamilyname(Validate.validateName(edtAddContactLastName.getText().toString()));
							single.setGender(ismale);
							single.setOrg1(Validate.validateName(edtAddContactCompanyName.getText().toString()));//company name
							single.setOrg2(Validate.validateName(edtAddContactCompanyWebsite.getText().toString()));// company website
							single.setSpinorg1(Validate.validateName(edtAddContactCompanySlogan.getText().toString())); //Company slogan
							single.setSpinphone(spnAddContactPhoneNumber.getSelectedItem().toString());
							single.setPhone(phonenumber);
							single.setSpinemail(spnAddContactEmailAddress.getSelectedItem().toString());
							single.setEmail(edtAddContactEmailAddress.getText()
									.toString().replaceAll("\\s", ""));
							single.setNotes(isyourcard);
							single.setPobox(Validate.validateName(edtAddContactPostalAddress.getText().toString()));
							
//							PeerActivity.peer.addBusinessCardList(single); //add my info
						
						
					}// end else
					cursor.close();// let java know that you are through with
									// the cursor.
					mySQLiteAdapter.close();
					}catch(SQLException e){
											}
				finish();
				}
			}// end first else
			
			break;
		}
		case R.id.btnAddContactRevert: {
			Toast.makeText(this, "Close !", Toast.LENGTH_LONG).show();
			try{
				if (mySQLiteAdapter != null)	mySQLiteAdapter.close();
				Manual_add_contact.this.finish();
				Manual_add_contact.this.moveTaskToBack(true);
				}catch(SQLException e){
//					Log.v("Database", e.getMessage());
				}
			break;
		}
		}// end switch
	}// end onClick

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try{
		if (mySQLiteAdapter != null)	mySQLiteAdapter.close();
		}catch(SQLException e){
//			Log.v("Database", e.getMessage());
		}
	}

	///////////////////////////////////////////////////////////////////////////////
	private void call_dialog_one_buttion(String title,final View v) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(title);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				v.requestFocus();
				dialog.cancel();
			}
		});
		alertDialog.show();
	}
	// /////////////////////////////////////////////////////////////////////
	/**
	 * This method is used when user click on "Custom" of spinner.
	 * After user inputs new data, the spinner will add new data to its list
	 * @param name is the title
	 * @param spn is a spinner
	 */
	public void show_custom_dialog(String name, final Spinner spn) {

		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.custom_dialog);
		dialog.setTitle(name);
		dialog.setCancelable(true);

		 final EditText edt_NewName = (EditText) dialog
				.findViewById(R.id.edtCustomDialog);
		
		edt_NewName.requestFocus();

		Button btnOK = (Button) dialog.findViewById(R.id.btnOK_CustomDialog);
		btnOK.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!edt_NewName.getText().toString().equals("")) {
//					Log.v("array size", "Arrat sie= "+  array_list_phone.size());
					ArrayAdapter<String> myAdap = (ArrayAdapter<String>)spn.getAdapter(); //cast to an ArrayAdapter
					myAdap.add(edt_NewName.getText().toString());
					int spinnerPosition = myAdap.getPosition(edt_NewName.getText().toString());					
					//set the default according to value
					spn.setSelection(spinnerPosition);
//					Log.v("array size", "Arrat sie= "+  array_list_phone.size());
				}
				dialog.dismiss();
			}
		});

		Button btnCancel = (Button) dialog
				.findViewById(R.id.btnCancel_CustomDialog);
		btnCancel.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				edt_NewName.setText("");
				dialog.dismiss();
			}
		});

		dialog.show();
	
		
	}// end show_custom_dialog
	/////////////////////////////////////////////////////
	/**
	 * Method for control checkbox, is your business care
	 */
	public void toglecheckbox(CheckBox ch){
	     if(ch.isChecked()){
	        ch.setText("This is my business card");
	        isyourcard = "yes";
	     }else{
	        ch.setText("This is not my business card");
	        isyourcard = "no";
	     }
	    }

	/////////////////////////////////////////////////////
	/**
	 * Pre-condition: mySQLiteAdapter has opentoWrite()
	 * Ask user update data or not (thise case, data has same phone number
	 * @param single  
	 * @param phonenumber
	 */
	/*
	private void show_dialog_update_cancel(final SingleData single,final String phonenumber){
		 AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
	       
	        // Setting Dialog Title
	        alertDialog.setTitle("Phone number has exist");
	        alertDialog.setMessage("Do you want to update?");
	 
	        // Setting Icon to Dialog
//	        alertDialog.setIcon(R.drawable.delete);
	        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog,int which) {
	 	            // Write your code here to invoke YES event
	            	
	            }
	        });
	 
	        // Setting Negative "NO" Button
	        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog,    int which) {
	            // Write your code here to invoke NO event
	            	edtAddContactPhoneNumber.setText("");
	            	edtAddContactPhoneNumber.requestFocus();
	            dialog.cancel();
	            }
	        });

	        alertDialog.show();
	} // end show_dialog_update_cancel
	*/
	

////////////////////////////////////////////////
	/**
	 * 
	 * @return false if email not in format
	 */
	private boolean CheckEmailInput(){
		boolean m_continue = true;
		//check email
		String temp_email = edtAddContactEmailAddress.getText().toString().trim();
		if(temp_email.length() >0){
			if(!Validate.checkEmailAddressValidate(temp_email)){
				//focus to email address edittext
				edtAddContactEmailAddress.setText("");
				edtAddContactEmailAddress.requestFocus();
				m_continue = false;
			}
		}	
		return m_continue;
	}
		
	
}
