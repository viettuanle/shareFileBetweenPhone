package npu.s2p.msg;



import it.unipr.ce.dsg.s2p.message.BasicMessage;
import it.unipr.ce.dsg.s2p.message.Payload;
import it.unipr.ce.dsg.s2p.peer.PeerDescriptor;


/**
 * Class <code>RequestDatabaseUpdate</code> implements a simple message sent by the peer to other peer.
 * The payload of request_message contains the peer descriptor.
 * 
 * @author Le, Tuan
 *
 */
public class RequestDatabaseUpdate extends BasicMessage {
	
	public static final String MSG_PEER_REQUEST_DATABASE="database_request"; 
	
	
	public RequestDatabaseUpdate(PeerDescriptor peerDesc) {
		
		super(MSG_PEER_REQUEST_DATABASE, new Payload(peerDesc));
	
	}

}

