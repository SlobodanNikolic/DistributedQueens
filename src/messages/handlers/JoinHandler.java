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

			while(!AppInfo.joinLock.compareAndSet(true, false)) {
//				Cekamo u petlji TODO: sleep
				
			}
			NodeInfo newCookie = clientMessage.getSenderInfo();
			
			if(bootstrap.getNodeCount() == 0) {
				JoinResponseMessage responseMessage = new JoinResponseMessage(newCookie, "0", AppInfo.myInfo.getToken());
				MessageUtil.sendMessage(responseMessage);
				
			}else {
				NodeInfo randomNode = bootstrap.randomNode();
				JoinResponseMessage responseMessage = new JoinResponseMessage(newCookie, randomNode, bootstrap.getNodeCount()+"");
				MessageUtil.sendMessage(responseMessage);
			}
			
			
		} else {
			AppInfo.timestampedErrorPrint("Join handler got: " + clientMessage);
		}
	}
	

}
