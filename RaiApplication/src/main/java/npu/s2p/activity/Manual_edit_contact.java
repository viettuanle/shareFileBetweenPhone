package npu.s2p.activity;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
//import android.content.Intent;
//import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.view.View.OnFocusChangeListener;
import android.widget.Toast;
import android.telephony.PhoneNumberFormattingTextWatcher;
import npu.database.*;
import npu.support.utility.Validate;

/**
 * Class name: Manual_edit_contact
 * 
 * @author Le Tuan This class similar class Manual_add_contact, but the
 *         different is allow edit one row data, then update into database
 */
public class Manual_edit_contact extends Activity implements OnClickListener,
		OnFocusChangeListener, RadioGroup.OnCheckedChangeListener {

	private SQLiteAdapter mySQLiteAdapter;

	Button btnAddContactDone, btnAddContactRevert;
	EditText edtAddContactFirstName, edtAddContactLastName,
			edtAddContactMiddleName, edtAddContactPhoneNumber,edtAddContactCompanyName,edtAddContactCompanyWebsite,edtAddContactCompanySlogan,
			edtAddContactEmailAddress, edtAddContactPostalAddress;
	RadioGroup male_female;
	private String ismale = null;
	Spinner spnAddContactGroup, spnAddContactPhoneNumber,
			spnAddContactEmailAddress;
	
	CheckBox chbAddContactYourCard;
	private String isBusinessCard=null;
	
	private ArrayList<String> array_list_group;
	private ArrayList<String> array_list_phone;
	private ArrayList<String> array_list_email;
	private SingleData single = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.manual_add_contact);
		mySQLiteAdapter = new SQLiteAdapter(this);

		btnAddContactDone = (Button) findViewById(R.id.btnAddContactDone);
		btnAddContactDone.setOnClickListener(this);
		btnAddContactRevert = (Button) findViewById(R.id.btnAddContactRevert);
		btnAddContactRevert.setOnClickListener(this);

		edtAddContactFirstName = (EditText) findViewById(R.id.edtAddContactFirstName);
		edtAddContactLastName = (EditText) findViewById(R.id.edtAddContactLastName);
		edtAddContactMiddleName = (EditText) findViewById(R.id.edtAddContactMiddleName);
		male_female = (RadioGroup) findViewById(R.id.rdgAddContactMaleFemale);
		male_female.setOnCheckedChangeListener(this);

		spnAddContactGroup = (Spinner) findViewById(R.id.spnAddContactGroup);// for title
		edtAddContactCompanyName = (EditText) findViewById(R.id.edtAddContactCompanyName);
		edtAddContactCompanyWebsite = (EditText) findViewById(R.id.edtAddContactCompanyWebsite);
		edtAddContactCompanySlogan =  (EditText) findViewById(R.id.edtAddContactCompanySlogan);
		
		spnAddContactPhoneNumber = (Spinner) findViewById(R.id.spnAddContactPhoneNumber);
		spnAddContactEmailAddress = (Spinner) findViewById(R.id.spnAddContactEmailAddress);

		edtAddContactPhoneNumber = (EditText) findViewById(R.id.edtAddContactPhoneNumber);
		edtAddContactPhoneNumber
				.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
		edtAddContactEmailAddress = (EditText) findViewById(R.id.edtAddContactEmailAddress);
		edtAddContactPostalAddress = (EditText) findViewById(R.id.edtAddContactPostalAddress);

		chbAddContactYourCard = (CheckBox) findViewById(R.id.chbAddContactYourCard);  // your card?
		chbAddContactYourCard.setOnCheckedChangeListener(new  CompoundButton.OnCheckedChangeListener(){
		     // @Override
	        public void  onCheckedChanged(CompoundButton  buttonView,boolean isChecked) {
	        // TODO Auto-generated method stub
	        	 toglecheckbox(chbAddContactYourCard);

	        }
	        });
		
		edtAddContactPhoneNumber.setOnFocusChangeListener(this);
		edtAddContactEmailAddress.setOnFocusChangeListener(this);

		Resources res = getResources();
		array_list_group = new ArrayList<String>(Arrays.asList(res
				.getStringArray(R.array.spinner_group)));
		array_list_phone = new ArrayList<String>(Arrays.asList(res
				.getStringArray(R.array.spinner_phone)));
		array_list_email = new ArrayList<String>(Arrays.asList(res
				.getStringArray(R.array.spinner_email)));
		// Read the values which class View_contact.java passed to this Child
		// Activity (Manual_edit_contact.java)
		String row_id = getIntent().getStringExtra(
				"edit_contact_base_on_row_id");
		// fill in data_row into some edit_text
		mySQLiteAdapter.openToRead();
		Cursor cursor = mySQLiteAdapter.queryRowId(Long.parseLong(row_id));
		startManagingCursor(cursor);
		cursor.moveToFirst();
		if (!cursor.isAfterLast()) // if has same phone # then update
		{
			single = new SingleData(cursor);

			edtAddContactFirstName.setText(single.getGivenname().toString());
			edtAddContactLastName.setText(single.getFamilyname().toString());
			edtAddContactMiddleName.setText(single.getMiddlename().toString());
			edtAddContactPhoneNumber.setText(single.getPhone().toString());
			edtAddContactEmailAddress.setText(single.getEmail().toString());
			edtAddContactPostalAddress.setText(single.getPobox().toString());
			edtAddContactCompanyName.setText(single.getOrg1().toString()); // Company name -- Org1
			edtAddContactCompanyWebsite.setText(single.getOrg2().toString()); // Company website -- Org2
			edtAddContactCompanySlogan.setText(single.getSpinorg1().toString()); // Company Slogan -- Spinorg1
			
			ismale = single.getGender().toString();
			if (ismale.equals("male")) {
				male_female.check(R.id.radiobuttonmale);
			} else {
				male_female.check(R.id.radiobuttionfemale);
			}

			if(single.getNotes().toString().equals("yes")){
				chbAddContactYourCard.setChecked(true);
			}else{
				chbAddContactYourCard.setChecked(false);
			}
			
			if (!array_list_group.contains(single.getTypes())) {
				array_list_group.add(single.getTypes());
			}
			if (!array_list_phone.contains(single.getSpinphone())) {
				array_list_phone.add(single.getSpinphone());
			}
			if (!array_list_email.contains(single.getSpinemail())) {
				array_list_email.add(single.getSpinemail());
			}
			
//			if()
		}
		cursor.close();// let java know that you are through with the cursor.

		//title
		refreshAndDisplaySpinner(spnAddContactGroup, array_list_group);
		spnAddContactGroup
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View v,
							int pos, long id) {
						String spinner_group_selected = parent
								.getItemAtPosition(pos).toString();
						if (spinner_group_selected.equalsIgnoreCase("Custom")) {// Dialog
							show_custom_dialog("Custom Group",
									spnAddContactGroup);
						}// end if
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

		refreshAndDisplaySpinner(spnAddContactPhoneNumber, array_list_phone);
		spnAddContactPhoneNumber
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View v,
							int pos, long id) {
						String spinner_phone_selected = parent
								.getItemAtPosition(pos).toString();
						if (spinner_phone_selected.equalsIgnoreCase("Custom")) {// Dialog
							show_custom_dialog("Custom Phone Type",
									spnAddContactPhoneNumber);
						}// end if
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

		refreshAndDisplaySpinner(spnAddContactEmailAddress, array_list_email);
		spnAddContactEmailAddress
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View v,
							int pos, long id) {
						String spinner_email_selected = parent
								.getItemAtPosition(pos).toString();
						if (spinner_email_selected.equalsIgnoreCase("Custom")) {// Dialog
							show_custom_dialog("Custom Email Type",
									spnAddContactEmailAddress);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}

				});
		//display value of spinner in database
		displaySpinnerAtValue(spnAddContactGroup,single.getTypes());
		displaySpinnerAtValue(spnAddContactPhoneNumber,single.getSpinphone());
		displaySpinnerAtValue(spnAddContactEmailAddress,single.getSpinemail());
		
	}// end onCreate

	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (group == male_female) {
			if (checkedId == R.id.radiobuttonmale) {
				ismale = "male";
			} else {
				ismale = "female";
			}
		}
		
		

	}// end onCheckChanged

