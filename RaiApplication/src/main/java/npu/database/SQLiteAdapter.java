package npu.database;



import java.util.ArrayList;
import java.util.Arrays;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;


public class SQLiteAdapter {
	//note: don't change anything inside SQLiteHelper because it's suitable for any database.
	public class SQLiteHelper extends SQLiteOpenHelper {

		   public SQLiteHelper(Context context, String name,
		     CursorFactory factory, int version) {
		    super(context, name, factory, version);
		   }
		   

			// Method is called during creation of the database
//			   @Override
			   public void onCreate(SQLiteDatabase db) {
			    // TODO Auto-generated method stub
			    db.execSQL(SCRIPT_CREATE_DATABASE);
			   }

//			   @Override
			   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			    // TODO Auto-generated method stub
				   Log.w(SQLiteAdapter.class.getName(),
							"Upgrading database from version " + oldVersion + " to "
									+ newVersion + ", which will destroy all old data");
				   db.execSQL("DROP TABLE IF EXISTS todo");
					onCreate(db);
			   }
		   
	}//end class SQLiteHelper
	
	  
   public static final String MYDATABASE_NAME = "NPU_DATABASE";
   public static final String DATABASE_TABLE = "npucontact";
   public static final int MYDATABASE_VERSION = 1;
   
// Database fields
   public static final String KEY_VERSION = "version";
	 public static final String KEY_TYPES = "types";
	 
	 public static final String KEY_GIVENNAME = "givenname";
	 public static final String KEY_MIDDLENAME = "middlename";
	 public static final String KEY_FAMILYNAME = "familyname";
	 
	 public static final String KEY_GENDER = "gender";  
	 
	 public static final String KEY_SPINPHONE = "spinphone";
	 public static final String KEY_PHONE = "phone";
	 
	 public static final String KEY_SPINEMAIL = "spinemail";
	 public static final String KEY_EMAIL = "email";
	 
	 public static final String KEY_SPINIM = "spinim";
	 public static final String KEY_IM = "im";
	 
	 public static final String KEY_SPINADDR = "spinaddr";
	 public static final String KEY_STREET = "street";
	 public static final String KEY_POBOX = "pobox";
	 public static final String KEY_CITY = "city";
	 public static final String KEY_STATE = "state";
	 public static final String KEY_ZIPCODE = "zipcode";
	 public static final String KEY_COUNTRY = "country";
	 
	 public static final String KEY_SPINSNS = "spinsns";
	 public static final String KEY_SNS = "sns";
	 
	 public static final String KEY_SPINORG1 = "spinorg1";
	 public static final String KEY_ORG1 = "org1";
	 
	 public static final String KEY_SPINORG2 = "spinorg2";
	 public static final String KEY_ORG2 = "org2";
	 
	 public static final String KEY_NOTES = "notes";	 
	 public static final String KEY_TIME = "time";	 
	 public static final String KEY_PHOTO = "photo";
	 	 
	 public static final String KEY_ROWID = "rowid";    
	
		private Context context;
//		private SQLiteDatabase database;
		 private SQLiteHelper sqLiteHelper;
		   private SQLiteDatabase sqLiteDatabase;


		 //create table MY_DATABASE (ID integer primary key, Content text not null);
