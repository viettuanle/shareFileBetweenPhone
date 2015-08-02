package npu.s2p.activity;


import npu.s2p.msg.ChatData;
import npu.support.adapter.FolderInfo;
import npu.support.adapter.ListviewChatAdapter;

//import java.io.BufferedInputStream;
//import java.io.BufferedOutputStream;
//import java.io.BufferedReader;
import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.ServerSocket;
//import java.net.Socket;
import java.util.ArrayList;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;


public class MyInstanceMSg  extends Activity implements OnClickListener, AdapterView.OnItemSelectedListener { 
    public static final int MESSAGE_READ 	= 1;
    public static final int MESSAGE_WRITE 	= 2;
    public static final int MESSAGE_TOAST 	= 3;
	public static final int MESSAGE_START_RECEIVE = 4;
	public static final int MESSAGE_PROGRESS_RECEIVE =5;
	
	final int SEND_FILE_REQUEST_CODE = 39;
	final int READ_FILE_REQUEST_CODE = 312;
	final int DELETE_FILE_REQUEST_CODE = 313;
	final int PORT = PeerActivity.PORT+1; //tcp file receive
//	private ServerSocket serverSocket; //for sever part (receive file)
	private ServerFileService mFileService = null;
	private Spinner spnaddress;
	private EditText outMessage;
	private Button sendMessage, sendFile;
	
	private int percentsend =0;
	private int percentreceive =0;
	public static String toaddress = null ;  //ex: A@192.168.1.5:5556
//	private ArrayList<ChatData> list;
	
