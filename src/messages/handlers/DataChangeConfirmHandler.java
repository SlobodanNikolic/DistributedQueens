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
import messages.ConnectionCallbackMessage;
import messages.ConnectionMessage;
import messages.ExitGrantedMessage;
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

public class DataChangeConfirmHandler implements MessageHandler {

	private Message clientMessage;
	
	public DataChangeConfirmHandler(Message clientMessage) {
		this.clientMessage = clientMessage;
	}

	@Override
	public void run() {
		if (clientMessage.getMessageType() == MessageType.DATA_CHANGE_CONFIRM) {
			AppInfo.timestampedStandardPrint(clientMessage.toString());
			
			AppInfo.myInfo.addNeighbour(clientMessage.getSenderInfo());
			AppInfo.myInfo.removeDataChangeNode(clientMessage.getSenderInfo().getId());
			
			if(AppInfo.myInfo.getDataChangeNodes().size() == 0 && AppInfo.myInfo.getConnBreakNodes().size()==0) {
				ExitGrantedMessage exitGrantMess = new ExitGrantedMessage(AppInfo.myInfo, AppInfo.myInfo, AppInfo.myInfo.getExitNode());
				MessageUtil.sendMessage(exitGrantMess);
				
//				Menjamo svoj id
				AppInfo.myInfo.setId(AppInfo.myInfo.getExitNode().getId());
				AppInfo.myInfo.setIdBase3(AppInfo.myInfo.getExitNode().getIdBase3());
			}
			
		} else {
			AppInfo.timestampedErrorPrint("Data change confirm handler got: " + clientMessage);
		}
	}
	

}
