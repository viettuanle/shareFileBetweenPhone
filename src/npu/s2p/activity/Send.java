package npu.s2p.activity;

//import static npu.database.Constant.KEY_EMAIL;
//import static npu.database.Constant.KEY_GIVENNAME;
//import static npu.database.Constant.KEY_PHONE;
//import static npu.database.Constant.KEY_ROWID;
//
//import java.util.ArrayList;
//import java.util.HashMap;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;//delete when finish test

import npu.database.SingleData;
import npu.database.SQLiteAdapter;

import android.text.format.Formatter;//for convert int to ipaddress
import android.net.DhcpInfo;
//import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * 
 * @author Le Tuan
 * The class implement many options of send, such as find broadcast address then send data.
 * This class also help to select peers in order to send multicast.
 */

public class Send extends Activity implements OnClickListener{
	private static final int DIALOG_SERVER = 125;
	private static final int DIALOG_CLOSE = 126;
	private final int REQUEST_UPDATE_ACTIVITY_CODE =1;
	private final int INVITE_UPDATE_ACTIVITY_CODE=2;
	private EditText edtNeighborTabSend, edtRequestUpdateTabSend, edtInviteUpdateTabSend;
	private TextView txtMyIPTabSend;
	private Button btnSendPingTabSend, btnRequestTabSend, btnInviteTabSend, btnPingBroadcast,
	btnPingServer,btnRequestMulticast, btnInviteMulticast, btnRequestBroadcast, btnInviteBroadcast, btnCloseTabSend;
//	private TextView contactAddressPeer;
//	private Button closeBut;
    public static String broadcastaddress = null;
	private SQLiteAdapter mySQLiteAdapter;
	
	private String port = ":"+ PeerActivity.PORT;  //":5556"
	/**
	 * Convert an ipaddress int integer ipv4 into ipaddress string
	 * ex:  0x01020304 will return "4.3.2.1".
	 * @param IpAddress
	 * @return ipaddress in String
	 */
	public  String FormatIP(int IpAddress)
	    {
	        return Formatter.formatIpAddress(IpAddress);
	    }
	 public  String GetBroadcast(){
	 	 /** This code will find broadcast address,
    	  * 
    	  */
    	  WifiManager mWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
    	  DhcpInfo dhcp = mWifi.getDhcpInfo(); 
    	  int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask; 
    	  String s_broadcastAddress;
    	  s_broadcastAddress= FormatIP(broadcast).toString();
//          s_networkAddress = FormatIP(dhcp.ipAddress & dhcp.netmask).toString();
//          s_subnetMask = FormatIP(dhcp.netmask).toString();
//          s_localIpAddress = FormatIP(dhcp.ipAddress).toString();
        
    	  s_broadcastAddress= s_broadcastAddress+ port; //use with port, change number at file PeerActivity.java
    	  return s_broadcastAddress;
	 }

	 /**
	  *  @param String address_of_destination
	  *  send all rows of data_contact to other, (by using loop)
	  */
	 public  void invitedata(String toaddress){
		 fulldatalist();
		 PeerActivity.peer.allDataListToPeer(toaddress);
	 }
	