/////////////////////////////////////////////////////
/**
* Method for control checkbox, is your business care
*/
public void toglecheckbox(CheckBox ch){
	if(ch.isChecked()){
		ch.setText("This is my business card");
		isBusinessCard = "yes";
	}else{
		ch.setText("This is not my business card");
		isBusinessCard = "no";
}
}
	
	
	private void refreshAndDisplaySpinner(Spinner spn,
			ArrayList<String> arraylist) {
		ArrayAdapter<String> spnAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arraylist);
		spnAdapter
				.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		spn.setAdapter(spnAdapter);

	}
	private void displaySpinnerAtValue(Spinner spn, String value){
		ArrayAdapter<String> myAdap = (ArrayAdapter<String>)spn.getAdapter();
		int spinnerPosition = myAdap.getPosition(value);					
		//set the default according to value
		spn.setSelection(spinnerPosition);
	}
	// click on return key
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (mySQLiteAdapter != null)
			mySQLiteAdapter.close();
		this.finish();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnAddContactDone: {
			// update to Database base on Row_ID (important!!, don't update base
			// on phone number or name because user could edit them)
			String temp = edtAddContactFirstName.getText().toString()
					+ edtAddContactLastName.getText().toString()
					+ edtAddContactMiddleName.getText().toString();
			// alert if don't input one of first, middle or last name
			if (temp.equals("")) {
				call_dialog_one_buttion("Input name please!",
						edtAddContactFirstName);

			} else {
				mySQLiteAdapter.openToWrite();
				// catch trap user doesn't input phone number, remind user, move
				// focus to phone TextView
				String phonenumber = Validate
						.validatePhoneNumber(edtAddContactPhoneNumber.getText()
								.toString());// cut off blank space
				if (phonenumber.equals("")) {
					call_dialog_one_buttion("Input number please!",
							edtAddContactPhoneNumber);
				} else {// update editing data
					single.setTypes(spnAddContactGroup.getSelectedItem()
							.toString());
					single.setGivenname(Validate
							.validateName(edtAddContactFirstName.getText()
									.toString()));
					single.setMiddlename(Validate
							.validateName(edtAddContactMiddleName.getText()
									.toString()));
					single.setFamilyname(Validate
							.validateName(edtAddContactLastName.getText()
									.toString()));
					single.setGender(ismale);
					single.setSpinphone(spnAddContactPhoneNumber.getSelectedItem().toString());
					single.setPhone(phonenumber);
					single.setSpinemail(spnAddContactEmailAddress
							.getSelectedItem().toString());
					single.setEmail(edtAddContactEmailAddress.getText()
							.toString().replaceAll("\\s", ""));
					single.setOrg1(Validate.validateName(edtAddContactCompanyName.getText().toString())); // Company name - Org1
					single.setOrg2(Validate.validateName(edtAddContactCompanyWebsite.getText().toString())); // Company website - Org2
					single.setSpinorg1(Validate.validateName(edtAddContactCompanySlogan.getText().toString()));// Company slogan - sprinOrg1
					single.setPobox(Validate
							.validateName(edtAddContactPostalAddress.getText()
									.toString()));
					single.setTime(PeerActivity.getcurrentTime());// update
																	// timestamp
					mySQLiteAdapter.update(single);// base on row_id
				}
				mySQLiteAdapter.close();
			}// end first else
			Intent intent = getIntent();
			setResult(RESULT_OK, intent);// return to previous class
											// View_contact
			finish();
			break;
		}
		case R.id.btnAddContactRevert: {
			mySQLiteAdapter.close();
			Intent intent = getIntent();
			setResult(RESULT_CANCELED, intent);// return to previous class
												// View_contact
			finish();
			break;
		}
		}// end switch
	}// end onClick
		// /////////////////////////////////////////////////////////////////////

	public void onFocusChange(View v, boolean hasFocus) {

		if (v instanceof EditText) {
			// handle phone, email focus
			switch (v.getId()) {
			case R.id.edtAddContactPhoneNumber: {
				if (!hasFocus
						&& edtAddContactPhoneNumber.getText().toString()
								.length() == 0) {
					call_dialog_one_buttion("Please input number",
							edtAddContactPhoneNumber);
				}
				break;
			}
			case R.id.edtAddContactEmailAddress: {
				if (!hasFocus) {
					if (!CheckEmailInput()) {
						call_dialog_one_buttion("Invalidate email",
								edtAddContactEmailAddress);
					}
				}
				break;
			}
			}// end switch

		}// end if
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mySQLiteAdapter != null)
			mySQLiteAdapter.close();
	}

	// /////////////////////////////////////////////////////////////////////////////
	private void call_dialog_one_buttion(String title, final View v) {
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
	 * This method is used when user click on "Custom" of spinner. After user
	 * inputs new data, the spinner will add new data to its list
	 * 
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
						ArrayAdapter<String> myAdap = (ArrayAdapter<String>) spn
							.getAdapter(); // cast to an ArrayAdapter
					myAdap.add(edt_NewName.getText().toString());
					int spinnerPosition = myAdap.getPosition(edt_NewName
							.getText().toString());
					// set the default according to value
					spn.setSelection(spinnerPosition);
					// Log.v("array size", "Arrat sie= "+
					// array_list_phone.size());
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
		// //////////////////////////////////////////////

	/**
	 * 
	 * @return false if email not in format
	 */
	private boolean CheckEmailInput() {
		boolean m_continue = true;
		// check email
		String temp_email = edtAddContactEmailAddress.getText().toString()
				.trim();
		if (temp_email.length() > 0) {
			if (!Validate.checkEmailAddressValidate(temp_email)) {
				// focus to email address edittext
				edtAddContactEmailAddress.setText("");
				edtAddContactEmailAddress.requestFocus();
				m_continue = false;
			}
		}
		return m_continue;
	}
}
