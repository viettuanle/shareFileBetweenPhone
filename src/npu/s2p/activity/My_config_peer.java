package npu.s2p.activity;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class My_config_peer extends Activity implements OnClickListener{
	private String port = ":"+PeerActivity.PORT;
	
	private TextView addressPeer,currentBootstrapTabConfig;
	private EditText addressBootstrapPeer,edtServerTabConfig;
	private Button saveTabConfig, closeTabConfig, joinBootstrapTabConfig,
					joinServerTabConfig, discoveryTabConfig;
	private CheckBox chbReceiveFileTabConfig;//config receive file or not.
	private CheckBox checkbox;
	private String checkReach;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.my_config_peer);
		checkReach="yes";
		
		addressPeer = (TextView)findViewById(R.id.txtMyIPTabconfig);
		currentBootstrapTabConfig=(TextView)findViewById(R.id.txtCurrentBootstrapTabConfig);
		addressBootstrapPeer= (EditText)findViewById(R.id.edtBootstapTabConfig);
		edtServerTabConfig = (EditText)findViewById(R.id.edtServerTabConfig);
		joinBootstrapTabConfig=(Button) findViewById(R.id.btnJoinBootstrapTabConfig);
		joinServerTabConfig=(Button) findViewById(R.id.btnJoinServerTabConfig);
		discoveryTabConfig=(Button) findViewById(R.id.btnDiscoveryTabConfig);
		saveTabConfig =(Button) findViewById(R.id.btnSaveTabConfig);
		closeTabConfig=(Button)findViewById(R.id.btnCloseTabConfig);
		
		//load status, control receive a file or not
		chbReceiveFileTabConfig =(CheckBox)findViewById(R.id.chbReceiveFileTabConfig);
		if(PeerActivity.ALLOW_RECEIVE_FILE ==1){
			chbReceiveFileTabConfig.setChecked(true);
		}else {
			chbReceiveFileTabConfig.setChecked(false);
		}
		chbReceiveFileTabConfig.setOnCheckedChangeListener(new  CompoundButton.OnCheckedChangeListener(){
	         // @Override
	            public void  onCheckedChanged(CompoundButton  buttonView,boolean isChecked) {
	            // TODO Auto-generated method stub
	              toglecheckbox(chbReceiveFileTabConfig);
	            }
	            });
		//control checkbox of reach local
		 checkbox = (CheckBox) findViewById(R.id.reachCheckBox);
