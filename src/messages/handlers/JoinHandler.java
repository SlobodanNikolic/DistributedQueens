package messages.handlers;

import app.AppInfo;
import bootstrap.BootstrapConfig;
import bootstrap.BootstrapWorker;
import messages.JoinMessage;
import messages.JoinResponseMessage;
import messages.Message;
import messages.MessageType;
import messages.MessageUtil;
import node.NodeInfo;

public class JoinHandler implements MessageHandler {

	private Message clientMessage;
	private BootstrapWorker bootstrap;
	
	public JoinHandler(Message clientMessage, BootstrapWorker bootstrap) {
		this.clientMessage = clientMessage;
		this.bootstrap = bootstrap;
	}

	@Override
	public void run() {
		if (clientMessage.getMessageType() == MessageType.JOIN) {
			
			if(AppInfo.getInstance().getMyInfo().isBootstrap()) {
				
//				Got a join request. Find a random node and send it in a response
				AppInfo.getInstance().timestampedStandardPrint(clientMessage.toString());
				
//				First see if the list of nodes is empty
				NodeInfo randomNode = null;
				if(bootstrap.getNodeCount() != 0) {
//					TODO: Q: Treba zakljucati uzimanje random nodea?
					randomNode = bootstrap.randomNode();		
				}
				
//				Q: Zakljucati i ovo?
				NodeInfo newCookie = bootstrap.addNode(clientMessage.getSenderInfo());
			
				sendJoinResponse(clientMessage.getSenderInfo(), randomNode, newCookie);
			}
			
			
		} else {
			AppInfo.getInstance().timestampedErrorPrint("Join handler got: " + clientMessage);
		}
	}
	
	public void sendJoinResponse(NodeInfo receiver, NodeInfo contact, NodeInfo newCookie) {
		JoinResponseMessage joinResponse = new JoinResponseMessage(receiver, contact, newCookie);
		MessageUtil.sendMessage(joinResponse);
	}

}
