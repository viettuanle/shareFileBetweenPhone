package npu.support.adapter;


import npu.s2p.activity.R;//to let it recognizes id of icon/textview, listview
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewFolderAdapter extends ArrayAdapter<FolderInfo> {

	   public ArrayList<FolderInfo> list;
	       public ListViewFolderAdapter(Context context, int textViewResourceId,  ArrayList<FolderInfo> list1) {
	           super(context, textViewResourceId, list1);
	           this.list = list1;     
	       }  
	  
	       @Override 
	       public int getCount() { 
	           return list.size();  
	       }
	 
	  
	       @Override
	       public long getItemId(int position) {
	           return 0; 
	       }
	      
	    
	       @Override
	       public View getView(int position, View convertView, ViewGroup viewGroup) {
	    	   View v = convertView;        
	         if (v == null) {
	           LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	        	 LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	           	v = vi.inflate(R.layout.row_folder_file, null);
	         }
	          FolderInfo folder = list.get(position);
	          
	          if (folder != null) {
					TextView filefoldername = (TextView) v.findViewById(R.id.filefoldername);
					TextView path = (TextView) v.findViewById(R.id.filesize);
					ImageView image = (ImageView)v.findViewById(R.id.avatar);
					
					if (filefoldername != null) {
						filefoldername.setText(folder.getFoldername().toString());
					}

					if(path != null) {//new la folder thi hien thi duong dan, neu la file thi hien thi size (va lastmodified)
						if(folder.getType() =="folder") path.setText(folder.getPath().toString());
						if(folder.getType() =="file") path.setText(folder.getSize()); 
					}
					if(image != null) {//display fileimage or folderimage
						if(folder.getType() =="file") image.setImageResource(R.drawable.file);
						if(folder.getType() =="folder") image.setImageResource(R.drawable.folder);
					}
				}
				return v;
	       }

}
