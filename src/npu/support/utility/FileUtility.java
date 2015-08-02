package npu.support.utility;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
//the filepath should be  "/abc/xss/filename.xyz"
/**
 * 
 * @author Tuan Le
 * The purpose of this file are:
 * - format file size for human read.
 * - get file name, file type, last modified
 */


public class FileUtility {
	private String filepath;
	private File file;
	
	public FileUtility(String filepath){
		this.filepath= filepath;
		this.file = new File(this.filepath);
	}
	
	
	public String getFileSize(){		
		if(file.exists()){
			return readableFileSize(file.length());
		}else return "not a file";
	}
	
	public String getLastModified(){
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		return sdf.format(file.lastModified());
	}
	
	public String getFileName(){
		return file.getName();
	}
	
	public String getExt(){
		 int dotposition= filepath.lastIndexOf(".");	     
	     String ext = filepath.substring(dotposition + 1, filepath.length());
	     return ext;
	}
	
	public String getFilenameWithoutExt(){
		
		int lastSlashposition = filepath.lastIndexOf("/");
		int dotposition= filepath.lastIndexOf(".");	
		return filepath.substring(lastSlashposition+1,dotposition);
		
	}
	public void rename(String newfilepath){
		//precondition: new file is not exist
		File myFile1 = new File(newfilepath);
		file.renameTo(myFile1);
		
	}
	 public  String readableFileSize(long size) {
	        if(size <= 0) return "0";
	        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
	        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
	        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	    }//end readableFileSize
}
