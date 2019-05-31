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
import node.NodeListener;

public class TokenRequestHandler implements MessageHandler {

	private Message clientMessage;
	
	
	public TokenRequestHandler(Message clientMessage) {
		this.clientMessage = clientMessage;
	}

	@Override
	public void run() {
		if (clientMessage.getMessageType() == MessageType.TOKEN_REQUEST) {
			
				AppInfo.timestampedStandardPrint(clientMessage.toString());

//				Got a token request.
				
			
			
			
		} else {
			AppInfo.timestampedErrorPrint("Token request handler got: " + clientMessage);
		}
	}
	
}
