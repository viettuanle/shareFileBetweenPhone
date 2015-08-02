package npu.s2p.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import npu.database.*;
import npu.s2p.service.BroadcastService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TabActivity;
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
//import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

/**
 * Class <code>PeerActivity</code> is the main activity where a new peer object
 * is instantiated and a ping message is sent.
 * 
 * 
 * @author Tuan Le
 * 
 */

public class PeerActivity extends TabActivity {

	public static final int PORT = 5556;

	public static SQLiteAdapter mySQLiteAdapter;
	public static int ALLOW_RECEIVE_FILE = 1; // Value 1 is allow, value 0 is
												// ban.
	private static final int MENU_ADDCONTACT = 1;
	private static final int MENU_VIEWCONTACT = 2;
	// private static final int MENU_LIST = 3;
	// private static final int MENU_SBC = 4;
	//
	// private static final int DIALOG_CONFIG = 5;
	// private static final int DIALOG_BOOTSTRAP = 6;
	//
	// private static final int ACTIVITY_PEER_LIST=7;

	public static Handler handler = new Handler();

	public static SimplePeer peer = null;


	// private Log log;
	// private FileHandler fileHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mySQLiteAdapter = new SQLiteAdapter(this);

		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
		TabSpec tabsend = tabHost.newTabSpec("tid1");
		TabSpec tabconfig = tabHost.newTabSpec("tid1");
		TabSpec tabjoin = tabHost.newTabSpec("tid1");
		TabSpec tablist = tabHost.newTabSpec("tid1");
		/** TabSpec setIndicator() is used to set name for the tab. */
		/** TabSpec setContent() is used to set content for a particular tab. */
		tabsend.setIndicator("Send",
				getResources().getDrawable(R.drawable.iconsend)).setContent(
				new Intent(this, Send.class));
		tabconfig.setIndicator("Configure",
				getResources().getDrawable(R.drawable.iconconfig)).setContent(
				new Intent(this, My_config_peer.class));
		tabjoin.setIndicator("Chat",
				getResources().getDrawable(R.drawable.iconjoin)).setContent(
				new Intent(this, MyInstanceMSg.class));
		tablist.setIndicator("List",
				getResources().getDrawable(R.drawable.iconlist)).setContent(
				new Intent(this, My_peer_list.class));
		/** Add tabSpec to the TabHost to display. */
		tabHost.addTab(tabsend);
		tabHost.addTab(tabconfig);  //just display first tab, hide all of them.
	//	tabHost.addTab(tabjoin);
	//	tabHost.addTab(tablist);
		tabHost.setCurrentTab(1);// display tabsend as default

