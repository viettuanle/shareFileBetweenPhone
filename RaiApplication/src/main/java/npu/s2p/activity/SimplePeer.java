package npu.s2p.activity;

import java.util.ArrayList;
import java.util.Iterator;

//import android.content.Intent;
//import android.database.Cursor;
//import android.util.Log;
import android.widget.Toast;

import npu.s2p.msg.AckMessage;
import npu.s2p.msg.ChatData;
import npu.s2p.msg.ChatMessage;
import npu.s2p.msg.FileMessage;
import npu.s2p.msg.JoinMessage;
import npu.s2p.msg.PeerListMessage;
import npu.s2p.msg.PingMessage;
import npu.s2p.msg.DiscoveryMessage;
import npu.s2p.msg.PeerDiscovery;
import npu.s2p.msg.RequestBusinessCard;
//import npu.s2p.msg.DataMessage;
import npu.s2p.msg.SingleDataMessage;
import npu.s2p.msg.RequestDatabaseUpdate;
//import npu.s2p.msg.RespondSingleRowDbUpdate;

import it.unipr.ce.dsg.s2p.org.json.JSONException;
import it.unipr.ce.dsg.s2p.org.json.JSONObject;
import it.unipr.ce.dsg.s2p.peer.NeighborPeerDescriptor;
import it.unipr.ce.dsg.s2p.peer.Peer;
import it.unipr.ce.dsg.s2p.peer.PeerDescriptor;
import it.unipr.ce.dsg.s2p.sip.Address;

import npu.database.SingleData;
import npu.database.DataListManager;
import npu.support.adapter.FolderInfo;

//import npu.database.SQLiteAdapter;
/**
 * Class <code>SimplePeer</code> implements many features of a peer. SimplePeer
 * extend the Peer class of sip2peer.
 * 
 * 
 * @author Fabrizio Caramia
 * @Edit by Le, Tuan
 * 
 */

public class SimplePeer extends Peer {

	private Address bootstrapPeer = null;
	private PeerActivity peerActivity = null;
	protected DataListManager datalist = null;// use to contain all data receive
	protected ArrayList<ChatData> chatlist = null;// use to contain chat message
													// send and receive.in
													// verson 2
//	protected DataListManager mybusinesscard=null; //use to contain my info
	// need remove chatlist, and method related, move them to MyInstance.java
	// --> in order to make SimplePeer class more simple
	// private SQLiteAdapter mySQLiteAdapter;
	private String TimeStampofPeer = "a";

	// private SQLiteAdapter mySQLiteAdapter;
	public void setTimeStampofPeer(String aa) {
		TimeStampofPeer = aa;
	}

	public String getTimeStampofPeer() {
		return TimeStampofPeer;
	}

	public SimplePeer(String pathConfig, String key, String peerName,
			int peerPort) {
		super(pathConfig, key, peerName, peerPort);
		initdatalist();
		initChatdata();
	}

	// init for datalist then add data recevie to list: by method addDataList
	private void initdatalist() {
		this.datalist = new DataListManager();
//		this.mybusinesscard = new DataListManager();

	}

	// init for ChatManager: by method addDataChat
	private void initChatdata() {
		chatlist = new ArrayList<ChatData>();

	}

	public ArrayList<ChatData> getChatlist() {
		return chatlist;
	}

	// add data received to datalist
	public SingleData addDataList(SingleData sn) {

		SingleData data = null;
		data = new SingleData(sn);

		datalist.put(data.getPhone(), data);

		return data;
	}
	
	// add my business card 
//	public SingleData addBusinessCardList(SingleData sn) {
//
//		SingleData data = null;
//		data = new SingleData(sn);
//
//		mybusinesscard.put(data.getPhone(), data);
//
//		return data;
//	}

	// add chatdata received to chatlist
	public void addChatList(ChatData sn) {

		chatlist.add(sn);

	}

	public int chatlistsize() {
		return chatlist.size();
	}

	// clear all data of datalist

