package npu.support.adapter;


public class FolderInfo {
	private String  foldername;
	private String  folder_path;
	private String  type;// phan duoi sau dau cham.
	private String  folder_size;
	
	public FolderInfo(){
		//nothing
	}
	
	public FolderInfo(String foldername, String folder_path, String type){
		this.setFoldername(foldername);
		this.setPath(folder_path);
		this.setType(type);
		
	}
	public FolderInfo(String foldername, String folder_path, String type,String folder_size){
		this.setFoldername(foldername);
		this.setPath(folder_path);
		this.setType(type);
		this.setSize(folder_size);
	}
	public FolderInfo(FolderInfo folderinfo){
		this.foldername= folderinfo.getFoldername();
		this.folder_path = folderinfo.getPath();
		this.type = folderinfo.getType();
		this.folder_size= folderinfo.getSize();
	}
	
	 public void setFoldername(String foldername) {
	       this.foldername = foldername;
	   }
	 public void setPath(String folder_path) {
	       this.folder_path = folder_path;
	   }
	 public void setType(String type) {
	       this.type = type;
	   }
	 public void setSize(String size) {
	       this.folder_size = size;
	   }
	 
	 
	 public String getFoldername() {
	       return foldername;
	   }
	 public String getPath() {
	       return folder_path;
	   }
	 public String getType() {
	       return type;
	   }
	 public String getSize() {
	       return folder_size;
	   }
	 
	 @Override
	    public String toString() {

	        StringBuilder result = new StringBuilder();
	        String NEW_LINE = System.getProperty("line.separator");

	        
	        result.append("folder name  : " + getFoldername() + NEW_LINE);
	        result.append("path: " + getPath()+ NEW_LINE);
	        result.append("type: " + getType() + NEW_LINE);
	        result.append("size: " + getSize() + NEW_LINE);
	        return result.toString();
	    }
}//end class