	 /**
	  *  @param String address_of_destination
	  *  send all rows of data_contact to other, (by using loop)
	  */
	 public  void invitebusinesscard(String toaddress){
//		 mybusinesscardlist();
		 fulldatalist();
		 PeerActivity.peer.allBusinessCardToPeer(toaddress);
	 }
	 
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.send);
		//database
		mySQLiteAdapter = new SQLiteAdapter(this);
        broadcastaddress = GetBroadcast();
		txtMyIPTabSend = (TextView)findViewById(R.id.txtMyIPTabSend);
		
		btnSendPingTabSend = (Button)findViewById(R.id.btnSendPingTagSend);
		
		btnRequestTabSend = (Button)findViewById(R.id.btnRequestTabSend);
		btnInviteTabSend = (Button)findViewById(R.id.btnInviteTabSend);
		btnPingBroadcast = (Button)findViewById(R.id.btnPingBroadcast);
		btnPingServer = (Button)findViewById(R.id.btnPingServer);
		btnRequestMulticast = (Button)findViewById(R.id.btnRequestMulticast);
		btnInviteMulticast = (Button)findViewById(R.id.btnInviteMulticast);
		btnRequestBroadcast = (Button)findViewById(R.id.btnRequestBroadcast);
		btnInviteBroadcast = (Button)findViewById(R.id.btnInviteBroadcast);
		btnCloseTabSend = (Button)findViewById(R.id.btnCloseTabSend);
		
		edtNeighborTabSend =(EditText)findViewById(R.id.edtNeighborTagSend); 
		edtRequestUpdateTabSend =(EditText)findViewById(R.id.edtRequestUpdateTabSend); 
		edtInviteUpdateTabSend =(EditText)findViewById(R.id.edtInviteUpdateTabSend);
		
		if(PeerActivity.peer!=null){
			txtMyIPTabSend.setText(PeerActivity.peer.getAddressPeer().toString());//display IP
			
			btnSendPingTabSend.setOnClickListener(this);
			btnRequestTabSend.setOnClickListener(this);
			btnInviteTabSend.setOnClickListener(this);
			btnPingBroadcast.setOnClickListener(this);
			btnPingServer.setOnClickListener(this);
			btnRequestMulticast.setOnClickListener(this);
			btnInviteMulticast.setOnClickListener(this);
			btnRequestBroadcast.setOnClickListener(this);
			btnInviteBroadcast.setOnClickListener(this);
			btnCloseTabSend.setOnClickListener(this);
			
			//database --> datalist
			fulldatalist();//at the beginning, need to fill all database to datalist.
		}//end if PeerActivity.peer !=null
		else{
			txtMyIPTabSend.setText("null");
		}
		
	}//end onCreate
	
	 public void onClick(View v){
	        if(v instanceof Button){
	             switch(v.getId()){
	             case R.id.btnSendPingTagSend:{
	            	 // cut off blank space
	            	 Thread cThread = new Thread(new PingThread());
	                    cThread.start();
//	            	 PeerActivity.peer.pingToPeer(edtNeighborTabSend.getText().toString().replaceAll("\\s", "")+port);
//	            	 remember check validate before ping, v2
	                  break;
	             }
	             case R.id.btnRequestTabSend:{
	            	//for test, send request_database_update to other peer
	            	 //check address
	            	 if(edtRequestUpdateTabSend.getText().toString().equals("")){
	            		 Toast.makeText(this, "You need input address" , Toast.LENGTH_LONG)
		                 .show();
	            	 }else{
	            		 //request other peer send new data to me!
	            		 PeerActivity.peer.requestUpdateToPeer(edtRequestUpdateTabSend.getText().toString().replaceAll("\\s", "")+port);
	            	 }
	            	 	            	 
	            	break;
	             }
	             case R.id.btnInviteTabSend:{
	            	 //the current peer know it has new data, so it send all its database to other peer.
	            	 //test address> send each row of database to other peer.
	            	 
	            	 if(edtInviteUpdateTabSend.getText().toString().equals("")){
	            		 Toast.makeText(this, "You need input address" , Toast.LENGTH_LONG)
		                 .show();
	            	 }
	            	 else{
	            	 //testing, empty DataList  --in order whenever receive package from other, it check new with db then update
//	            		 PeerActivity.peer.ClearDataList();
	            	//	 > DatabasetoDataList > dataToPeer(address)
//	            		 PeerActivity.peer.businessToPeer(edtInviteUpdateTabSend.getText().toString().replaceAll("\\s", "")+port); //ask sender reply business card of sender
	            		 fulldatalist();
	            		 Thread cThread = new Thread(new RequestBusinessThread());
		                    cThread.start();
		                    
		                    	invitebusinesscard(edtInviteUpdateTabSend.getText().toString().replaceAll("\\s", "")+port);
		                    
	            		
	            	 }
	            	 break;
		          }
	             case R.id.btnPingBroadcast:{
		            	//send ping to all peers
	            	/** 
	            	 * This code below only work when the node has all IP address of other,
	            	 * but at the beginning, the node doesn't has other add, so we should change.
	            	 * 
	            	 int sizelist = PeerActivity.peer.getsizelist();
	         		if(sizelist ==0){
	         			Toast.makeText(this, "doesn't has any peer on the list! " , Toast.LENGTH_LONG)
	         			.show();
	         		}
	         		else{
	         		
	         			ArrayList<String> newaddresslist = new ArrayList<String>();
	         			newaddresslist.addAll(PeerActivity.peer.getListAddressPeer());
	         			for (int i=0;i<sizelist;i++){
	         				PeerActivity.peer.pingToPeer(newaddresslist.get(i).toString());			
	         			}
	         		}
		            	break;
		            	**/
	            	 
	            	 
	            //send broadcast address

	            	 	PeerActivity.peer.pingToPeer(GetBroadcast());
	            	 	break;
		          }
	             case R.id.btnPingServer:{
		            	//send ping to server
//	            	 	String serveradd = PeerActivity.peer.getSBCAddress().toString();
	            	 	if(PeerActivity.peer.getSBCAddress()==null){
	            	 		showDialog(DIALOG_SERVER); 
	            	 	}
	            	 	else{
	            	 		String serveradd = PeerActivity.peer.getSBCAddress().toString().replaceAll("\\s", "");
	            	 		serveradd = serveradd+ port;
	            	 		PeerActivity.peer.pingToPeer(serveradd); //note: need write function pingtoServer       
	            	 	}
	            	 	break;
		          }
	             case R.id.btnRequestMulticast:{
		            	/** send multicast **/
	            	 	//check if this peer has address of other or not, 
	            	 	//if not, display message,
	            	 	//if yes, call other class, require check on some Peers address then click OK, or Cancel. 
	            	 	
	            	 	//check peer has ip-address of other or not
	            	 	int sizelist = PeerActivity.peer.getsizelist();
		         		if(sizelist ==0){
		         			Toast.makeText(this, "doesn't has any peer on the list! " , Toast.LENGTH_LONG)
		         			.show();
		         		}
		         		else{//now call class and display all current peers
		         			Intent intent = new Intent(getBaseContext(),RequestMultiUpdate.class);
		         			intent.putExtra("requestupdate", "");
		         			startActivityForResult(intent, REQUEST_UPDATE_ACTIVITY_CODE );
		         		}//end else
	            	 	
	            	 	break;
		          }
	             case R.id.btnInviteMulticast:{
		            	/**send invite data to multicast **/
	            	 	//check if this peer has address of other or not,
	            	 	//if not, display message, otherwise: fulldatalist, call other class, check on some Peers address then click OK, or Cancel.
	            	 	int sizelist = PeerActivity.peer.getsizelist();
		         		if(sizelist ==0){
		         			Toast.makeText(this, "doesn't has any peer on the list! " , Toast.LENGTH_LONG)
		         			.show();
		         		}
		         		else{//now call class and display all current peers
//		         			fulldatalist();
//		         			mybusinesscardlist();
		         		
		         			Intent intent = new Intent(getBaseContext(),InviteMultiUpdate.class);
		         			intent.putExtra("inviteupdate", "");
		         			startActivityForResult(intent, INVITE_UPDATE_ACTIVITY_CODE );
		         		}//end else
	           
	            	 	
	            	 	break;
		          }
	             case R.id.btnRequestBroadcast:{
		            	//request all other peer send new data to me
	            	 	PeerActivity.peer.requestUpdateToPeer(GetBroadcast());
		            	break;
		          }
	             case R.id.btnInviteBroadcast:{
		            	
	            	 	//note: this method will send to itself too, so, need update code
//	            	 	invitedata(GetBroadcast()); //send all database to all peers, 
	            	 fulldatalist();
            		 Thread cThread = new Thread(new RequestBusinessThread());
	                    cThread.start();                                                          
	            	             	 
	            	 	invitebusinesscard(GetBroadcast()); //send only my business card
	            	 	Toast.makeText(this, "Done!" , Toast.LENGTH_LONG)
		                 .show();
		            	break;
		          }
	             case R.id.btnCloseTabSend:{
	            	 	showDialog(DIALOG_CLOSE);
		            	break;
		          }
	             }//end switch
	        }// end if -instance of Button
	        
	       }//end onClick
	
	 
	 //fill all rows of database into datalist
	 public  void fulldatalist(){
		 PeerActivity.peer.ClearDataList();
   	 
   	 //send all row of Database
        mySQLiteAdapter.openToRead();

        Cursor cursor = mySQLiteAdapter.queueAll();
        startManagingCursor(cursor);
       
        cursor.moveToFirst();
      
        if (!cursor.isAfterLast())
        {
       	 do
            {
       		 SingleData single = new SingleData(cursor);       		             
             //Send singleData
                   PeerActivity.peer.addDataList(single);	                           
            }
            while (cursor.moveToNext());
                    
        }
        
        cursor.close();
        mySQLiteAdapter.close();
   	 
	}


	 //should delete, and use  mybusinesscard
	 //fill all rows of database into datalist with conditions that the list contains only my info on business card  