	public void ClearDataList() {
		datalist.clear();
	}

	public void ClearChatList() {
		chatlist.clear();
	}

	// check datalist is empty or not
	public boolean CheckDataListEmpty() {
		return datalist.isEmpty(); // true if empty
	}

	public String getAddressPeer() {

		return getAddress().getURL();
	}

	public String getContactAddressPeer() {

		return peerDescriptor.getContactAddress();
	}

	public ArrayList<String> getListAddressPeer() {

		ArrayList<String> addressList = new ArrayList<String>();

		Iterator<NeighborPeerDescriptor> iter = this.peerList.values()
				.iterator();

		PeerDescriptor peerDesc = new PeerDescriptor();

		// Integer sizeList = new Integer(this.peerList.size());

		while (iter.hasNext()) {

			peerDesc = (PeerDescriptor) iter.next();
			if(!addressList.contains(peerDesc.getContactAddress())) 	addressList.add(peerDesc.getContactAddress());

		}

		return addressList;
	}

	public ArrayList<String> getListPhone() {

		ArrayList<String> phoneList = new ArrayList<String>();

		Iterator<SingleData> iter = this.datalist.values().iterator();

		SingleData singleData = new SingleData();

		// Integer sizeList = new Integer(this.datalist.size());

		while (iter.hasNext()) {

			singleData = (SingleData) iter.next();
			phoneList.add(singleData.getPhone());

		}

		return phoneList;
	}

	/**
	 * 
	 * @return all data of database
	 */
	public ArrayList<SingleData> getDataList() {

		ArrayList<SingleData> List = new ArrayList<SingleData>();

		// Iterator<SingleData> iter = this.datalist.values().iterator();
		Iterator<SingleData> iter = PeerActivity.peer.datalist.values()
				.iterator();
		SingleData singleData = new SingleData();

		// Integer sizeList = new Integer(this.datalist.size());
		while (iter.hasNext()) {

			singleData = (SingleData) iter.next();
			List.add(singleData);

		}

		return List;
	}
//	public int getsizebusinesscard(){
//		Integer sizedatatlist = new Integer(this.mybusinesscard.size());
//		return sizedatatlist;
//	}

	/**
	 * 
	 * @return the number of other peers which it knows
	 */
	public int getsizelist() {
		Integer sizeList = new Integer(this.peerList.size());
		return sizeList;
	}

	/**
	 * 
	 * @return the number of temperaly data in datalist
	 */
	public int getsizedata() {
		Integer sizedatatlist = new Integer(this.datalist.size());
		return sizedatatlist;
	}

	public void businessToPeer(String address) {

		RequestBusinessCard newBusMsg = new RequestBusinessCard(peerDescriptor);

		// !!!!!!send to local address
		send(new Address(address), newBusMsg);

	}

	public void pingToPeer(String address) {

		PingMessage newPingMsg = new PingMessage(peerDescriptor);

		// !!!!!!send to local address
		send(new Address(address), newPingMsg);

	}
	
	public void ackToSender(String address) {

		AckMessage newAckMsg = new AckMessage(peerDescriptor);

		// !!!!!!send to local address
		send(new Address(address), newAckMsg);

	}

	public void discoveryPeer(String address, String timestamp) {

		PeerDiscovery newPeerDiscovery = new PeerDiscovery(
				peerDescriptor.getName(), peerDescriptor.getAddress(),
				peerDescriptor.getKey(), peerDescriptor.getContactAddress(),
				timestamp);

		DiscoveryMessage newDiscoveryMsg = new DiscoveryMessage(
				newPeerDiscovery);
		// !!!!!!send to local address
		send(new Address(address), newDiscoveryMsg);

	}

	public void requestUpdateToPeer(String address) {

		RequestDatabaseUpdate newResMsg = new RequestDatabaseUpdate(
				peerDescriptor);

		// !!!!!!send to local address
		send(new Address(address), newResMsg);

	}

