package npu.s2p.msg;

import it.unipr.ce.dsg.s2p.peer.PeerDescriptor;

public  class PeerDiscovery {
	private String  timestamp;
	 private String name;
    private String key;
    private String address;
    private String contactAddress;

	public PeerDiscovery(){
		
	}
	public PeerDiscovery(String name, String address, String key){
   this .setName(name);
   this .setAddress(address);
   this .setKey(key);
   this .setContactAddress(address);
	}
	
   public PeerDiscovery(String name, String address, String key,
           String contactAddress) {

       this .setName(name);
       this .setAddress(address);
       this .setKey(key);
       this .setContactAddress(contactAddress);

   }
   
   
   
   public PeerDiscovery(String name, String address, String key,
           String contactAddress, String timestamp) {

       this .setName(name);
       this .setAddress(address);
       this .setKey(key);
       this .setContactAddress(contactAddress);
       this. setTimeStamp(timestamp);

   }
   
   public PeerDiscovery(PeerDescriptor aa, String timestamp) {

       this .setName(aa.getName().toString());
       this .setAddress(aa.getAddress().toString());
       this .setKey(aa.getKey().toString());
       this .setContactAddress(aa.getContactAddress().toString());
       this. setTimeStamp(timestamp);

   } 
   
   public PeerDiscovery(PeerDiscovery xx) {

       this .setName(xx.getName().toString());
       this .setAddress(xx.getAddress().toString());
       this .setKey(xx.getKey().toString());
       this .setContactAddress(xx.getContactAddress().toString());
       this. setTimeStamp(xx.getTimeStamp().toString());

   } 
   
   public void setTimeStamp(String timestamp) {
       this .timestamp = timestamp;
   }
   public String getTimeStamp() {
       return timestamp;
   }

   public void setName(String newname) {
       this .name = newname;
   }
   public String getName() {
       return name;
   }
   
   public void setKey(String newkey) {
       this .key = newkey;
   }
   public String getKey() {
       return key;
   }
   
   public void setAddress(String newaddress) {
       this .address = newaddress;
   }
   public String getAddress() {
       return address;
   }
   
   public void setContactAddress(String newcontactaddress) {
       this .contactAddress = newcontactaddress;
   }
   public String getContactAddress() {
       return contactAddress;
   }
   
   @Override
   public String toString() {

       StringBuilder result = new StringBuilder();
       String NEW_LINE = System.getProperty("line.separator");

       result.append("name: " + getName() + NEW_LINE);
       result.append("address: " + getAddress() + NEW_LINE);
       result.append("key: " + getKey() + NEW_LINE);
       result.append("contact address: " + getContactAddress() + NEW_LINE);
       result.append("timestamp: " + getTimeStamp()
               + NEW_LINE);
       return result.toString();
   }

   
}//end class