	private ListviewChatAdapter adapter;//is a extend of ArrayAdapter
	private ListView ltvDisplay;
    private ProgressBar pgbSendMyInstanceMsg,pgbReceiveMyInstanceMsg;
    private TextView txtPercentSendMyInstanceMsg,txtPercentReceiveMyInstanceMsg;
    
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        	
            switch (msg.what) {
            
	            case MESSAGE_READ:
	                final String readBuf = (String) msg.obj; // readBuf is file_path	      
	                String file_name = readBuf.substring(readBuf.lastIndexOf("/")+1,readBuf.length());
	                new AlertDialog.Builder(MyInstanceMSg.this)

					.setIcon(R.drawable.iconsend)
					.setTitle("You received a file:" + file_name + " !")
					

					.setPositiveButton("Open file",

							new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								//call openFile here
								openFile(readBuf);					
							}

					})
					.setNegativeButton("Close File",

							new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Toast.makeText(getBaseContext(), "close file !",
									Toast.LENGTH_SHORT).show();
						}

					})
				

					.show();
	                
	                //remember reopen Socket before break 
	                if(PeerActivity.ALLOW_RECEIVE_FILE ==1){
	    	        	mFileService.start();// restart server when user click other tab/activity then come back
	    	        }else{
	    	        	mFileService.stop();
	    	        }
	                
	                break;               
	            case MESSAGE_START_RECEIVE:
	            	visible_progressbar_percenttext(pgbReceiveMyInstanceMsg, txtPercentReceiveMyInstanceMsg);
	                break;
	            case MESSAGE_PROGRESS_RECEIVE:
	            	String ss =(String) msg.obj;   	
	            	percentreceive = Integer.parseInt(ss);
	            	pgbReceiveMyInstanceMsg.setProgress(percentreceive); 
	            	txtPercentReceiveMyInstanceMsg.setText(" "+percentreceive +"%");
	            	  //when receive 100%, after 5s then invisible pgbReceiveMyInstanceMsg and txtPercentReceiveMyInstanceMsg
	 			   if(percentreceive ==100){		   
	 				   PeerActivity.handler.postDelayed((new Runnable() {
	 						public void run() {
	 							invisible_progressbar_percenttext(pgbReceiveMyInstanceMsg, txtPercentReceiveMyInstanceMsg);
	 						}
	 					}),5000);
	 			   }
	            	   break;                
            }
        }
    };    
    
    /**
     * This Handler is use to control sending progress bar
     */
	  Handler sendingHandle = new Handler(){

		   @Override
		   public void handleMessage(Message msg) {
			 
			       percentsend = (int) msg.getData().getLong("partsend");

			   txtPercentSendMyInstanceMsg.setText(" "+percentsend +"%");
			   pgbSendMyInstanceMsg.setProgress( percentsend); 
			   //when receive 100%, after 5s then invisible pgbSendMyInstanceMsg and txtPercentSendMyInstanceMsg
			   if(percentsend ==100){
				   
				   PeerActivity.handler.postDelayed((new Runnable() {
						public void run() {
							invisible_progressbar_percenttext(pgbSendMyInstanceMsg, txtPercentSendMyInstanceMsg);
						}
					}),5000);
				  
			   }
		   }
		  
		  };
		  
	private void visible_progressbar_percenttext(ProgressBar progressbar, TextView txt){
		progressbar.setVisibility(View.VISIBLE);
		txt.setVisibility(View.VISIBLE);
	}
	private void invisible_progressbar_percenttext(ProgressBar progressbar, TextView txt){
		progressbar.setVisibility(View.GONE);
		txt.setVisibility(View.GONE);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.myinstancemsg);
	
		
		spnaddress = (Spinner)findViewById(R.id.spnMyInstanceMsg);
		outMessage = (EditText)findViewById(R.id.edtOutMyInstanceMsg);
		outMessage.setOnEditorActionListener(mWriteListener);
		sendMessage = (Button)findViewById(R.id.btnSendMyInstanceMsg);
		sendFile = (Button)findViewById(R.id.btnSendFileMyInstanceMsg);
		pgbSendMyInstanceMsg=(ProgressBar)findViewById(R.id.pgbSendMyInstanceMsg);
		pgbSendMyInstanceMsg.setMax(100);
		txtPercentSendMyInstanceMsg = (TextView) findViewById(R.id.txtPercentSendMyInstanceMsg);
		pgbReceiveMyInstanceMsg=(ProgressBar)findViewById(R.id.pgbReceiveMyInstanceMsg);;
		pgbReceiveMyInstanceMsg.setMax(100);
		txtPercentReceiveMyInstanceMsg =(TextView) findViewById(R.id.txtPercentReceiveMyInstanceMsg);
		ltvDisplay = (ListView)findViewById(R.id.ltvMyInstanceMsg);
		
		if(PeerActivity.peer!=null){
			spnaddress.setOnItemSelectedListener(this);
			sendMessage.setOnClickListener(this);//button action
			sendFile.setOnClickListener(this);//button action						
		
			//load to spinner
			ArrayList<String> newaddresslist = new ArrayList<String>();
	        newaddresslist.addAll(PeerActivity.peer.getListAddressPeer());
	        String[] itemArray = new String[newaddresslist.size()];
	        String[] array = newaddresslist.toArray(itemArray);
	        ArrayAdapter<String> aa= new  ArrayAdapter<String>(this, android.R.layout.
	                simple_spinner_item,array);
	        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        spnaddress.setAdapter(aa);
	  
	        setupchat();  
	        updater.run();

		}//end if PeerActivity.peer!=null
		
		//do some more
	}//end onCreate
	
	 public void onStart() {
	        super.onStart();
	        
	        setupchat();
//	        loadtochatlistview();
	        refreshListView();
	      
	    }
	 
	 private void setupchat(){
		 this.adapter = new ListviewChatAdapter(this, android.R.layout.simple_list_item_1, PeerActivity.peer.chatlist);
		 refreshListView();//refresh listview
		  mFileService = new ServerFileService(this, handler); 
	        //restart server
	 }
	 
	 
	 public void refreshListView(){//before call this method, should update content of list
		 adapter.notifyDataSetChanged();//refresh listview
         ltvDisplay.setAdapter(adapter); 
         ltvDisplay.invalidateViews();
	 }
	 

	 @Override
	    public synchronized void onResume() {
	        super.onResume();
	        if(PeerActivity.ALLOW_RECEIVE_FILE ==1){
	        	mFileService.start();// restart server when user click other tab/activity then come back
	        }else{
	        	mFileService.stop();
	        }
	    }
