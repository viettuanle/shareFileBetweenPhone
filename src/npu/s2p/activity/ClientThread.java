package npu.s2p.activity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import npu.support.utility.Convert;
/*
 * @author: Le Tuan
 * @class ClientThread
 * Condition: set pathFile, and toAddress before using run() method
 * Run:  first 30 byte will is filename, the rest has file content.
 */

public class ClientThread implements Runnable {
	// constructor
	final int PORT = PeerActivity.PORT +1;
	String pathFile;
	String toAddress;//192.168.1.5
	private  Handler mHandler;
	public ClientThread(Handler h) {
		mHandler =h;
		this.pathFile = "/sdcard/mysdfile.txt"; // default filename_and_its_path
	}

	public ClientThread(String path,Handler h) {
		mHandler =h;
		this.pathFile = path;
	}

	public ClientThread(String path, String toAddress,Handler h) {
		this.pathFile = path;
		this.toAddress = toAddress;
		mHandler =h;
	}

	public void setPathFile(String pathFile) {
		this.pathFile = pathFile;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public void run() {
		try {
			InetAddress serverAddr = InetAddress.getByName(toAddress);
			Log.d("ClientActivity", "C: Connecting...");
			Socket socket = new Socket(serverAddr, PORT);// SERVERPORT =5557
																				

			try {
				Log.d("ClientActivity", "C: Sending command.");
				// send here
					
				int lastslashposition = pathFile.lastIndexOf("/");
				String filename_with_ext = pathFile.substring(
						lastslashposition + 1, pathFile.length());// abc.txt
				byte[] bytefilename = filename_with_ext.getBytes();
				byte[] byte_prepare = new byte[38];// dung de chua byte of
													// filename (30 bytes) va filelength(8 bytes)
				// vi ko biet ten file dai hay ngan nen chi can chep 30 byte, neu
				// dai hon thi cat bo
				if (30 < bytefilename.length) {
					System.arraycopy(bytefilename, 0, byte_prepare, 0,30);
				} else {
					System.arraycopy(bytefilename, 0, byte_prepare, 0,	bytefilename.length);
				}

				File myFile = new File(pathFile);
				
				byte[] bytefilelengh = Convert.long2bytearray(myFile.length());//8 byte
				System.arraycopy(bytefilelengh, 0, byte_prepare, 30, 8);
				FileInputStream fIn = new FileInputStream(myFile);
				BufferedInputStream bis = new BufferedInputStream(fIn);
				OutputStream os = socket.getOutputStream();// prepare send
				// 30byte dau tien chua filename, 8 byte ke tiep chua filelength
				os.write(byte_prepare, 0, byte_prepare.length);// send 38 byte
																// cua filename

				long tmp = 38;
				long tmp_percent ;
				
				int count;
				byte data[] = new byte[1024];
				while ((count = bis.read(data)) != -1) {
					os.write(data, 0, count);// send byte
					
					tmp += count;
					tmp_percent = 	(long)100*tmp/(38+myFile.length());
					 try{
                		 Message msg = mHandler.obtainMessage();
                         Bundle b = new Bundle();
                         b.putLong("partsend", tmp_percent);
                         msg.setData(b);
               
                         mHandler.sendMessage(msg);   
                         
                        }
                        catch(Throwable t){
                        }
					// System.out.println(tmp);
				}

				os.flush();
				socket.close();
				Log.d("ClientActivity", "C: Sent.");
			} catch (Exception e) {
				Log.e("ClientActivity", "S: Error", e);
			}

			socket.close();
			Log.d("ClientActivity", "C: Closed.");
		} catch (Exception e) {
			Log.e("ClientActivity", "C: Error", e);

		}
	}
}
