package npu.support.adapter;
import npu.s2p.activity.R;
import java.util.ArrayList;

import npu.s2p.msg.ChatData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.content.Context;

public class ListviewChatAdapter extends ArrayAdapter<ChatData> {

   public ArrayList<ChatData> list;
       public ListviewChatAdapter(Context context, int textViewResourceId,  ArrayList<ChatData> list1) {
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
//        	 LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           	v = vi.inflate(R.layout.row_chat, null);
         }
          ChatData chat = list.get(position);
          
          if (chat != null) {
				TextView messagecontent = (TextView) v.findViewById(R.id.rowchatmessage);
				TextView timechat = (TextView) v.findViewById(R.id.rowchattime);

				if (messagecontent != null) {
					messagecontent.setText(chat.getData().toString());
				}

				if(timechat != null) {
					timechat.setText(chat.getTimeStamp().toString());
				}
			}
			return v;
       }


}

