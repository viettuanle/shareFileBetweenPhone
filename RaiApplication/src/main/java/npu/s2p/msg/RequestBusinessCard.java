

package npu.s2p.msg;




import it.unipr.ce.dsg.s2p.message.BasicMessage;
import it.unipr.ce.dsg.s2p.message.Payload;
import it.unipr.ce.dsg.s2p.peer.PeerDescriptor;


/**
 * Class <code>AckMessage</code> implements a simple message sent by the peer to other peer.
 * The payload of AckMessage contains the peer descriptor.
 * 
 * @author Le, Tuan
 *
 */
public class RequestBusinessCard extends BasicMessage {
public static final String MSG_PEER_BUSINESS="peer_business_card"; 
	
	
	public RequestBusinessCard(PeerDescriptor peerDesc) {
		
		super(MSG_PEER_BUSINESS, new Payload(peerDesc));
	
	}
}
