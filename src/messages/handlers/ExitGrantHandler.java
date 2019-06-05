package messages.handlers;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import app.AppInfo;
import bootstrap.BootstrapConfig;
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
import messages.PositionSetMessage;
import messages.SwitchPlacesMessage;
import node.NodeInfo;
import node.NodeListener;

public class ExitGrantHandler implements MessageHandler {

	private Message clientMessage;
	private NodeListener listener;
	
	public ExitGrantHandler(Message clientMessage, NodeListener listener) {
		this.clientMessage = clientMessage;
		this.listener = listener;
	}

	@Override
	public void run() {
		
		if (clientMessage.getMessageType() == MessageType.EXIT_GRANTED) {
			ExitGrantedMessage message = (ExitGrantedMessage)clientMessage;
			listener.getCli().stop();
			listener.stop();
		} else {
			AppInfo.timestampedErrorPrint("Exit granted handler got: " + clientMessage);
		}
	}
	

}
