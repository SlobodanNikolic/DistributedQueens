package messages.handlers;

import app.AppInfo;
import bootstrap.BootstrapConfig;
import bootstrap.BootstrapWorker;
import messages.JoinMessage;
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
//				Q: Zakljucati i ovo?
				if(bootstrap.getNodeCount() == 0) {
					
				}
				
	//			TODO: Q: Treba zakljucati uzimanje random nodea?
				NodeInfo randomNode = bootstrap.randomNode();
				sendJoinResponse(clientMessage.getSenderInfo(), randomNode);
			}
			else {
//				Got a join response, with NodeInfo of the node that we need to contact
				NodeInfo nodeToContact = clientMessage.getOriginalSenderInfo();
				System.out.println("Got a join response. Node to contact ip: " + nodeToContact.getIp()
				+ " port: " + nodeToContact.getPort());
			}
			
		} else {
			AppInfo.getInstance().timestampedErrorPrint("Join handler got: " + clientMessage);
		}
	}
	
	public void sendJoinResponse(NodeInfo receiver, NodeInfo contact) {
		JoinMessage joinResponse = new JoinMessage(receiver, contact);
		MessageUtil.sendMessage(joinResponse);
	}

}
