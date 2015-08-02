package npu.s2p.msg;

import it.unipr.ce.dsg.s2p.message.BasicMessage;
import it.unipr.ce.dsg.s2p.message.Payload;

import npu.database.SingleData;

public class SingleDataMessage extends BasicMessage{
	public static final String MSG_PEER_SINGLEDATA="single_contact"; 
	
	public SingleDataMessage(SingleData singlerow) {
		super(MSG_PEER_SINGLEDATA, new Payload(singlerow));
		
	}
}
