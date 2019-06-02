package messages;

import bootstrap.BootstrapConfig;
import node.NodeInfo;

public class ConnectionCallbackMessage extends BasicMessage {

	private static final long serialVersionUID = -333251402058492901L;
	
	public ConnectionCallbackMessage(NodeInfo sender, NodeInfo receiver) {
		super(MessageType.CONNECTION_CALLBACK, sender, sender, receiver);
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
			+ "|" + getOriginalSenderInfo().getId() 
			+ "|" + getSenderInfo().getId() + "|" + getReceiverInfo().getId() + "|" 
			+ getMessageText() + "|";
	}

	@Override
	public NodeInfo getResponseObject() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
