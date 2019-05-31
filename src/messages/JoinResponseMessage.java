package messages;

import app.AppInfo;
import bootstrap.BootstrapConfig;
import concurrent.Token;
import node.NodeInfo;

public class JoinResponseMessage extends BasicMessage {

	private static final long serialVersionUID = -333251402058492901L;
	private Token token;
	
	public JoinResponseMessage(NodeInfo receiver, NodeInfo contact, String id) {
		super(MessageType.JOIN_RESPONSE, contact, AppInfo.bootstrapInfo, receiver);
	}
	
	public JoinResponseMessage(NodeInfo receiver, NodeInfo contact, Token token, String id) {
		super(MessageType.JOIN_RESPONSE, contact, AppInfo.bootstrapInfo, receiver);
		this.token = token;
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
	public Object getResponseObject() {
		// TODO Auto-generated method stub
		return this.token;
	}
	
}
