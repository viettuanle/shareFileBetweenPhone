package npu.s2p.activity;

import npu.s2p.activity.R;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;

/**
 * Class <code>PeerListActivity</code> manage a list of peers.
 * 
 * 
 * @author Fabrizio Caramia
 *
 */


public class PeerListActivity extends ListActivity {
	
	
	public static final String PEER_ADDRESS="address";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  

		  

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
	}

}
