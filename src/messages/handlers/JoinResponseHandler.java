package messages.handlers;

import app.AppInfo;
import bootstrap.BootstrapConfig;
import bootstrap.BootstrapWorker;
import concurrent.Token;
import messages.JoinMessage;
import messages.JoinResponseMessage;
import messages.Message;
import messages.MessageType;
import messages.MessageUtil;
import messages.PositionMessage;
import node.NodeInfo;

public class JoinResponseHandler implements MessageHandler {

	private Message clientMessage;
	
	public JoinResponseHandler(Message clientMessage) {
		this.clientMessage = clientMessage;
	}

//	A message from bootstrap - join request response
	@Override
	public void run() {
		if (clientMessage.getMessageType() == MessageType.JOIN_RESPONSE) {
//			He either sent us a token (we were first) and an id, or he is sending us a 
//			contact in the network. Either way, we should find our place and let bootstrap know
//			when its finished
			JoinResponseMessage message = (JoinResponseMessage)clientMessage;
			if(((Token)message.getResponseObject()) != null) {
				AppInfo.timestampedStandardPrint("We got a join response with " + ((Token)message.getResponseObject()));
			}
			else {
				AppInfo.timestampedStandardPrint("We got a join response with a contact " + message.getOriginalSenderInfo().toString());
				
			}
	
		} else {
			AppInfo.timestampedErrorPrint("Join handler got: " + clientMessage);
		}
	}
	

}
