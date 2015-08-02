package npu.s2p.msg;


import it.unipr.ce.dsg.s2p.message.BasicMessage;
import it.unipr.ce.dsg.s2p.message.Payload;
//import it.unipr.ce.dsg.s2p.peer.PeerDescriptor;


/**
 * Class <code>DiscoveryMessage</code> implements a simple message sent by the peer to other peer.
 * The payload of DiscoveryMessage contains the peer descriptor.
 * 
 * @author Le, Tuan
 *
 */

public class DiscoveryMessage extends BasicMessage {
	
	public static final String MSG_PEER_DISCOVERY="peer_discovery"; 
	
	public DiscoveryMessage(PeerDiscovery peerDesc) {
		
		super(MSG_PEER_DISCOVERY, new Payload(peerDesc));
	
	}
	
	
}//end class DiscoveryMessage





