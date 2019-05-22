package messages.handlers;

import app.AppInfo;
import bootstrap.BootstrapConfig;
import messages.Message;
import messages.MessageType;

public class PingHandler implements MessageHandler {

	private Message clientMessage;
	
	public PingHandler(Message clientMessage) {
		this.clientMessage = clientMessage;
	}

	@Override
	public void run() {
		if (clientMessage.getMessageType() == MessageType.PING) {
			AppInfo.getInstance().timestampedStandardPrint(clientMessage.toString());
		} else {
			AppInfo.getInstance().timestampedErrorPrint("Ping handler got: " + clientMessage);
		}
	}

}
