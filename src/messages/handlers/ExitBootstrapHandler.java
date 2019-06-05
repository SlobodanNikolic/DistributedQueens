package messages.handlers;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import app.AppInfo;
import bootstrap.BootstrapConfig;
import bootstrap.BootstrapWorker;
import helpers.Constants;
import helpers.Functions;
import messages.ConnectionBrakeMessage;
import messages.ConnectionCallbackMessage;
import messages.ConnectionMessage;
import messages.ExitAcceptedMessage;
import messages.ExitGrantedMessage;
import messages.Message;
import messages.MessageType;
import messages.MessageUtil;
import messages.NodeCountMessage;
import messages.PositionSetMessage;
import messages.SwitchPlacesMessage;
import node.NodeInfo;
import node.NodeListener;

public class ExitBootstrapHandler implements MessageHandler {

	private Message clientMessage;
	private BootstrapWorker bootstrap;
	
	public ExitBootstrapHandler(Message clientMessage, BootstrapWorker bootstrap) {
		this.clientMessage = clientMessage;
		this.bootstrap = bootstrap;
	}

	@Override
	public void run() {
		
		if (clientMessage.getMessageType() == MessageType.EXIT_GRANTED) {
			ExitGrantedMessage message = (ExitGrantedMessage)clientMessage;
			bootstrap.changeNodeInfo(message.getOriginalSenderInfo(), message.getSenderInfo());
			bootstrap.removeLastNode();
			
			for (NodeInfo node : bootstrap.getNodes()) {
				NodeCountMessage message2 = new NodeCountMessage(AppInfo.myInfo, node, bootstrap.getNodeCount()+"");
				MessageUtil.sendMessage(message2);
			}
//			Oslobadjamo join lock
			AppInfo.joinLock.set(true);
			
		} else {
			AppInfo.timestampedErrorPrint("Exit granted BOOTSTRAP handler got: " + clientMessage);
		}
	}
	

}