//be careful when using onStop	
//	 public void onStop() {
//	    	super.onStop();
//			try {
//				// MAKE SURE YOU CLOSE THE SOCKET UPON EXITING
//				serverSocket.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//	    }
	  public void onDestroy() {
	        super.onDestroy();
	        // Stop the Broadcast chat services
	        if (mFileService != null) mFileService.stop();
	      
	    }
	    
	    
	  public void onItemSelected(AdapterView<?> parent,View v, int pos, long id) { //for spinner
		  toaddress=parent.getItemAtPosition(pos).toString();
		  Toast.makeText(parent.getContext(), toaddress,Toast.LENGTH_SHORT).show();
		  //clear chatlist when choose new address
		  PeerActivity.peer.ClearChatList(); 
	} 
	  
	  @Override
	  public void onNothingSelected(AdapterView<?> parent) { 

		  } 
	  
	 public void onClick(View v){
	        if(v instanceof Button){
	             switch(v.getId()){
	             case R.id.btnSendMyInstanceMsg:{
	            	 if(toaddress !=null){
	            		 sendMessage();
	         		 }else{
	         			Toast.makeText(MyInstanceMSg.this, "Please choose the peer chat!",Toast.LENGTH_SHORT).show();
	         		 }
	                  break;
	             }//end btnSend
	            
	             case R.id.btnSendFileMyInstanceMsg:{	
	            	 if(toaddress !=null){
	            		 Intent fileExplorer = new Intent(MyInstanceMSg.this,
		     						FileFolderExplorerActivity.class);
		     				startActivityForResult(fileExplorer, SEND_FILE_REQUEST_CODE);  //continue at onActivityforResult
	         		 }else{
	         			Toast.makeText(MyInstanceMSg.this, "Please choose the address before sending!",Toast.LENGTH_SHORT).show();
	         		 }
	     				
	     				     		
	                  break;
	             }//end btnSendFile
	             
	             }//end switch
	        }// end if -instance of Button
	        
	       }//end onClick
	/**
	 * Method sendMessage(), send message to toaddress
	 * add message to Chatlist (by method addChatList), then 
	 * automatic display update by listview
	 * Condition: make sure we has address before call this function
	 */
	private void sendMessage(){
		//
			
			String data = outMessage.getText().toString();
			if(data.length()>0){
				String time = PeerActivity.getcurrentTime();	
				String newdata = "Me: " + data;
				ChatData senddata = new ChatData(time, data );
				ChatData adddata = new ChatData(time, newdata );
//				Toast.makeText(this, toaddress,Toast.LENGTH_SHORT).show();
				PeerActivity.peer.addChatList(adddata);//add to chatlist
				PeerActivity.peer.chatToPeer(toaddress,senddata);//send
			}
			 outMessage.setText("");
			 refreshListView();
			 InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0); //dimiss soft keyboard
	}//end sendMessage
	
	/**
	 * This method help to send file_folder info to other peer
	 * @param folderinfo
	 * precondition: the ip-address is ready.
	 */
	private void sendFileMessage(FolderInfo folderinfo){ //use SIP

		PeerActivity.peer.fileInfoToPeer(toaddress, folderinfo);// ipaddress
																// format:
																// A@192.168.1.5:5556
			
			
		
	}//end sendFileMessage
	/**
	 * this method help to send a file to other peer
	 * 
	 * @param: filename: the file you want to send. 
	 * file_form: filepath_and_name. Such
	 *         as: "/sdcard/mysdfile.txt"
	 * toAddress format: xx.xx.xx.xx such as 192.168.1.5        
	 */
	private void sendFile(String filepath, String toAddress) {
		Thread cThread = new Thread(new ClientThread(filepath, toAddress,sendingHandle));
		cThread.start();
		
	}
	
	private void openFile(String filepath){
		Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        File file = new File(filepath);
       
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String ext=file.getName().substring(file.getName().indexOf(".")+1);
        String type = mime.getMimeTypeFromExtension(ext);
      
        intent.setDataAndType(Uri.fromFile(file),type);
       
        startActivity(intent);
	}
	/**
	 * refresh listview every 0.5 second, in order to display new message chat at listview
	 */
	private Runnable updater = new Runnable(){
		 public void run() {
			 	
		        //listview chat
//		         adapter = new ListviewChatAdapter(MyInstanceMSg.this, android.R.layout.simple_list_item_1, PeerActivity.peer.chatlist);
			 	 refreshListView();
		         handler.postDelayed(this, 500);//delay 0.5s then refresh
		 }//end run
		 };

