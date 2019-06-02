package messages;

import app.AppInfo;
import bootstrap.BootstrapConfig;
import concurrent.Token;
import node.NodeInfo;

public class JoinResponseMessage extends BasicMessage {

	private static final long serialVersionUID = -333251402058492901L;
	private Token token;
	private int id = -1;
	
	public JoinResponseMessage(NodeInfo receiver, NodeInfo contact, String id) {
		super(MessageType.JOIN_RESPONSE, contact, AppInfo.bootstrapInfo, receiver, id);
		this.id = Integer.parseInt(id);
	}
	
	public JoinResponseMessage(NodeInfo receiver, String id, Token token) {
		super(MessageType.JOIN_RESPONSE, null, AppInfo.bootstrapInfo, receiver, id);
		this.token = token;
		this.id = Integer.parseInt(id);
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

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