	//call broadcast service
        callservicebroadcast();
	}// end onCreate

	// menu handle
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		boolean result = super.onCreateOptionsMenu(menu);
		// create menu
		MenuItem addcontact = menu.add(Menu.NONE, MENU_ADDCONTACT, 1,
				"Add Contact");
		addcontact.setIcon(R.drawable.config);
		MenuItem viewcontact = menu.add(Menu.NONE, MENU_VIEWCONTACT, 2,
				"View Contact");
		viewcontact.setIcon(R.drawable.boot);

		// MenuItem peerlist = menu.add(Menu.NONE, MENU_LIST, 3, "Peer List");
		// peerlist.setIcon(R.drawable.list);
		// MenuItem sbc = menu.add(Menu.NONE, MENU_SBC, 4, "SBC");
		// sbc.setIcon(R.drawable.sbc);

		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// select menu
		int id = item.getItemId();
		switch (id) {
		case MENU_ADDCONTACT:
			addContact();
			return true;
		case MENU_VIEWCONTACT:
			viewContact();
			return true;
			// case MENU_LIST:
			// viewPeerList();
			// return true;
			// case MENU_SBC:
			// contactSBC();
			// return true;

		}

		return super.onOptionsItemSelected(item);
	}

	// ////end Menu Handle

	private void init(String name) {

	
		String dateTimeNow = getcurrentTime();// ==> 2011/Oct/18 13:14:56
		peer = new SimplePeer(null, dateTimeNow, name, PORT); // each peer has
																// different
																// key, because
																// setup at
																// different
																// time
		// peer = new SimplePeer(null, "4654amv65d4as4d65a4", name, 5554);
		peer.setPeerActivity(this);

	}

	@Override
	protected void onStart() {
		super.onStart();
		if (peer == null) {
			init("A");// write function to get phone name or phone number -->
						// not work well because some carrier don't include
						// number in sim card
			// another way to solve --> get IMEL number:
			// TelephonyManager.getDeviceId()
			// addressPeer.setText("Address: "+peer.getAddressPeer());
			// addressPeer.setText("Address: "+peer.getAddressPeer()+peer.getBootstrapPeer()+"aa");
		}
	}

	//
	//
	//
	//
	// @Override
	// public void onConfigurationChanged(Configuration newConfig) {
	// super.onConfigurationChanged(newConfig);
	// setContentView(R.layout.main);
	// }// for avoid restart activity, but not work well
	@Override
	protected void onResume() {

		super.onResume();
		// addressPeer.setText("Address: "+peer.getAddressPeer());
		// contactAddressPeer.setText("Contact Address: "+peer.getContactAddressPeer());

	}

	public void onStop() {
		super.onStop();
		// this.finish();
		// this.moveTaskToBack(true);
	}

	public void onDestroy() {
		super.onDestroy();
		// this.finish();
		// this.moveTaskToBack(true);
	}

	//
	// private void contactSBC() {
	//
	// peer.contactSBC();
	//
	// }
	//
	// private void viewPeerList() {
	//
	// //call activity PeerList
	// Intent intent = new Intent(this, PeerListActivity.class);
	// startActivityForResult(intent, ACTIVITY_PEER_LIST);
	// }
	//
	// private void contactBootstrap() {
	//
	// if(peer.getBootstrapPeer()!=null){
	// Intent intent = new Intent(this, JoinPeerActivity.class);
	// startActivity(intent);
	// }
	// else
	// showDialog(DIALOG_CONFIG);
	// }
	//
	private void addContact() {

		// call activity ConfigPeer
		Intent intent = new Intent(this, Manual_add_contact.class);
		startActivity(intent);

	}

	private void viewContact() {

		// call activity ConfigPeer
		Intent intent = new Intent(this, View_contact.class);
		startActivity(intent);

	}

	//
	//
	//
	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	//
	// super.onActivityResult(requestCode, resultCode, data);
	//
	// Bundle extras = data.getExtras();
	//
	// switch(requestCode) {
	// case ACTIVITY_PEER_LIST:
	// String peerAddress = extras.getString(PeerListActivity.PEER_ADDRESS);
	// addressEdit.setText(peerAddress);
	// break;
	// }
	//
	// }
	//
	//
	//
	// @Override
	// protected Dialog onCreateDialog(int id) {
	// Dialog dialog;
	//
	// switch(id) {
	// case DIALOG_CONFIG:
	// AlertDialog.Builder builderConf = new AlertDialog.Builder(this);
	// builderConf.setMessage("Set peer configuration!")
	// .setTitle("Warning")
	// .setCancelable(false)
	// .setPositiveButton("Close", new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int id) {
	// dismissDialog(DIALOG_CONFIG);
	// }
	// });
	// dialog = builderConf.create();
	// break;
	// case DIALOG_BOOTSTRAP:
	// AlertDialog.Builder builderBoot = new AlertDialog.Builder(this);
	// builderBoot.setMessage("Set the address of BootstrapPeer in the Configuration section!")
	// .setTitle("Warning")
	// .setCancelable(false)
	// .setPositiveButton("Close", new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int id) {
	// dismissDialog(DIALOG_BOOTSTRAP);
	// }
	// });
	// dialog = builderBoot.create();
	// break;
	// default:
	// dialog = null;
	// }
	// return dialog;
	//
	//
	// }
	//
	public static String getcurrentTime() {
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy/MMM/dd HH:mm:ss");
		String dateTimeNow = formatter.format(currentDate.getTime());
		return dateTimeNow;
	}

    private void callservicebroadcast(){
        //service
        AlarmManager alarm ;
        PendingIntent pintentbroadcast;
        Calendar cal;
        cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 10);

        Intent intent = new Intent(this, BroadcastService.class);

        pintentbroadcast = PendingIntent.getService(this, 0, intent, 0);

        alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //for 30 seconds 30*1000
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                30*1000, pintentbroadcast);
    }
}