//		   private static final String SCRIPT_CREATE_DATABASE =
//		   "create table " + DATABASE_TABLE + " ("
//		   + KEY_ROWID  + " integer primary key autoincrement, "
//		   + KEY_TYPES + "text not null, "
//		   + KEY_GIVENNAME + " text not null, "
//		   + KEY_MIDDLENAME + " text not null, "
//		   + KEY_FAMILYNAME + " text not null, "
//		   + KEY_GENDER + " text not null, "
//		   + KEY_SPINPHONE + " text , "
//		   + KEY_PHONE + " text not null, "
//		   + KEY_SPINEMAIL + " text , "
// 		   + KEY_EMAIL  + " text not null, "
//		   + KEY_SPINIM + " text, "
//		   + KEY_IM + " text not null, "
//		   + KEY_SPINADDR + " text , "
//		   + KEY_STREET + " text not null, "
//		   + KEY_POBOX + " text not null, "
//		   + KEY_CITY + " text not null, "
//		   + KEY_STATE + " text not null, "
//		   + KEY_ZIPCODE + " text not null, "
//		   + KEY_COUNTRY + " text not null, "
//		   + KEY_SPINSNS + " text  , "
//		   + KEY_SNS + " text not null, "
//		   + KEY_SPINORG1 + " text, "
//		   + KEY_ORG1 + " text not null, "
//		   + KEY_SPINORG2   + " text , "      
//		   + KEY_ORG2 + " text not null, "
//		   + KEY_NOTES + " text not null, "
//		   + KEY_TIME + " text not null, "   
//		   + KEY_PICTURE + " text);";
		   
		   
		   
		   private static final String SCRIPT_CREATE_DATABASE = "create table "+ DATABASE_TABLE+ " (rowid integer primary key autoincrement, "                    
					 + "version text not null, types text not null, givenname text not null, middlename text not null, familyname text not null, " 
					 + "gender text not null, spinphone text, phone text not null,"
					 + "spinemail text, email text not null, spinim text, im text not null,"
					 + "spinaddr text, street text not null, pobox text not null, city text not null, state text not null, zipcode text not null,country text not null,"
					 + "spinsns text, sns text not null, spinorg1 text, org1 text not null, spinorg2 text, org2 text not null, notes text not null,"
					 + "time text not null, photo blob not null);"; 

       

  


