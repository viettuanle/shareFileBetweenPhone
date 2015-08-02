package npu.database;

import android.database.Cursor;

/**
 * Class name: SingleData
 * @author Le Tuan
 * This class will has almost information of a user. 
 * In version 2: This class should  implements Parcelable in order to transfer the object between activities
 */
public class SingleData {
	private String  rowid;  
	private String  version;
	private String  types;
	private String  givenname;
	private String  middlename;
	private String  familyname;
	private String  gender;  
	private String  spinphone;
	private String  phone;
	private String  spinemail;
	private String  email;
	private String  spinim;
	private String  im;
	private String  spinaddr;
	private String  street;
	private String  pobox;
	private String  city;
	private String  state;
	private String  zipcode;
	private String  country;
	private String  spinsns;
	private String  sns;
	private String  spinorg1;
	private String  org1;
	private String  spinorg2;
	private String  org2;
	private String  notes;
	private String  time;
	private String  photo;

	/**
     * Create a new empty SingleData
     * 
     */

    public SingleData() {
    	//init null; version = 1;
    }
    
    /**
     * Create a new SingleData
     * 
     * @param SingleData    
     */ 
    public SingleData(SingleData aa) {
    	
    	this.rowid = aa.getRowid();
    	this.version = aa.getVersion();
    	this.types = aa.getTypes();
    	this.givenname = aa.getGivenname();
    	this.middlename = aa.getMiddlename();
    	this.familyname = aa.getFamilyname();
    	this.gender = aa.getGender  ();
    	this.spinphone = aa.getSpinphone();
    	this.phone = aa.getPhone();
    	this.spinemail = aa.getSpinemail();
    	this.email = aa.getEmail();
    	this.spinim = aa.getSpinim();
    	this.im = aa.getIm();
    	this.spinaddr = aa.getSpinaddr();
    	this.street = aa.getStreet();
    	this.pobox = aa.getPobox();
    	this.city = aa.getCity();
    	this.state = aa.getState();
    	this.zipcode = aa.getZipcode();
    	this.country = aa.getCountry();
    	this.spinsns = aa.getSpinsns();
    	this.sns = aa.getSns();
    	this.spinorg1 = aa.getSpinorg1();
    	this.org1 = aa.getOrg1();
    	this.spinorg2 = aa.getSpinorg2();
    	this.org2 = aa.getOrg2();
    	this.notes = aa.getNotes();
    	this.time = aa.getTime();
    	this.photo = aa.getPhoto ();   

    }//end constructor
    
