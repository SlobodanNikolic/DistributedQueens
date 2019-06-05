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
		AppInfo.timestampedStandardPrint(clientMessage.toString());

		if (clientMessage.getMessageType() == MessageType.JOIN) {
			AppInfo.timestampedStandardPrint(clientMessage.toString());

			while(!AppInfo.joinLock.compareAndSet(true, false)) {
				try {
					AppInfo.timestampedStandardPrint("Lock blocked.");
					Random random = new Random();
					Thread.sleep(random.nextInt(3000)+1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			AppInfo.timestampedStandardPrint("Got the join lock");

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