//constructor
   public SQLiteAdapter(Context c){
   context = c;
   }
   
	

   public SQLiteAdapter openToRead() throws android.database.SQLException {
   sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);
   sqLiteDatabase = sqLiteHelper.getReadableDatabase();
   return this;
   }

   public SQLiteAdapter openToWrite() throws android.database.SQLException {
   sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);
   sqLiteDatabase = sqLiteHelper.getWritableDatabase();
   return this;
   }

   public void close(){
   sqLiteHelper.close();
   }

   /**
    * insert short allow only 5 parameters, other column will get value ""
	 * Insert data to Table. If the insert is successfully created return the new
	 * rowId for that note, otherwise return a -1 to indicate failure.
	 */
  public long insertshort(String givenname, String familyname, String phone,
			              String email, String pobox){

  ContentValues initialValues = new ContentValues(); 
		 initialValues.put(KEY_VERSION, "");
		 initialValues.put(KEY_TYPES, "");		 
		 initialValues.put(KEY_GIVENNAME, givenname);
		 initialValues.put(KEY_MIDDLENAME, "");
		 initialValues.put(KEY_FAMILYNAME, familyname);
		 initialValues.put(KEY_GENDER,"");  
		 initialValues.put(KEY_SPINPHONE, "");
		 initialValues.put(KEY_PHONE, phone);
		 initialValues.put(KEY_SPINEMAIL, "");
		 initialValues.put(KEY_EMAIL, email);
		 initialValues.put(KEY_SPINIM, "");
		 initialValues.put(KEY_IM, "");
		 initialValues.put(KEY_SPINADDR, "");
		 initialValues.put(KEY_STREET, "");
		 initialValues.put(KEY_POBOX, pobox);
		 initialValues.put(KEY_CITY, "");
		 initialValues.put(KEY_STATE, "");
		 initialValues.put(KEY_ZIPCODE, "");
		 initialValues.put(KEY_COUNTRY, "");
		 initialValues.put(KEY_SPINSNS, "");
		 initialValues.put(KEY_SNS, "");
		 initialValues.put(KEY_SPINORG1, "");
		 initialValues.put(KEY_ORG1, "");
		 initialValues.put(KEY_SPINORG2, "");
		 initialValues.put(KEY_ORG2, "");		 
		 initialValues.put(KEY_NOTES, ""); 
		 initialValues.put(KEY_TIME, ""); 
		 initialValues.put(KEY_PHOTO, "");
		 
		 return sqLiteDatabase.insert(DATABASE_TABLE, null, initialValues);   
  }
  
   
   /**
	 * Insert data to Table. If the insert is successfully created return the new
	 * rowId for that note, otherwise return a -1 to indicate failure.
	 */
   public long insert(String group, String givenname, String middlename, String familyname, String gender, String spinphone, String phone,
			String spinemail, String email, String spinim, String im, String spinaddr, String street, String pobox, 
			String city, String state, String zipcode, String country, String spinsns, String sns, String spinorg1, 
			String org1, String spinorg2, String org2, String notes, String time, String photo) {        
		 
		 ContentValues initialValues = new ContentValues(); 
		 initialValues.put(KEY_VERSION, "");
		 initialValues.put(KEY_TYPES, group);	//type is used to save group name	 
		 initialValues.put(KEY_GIVENNAME, givenname);
		 initialValues.put(KEY_MIDDLENAME, middlename);
		 initialValues.put(KEY_FAMILYNAME, familyname);
		 initialValues.put(KEY_GENDER,gender);  
		 initialValues.put(KEY_SPINPHONE, spinphone);
		 initialValues.put(KEY_PHONE, phone);
		 initialValues.put(KEY_SPINEMAIL, spinemail);
		 initialValues.put(KEY_EMAIL, email);
		 initialValues.put(KEY_SPINIM, spinim);
		 initialValues.put(KEY_IM, im);
		 initialValues.put(KEY_SPINADDR, spinaddr);
		 initialValues.put(KEY_STREET, street);
		 initialValues.put(KEY_POBOX, pobox);
		 initialValues.put(KEY_CITY, city);
		 initialValues.put(KEY_STATE, state);
		 initialValues.put(KEY_ZIPCODE, zipcode);
		 initialValues.put(KEY_COUNTRY, country);
		 initialValues.put(KEY_SPINSNS, spinsns);
		 initialValues.put(KEY_SNS, sns);
		 initialValues.put(KEY_SPINORG1, spinorg1);
		 initialValues.put(KEY_ORG1, org1);
		 initialValues.put(KEY_SPINORG2, spinorg2);
		 initialValues.put(KEY_ORG2, org2);		 
		 initialValues.put(KEY_NOTES, notes); 
		 initialValues.put(KEY_TIME, time); 
		 initialValues.put(KEY_PHOTO, photo);
		 
		 return sqLiteDatabase.insert(DATABASE_TABLE, null, initialValues);    
	} //createContact
   
   /**
 	 * Insert data to Table. If the insert is successfully created return the new
 	 * rowId for that note, otherwise return a -1 to indicate failure.
 	 */
    public long insert(SingleData sng) {        
 		 
    	ContentValues args = new ContentValues(); 
		
		args.put(KEY_VERSION, sng.getVersion());
		args.put(KEY_TYPES, sng.getTypes());
		args.put(KEY_GIVENNAME, sng.getGivenname());
		args.put(KEY_MIDDLENAME, sng.getMiddlename());
		args.put(KEY_FAMILYNAME, sng.getFamilyname());
		args.put(KEY_GENDER, sng.getGender());  
		args.put(KEY_SPINPHONE, sng.getSpinphone());
		args.put(KEY_PHONE, sng.getPhone());
		args.put(KEY_SPINEMAIL, sng.getSpinemail());
		args.put(KEY_EMAIL, sng.getEmail());
		args.put(KEY_SPINIM, sng.getSpinim());
		args.put(KEY_IM, sng.getIm());
		args.put(KEY_SPINADDR, sng.getSpinaddr());
		args.put(KEY_STREET, sng.getStreet());
		args.put(KEY_POBOX, sng.getPobox());
		args.put(KEY_CITY, sng.getCity());
		args.put(KEY_STATE, sng.getState());
		args.put(KEY_ZIPCODE, sng.getZipcode());
		args.put(KEY_COUNTRY, sng.getCountry());
		args.put(KEY_SPINSNS, sng.getSpinsns());
		args.put(KEY_SNS, sng.getSns());
		args.put(KEY_SPINORG1, sng.getSpinorg1());
		args.put(KEY_ORG1, sng.getOrg1());
		args.put(KEY_SPINORG2, sng.getSpinorg2());
		args.put(KEY_ORG2, sng.getOrg2());		
		args.put(KEY_NOTES, sng.getNotes()); 
		args.put(KEY_TIME, sng.getTime());
		args.put(KEY_PHOTO, sng.getPhoto());
 		 
 		 return sqLiteDatabase.insert(DATABASE_TABLE, null, args);    
 	} //createContact
   
	/**
	 * Update the table contact belong to rowID
	 */

	public boolean update(long rowId, String version, String types, String givenname, String middlename, String familyname, String gender, String spinphone, String phone,
			String spinemail, String email, String spinim, String im, String spinaddr, String street, String pobox, 
			String city, String state, String zipcode, String country, String spinsns, String sns, String spinorg1, 
			String org1, String spinorg2, String org2, String notes, String time, String photo) { 
		
		ContentValues args = new ContentValues(); 
		
		args.put(KEY_VERSION, version);
		args.put(KEY_TYPES, types);
		args.put(KEY_GIVENNAME, givenname);
		args.put(KEY_MIDDLENAME, middlename);
		args.put(KEY_FAMILYNAME, familyname);
		args.put(KEY_GENDER,gender);  
		args.put(KEY_SPINPHONE, spinphone);
		args.put(KEY_PHONE, phone);
		args.put(KEY_SPINEMAIL, spinemail);
		args.put(KEY_EMAIL, email);
		args.put(KEY_SPINIM, spinim);
		args.put(KEY_IM, im);
		args.put(KEY_SPINADDR, spinaddr);
		args.put(KEY_STREET, street);
		args.put(KEY_POBOX, pobox);
		args.put(KEY_CITY, city);
		args.put(KEY_STATE, state);
		args.put(KEY_ZIPCODE, zipcode);
		args.put(KEY_COUNTRY, country);
		args.put(KEY_SPINSNS, spinsns);
		args.put(KEY_SNS, sns);
		args.put(KEY_SPINORG1, spinorg1);
		args.put(KEY_ORG1, org1);
		args.put(KEY_SPINORG2, spinorg2);
		args.put(KEY_ORG2, org2);		
		args.put(KEY_NOTES, notes); 
		args.put(KEY_TIME, time);
		args.put(KEY_PHOTO, photo);
	
		return sqLiteDatabase.update(DATABASE_TABLE, args, KEY_ROWID  + "=" + rowId, null) > 0;   
	}
	
	/**
	 * 
	 */