//		 checkbox.setChecked(true);//default is check, set by layout already android:checked="true"
		 checkbox.setOnClickListener(new OnClickListener() {
		     public void onClick(View v) {
		         // Perform action on clicks, depending on whether it's now checked
		         if (((CheckBox) v).isChecked()) {
		        	 checkReach="yes";
		         } else {
		        	 checkReach="no";
		         }
		     }
		 });//end checkbox
		
		if(PeerActivity.peer!=null){
			addressPeer.setText(PeerActivity.peer.getAddressPeer().toString());//display IP:  A@192.168.1.3:5556
			
			if(PeerActivity.peer.getBootstrapPeer()!=null){
//				PeerActivity.peer.getBootstrapPeer().toString(); //sip:192.168.1.3
				String temp_bootstrap = PeerActivity.peer.getBootstrapPeer().getURL(); //A@192.168.1.3:5556
				int colonposition = temp_bootstrap.lastIndexOf(":");
				String bootstrap = temp_bootstrap.substring(0,colonposition );
				addressBootstrapPeer.setText(bootstrap); 
				currentBootstrapTabConfig.setText(bootstrap); 
			}
			else{
				currentBootstrapTabConfig.setText("");
			}
			joinBootstrapTabConfig.setOnClickListener(this);
			joinServerTabConfig.setOnClickListener(this);
			discoveryTabConfig.setOnClickListener(this);
			saveTabConfig.setOnClickListener(this);
			closeTabConfig.setOnClickListener(this);
		}		
	}//end onCreate
	
	  private void toglecheckbox(CheckBox ch){
		     if(ch.isChecked()){
		        PeerActivity.ALLOW_RECEIVE_FILE =1;
		     }else{
		    	PeerActivity.ALLOW_RECEIVE_FILE =0;
		     }
		    }

	
    public void onClick(View v){
        if(v instanceof Button){
             switch(v.getId()){
             case R.id.btnJoinBootstrapTabConfig:{
                 //  Join with bootstrap
            	 sendToJoinBootstrap();
//           	 finish();
                 break;
            }
            case R.id.btnJoinServerTabConfig:{
                 // Join with server

                 break;
            }
            case R.id.btnDiscoveryTabConfig:{
            	 discoveryQueryAction();
                break;
            } 
             case R.id.btnSaveTabConfig:{
                  //Save current bootstrap and server address. Then Join
            	 saveAction();
                  break;
             }
             case R.id.btnCloseTabConfig:{
//            	 PeerActivity.peer.halt();
            	 finish();
                  break;
             }
             
             }//end switch
        }// end if -instance of Button
        
       }//end onClick
    
	private void saveAction(){
		String addBootstrap = (String)addressBootstrapPeer.getText().toString();
		String server =(String)edtServerTabConfig.getText().toString();
 		if(addBootstrap.length()==0){
			Toast toast = Toast.makeText(getApplicationContext(),"Please type a BootstrapPeer (ex. bootstrap@192.168.1.2)" ,Toast.LENGTH_LONG);
		 		toast.show();		 		
		}
		else{
//			Configuration bootstrap here
			//PeerActivity.peer.setConfiguration(sbc, bootstrap, reachability)
			
			PeerActivity.peer.setConfiguration(server,addBootstrap+port,checkReach);
			currentBootstrapTabConfig.setText(addBootstrap);//just in case user put home then come back application
		}
	}//end saveAction
	public void sendToJoinBootstrap(){
		//if user input new bootstrapIP --> update bootstrapIP
		saveAction();
		
		if(addressBootstrapPeer.getText().toString().length() >0){
			String newBootstrap = addressBootstrapPeer.getText().toString()+port;
			currentBootstrapTabConfig.setText(addressBootstrapPeer.getText().toString());
			if(PeerActivity.peer.getBootstrapPeer()!=null){
				String oldBootstrap = 	PeerActivity.peer.getBootstrapPeer().getURL();
			
				if(!newBootstrap.contentEquals(oldBootstrap)){
					String server =(String)edtServerTabConfig.getText().toString();
					PeerActivity.peer.setConfiguration(server,newBootstrap,checkReach);
//					PeerActivity.peer.setConfiguration(newBootstrap);//if different, update new bootstrap IP
					Toast.makeText(this,"Update new Bootstrap " + newBootstrap , Toast.LENGTH_LONG)
					.show();
				}
			}
		}
		
		if(currentBootstrapTabConfig.getText().toString().length() !=0){
					PeerActivity.peer.joinToPeer(PeerActivity.peer.getBootstrapPeer());	
		}
	}//end sendToJoinAction
	
	public void discoveryQueryAction(){
		//check any peer in list, if equal 0, toast.
		// otherwise, send query_message to all peers
		int sizelist = PeerActivity.peer.getsizelist();
		if(sizelist ==0){
			Toast.makeText(this, "You need configure bootstrap first! " , Toast.LENGTH_LONG)
			.show();
		}
		else{
			//send (address + discovery_query_message) to all peers in list
//			PeerDescriptor originalDesc = new PeerDescriptor();
			
			String myaddress = PeerActivity.peer.getAddressPeer().toString();//my address
//			originalDesc.setAddress(origninaladdress);
			
			ArrayList<String> newaddresslist = new ArrayList<String>();
 			newaddresslist.addAll(PeerActivity.peer.getListAddressPeer());
 			//create timestamp
 			Calendar currentDate = Calendar.getInstance();
 			SimpleDateFormat formatter= 
 					  new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss");
 					  String dateNow = formatter.format(currentDate.getTime());//==>  2011/Oct/18 13:14:56
 					  
 					
 			for (int i=0;i<sizelist;i++){
// 				PeerActivity.peer.discoveryPeer(origninaladdress,newaddresslist.get(i).toString());
 				String newaddress = newaddresslist.get(i).toString();
 				//don't send to itself
 				
 				if(!newaddress.contentEquals(myaddress))
 				PeerActivity.peer.discoveryPeer(newaddress, dateNow);
 				}
 			PeerActivity.peer.ClearDataList();//clear for refesh, make sure any other don't responsd, is quit the group
		}
	}
	
}