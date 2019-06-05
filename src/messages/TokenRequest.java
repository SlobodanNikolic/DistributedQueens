package messages;

import app.AppInfo;
import bootstrap.BootstrapConfig;
import node.NodeInfo;

public class TokenRequest extends BasicMessage {

	private static final long serialVersionUID = -333251402058492901L;
	
	public TokenRequest(NodeInfo originalSender, NodeInfo sender, int forWho, NodeInfo receiver) {
		super(MessageType.TOKEN_REQUEST, originalSender, sender, receiver, forWho+"");		
	}
		
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