    /**
     * Create a new SingleData
     * 
     * @param Cursor   
     */ 
    public SingleData(Cursor cursor) {
    	 Long lObj = new Long(cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAdapter.KEY_ROWID)));
         String str = lObj.toString();//convert long to string
         
        this.rowid = str;
    	this.version = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAdapter.KEY_VERSION));
    	this.types = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAdapter.KEY_TYPES ));
    	this.givenname = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAdapter.KEY_GIVENNAME ));
    	this.middlename = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAdapter.KEY_MIDDLENAME ));
    	this.familyname = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAdapter.KEY_FAMILYNAME ));
    	this.gender = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAdapter.KEY_GENDER ));
    	this.spinphone = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAdapter.KEY_SPINPHONE ));
    	this.phone = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAdapter.KEY_PHONE ));
    	this.spinemail = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAdapter.KEY_SPINEMAIL ));
    	this.email = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAdapter.KEY_EMAIL ));
    	this.spinim = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAdapter.KEY_SPINIM ));
    	this.im = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAdapter.KEY_IM ));
    	this.spinaddr = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAdapter.KEY_SPINADDR ));
    	this.street = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAdapter.KEY_STREET ));
    	this.pobox = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAdapter.KEY_POBOX ));
    	this.city = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAdapter.KEY_CITY ));
    	this.state = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAdapter.KEY_STATE ));
    	this.zipcode = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAdapter.KEY_ZIPCODE ));
    	this.country = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAdapter.KEY_COUNTRY ));
    	this.spinsns = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAdapter.KEY_SPINSNS ));
    	this.sns = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAdapter.KEY_SNS ));
    	this.spinorg1 = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAdapter.KEY_SPINORG1 ));
    	this.org1 = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAdapter.KEY_ORG1 ));
    	this.spinorg2 = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAdapter.KEY_SPINORG2 ));
    	this.org2 = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAdapter.KEY_ORG2 ));
    	this.notes = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAdapter.KEY_NOTES ));
    	this.time = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAdapter.KEY_TIME ));
    	this.photo = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAdapter.KEY_PHOTO));   

    }//end constructor
    
    
    /**
     * Create a new SingleDat
     * 
     * @param givenname
     * @param familyname
     * @param phone number 
     * @param email address 
     * 
     */

    public SingleData(String rowid, String givenname, String phone, String address) {

        this .setGivenname(givenname);
        this .setRowid(rowid);
        this .setPhone(phone);
        this .setSpinaddr(address);

    }//end constructor
    
    
    
    public SingleData(String rowid,String version, String types, String givenname, String middlename, String familyname, String gender, String spinphone, String phone,
			String spinemail, String email, String spinim, String im, String spinaddr, String street, String pobox, 
			String city, String state, String zipcode, String country, String spinsns, String sns, String spinorg1, 
			String org1, String spinorg2, String org2, String notes, String time, String photo) {        
		 
    	this. rowid = rowid;
    	this. version = version;
    	this. types = types;
    	this. givenname = givenname;
    	this. middlename = middlename;
    	this. familyname = familyname;
    	this. gender = gender;
    	this. spinphone = spinphone;
    	this. phone = phone;
    	this. spinemail = spinemail;
    	this. email = email;
    	this. spinim = spinim;
    	this. im = im;
    	this. spinaddr = spinaddr;
    	this. street = street;
    	this. pobox = pobox;
    	this. city = city;
    	this. state = state;
    	this. zipcode = zipcode;
    	this. country = country;
    	this. spinsns = spinsns;
    	this. sns = sns;
    	this. spinorg1 = spinorg1;
    	this. spinorg2 = spinorg2;
    	this. org1 = org1;
    	this. org2 = org2;
    	this. notes = notes;
    	this. time = time;
    	this. photo = photo;

    
    }
    
    
    // get field value
    public String getRowid(){
    	return rowid;
    }
    public  String getVersion(){
        return version;
    }
    
    public String getTypes(){
        return types;
    }
    
    public String getGivenname(){ 
         return givenname;
    }
    
    public String getMiddlename(){ 
                return middlename;
        }
        
    public String getFamilyname(){
                return familyname;
        }
        
    public  String getGender(){
                return gender;
        }
        
    public  String getSpinphone(){
                return spinphone;
        }
        
    public String getPhone(){ 
                return phone;
        }
        
    public String getSpinemail(){ 
                return spinemail;
        }
        
    public String getEmail(){ 
                return email;
        }
        
    public String getSpinim(){
                return spinim;
        }
        
    public String getIm(){ 
                return im;
        }
        
    public String getSpinaddr(){
                return spinaddr;
        }
        
    public String getStreet(){ 
                return street;
        }
        
    public String getPobox(){ 
                return pobox;
        }
        
    public String getCity(){ 
                return city;
        }
        
    public String getState(){
                return state;
        }
        
    public String getZipcode(){
                return zipcode;
        }
        
    public String getCountry(){ 
                return country;
        }
        
    public String getSpinsns(){
                return spinsns;
        }
        
    public String getSns(){ 
                return sns;
        }
        
    public String getSpinorg1(){ 
                return spinorg1;
        }
    public String getSpinorg2(){ 
            return spinorg2;
    }
        
    public String getOrg1(){ 
                return org1;
        }
        
    public String getOrg2(){ 
            return org2;
        }
        
    public String getNotes(){ 
                return notes;
        }
        
    public String getTime(){ 
                return time;
        }
        
    public String getPhoto(){ 
            return photo;
    }
        
        ////////////////////////////////////
    public void  setRowid(String rowid){
    	this. rowid = rowid;
    }
    public  void setVersion(String version){
        this. version = version;
    }
    
    public void  setTypes(String types){
        this. types = types;
    }
    
    public void  setGivenname(String givenname){ 
         this. givenname = givenname;
    }
    
    public void  setMiddlename(String middlename){ 
                this. middlename = middlename;
        }
        
    public void  setFamilyname(String familyname){
                this. familyname = familyname;
        }
        
    public  void setGender(String gender){
                this. gender = gender;
        }
        
    public void setSpinphone(String spinphone){
                this. spinphone = spinphone;
        }
        
    public void  setPhone(String phone){ 
                this. phone = phone;
        }
        
    public void  setSpinemail(String spinemail){ 
                this. spinemail = spinemail;
        }
        
    public void  setEmail(String email){ 
                this. email = email;
        }
        
    public void  setSpinim(String spinim){
                this. spinim = spinim;
        }
        
    public void  setIm(String im){ 
                this. im = im;
        }
        
    public void  setSpinaddr(String spinaddr){
                this. spinaddr = spinaddr;
        }
        
    public void  setStreet(String street){ 
                this. street = street;
        }
        
    public void  setPobox(String pobox){ 
                this. pobox = pobox;
        }
        
    public void  setCity(String city){ 
                this. city = city;
        }
        
    public void  setState(String state){
                this. state = state;
        }
        
    public void  setZipcode(String zipcode){
                this. zipcode = zipcode;
        }
        
    public void  setCountry(String country){ 
                this. country = country;
        }
        
    public void  setSpinsns(String spinsns){
                this. spinsns = spinsns;
        }
        
    public void  setSns(String sns){ 
                this. sns = sns;
        }
        
    public void  setSpinorg1(String spinorg1){ 
                this. spinorg1 = spinorg1;
        }
    public void  setSpinorg2(String spinorg2){ 
            this. spinorg2 = spinorg2;
    }
        
    public void  setOrg1(String org1){ 
                this. org1 = org1;
        }
        
    public void  setOrg2(String org2){ 
            this. org2 = org2;
        }
        
    public void  setNotes(String notes){ 
                this. notes = notes;
        }
        
    public void  setTime(String time){ 
                this. time = time;
        }
        
    public void  setPhoto(String photo){ 
            this. photo = photo;
    }
    