public boolean update(SingleData sng) { 
		
		ContentValues args = new ContentValues(); 
		
		args.put(KEY_VERSION, sng.getVersion());
		args.put(KEY_TYPES, sng.getTypes());
		args.put(KEY_GIVENNAME, sng.getGivenname());
		args.put(KEY_MIDDLENAME, sng.getMiddlename());
		args.put(KEY_FAMILYNAME, sng.getFamilyname());
		args.put(KEY_GENDER, sng.getGender());  
		args.put(KEY_SPINPHONE, sng.getSpinphone());
		args.put(KEY_PHONE, sng.getPhone());
		args.put(KEY_SPINEMAIL, sng.getSpinemail());
		args.put(KEY_EMAIL, sng.getEmail());
		args.put(KEY_SPINIM, sng.getSpinim());
		args.put(KEY_IM, sng.getIm());
		args.put(KEY_SPINADDR, sng.getSpinaddr());
		args.put(KEY_STREET, sng.getStreet());
		args.put(KEY_POBOX, sng.getPobox());
		args.put(KEY_CITY, sng.getCity());
		args.put(KEY_STATE, sng.getState());
		args.put(KEY_ZIPCODE, sng.getZipcode());
		args.put(KEY_COUNTRY, sng.getCountry());
		args.put(KEY_SPINSNS, sng.getSpinsns());
		args.put(KEY_SNS, sng.getSns());
		args.put(KEY_SPINORG1, sng.getSpinorg1());
		args.put(KEY_ORG1, sng.getOrg1());
		args.put(KEY_SPINORG2, sng.getSpinorg2());
		args.put(KEY_ORG2, sng.getOrg2());		
		args.put(KEY_NOTES, sng.getNotes()); 
		args.put(KEY_TIME, sng.getTime());
		args.put(KEY_PHOTO, sng.getPhoto());
	    
		long rowId = (Long.parseLong(sng.getRowid().toString()));
		return sqLiteDatabase.update(DATABASE_TABLE, args, KEY_ROWID  + "=" + rowId, null) > 0;   
	}

	/**
	 * Update the table contact belong to phone number
	 */

	public boolean updatephonenumber(long rowId, String version, String types, String givenname, String middlename, String familyname, String gender, String spinphone, String phone,
			String spinemail, String email, String spinim, String im, String spinaddr, String street, String pobox, 
			String city, String state, String zipcode, String country, String spinsns, String sns, String spinorg1, 
			String org1, String spinorg2, String org2, String notes, String time, String photo) { 
		
		ContentValues args = new ContentValues(); 
		
		args.put(KEY_VERSION, version);
		args.put(KEY_TYPES, types);
		args.put(KEY_GIVENNAME, givenname);
		args.put(KEY_MIDDLENAME, middlename);
		args.put(KEY_FAMILYNAME, familyname);
		args.put(KEY_GENDER,gender);  
		args.put(KEY_SPINPHONE, spinphone);
		args.put(KEY_PHONE, phone);
		args.put(KEY_SPINEMAIL, spinemail);
		args.put(KEY_EMAIL, email);
		args.put(KEY_SPINIM, spinim);
		args.put(KEY_IM, im);
		args.put(KEY_SPINADDR, spinaddr);
		args.put(KEY_STREET, street);
		args.put(KEY_POBOX, pobox);
		args.put(KEY_CITY, city);
		args.put(KEY_STATE, state);
		args.put(KEY_ZIPCODE, zipcode);
		args.put(KEY_COUNTRY, country);
		args.put(KEY_SPINSNS, spinsns);
		args.put(KEY_SNS, sns);
		args.put(KEY_SPINORG1, spinorg1);
		args.put(KEY_ORG1, org1);
		args.put(KEY_SPINORG2, spinorg2);
		args.put(KEY_ORG2, org2);		
		args.put(KEY_NOTES, notes); 
		args.put(KEY_TIME, time);
		args.put(KEY_PHOTO, photo);
	
		return sqLiteDatabase.update(DATABASE_TABLE, args, KEY_PHONE  + "=" + phone, null) > 0;   
	}
	
	public boolean updatephonenumber(SingleData sng) { 
		
		ContentValues args = new ContentValues(); 
		
		args.put(KEY_VERSION, sng.getVersion());
		args.put(KEY_TYPES, sng.getTypes());
		args.put(KEY_GIVENNAME, sng.getGivenname());
		args.put(KEY_MIDDLENAME, sng.getMiddlename());
		args.put(KEY_FAMILYNAME, sng.getFamilyname());
		args.put(KEY_GENDER, sng.getGender());  
		args.put(KEY_SPINPHONE, sng.getSpinphone());
		args.put(KEY_PHONE, sng.getPhone());
		args.put(KEY_SPINEMAIL, sng.getSpinemail());
		args.put(KEY_EMAIL, sng.getEmail());
		args.put(KEY_SPINIM, sng.getSpinim());
		args.put(KEY_IM, sng.getIm());
		args.put(KEY_SPINADDR, sng.getSpinaddr());
		args.put(KEY_STREET, sng.getStreet());
		args.put(KEY_POBOX, sng.getPobox());
		args.put(KEY_CITY, sng.getCity());
		args.put(KEY_STATE, sng.getState());
		args.put(KEY_ZIPCODE, sng.getZipcode());
		args.put(KEY_COUNTRY, sng.getCountry());
		args.put(KEY_SPINSNS, sng.getSpinsns());
		args.put(KEY_SNS, sng.getSns());
		args.put(KEY_SPINORG1, sng.getSpinorg1());
		args.put(KEY_ORG1, sng.getOrg1());
		args.put(KEY_SPINORG2, sng.getSpinorg2());
		args.put(KEY_ORG2, sng.getOrg2());		
		args.put(KEY_NOTES, sng.getNotes()); 
		args.put(KEY_TIME, sng.getTime());
		args.put(KEY_PHOTO, sng.getPhoto());
	
		return sqLiteDatabase.update(DATABASE_TABLE, args, KEY_PHONE  + "=" + sng.getPhone(), null) > 0;   
	}