//		 private void loadtochatlistview() { 
//			 list.clear();
//			 list.addAll(PeerActivity.peer.chatlist);
//		 }
	    // The action listener for the EditText widget, to listen for the return key
	    private TextView.OnEditorActionListener mWriteListener =
	        new TextView.OnEditorActionListener() {
	        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
	            // If the action is a key-up event on the return key, send the message
	            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
	                sendMessage();
	            }
	           
	            return true;
	        }
	    };
	    
	    /**
	     * Handle action after choose a file: send, read, or delete
	     * precondition: the toaddress has choosen (not null)
	     */
	    
	    @Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			final String file_path = data.getStringExtra("file_path");  // /sdcard/abc.txt
			final String filename = data.getStringExtra("file_name");  //abc.txt
			final String file_size= data.getStringExtra("file_size");
			final String file_type= data.getStringExtra("file_type");
			//extract address from toaddress A@192.168.1.8:5556  --> 192.168.1.8
			int atposition = toaddress.indexOf("@");
			int colonposition = toaddress.indexOf(":");
			final String address = toaddress.substring(atposition+1, colonposition);
					

//			if (resultCode == RESULT_OK && requestCode == DELETE_FILE_REQUEST_CODE) {
//				try {
//					File myFile1 = new File(file_path);
//					if (myFile1.exists()) {
//						myFile1.delete();
//					}
//				} catch (Exception e) {
//					Toast.makeText(getBaseContext(), e.getMessage(),
//							Toast.LENGTH_SHORT).show();
//				}
//			}

			if (resultCode == RESULT_OK && requestCode == SEND_FILE_REQUEST_CODE) {
				// send file here
				new AlertDialog.Builder(this)

				.setIcon(R.drawable.iconsend)
				.setTitle("[Are you sure to send " + filename + "] ?")
				

				.setPositiveButton("Yes",

						new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
						// send here, edit: send information of a file, if receive a MSG_ACCEPT_RECEIVE_FILE from receiver then start send file																				
							//send information of a file, message_File_control: INVITE_FILE
							FolderInfo folderinfo = new FolderInfo(filename,"INVITE_FILE",file_type,file_size);
							sendFileMessage(folderinfo); //send file_foler info to peer, use sip-udp
							percentsend = 0;
							visible_progressbar_percenttext(pgbSendMyInstanceMsg,txtPercentSendMyInstanceMsg);
							sendFile(file_path, address);	//send file, use tcp-package		
//							Thread cThread = new Thread(new ClientThread(file_path,address,sendingHandle));
//							cThread.start();
						}

				})
				.setNegativeButton("No",

						new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(getBaseContext(), "cancel send !",
								Toast.LENGTH_SHORT).show();
					}

				})
			

				.show();
			}// end if SEND_FILE_REQUEST_CODE
		}
	   
	    
	   ///////////////////////////////////////////////////////////////

	

	    
	    
	    
	    
	    
}//end class MyInstanceMSg