	public void backToSender(String address) {
		// send back peer_list to sender
		PeerListMessage newpeerlist = new PeerListMessage(this.peerList);

		// !!!!!!send to local address
		send(new Address(address), newpeerlist);

	}

	public void forwardDiscoveryMessage(PeerDiscovery org, String address) {

		String time = org.getTimeStamp().toString();
		String originaladdress = org.getAddress().toString();
		String myaddress = getAddressPeer().toString();
		// String bootstrap = getBootstrapPeer().getURL();
		int size = this.getsizelist();
		ArrayList<String> newaddresslist = new ArrayList<String>();
		newaddresslist.addAll(getListAddressPeer());
		if (!time.contentEquals(getTimeStampofPeer())) {// not duplicate
			setTimeStampofPeer(time);
			for (int i = 0; i < size; i++) {
				// !!!!!!send to local address
				// forward to all except my_address and original address(sender)
				String newaddress = newaddresslist.get(i).toString();
				if (// different itself, original_address, bootstrap, sender
				(!newaddress.contentEquals(myaddress))
				// &&
				// (!newaddress.contentEquals(originaladdress)) &&
				// (!newaddress.contentEquals(bootstrap)) &&
				// (!newaddress.contentEquals(address))

				) {
					// continue send original PeerDiscovery to other

					DiscoveryMessage newDiscoveryMsg = new DiscoveryMessage(org);
					// !!!!!!send to local address
					send(new Address(newaddress), newDiscoveryMsg);
				}
			}// end for
		}// end if time
	}

	/*
	 * public void dataToPeer(String address){
	 * 
	 * //add value to data DataMessage data = new DataMessage(this.datalist);
	 * 
	 * //!!!!!!send to local address , choose other para to send global send(new
	 * Address(address), data); // it'ok to send but make sure < 512 byte }
	 */
	public void chatToPeer(String address, ChatData chat) {

		// ChatMessage data = new ChatMessage(chat);
		PeerDescriptor aa = new PeerDescriptor(chat.getData(),
				chat.getTimeStamp(), chat.getData());// muon Descriptor, hoac
														// SingleData de send
		ChatMessage data = new ChatMessage(aa);// will delete
		// !!!!!!send to local address , choose other para to send global
		send(new Address(address), data);

	}

	public void fileInfoToPeer(String address, FolderInfo folder) {

		PeerDescriptor aa = new PeerDescriptor(folder.getFoldername(),
				folder.getSize(), folder.getType());// muon Descriptor, hoac
													// SingleData de send
		FileMessage data = new FileMessage(aa);// theo thu tu la name, type va
												// size. Co the dung 1 thanh to
												// de phan biet OK_FILE_ACCEPT
												// hay DENY_FILE
		// !!!!!!send to local address , choose other para to send global
		send(new Address(address), data);
	}

	public void singleRowToPeer(String address, SingleData singlerow) {

		SingleDataMessage data = new SingleDataMessage(singlerow);

		// !!!!!!send to local address , choose other para to send global
		send(new Address(address), data);

	}

	/**
	 * This method will send all content of DataList to peer by sending all
	 * singlerow Note: if you want send the whole database, you should call
	 * fulldatalist in order to fill database to datalist
	 * 
	 * @param address
	 */
	public void allDataListToPeer(String address) {
		// !!!!!!send to local address , choose other para to send global
		Iterator<SingleData> iter = PeerActivity.peer.datalist.values()
				.iterator();
		SingleData singleData = new SingleData();

		// Integer sizeList = new Integer(this.datalist.size());
		while (iter.hasNext()) {

			singleData = (SingleData) iter.next();
			singleRowToPeer((String) address.toString(), singleData);

		}
	}