/*	
public void updatephonenumber(SingleData sng) { 
		
		ContentValues args = new ContentValues(); 
		
		args.put(KEY_VERSION, sng.getVersion());
		args.put(KEY_TYPES, sng.getTypes());
		args.put(KEY_GIVENNAME, sng.getGivenname());
		args.put(KEY_MIDDLENAME, sng.getMiddlename());
		args.put(KEY_FAMILYNAME, sng.getFamilyname());
		args.put(KEY_GENDER, sng.getGender());  
		args.put(KEY_SPINPHONE, sng.getSpinphone());
		args.put(KEY_PHONE, sng.getPhone());
		args.put(KEY_SPINEMAIL, sng.getSpinemail());
		args.put(KEY_EMAIL, sng.getEmail());
		args.put(KEY_SPINIM, sng.getSpinim());
		args.put(KEY_IM, sng.getIm());
		args.put(KEY_SPINADDR, sng.getSpinaddr());
		args.put(KEY_STREET, sng.getStreet());
		args.put(KEY_POBOX, sng.getPobox());
		args.put(KEY_CITY, sng.getCity());
		args.put(KEY_STATE, sng.getState());
		args.put(KEY_ZIPCODE, sng.getZipcode());
		args.put(KEY_COUNTRY, sng.getCountry());
		args.put(KEY_SPINSNS, sng.getSpinsns());
		args.put(KEY_SNS, sng.getSns());
		args.put(KEY_SPINORG1, sng.getSpinorg1());
		args.put(KEY_ORG1, sng.getOrg1());
		args.put(KEY_SPINORG2, sng.getSpinorg2());
		args.put(KEY_ORG2, sng.getOrg2());		
		args.put(KEY_NOTES, sng.getNotes()); 
		args.put(KEY_TIME, sng.getTime());
		args.put(KEY_PHOTO, sng.getPhoto());
	
		return sqLiteDatabase.update(DATABASE_TABLE, args, KEY_PHONE  + "=" + sng.getPhone(), null);   
	}
*/	
   public int deleteAll(){
   return sqLiteDatabase.delete(DATABASE_TABLE, null, null);
   }

	public boolean deleteRow(long rowId) {
		return sqLiteDatabase.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}
   
	/**
	 * Return a Cursor over the list of all table contact in the database
	 * 
	 * @return Cursor over all notes
	 */
	
   public Cursor queueAll(){
	   String[] columns = new String[]{KEY_ROWID, KEY_VERSION, KEY_TYPES,
				KEY_GIVENNAME, KEY_MIDDLENAME, KEY_FAMILYNAME, KEY_GENDER, KEY_SPINPHONE, KEY_PHONE,
				KEY_SPINEMAIL, KEY_EMAIL, KEY_SPINIM, KEY_IM, KEY_SPINADDR, KEY_STREET, KEY_POBOX,
				KEY_CITY, KEY_STATE, KEY_ZIPCODE, KEY_COUNTRY, KEY_SPINSNS, KEY_SNS, KEY_SPINORG1,
				KEY_ORG1, KEY_SPINORG2, KEY_ORG2, KEY_NOTES, KEY_TIME, KEY_PHOTO};
   
	   Cursor cursor = sqLiteDatabase.query(DATABASE_TABLE, columns,
			   								null, null, null, null, null);

	   return cursor;
   }
   
   
	/**
	 * Return a Cursor over the list of all table contact in the database sort by Given name
	 * 
	 * @return Cursor over all notes
	 */
	
  public Cursor queueAll_SortBy_GivenName(){
	   String[] columns = new String[]{KEY_ROWID, KEY_VERSION, KEY_TYPES,
				KEY_GIVENNAME, KEY_MIDDLENAME, KEY_FAMILYNAME, KEY_GENDER, KEY_SPINPHONE, KEY_PHONE,
				KEY_SPINEMAIL, KEY_EMAIL, KEY_SPINIM, KEY_IM, KEY_SPINADDR, KEY_STREET, KEY_POBOX,
				KEY_CITY, KEY_STATE, KEY_ZIPCODE, KEY_COUNTRY, KEY_SPINSNS, KEY_SNS, KEY_SPINORG1,
				KEY_ORG1, KEY_SPINORG2, KEY_ORG2, KEY_NOTES, KEY_TIME, KEY_PHOTO};
  
	   Cursor cursor = sqLiteDatabase.query(DATABASE_TABLE, columns,
			   								null, null, null, null, KEY_GIVENNAME);

	   return cursor;
  }
   
   
	/**
	 * Return a Cursor positioned at the defined todo
	 */

	public Cursor queryRowId(long rowId) throws SQLException {
		String[] columns = new String[]{KEY_ROWID, KEY_VERSION, KEY_TYPES,                  
				 KEY_GIVENNAME, KEY_MIDDLENAME, KEY_FAMILYNAME, KEY_GENDER, KEY_SPINPHONE, KEY_PHONE,
				 KEY_SPINEMAIL, KEY_EMAIL, KEY_SPINIM, KEY_IM, KEY_SPINADDR, KEY_STREET, KEY_POBOX,
				 KEY_CITY, KEY_STATE, KEY_ZIPCODE, KEY_COUNTRY, KEY_SPINSNS, KEY_SNS, KEY_SPINORG1,
				 KEY_ORG1, KEY_SPINORG2, KEY_ORG2, KEY_NOTES, KEY_TIME, KEY_PHOTO};
		
		Cursor mCursor = sqLiteDatabase.query(true, DATABASE_TABLE, columns,
				KEY_ROWID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	/**
	 * Return a Cursor positioned at the defined todo
	 */

	public Cursor checkphone(String phonenumber) throws SQLException {
//		String[] columns = new String[]{KEY_ROWID, KEY_VERSION, KEY_TYPES,                  
//				 KEY_GIVENNAME, KEY_MIDDLENAME, KEY_FAMILYNAME, KEY_GENDER, KEY_SPINPHONE, KEY_PHONE,
//				 KEY_SPINEMAIL, KEY_EMAIL, KEY_SPINIM, KEY_IM, KEY_SPINADDR, KEY_STREET, KEY_POBOX,
//				 KEY_CITY, KEY_STATE, KEY_ZIPCODE, KEY_COUNTRY, KEY_SPINSNS, KEY_SNS, KEY_SPINORG1,
//				 KEY_ORG1, KEY_SPINORG2, KEY_ORG2, KEY_NOTES, KEY_TIME, KEY_PHOTO};
//		
//		Cursor mCursor = sqLiteDatabase.query(true, DATABASE_TABLE, columns,
//				KEY_PHONE + "=" + phone, null, null, null, null, null);
//		if (mCursor != null) {
//			mCursor.moveToFirst();
//		}
//		return mCursor;
		/**
		 * Note: the previous code above doesn't give correct result at anytime, so, we edit the code as below
		 * especially, the android don't clear understand the String type, so that, we need (') and (') before and after phonenumber		
		 */
				String q = "SELECT * FROM " + DATABASE_TABLE+ " WHERE " + KEY_PHONE + " = " + "'"+phonenumber+"'" +";" ;
				
				sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);
				sqLiteDatabase = sqLiteHelper.getReadableDatabase();
				Cursor mCursor = sqLiteDatabase.rawQuery(q, null);
				if (mCursor != null) {
					mCursor.moveToFirst();
				}
				return mCursor;
	}
 
	/**
	    * return list of column name
	    */
		public ArrayList<String> getColumns( final String tableName) {
			ArrayList<String> ar = null;
	        Cursor c = null;
	        try {
	            c = sqLiteDatabase.rawQuery("SELECT * FROM " + tableName + " LIMIT 1", null);
	            if (c != null) {
	                ar = new ArrayList<String>(Arrays.asList(c.getColumnNames()));
	            }
	        } finally {
	            if (c != null)
	                c.close();
	        }
	        return ar;
	    }
   
}
