package npu.support.utility;

import java.nio.ByteBuffer;

public class Convert {
	public static byte[] long2bytearray(long l) {  
		  byte b[] = new byte[8];  
		   
		  ByteBuffer buf = ByteBuffer.wrap(b);  
		  buf.putLong(l);  
		  return b;  
		}  
		   
		public static byte[] int2bytearray(int i) {  
		  byte b[] = new byte[4];  
		   
		  ByteBuffer buf = ByteBuffer.wrap(b);  
		  buf.putInt(i);  
		  return b;  
		}  
		   
		public static long bytearray2long(byte[] b) {  
		  ByteBuffer buf = ByteBuffer.wrap(b);  
		  return buf.getLong();  
		}  
		   
		public static int bytearray2int(byte[] b) {  
		  ByteBuffer buf = ByteBuffer.wrap(b);  
		  return buf.getInt();  
		}  
}