	/**
	 * This method will send all content of MyBusinesscard to peer by sending all
	 * singlerow 
	 * 
	 * @param address
	 */
	public void allBusinessCardToPeer(String address) {
		// !!!!!!send to local address , choose other para to send global
		Iterator<SingleData> iter = PeerActivity.peer.datalist.values()
				.iterator();
		SingleData singleData = new SingleData();

		// Integer sizeList = new Integer(this.datalist.size());
		while (iter.hasNext()) {

			singleData = (SingleData) iter.next();
			if(singleData.getNotes().equals("yes")){
				singleRowToPeer((String) address.toString(), singleData);
			}
		}
	}

	
	public void joinToPeer(Address address) {

		JoinMessage newJoinMsg = new JoinMessage(peerDescriptor);

		// !!!!!!send to local address
		send(new Address(address), newJoinMsg);

		/*
		 * send global JoinMessage peerMsg = new JoinMessage(peerDescriptor);
		 * 
		 * send(new Address(toAddress), new Address(contactAddress), peerMsg);
		 */
	}

	public void setConfiguration(String sbc, String bootstrap,
			String reachability) {

		nodeConfig.sbc = sbc;
		nodeConfig.test_address_reachability = reachability;
		setBootstrapPeer(new Address(bootstrap));

	}

	public void setConfiguration(String bootstrap) {

		setBootstrapPeer(new Address(bootstrap));

	}

	public void contactSBC() {

		requestPublicAddress();

	}

	public Address getBootstrapPeer() {
		return bootstrapPeer;
	}

	private void setBootstrapPeer(Address bootstrapPeer) {
		this.bootstrapPeer = bootstrapPeer;
	}

	public String getSBCAddress() {

		return nodeConfig.sbc;
	}