//	 public  void mybusinesscardlist(){
////		 PeerActivity.peer.ClearDataList();
//   	 
//   	 //send all row of Database
//        mySQLiteAdapter.openToRead();
//
//        Cursor cursor = mySQLiteAdapter.queueAll();
//        startManagingCursor(cursor);
//       
//        cursor.moveToFirst();
//      
//        if (!cursor.isAfterLast())
//        {
//       	 do
//            {
//       		 SingleData single = new SingleData(cursor);       		             
//             //Send singleData
//       		 	if(single.getNotes().equals("yes")){
//                   PeerActivity.peer.addBusinessCardList(single);
//                   }	                           
//            }
//            while (cursor.moveToNext());
//                    
//        }
//        
//        cursor.close();
//        mySQLiteAdapter.close();
//   	 
//	}
	 
	 
	 public class PingThread implements Runnable {

	        public void run() {
	            try {
	                	            
//	                while (connected) {
	                    try {
	                        Log.d("ClientActivity", "C: Sending command.");
	                   	 PeerActivity.peer.pingToPeer(edtNeighborTabSend.getText().toString().replaceAll("\\s", "")+port);
	                            // WHERE YOU ISSUE THE COMMANDS
	                     
	                            Log.d("ClientActivity", "C: Sent.");
	                    } catch (Exception e) {
	                        Log.e("ClientActivity", "S: Error", e);
	                    }
//	                }
	                
	                Log.d("ClientActivity", "C: Closed.");
	            } catch (Exception e) {
	                Log.e("ClientActivity", "C: Error", e);
	              	            }
	        }
	    }
	 
	 
	 public class RequestBusinessThread implements Runnable {

	        public void run() {
	            try {
	                	            
//	                while (connected) {
	                    try {
	                        Log.d("ClientActivity", "C: Sending command.");
	                   	 PeerActivity.peer.businessToPeer(edtInviteUpdateTabSend.getText().toString().replaceAll("\\s", "")+port);
	                            // WHERE YOU ISSUE THE COMMANDS
	                     
	                            Log.d("ClientActivity", "C: Sent.");
	                    } catch (Exception e) {
	                        Log.e("ClientActivity", "S: Error", e);
	                    }
//	                }
	                
	                Log.d("ClientActivity", "C: Closed.");
	            } catch (Exception e) {
	                Log.e("ClientActivity", "C: Error", e);
	              	            }
	        }
	    }
	 
	 
	 
	 /**
	  * This method is used because we call ActivityForResult at the btnRequestMulticast
	  * get the total request which has send to peers then display
	  */
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (requestCode == REQUEST_UPDATE_ACTIVITY_CODE && resultCode == RESULT_OK) {
	            //Display total peers has ask request update
	        	Toast.makeText(Send.this, "Total peer request update: "+data.getExtras().getString("datareturn") , Toast.LENGTH_LONG).show(); 
	         }
	        if (requestCode == REQUEST_UPDATE_ACTIVITY_CODE && resultCode == RESULT_CANCELED) {
	            //Display total peers has ask request update
	        	Toast.makeText(Send.this, "Cancel, don't send request! ", Toast.LENGTH_LONG).show(); 
	         }
	        if (requestCode == INVITE_UPDATE_ACTIVITY_CODE && resultCode == RESULT_OK) {
	            //Display total peers has ask request update
	        	Toast.makeText(Send.this, "Total peers invite update: "+data.getExtras().getString("datareturn") , Toast.LENGTH_LONG).show(); 
	         }
	        if (requestCode == INVITE_UPDATE_ACTIVITY_CODE && resultCode == RESULT_CANCELED) {
	            //Display total peers has ask request update
	        	Toast.makeText(Send.this, "Cancel, don't send invite! ", Toast.LENGTH_LONG).show(); 
	         }
	        super.onActivityResult(requestCode, resultCode, data);
	    }
	    
	   /**
	    * Show Dialog alert
	    */
	    @Override
		protected Dialog onCreateDialog(int id) {
			Dialog dialog;
			
		    switch(id) {
		    case DIALOG_SERVER:
		    	//do sth
		    	AlertDialog.Builder builderBoot = new AlertDialog.Builder(this);
		    	builderBoot.setMessage("Your phone wasn't set server ip! Do you want set ip now?");
		    	builderBoot.setTitle("Warning")
		    	       .setCancelable(false)
		    	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		    	           public void onClick(DialogInterface dialog, int id) {//call class Configure here
		    	                dismissDialog(DIALOG_SERVER);
		    	                Intent intent = new Intent(getBaseContext(),My_config_peer.class);
			         			intent.putExtra("requestupdate", "");
			         			startActivity(intent);
		    	                
		    	           }
		    	       });
		    	dialog = builderBoot.create();
		    	break;
		    case DIALOG_CLOSE:
		    	AlertDialog.Builder builderClose = new AlertDialog.Builder(this);
		    	builderClose.setTitle("Warning! Close application");
		    	builderClose.setMessage("Are you sure!");
		    	builderClose.setCancelable(true);
		    	       builderClose.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		    	           public void onClick(DialogInterface dialog, int id) {
		    	                dismissDialog(DIALOG_CLOSE);
//		    	                PeerActivity.peer.halt();  //not use because when use it, MyIP will beA@10.0.2.15:5556;transport=upd --> can not get ipaddress as normal.
		    	                int pid = android.os.Process.myPid(); 
		    		    		android.os.Process.killProcess(pid); 
		    	                finish();
		    	           }
		    	       });
		    	       builderClose.setNegativeButton("No", new DialogInterface.OnClickListener() {
		    	           public void onClick(DialogInterface dialog, int id) {
		    	                dismissDialog(DIALOG_CLOSE);
		    	           }
		    	       });
		    	dialog = builderClose .create();
		    	break;	
		    default:
		        dialog = null;
		    }
		    return dialog;
	    }
}
