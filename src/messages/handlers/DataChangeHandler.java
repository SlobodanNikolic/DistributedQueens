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
import messages.ChangeDataMessage;
import messages.ConnectionBrakeMessage;
import messages.ConnectionCallbackMessage;
import messages.ConnectionMessage;
import messages.DataChangeConfirmedMessage;
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

public class DataChangeHandler implements MessageHandler {

	private Message clientMessage;
	
	public DataChangeHandler(Message clientMessage) {
		this.clientMessage = clientMessage;
	}

	@Override
	public void run() {
		if (clientMessage.getMessageType() == MessageType.DATA_CHANGE) {
			AppInfo.timestampedStandardPrint(clientMessage.toString());
			
			ChangeDataMessage message = (ChangeDataMessage)clientMessage;

			NodeInfo newData = clientMessage.getSenderInfo();
			NodeInfo oldData = clientMessage.getOriginalSenderInfo();
			
			AppInfo.myInfo.changeNeighbourData(oldData, newData);
			DataChangeConfirmedMessage datChConMess = new DataChangeConfirmedMessage(AppInfo.myInfo, AppInfo.myInfo, newData);
			MessageUtil.sendMessage(datChConMess);
			
		} else {
			AppInfo.timestampedErrorPrint("Join handler got: " + clientMessage);
		}
	}
	

}
