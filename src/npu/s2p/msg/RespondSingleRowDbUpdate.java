

package npu.s2p.msg;

import it.unipr.ce.dsg.s2p.message.BasicMessage;
import it.unipr.ce.dsg.s2p.message.Payload;

import npu.database.SingleData;

public class RespondSingleRowDbUpdate extends BasicMessage{
	public static final String MSG_PEER_RESPONDSINGLEDATA="respond_single_contact"; 
	
	public RespondSingleRowDbUpdate(SingleData singlerow) {
		super(MSG_PEER_RESPONDSINGLEDATA, new Payload(singlerow));
		
	}
}
