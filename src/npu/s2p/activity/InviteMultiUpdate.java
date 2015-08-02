package npu.s2p.activity;


import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
import android.widget.ListView;
//import android.widget.Toast;
import android.view.View.OnClickListener;
import android.view.View;
import android.content.Intent;
import java.util.ArrayList;

public class InviteMultiUpdate extends Activity{
	ListView listView;
	Button btnOK, btnCancel;
	String array[];

//	private String PORT=":5556";


	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_request_invite);
        btnOK = (Button)findViewById(R.id.btnMultiRequestInvite);
        btnCancel = (Button)findViewById(R.id.btnCancelMultiRequestInvite);
        //get data into arraylist, then convert arralylist to array
        ArrayList<String> newaddresslist = new ArrayList<String>();
		newaddresslist.addAll(PeerActivity.peer.getListAddressPeer());
        String[] itemArray = new String[newaddresslist.size()];
        String[] array = newaddresslist.toArray(itemArray);
			
        listView = (ListView)findViewById(R.id.lstMultiRequestInvite);
        /**
         * Setting ListView adapter using AttayAdapter.
         */

        listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,array));
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        
        
        btnOK.setOnClickListener(new OnClickListener(){
        	@Override
      	   public void onClick(View v) {
      		     // send to all selected
      		     sendMulticastInvite();
      		     Intent intent= getIntent();
      		     String msg = intent.getStringExtra("inviteupdate");
      		     msg = msg + getNumberofSelect();
      		     intent.putExtra("datareturn", msg);//return previous class the number of selected peers
      		     setResult(RESULT_OK, intent); 
      		     finish();
      	   }
      	  });
        
        
        btnCancel.setOnClickListener(new OnClickListener() {
 
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                setResult(RESULT_CANCELED, intent);   
                finish();
            }
        });
    }//end onCreate
	
	/**
	 * This method will send the whole database to all peers which have choosen.
	 */
	private void sendMulticastInvite(){//note: the previous class has to called fulldatalist() before call this method 
	    for (long checkedItemId : getCheckItemIds()) {
	    	String singleaddress= listView.getItemAtPosition((int) checkedItemId).toString();//singleaddress = A@192.168.1.23:5556
	    	//invite other peer receive package, only invite the ip-address which has checked,
//	    	PeerActivity.peer.allDataListToPeer(singleaddress);//PORT will use for which group is currently used  	 //will replace by another list
	    	
	    	PeerActivity.peer.allBusinessCardToPeer(singleaddress);
	    	PeerActivity.peer.businessToPeer(singleaddress);
	    	
	    	
	    	
	   	 PeerActivity.peer.businessToPeer(singleaddress);
	   	 Send send = new Send();
	   	 send.fulldatalist();
		 PeerActivity.peer.allBusinessCardToPeer(singleaddress);
	    }
		
	   }
	/**
	 * @return number of item selected
	 * Note: This function only use with a string, it will occur error if you require a int 
	 */
	private int getNumberofSelect(){
	    
	    return listView.getCheckItemIds().length;
	   
	   }
/**
 * 
 * @return array of all positions which have checked
 */
    private long[] getCheckItemIds() {
        long[] ids = null;
        ArrayList<Integer> checkedIds = new ArrayList<Integer>();
 
    
        int childCount = listView.getChildCount();
 
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            if (listView.isItemChecked(childIndex))
                checkedIds.add(childIndex);
        }
 
        childCount = checkedIds.size();
        ids = new long[checkedIds.size()];
 
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            ids[childIndex] = checkedIds.get(childIndex);
        }
 
        return ids;
    }
}
