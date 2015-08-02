package npu.s2p.activity;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import npu.support.utility.Convert;

import android.content.Context;
import android.os.Handler;

public class ServerFileService {
	  // Member fields

    private final Handler mHandler;
    private ServerThread mConnectedThread;
    Context mContext ;
    private long filelength;
    
    public ServerFileService(Context context, Handler handler) {
        //mAdapter = BluetoothAdapter.getDefaultAdapter();
    	mContext = context;
        mHandler = handler;
    }
    
    
 
    
    /**
     * Start the chat service. Specifically start ComThread to begin 
     * listening incoming broadcast packets. 
     */
    public synchronized void start() {
      
        
        mConnectedThread = new ServerThread();
        mConnectedThread.start();
    }
    
    /**
     * Stop thread
     */
    public synchronized void stop() {
       
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}
    }
    //=======================================================================================
    
    public class ServerThread extends Thread{
    	final int PORT = PeerActivity.PORT +1;
    	private ServerSocket serverSocket; //for sever part (receive file)
    	
    	//constructor
    	public ServerThread(){
    		try { 
     		    		   
    			serverSocket = new ServerSocket(PORT);
     	   
     	     } catch (IOException e) { 
//     	    	 Log.e("error socket", "Could not make socket", e); 
     	     } 
    	}// end constructor
    	
//    	public void stopServer(){
//    		if (serverSocket.isBound()){
//    			try {
//    				serverSocket.close();
//    			} catch (Exception e) {
//    				// TODO Auto-generated catch block
//    				e.printStackTrace();
//    			}
//    		}
//    	}
    	
    	public void run() {
    		try {
    			
    				byte[] bfilename = new byte[30];
    				String get30;
    				byte[] bfilelength = new byte[8];
    				
//    				serverSocket = new ServerSocket(PORT);
    				String filename = "mysdfile.txt";// default
    				String filepath = "/sdcard/" + filename;
    				File myFile = new File(filepath);// create text file
    				myFile.createNewFile();


    				FileOutputStream fOut = new FileOutputStream(myFile);

    				BufferedOutputStream bos = new BufferedOutputStream(fOut);

    				while (true) {
    					// LISTEN FOR INCOMING CLIENTS
    					Socket client = serverSocket.accept();

    					mHandler.obtainMessage(MyInstanceMSg.MESSAGE_START_RECEIVE,-1,-1)
    	    			.sendToTarget();

    					try {
    						InputStream is = client.getInputStream();// receive
    																	// byte
    																	// stream30(is,bos1);
    						is.read(bfilename, 0, bfilename.length);// lay vao
    																// 30 byte
    																// dau tien --> filename
    						//lay tiep 8 byte --> filelength
    						is.read(bfilelength,0,bfilelength.length);
    						filelength = Convert.bytearray2long(bfilelength);
    						
    						stream(is, bos);// lay so byte con lai, cat vao bos    						

    						get30 = new String(bfilename);
    					
    					
    						  
    						cancel();//close socketserver, even though not close socketserver, it can not get next file because of error Address already in use
    						break; // quit while de chi nhan 1 socket thoi

    					} catch (Exception e) {
//    						handler.post(new Runnable() {
//    							@Override
//    							public void run() {
//    								serverStatus
//    										.setText("Oops. Connection interrupted. Please reconnect your phones.");
//    							}
//    						});
    						e.printStackTrace();
    					}
    				}// end while(true)
    				
    				get30=get30.trim();
    	
    				// kiem tra da co ten file hay chua, neu co roi the them 1
    				// vao cuoi ten file,  sau do doi ten file
    				//rename file in order to have same filename at sender,if exist then increase 1
    				String filepathnew = "/sdcard/" + get30;  //"sdcard/abc.txt"
    				int dotposition = get30.lastIndexOf(".");
    				String filenamewithout_ext = get30.substring(0, dotposition);  // "abc"
    				String ext = get30.substring(dotposition, get30.length());// ".txt"
    				File myFile1 = new File(filepathnew);
    				int i = 0;
    				while (myFile1.exists()) {
    					i++;
    					filepathnew = "/sdcard/" + filenamewithout_ext + "("+i+")"+ ext;
    					myFile1 = new File(filepathnew);
    					
    				}
    				
    				if (myFile.exists()) {
    					myFile.renameTo(myFile1);
    					
    				}
    				
    				
//    				final String xx=get30;
//
//					mHandler.post(new Runnable() {
//						@Override
//						public void run() {
//
//							Toast.makeText(mContext,"receive file: " + xx,
//									Toast.LENGTH_LONG).show();
//						}
//					});
					mHandler.obtainMessage(MyInstanceMSg.MESSAGE_READ,-1,-1, filepathnew)
	    			.sendToTarget();
    				
    				
    				fOut.close();
    				bos.flush();
    				bos.close();

    				
    							
    	
    		} catch (final Exception e) {
//    			handler.post(new Runnable() {
//    				@Override
//    				public void run() {
//    					serverStatus.setText("Error: " + e.getMessage());
//    				}
//    			});
    			e.printStackTrace();
    		}
    	}
    	
    	 public void cancel() {
             try {
            	 serverSocket.close();
             } catch (Exception e) {
//                 Log.e("close socket", "close() of connect socket failed", e);
             }
         }
    	/*
    	 * copy inputstream to outputstrea, useful when send a large file
    	 */
    	public  void stream(InputStream in, OutputStream out)
    			throws IOException {
    		byte[] buf = new byte[1024];
    		int bytesRead = 0;
    		long tmp = 38;
			long tmp_percent ;
    		try {

    			while (-1 != (bytesRead = in.read(buf, 0, buf.length))) {
    				out.write(buf, 0, bytesRead);//write file
    				tmp += bytesRead;
    				tmp_percent = 	(long)100*tmp/(38+filelength);
    				mHandler.obtainMessage(MyInstanceMSg.MESSAGE_PROGRESS_RECEIVE,-1,-1, tmp_percent+"")
	    			.sendToTarget();
    			}

    		} catch (IOException e) {
    			// log.error("Error with streaming op: " + e.getMessage());
    			throw (e);
    		} finally {
    			try {
    				in.close();
    				out.flush();
    				out.close();
    			} catch (Exception e) {
    			}// Ignore
    		}
    	}

    	
    }// end class ServerThread
}// end class ServerFileService
