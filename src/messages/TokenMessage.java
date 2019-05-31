package messages;

import bootstrap.BootstrapConfig;
import concurrent.Token;
import node.NodeInfo;

public class TokenMessage extends BasicMessage {

	private static final long serialVersionUID = -333251402058492901L;
	private Token token;
	
	public TokenMessage(NodeInfo originalSender, NodeInfo sender, NodeInfo receiver, Token token) {
		super(MessageType.PING, originalSender, sender, receiver);
		this.token = token;
	}
	
	/**
	 * We want to take away our amount exactly as we are sending, so our snapshots don't mess up.
	 * This method is invoked by the sender just before sending, and with a lock that guarantees
	 * that we are white when we are doing this in Chandy-Lamport.
	 */
	@Override
	public void sendEffect() {
		
	}
	
	@Override
	public String toString() {
		String mtype = getMessageType().toString();
		String id = getMessageId() + "";
		String info = getOriginalSenderInfo().getId() + "";
		String info2 = getSenderInfo().getId() + "";
		String rec = getReceiverInfo().getId() + "";
		String txt = getMessageText();

		
		return "Message: " + getMessageType()
			+ "|" + getMessageId() + "|" + getOriginalSenderInfo().getId() 
			+ "|" + getSenderInfo().getId() + "|" + getReceiverInfo().getId() + "|" 
			+ getMessageText() + "|";
	}

	@Override
	public NodeInfo getResponseObject() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
