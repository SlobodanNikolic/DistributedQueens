package messages.handlers;

import app.AppInfo;
import bootstrap.BootstrapConfig;
import messages.ConnectionCallbackMessage;
import messages.Message;
import messages.MessageType;
import messages.MessageUtil;
import messages.PositionSetMessage;
import node.NodeInfo;

public class ConnectionCallbackHandler implements MessageHandler {

	private Message clientMessage;
	
	public ConnectionCallbackHandler(Message clientMessage) {
		this.clientMessage = clientMessage;
	}

	@Override
	public void run() {

		
		if (clientMessage.getMessageType() == MessageType.CONNECTION_CALLBACK) {
			ConnectionCallbackMessage message = (ConnectionCallbackMessage)clientMessage;
			NodeInfo neighbour = message.getSenderInfo();
			
			AppInfo.myInfo.addNeighbour(neighbour);
			AppInfo.myInfo.removeNeighbourToContact(neighbour.getId());
			
			if(AppInfo.myInfo.getNeighborsToContact().size() == 0) {
				PositionSetMessage posSetMess = new PositionSetMessage(AppInfo.myInfo, AppInfo.bootstrapInfo);
				AppInfo.timestampedStandardPrint("I sent a position set message. My id is " + AppInfo.myInfo.getId());
				MessageUtil.sendMessage(posSetMess);
			}
			
		} else {
			AppInfo.timestampedErrorPrint("Ping handler got: " + clientMessage);
		}
	}

}
