package messages.handlers;

import java.util.Random;

import app.AppInfo;
import bootstrap.BootstrapConfig;
import bootstrap.BootstrapWorker;
import helpers.Constants;
import messages.ExitAcceptedMessage;
import messages.JoinMessage;
import messages.JoinResponseMessage;
import messages.Message;
import messages.MessageType;
import messages.MessageUtil;
import messages.TokenMessage;
import messages.TokenRequest;
import node.NodeInfo;

public class ExitRequestHandler implements MessageHandler {

	private Message clientMessage;
	private BootstrapWorker bootstrap;
	
	public ExitRequestHandler(Message clientMessage, BootstrapWorker bootstrap) {
		this.clientMessage = clientMessage;
		this.bootstrap = bootstrap;
	}

	@Override
	public void run() {
		AppInfo.timestampedStandardPrint(clientMessage.toString());

		if (clientMessage.getMessageType() == MessageType.EXIT_REQUEST) {
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
			AppInfo.timestampedStandardPrint("Got the join lock for exit request");
			
			ExitAcceptedMessage exitAcceptedMess = new ExitAcceptedMessage(clientMessage.getSenderInfo());
			MessageUtil.sendMessage(exitAcceptedMess);
			
			
		} else {
			AppInfo.timestampedErrorPrint("Exit request handler got: " + clientMessage);
		}
	}
	

}
