package npu.s2p.msg;

import it.unipr.ce.dsg.s2p.message.BasicMessage;
import it.unipr.ce.dsg.s2p.message.Payload;
import it.unipr.ce.dsg.s2p.peer.PeerDescriptor;



public class FileMessage extends BasicMessage{
	public static final String MSG_PEER_FILE="file_message"; 
	
//	public ChatMessage(ChatData chatdata) {
//		super(MSG_PEER_CHAT, new Payload(chatdata));
//		
//	}
	//public ChatMessage(SingleData peerDesc) {
//	
//	super(MSG_PEER_CHAT, new Payload(peerDesc));
//
//}  //y tuong, co the muong PeerDescriptor hoac SingleData de send. Ko biet tai sao dung 2 class nay thi OK, con dung ChatData thuan tuy thi ko duoc !!!	
// ben duoi la vi du de dung PeerDescriptor.
	public FileMessage(PeerDescriptor peerDesc) {
		
		super(MSG_PEER_FILE, new Payload(peerDesc));
	
	}
}
