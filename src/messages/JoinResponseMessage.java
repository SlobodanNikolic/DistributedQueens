package messages;

import app.AppInfo;
import bootstrap.BootstrapConfig;
import node.NodeInfo;

public class JoinResponseMessage extends BasicMessage {

	private static final long serialVersionUID = -333251402058492901L;
	private NodeInfo newCookie;
	
	public JoinResponseMessage(NodeInfo receiver, NodeInfo contact, NodeInfo newCookie) {
		super(MessageType.JOIN_RESPONSE, contact, AppInfo.getInstance().getBootstrapInfo(), receiver);
		this.newCookie = newCookie;
	}
	
	
	@Override
	public void sendEffect() {
		
	}
	
	@Override
	public String toString() {
		String mtype = getMessageType().toString();
		String id = getMessageId() + "";
		String info = "";
		if(getOriginalSenderInfo() != null)
			info = getOriginalSenderInfo().getId() + "";
		
		String info2 = getSenderInfo().getId() + "";
		String rec = getReceiverInfo().getId() + "";
		String txt = getMessageText();
 
		return "Message: " + mtype
			+ "|" + id + "|" + info
			+ "|" + info2 + "|" + rec + "|" 
			+ txt + "|";
	}

	@Override
	public NodeInfo getResponseObject() {
		// TODO Auto-generated method stub
		return this.newCookie;
	}
	
}
