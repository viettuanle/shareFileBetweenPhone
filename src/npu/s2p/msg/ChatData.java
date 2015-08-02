package npu.s2p.msg;

public class ChatData {
	private String  timestamp;
	private String  msg;
	
	public ChatData(){
		//nothing
	}
	public ChatData(String time, String msg){
		this.setTimeStamp(time);
		this.setData(msg);
	}
	public ChatData(ChatData chatdata){
		this.timestamp=chatdata.getTimeStamp();
		this.msg = chatdata.getData();
	}
	
	 public void setTimeStamp(String timestamp) {
	       this.timestamp = timestamp;
	   }
	 public void setData(String data) {
	       this.msg = data;
	   }
	 public String getTimeStamp() {
	       return timestamp;
	   }
	 public String getData() {
	       return msg;
	   }
	 
	  @Override
	    public String toString() {

	        StringBuilder result = new StringBuilder();
	        String NEW_LINE = System.getProperty("line.separator");

	        
	        result.append("timestamp  : " + getTimeStamp() + NEW_LINE);
	        result.append("msg: " + getData()+ NEW_LINE);
	        
	        return result.toString();
	    }
	    
}
