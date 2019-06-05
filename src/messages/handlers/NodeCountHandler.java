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
import messages.ConnectionCallbackMessage;
import messages.ConnectionMessage;
import messages.JoinMessage;
import messages.JoinResponseMessage;
import messages.Message;
import messages.MessageType;
import messages.MessageUtil;
import messages.NodeCountMessage;
import messages.PositionSetMessage;
import messages.TokenMessage;
import messages.TokenRequest;
import node.NodeInfo;
import sun.management.MemoryNotifInfoCompositeData;

public class NodeCountHandler implements MessageHandler {

	private Message clientMessage;
	
	public NodeCountHandler(Message clientMessage) {
		this.clientMessage = clientMessage;
	}

	@Override
	public void run() {
		if (clientMessage.getMessageType() == MessageType.NODE_COUNT) {
			AppInfo.timestampedStandardPrint(clientMessage.toString());
			
			NodeCountMessage message = (NodeCountMessage)clientMessage;
			int nodeCount = Integer.parseInt(message.getMessageText());
			AppInfo.timestampedStandardPrint("Got a node count message. Node count: " + nodeCount);
			AppInfo.nodeCount = nodeCount;
			
		} else {
			AppInfo.timestampedErrorPrint("Node count handler got: " + clientMessage);
		}
	}

}
