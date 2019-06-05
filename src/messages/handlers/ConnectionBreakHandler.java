package messages.handlers;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import com.sun.beans.editors.IntegerEditor;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import app.AppInfo;
import bootstrap.BootstrapConfig;
import bootstrap.BootstrapWorker;
import helpers.Constants;
import helpers.Functions;
import messages.ConnectionBrakeMessage;
import messages.ConnectionBrokenMessage;
import messages.ConnectionCallbackMessage;
import messages.ConnectionMessage;
import messages.JoinMessage;
import messages.JoinResponseMessage;
import messages.Message;
import messages.MessageType;
import messages.MessageUtil;
import messages.PositionSetMessage;
import messages.TokenMessage;
import messages.TokenRequest;
import node.NodeInfo;
import sun.management.MemoryNotifInfoCompositeData;

public class ConnectionBreakHandler implements MessageHandler {

	private Message clientMessage;
	
	public ConnectionBreakHandler(Message clientMessage) {
		this.clientMessage = clientMessage;
	}

	@Override
	public void run() {
		
		if (clientMessage.getMessageType() == MessageType.CONNECTION_BREAK) {
			AppInfo.timestampedStandardPrint(clientMessage.toString());
			
			ConnectionBrakeMessage message = (ConnectionBrakeMessage)clientMessage;

			NodeInfo sender = clientMessage.getSenderInfo();
			AppInfo.myInfo.removeNeighbour(sender);
			
			ConnectionBrokenMessage message2 = new ConnectionBrokenMessage(AppInfo.myInfo, AppInfo.myInfo, message.getSenderInfo());
			MessageUtil.sendMessage(message2);
			
		} else {
			AppInfo.timestampedErrorPrint("Connection break handler got: " + clientMessage);
		}
	}
	

}
