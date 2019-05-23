package messages;

import app.AppInfo;
import bootstrap.BootstrapConfig;
import node.NodeInfo;

public class JoinMessage extends BasicMessage {

	private static final long serialVersionUID = -333251402058492901L;
	
	public JoinMessage() {
		super(MessageType.JOIN, AppInfo.getInstance().getMyInfo(), AppInfo.getInstance().getMyInfo(),
				AppInfo.getInstance().getBootstrapInfo());
	}
	
	public JoinMessage(NodeInfo receiver, NodeInfo contact) {
		super(MessageType.JOIN, contact, AppInfo.getInstance().getBootstrapInfo(), receiver);
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
			+ "|" + getMessageId() + "|" + getOriginalSenderInfo().getId() 
			+ "|" + getSenderInfo().getId() + "|" + getReceiverInfo().getId() + "|" 
			+ getMessageText() + "|";
	}
	
}
