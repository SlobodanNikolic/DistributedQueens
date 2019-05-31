package messages.handlers;

import java.util.Random;

import app.AppInfo;
import bootstrap.BootstrapConfig;
import bootstrap.BootstrapWorker;
import helpers.Constants;
import messages.JoinMessage;
import messages.JoinResponseMessage;
import messages.Message;
import messages.MessageType;
import messages.MessageUtil;
import messages.TokenMessage;
import messages.TokenRequest;
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
			AppInfo.timestampedStandardPrint(clientMessage.toString());

			synchronized (AppInfo.joinLock) {
				
				if(AppInfo.myInfo.isBootstrap()) {
					
					if(AppInfo.myInfo.getToken().tokenAvailable.compareAndSet(true, false)) {
//						Got a join request. Find a random node and send it in a response
		//				First see if the list of nodes is empty. If it is, bootstrap has the token
		//				He sends a message with the token and node id to the node that requested to join
						if(bootstrap.getNodeCount() == 0) {
							
							JoinResponseMessage message = new JoinResponseMessage(clientMessage.getSenderInfo(), null, 
									AppInfo.myInfo.getToken(), "0");						
							AppInfo.myInfo.getToken().tokenAvailable.set(false);
							MessageUtil.sendMessage(message);
						}
						else {
							NodeInfo randomNode = bootstrap.randomNode();
							JoinResponseMessage message = new JoinResponseMessage(clientMessage.getSenderInfo(), randomNode, 
									null, bootstrap.getNodeCount() +"");						
							MessageUtil.sendMessage(message);
						}
						return;
					}
					
					while(true) {
						
		//				Ako token nije slobodan, posalji zahtev za tokenom
						if(bootstrap.getNodeCount()>0) {
							AppInfo.myInfo.setSequence(Constants.MAX_NODES);
							TokenRequest request = new TokenRequest(AppInfo.myInfo, AppInfo.myInfo, 
									AppInfo.myInfo.getSequenceNumber(Constants.MAX_NODES), bootstrap.randomNode());
							MessageUtil.sendMessage(request);
							break;
						}
//						TODO: thread sleep
					}
					
	//				Vrti se nad tokenom
					while(!AppInfo.myInfo.getToken().tokenAvailable.compareAndSet(true, false)) {
						
					}
					
					
	//				Got a join request. Find a random node and send it in a response
	//				First see if the list of nodes is empty. If it is, bootstrap has the token
	//				He sends a message with the token and node id to the node that requested to join
					if(bootstrap.getNodeCount() == 0) {
						
						JoinResponseMessage message = new JoinResponseMessage(clientMessage.getSenderInfo(), null, 
								AppInfo.myInfo.getToken(), "0");						
						AppInfo.myInfo.getToken().tokenAvailable.set(false);
						MessageUtil.sendMessage(message);
					}
					else {
						NodeInfo randomNode = bootstrap.randomNode();
						JoinResponseMessage message = new JoinResponseMessage(clientMessage.getSenderInfo(), randomNode, 
								null, bootstrap.getNodeCount() +"");						
						MessageUtil.sendMessage(message);
					}
					
				}
		
			}
			
			
		} else {
			AppInfo.timestampedErrorPrint("Join handler got: " + clientMessage);
		}
	}
	

}