//    public boolean compareSingleData(SingleData newdata, SingleData olddata){
//    	boolean test = true;
//    	
//    	return test;
//    }
   ///////////////////////////////////////
    

    @Override
    public String toString() {

        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");

        
        result.append("rowid  : " + getRowid  () + NEW_LINE);
        result.append("version: " + getVersion() + NEW_LINE);
        result.append("types: " + getTypes() + NEW_LINE);
        result.append("givenname: " + getGivenname() + NEW_LINE);
        result.append("middlename: " + getMiddlename() + NEW_LINE);
        result.append("familyname: " + getFamilyname() + NEW_LINE);
        result.append("gender  : " + getGender  () + NEW_LINE);
        result.append("spinphone: " + getSpinphone() + NEW_LINE);
        result.append("phone: " + getPhone() + NEW_LINE);
        result.append("spinemail: " + getSpinemail() + NEW_LINE);
        result.append("email: " + getEmail() + NEW_LINE);
        result.append("spinim: " + getSpinim() + NEW_LINE);
        result.append("im: " + getIm() + NEW_LINE);
        result.append("spinaddr: " + getSpinaddr() + NEW_LINE);
        result.append("street: " + getStreet() + NEW_LINE);
        result.append("pobox: " + getPobox() + NEW_LINE);
        result.append("city: " + getCity() + NEW_LINE);
        result.append("state: " + getState() + NEW_LINE);
        result.append("zipcode: " + getZipcode() + NEW_LINE);
        result.append("country: " + getCountry() + NEW_LINE);
        result.append("spinsns: " + getSpinsns() + NEW_LINE);
        result.append("sns: " + getSns() + NEW_LINE);
        result.append("spinorg1: " + getSpinorg1() + NEW_LINE);
        result.append("org1: " + getOrg1() + NEW_LINE);
        result.append("spinorg2: " + getSpinorg2() + NEW_LINE);
        result.append("org2: " + getOrg2() + NEW_LINE);
        result.append("notes: " + getNotes() + NEW_LINE);
        result.append("time: " + getTime() + NEW_LINE);
        result.append("photo : " + getPhoto () + NEW_LINE);


        return result.toString();
    }
    
    
    
}//end class
 
 
