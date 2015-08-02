package npu.s2p.activity;

//import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;


public class My_peer_list extends ListActivity {
//	private TextView txtMyIpTabList;
//	private ListView livPeerList;
	public static final String PEER_ADDRESS="address";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		//check peer List first, if it doesn't have any thing, toast. otherwise display.
		int sizelist = PeerActivity.peer.getsizelist();
		if(sizelist ==0){
			Toast.makeText(this, "doesn't has any peer on the list! " , Toast.LENGTH_LONG)
			.show();
		}
		else{
			
			Toast.makeText(this, "there are "+ sizelist+ " peers on the list!" , Toast.LENGTH_LONG)
			.show();
		 setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, PeerActivity.peer.getListAddressPeer()));
		 
		  ListView lv = getListView();
		  lv.setTextFilterEnabled(true);

		  lv.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view,
		        int position, long id) {
		    	
		    	Bundle bundle = new Bundle();
		    	
		    	bundle.putString(PEER_ADDRESS, ((TextView) view).getText().toString());
		    	
		    	Intent mIntent = new Intent();
		    	mIntent.putExtras(bundle);
		    	setResult(RESULT_OK, mIntent);
		    	finish();
		    }
		  });
		}// end else
//		setContentView(R.layout.my_peer_list);
		
//		txtMyIpTabList = (TextView)findViewById(R.id.txtMyIpTabList);
//		livPeerList = (ListView)findViewById(R.id.livPeerList);
//		livPeerList.setOnItemClickListener(this);
		
//		if(PeerActivity.peer!=null){
//			txtMyIpTabList.setText(PeerActivity.peer.getAddressPeer().toString());
			
			//handle ListView
//			final ArrayAdapter<String> aa;
//			 aa = new ArrayAdapter<String>(this, R.layout.list_item, PeerActivity.peer.getListAddressPeer());	
//			 livPeerList.setAdapter(aa);
//			if(PeerActivity.peer.getListAddressPeer()!=null){
//			 setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, PeerActivity.peer.getListAddressPeer()));
//			}
//			else{
//				Toast.makeText(this, "doesn't has any address " , Toast.LENGTH_LONG)
//		        .show();
//			}
			
//			livPeerList.setAdapter(new  ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,PeerActivity.peer.getListAddressPeer()));
//			livPeerList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//			  ListView lv = getListView();
//			  lv.setTextFilterEnabled(true);
			
		}
		
//	}//end onCreate
	
}
