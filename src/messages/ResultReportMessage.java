package messages;

import java.util.ArrayList;

import app.AppInfo;
import bootstrap.BootstrapConfig;
import node.NodeInfo;

public class ResultReportMessage extends BasicMessage {

	private static final long serialVersionUID = -333251402058492901L;
	
	
	public ResultReportMessage(NodeInfo originalSender, NodeInfo sender, NodeInfo receiver, String jobResult) {
		super(MessageType.RESULT_REPORT, originalSender, sender, receiver, jobResult);
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
