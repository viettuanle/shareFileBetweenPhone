package npu.s2p.msg;


import it.unipr.ce.dsg.s2p.message.BasicMessage;
import it.unipr.ce.dsg.s2p.message.Payload;
//import it.unipr.ce.dsg.s2p.peer.PeerListManager;

import npu.database.DataListManager;


public class DataMessage extends BasicMessage {
	public static final String MSG_PEER_DATA="database_contact";
//	public static final String MSG_PEER_SINGLEDATA="single_contact"; 

	public DataMessage(DataListManager dataList) {
		super(MSG_PEER_DATA, new Payload(dataList));
		
	}
	
	
	
}