	@Override
	protected void onReceivedJSONMsg(JSONObject jsonMsg, Address sender) {

		try {

			JSONObject params = jsonMsg.getJSONObject("payload").getJSONObject(
					"params");

			if (jsonMsg.get("type").equals(PeerListMessage.MSG_PEER_LIST)) {

				PeerActivity.handler.post(new Runnable() {
					public void run() {
						Toast toast = Toast.makeText(
								peerActivity.getBaseContext(), "Received: "
										+ PeerListMessage.MSG_PEER_LIST,
								Toast.LENGTH_LONG);
						toast.show();
					}
				});

				Iterator<String> iter = params.keys();

				while (iter.hasNext()) {

					String key = (String) iter.next();

					JSONObject keyPeer = params.getJSONObject(key);
					PeerDescriptor neighborPeerDesc = new PeerDescriptor(
							keyPeer.get("name").toString(), keyPeer.get(
									"address").toString(), keyPeer.get("key")
									.toString());

					if (keyPeer.get("contactAddress").toString() != "null")
						neighborPeerDesc.setContactAddress(keyPeer.get(
								"contactAddress").toString());

					addNeighborPeer(neighborPeerDesc);

					// Integer size = new Integer(this.peerList.size());

				}

			}// end if
			/*
			 * //handle singleData , not use because it's two big package.
			 * if(jsonMsg.get("type").equals(DataMessage.MSG_PEER_DATA)){
			 * 
			 * PeerActivity.handler.post(new Runnable() { public void run() {
			 * Toast toast =
			 * Toast.makeText(peerActivity.getBaseContext(),"Received: data"+
			 * DataMessage.MSG_PEER_DATA ,Toast.LENGTH_LONG); toast.show(); }
			 * });
			 * 
			 * 
			 * Iterator<String> iter = params.keys();
			 * 
			 * 
			 * 
			 * while(iter.hasNext()){
			 * 
			 * String key = (String) iter.next();
			 * 
			 * JSONObject datarow = params.getJSONObject(key); SingleData
			 * newsingledata = new SingleData( datarow.get("rowid").toString(),
			 * datarow.get("version").toString(),
			 * datarow.get("types").toString(),
			 * datarow.get("givenname").toString(),
			 * datarow.get("middlename").toString(),
			 * datarow.get("familyname").toString(),
			 * datarow.get("gender").toString(),
			 * datarow.get("spinphone").toString(),
			 * datarow.get("phone").toString(),
			 * datarow.get("spinemail").toString(),
			 * datarow.get("email").toString(),
			 * datarow.get("spinim").toString(), datarow.get("im").toString(),
			 * datarow.get("spinaddr").toString(),
			 * datarow.get("street").toString(),
			 * datarow.get("pobox").toString(), datarow.get("city").toString(),
			 * datarow.get("state").toString(),
			 * datarow.get("zipcode").toString(),
			 * datarow.get("country").toString(),
			 * datarow.get("spinsns").toString(), datarow.get("sns").toString(),
			 * datarow.get("spinorg1").toString(),
			 * datarow.get("org1").toString(),
			 * datarow.get("spinorg2").toString(),
			 * datarow.get("org2").toString(), datarow.get("notes").toString(),
			 * datarow.get("time").toString(), datarow.get("photo").toString()
			 * 
			 * );
			 * 
			 * //add to list row, compare before add to database -->done
			 * addDataList(newsingledata);
			 * 
			 * PeerDescriptor neighborPeerDesc = new
			 * PeerDescriptor(params.get("name").toString(),
			 * params.get("address").toString(), params.get("key").toString(),
			 * params.get("contactAddress").toString());
			 * addNeighborPeer(neighborPeerDesc);
			 * 
			 * // later, if whant to get list, do similar getListAddressPeer()
			 * // Integer size = new Integer(this.datalist.size());
			 * 
			 * }
			 * 
			 * 
			 * }//end if
			 */

			if (jsonMsg.get("type").equals(
					SingleDataMessage.MSG_PEER_SINGLEDATA)) {
				PeerActivity.handler.post(new Runnable() {
					public void run() {
						Toast toast = Toast.makeText(
								peerActivity.getBaseContext(),
								"Received: "
										+ SingleDataMessage.MSG_PEER_SINGLEDATA,
								Toast.LENGTH_LONG);
						toast.show();
					}
				});
				// get content of package
				SingleData newsingledata = new SingleData(params.get("rowid")
						.toString(), params.get("version").toString(), params
						.get("types").toString(), params.get("givenname")
						.toString(), params.get("middlename").toString(),
						params.get("familyname").toString(), params.get(
								"gender").toString(), params.get("spinphone")
								.toString(), params.get("phone").toString(),
						params.get("spinemail").toString(), params.get("email")
								.toString(), params.get("spinim").toString(),
						params.get("im").toString(), params.get("spinaddr")
								.toString(), params.get("street").toString(),
						params.get("pobox").toString(), params.get("city")
								.toString(), params.get("state").toString(),
						params.get("zipcode").toString(), params.get("country")
								.toString(), params.get("spinsns").toString(),
						params.get("sns").toString(), params.get("spinorg1")
								.toString(), params.get("org1").toString(),
						params.get("spinorg2").toString(), params.get("org2")
								.toString(), params.get("notes").toString(),
						params.get("time").toString(), params.get("photo")
								.toString());
				newsingledata.setNotes("no"); //to let machine knowns that the info is not itself (not your business card)
				addDataList(newsingledata);
				//return my business card to sender
				// ArrayList<SingleData> mybusinesscard = getDataList();
//				 for(int i=0;i<mybusinesscard.size();i++){
//						 //send my business card back to sender   
//						 singleRowToPeer((String)sender.toString(), mybusinesscard.get(i));
//					
//				 }

			}// end if --MSG_PEER_SINGLEDATA

			if (jsonMsg.get("type").equals(RequestBusinessCard.MSG_PEER_BUSINESS)) {
				PeerActivity.handler.post(new Runnable() {
					public void run() {
						Toast toast = Toast.makeText(
								peerActivity.getBaseContext(), "Received: "
										+ RequestBusinessCard.MSG_PEER_BUSINESS,
								Toast.LENGTH_LONG);
						toast.show();
					}
				});

		
				
//				Send xx = new Send();
//
//				xx.fulldatalist();
//				xx.invitebusinesscard((String)sender.toString());
				allBusinessCardToPeer((String)sender.toString());
//				ClearDataList();// after finish, should clear datalist


			}// end if --MSG_PEER_PING
			
			if (jsonMsg.get("type").equals(PingMessage.MSG_PEER_PING)) {
				PeerActivity.handler.post(new Runnable() {
					public void run() {
						Toast toast = Toast.makeText(
								peerActivity.getBaseContext(), "Received: "
										+ PingMessage.MSG_PEER_PING,
								Toast.LENGTH_LONG);
						toast.show();
					}
				});

				PeerDescriptor neighborPeerDesc = new PeerDescriptor(params
						.get("name").toString(), params.get("address")
						.toString(), params.get("key").toString(), params.get(
						"contactAddress").toString());
				addNeighborPeer(neighborPeerDesc);
				// send ACK to sender
				ackToSender((String) sender.toString());
				
				

			}// end if --MSG_PEER_PING

			if (jsonMsg.get("type").equals(AckMessage.MSG_PEER_ACK)) {
				PeerActivity.handler.post(new Runnable() {
					public void run() {
						Toast toast = Toast.makeText(
								peerActivity.getBaseContext(), "Received: "
										+ AckMessage.MSG_PEER_ACK,
								Toast.LENGTH_LONG);
						toast.show();
					}
				});

				PeerDescriptor neighborPeerDesc = new PeerDescriptor(params
						.get("name").toString(), params.get("address")
						.toString(), params.get("key").toString(), params.get(
						"contactAddress").toString());
				addNeighborPeer(neighborPeerDesc);

			}// end if --MSG_PEER_ACK

			if (jsonMsg.get("type").equals(ChatMessage.MSG_PEER_CHAT)) {

				// note: be carefull with params.get("timestamp").toString() -->
				// if it not properly, can not addChatlist but we don't predict
				// this problem clearly
				// //note: neu chi can 1 dong lenh ko tra ve ket qua dung (vi du
				// params.get("msg").toString();) thi tu do ve sau cac lenh khac
				// se ko thuc hien, ko hieu tai sao
				// //note: muon PeerDescriptor de send message
				String username = sender.getHost().toString();// use ipaddress
																// for user name
				String receivemsg = username + ": "
						+ params.get("name").toString();
				ChatData newdata2 = new ChatData(params.get("address")
						.toString(), receivemsg);
				addChatList(newdata2);

			}

			if (jsonMsg.get("type").equals(FileMessage.MSG_PEER_FILE)) {
				// tuong tu nhu MSG_PEER_CHAT, //note: muon PeerDescriptor de
				// send message

				// String username = sender.getHost().toString();// use
				// ipaddress for user name

				final String msg_toast = sender.getHost().toString()
						+ " wants to send a file to you,\nfile name: "
						+ params.get("name").toString() + "\ntype: "
						+ params.get("key").toString() + "\nsize: "
						+ params.get("address").toString();

				{// file_invite ==INVITE_FILE
					// aler a Dialog, if click yes then start listenning, server
					// start, prepare receive and send ACCEPTF_FILE message,
					// if click NO, send back DENY_FILE message to sender.
					PeerActivity.handler.post(new Runnable() {
						public void run() {
							Toast.makeText(peerActivity.getBaseContext(),
									msg_toast, Toast.LENGTH_LONG).show();
						}
					});

				}// end file_invite = INVITE_FILE

				// String message_file_control =
				// params.get("contactAddress").toString();//muon contactAddress
				// de xem thong diep giua 2 peers
				// if(message_file_control.equals("ACCEPT_FILE")){
				// //send file to sender
				// }else if(message_file_control.equals("DENY_FILE")){
				//
				// }

			}// end MSG_PEER_FILE

			if (jsonMsg.get("type").equals(JoinMessage.MSG_PEER_JOIN)) {
				PeerActivity.handler.post(new Runnable() {
					public void run() {
						Toast toast = Toast.makeText(
								peerActivity.getBaseContext(), "Received: "
										+ JoinMessage.MSG_PEER_JOIN,
								Toast.LENGTH_LONG);
						toast.show();
					}
				});

				PeerDescriptor neighborPeerDesc = new PeerDescriptor(params
						.get("name").toString(), params.get("address")
						.toString(), params.get("key").toString(), params.get(
						"contactAddress").toString());
				addNeighborPeer(neighborPeerDesc);
				// send back to sender the peerlist
				backToSender((String) sender.toString());
			}// end if --JoinMessage.MSG_PEER_JOIN

			if (jsonMsg.get("type").equals(DiscoveryMessage.MSG_PEER_DISCOVERY)) {// +
																					// respond
																					// to
																					// sender
																					// its
																					// address
																					// +
																					// forward
																					// to
																					// all
																					// others
																					// peer
																					// (except
																					// itself
																					// and
																					// sender)
																					// discovery_query
				PeerActivity.handler.post(new Runnable() {
					public void run() {
						Toast toast = Toast.makeText(
								peerActivity.getBaseContext(), "Received: "
										+ DiscoveryMessage.MSG_PEER_DISCOVERY,
								Toast.LENGTH_LONG);
						toast.show();
					}
				});

				PeerDescriptor originalDesc = new PeerDescriptor(params.get(
						"name").toString(), params.get("address").toString(),
						params.get("key").toString(), params.get(
								"contactAddress").toString());

				PeerDiscovery newpeerdiscovery = new PeerDiscovery(
						originalDesc, params.get("timestamp").toString());

				addNeighborPeer(originalDesc);// if not in list, addtolist

				// String orgname = originalDesc.getName().toString();
				// String orgadd = originalDesc.getAddress().toString();
				// String orgkey = originalDesc.getKey().toString();
				// String orgcontactAddress =
				// originalDesc.getContactAddress().toString();

				// send back to original the peerlist
				ackToSender(originalDesc.getAddress().toString());

				String time = newpeerdiscovery.getTimeStamp().toString();
				if (!time.contentEquals(getTimeStampofPeer())) {
					setTimeStampofPeer(newpeerdiscovery.getTimeStamp()
							.toString()); // make sure not duplicate forward
					// forward to other
					forwardDiscoveryMessage(newpeerdiscovery,
							(String) sender.toString());
				}
			}// end if --DiscoveryMessage

			if (jsonMsg.get("type").equals(
					RequestDatabaseUpdate.MSG_PEER_REQUEST_DATABASE)) {// toast>
																		// send
																		// all
																		// database
				PeerActivity.handler.post(new Runnable() {
					public void run() {
						Toast toast = Toast.makeText(
								peerActivity.getBaseContext(),
								"Received: "
										+ RequestDatabaseUpdate.MSG_PEER_REQUEST_DATABASE,
								Toast.LENGTH_LONG);
						toast.show();
					}
				});

				PeerDescriptor neighborPeerDesc = new PeerDescriptor(params
						.get("name").toString(), params.get("address")
						.toString(), params.get("key").toString(), params.get(
						"contactAddress").toString());
				addNeighborPeer(neighborPeerDesc);// if the peer which send
													// request doesn't has in
													// peer_list, then add to
													// peerlist
				// send back to sender all datalist, note datalist could
				// different database,
				// if datalist different database, need to go send interface to
				// refresh
				// ackToSender((String)sender.toString());
				Send xx = new Send();

				xx.fulldatalist();
				allDataListToPeer((String) sender.toString());
				ClearDataList();// after finish, should clear datalist
			}// end if --RequestDatabaseUpdate

		} catch (JSONException e) {

			e.printStackTrace();
		}

	}

	public void setPeerActivity(PeerActivity peerActivity) {
		this.peerActivity = peerActivity;

	}

	@Override
	protected void onDeliveryMsgFailure(String arg0, Address arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDeliveryMsgSuccess(String arg0, Address arg1, String arg2) {
		// TODO Auto-generated method stub

	}

}
