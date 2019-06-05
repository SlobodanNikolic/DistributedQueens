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
import messages.PositionSetMessage;
import messages.TokenMessage;
import messages.TokenRequest;
import node.NodeInfo;
import sun.management.MemoryNotifInfoCompositeData;

public class ConnectionHandler implements MessageHandler {

	private Message clientMessage;
	
	public ConnectionHandler(Message clientMessage) {
		this.clientMessage = clientMessage;
	}

	@Override
	public void run() {
		if (clientMessage.getMessageType() == MessageType.CONNECT) {
			AppInfo.timestampedStandardPrint(clientMessage.toString());
			
			ConnectionMessage message = (ConnectionMessage)clientMessage;

			NodeInfo sender = clientMessage.getSenderInfo();
			NodeInfo originalSender = clientMessage.getOriginalSenderInfo();
			int recieverId = Integer.parseInt(clientMessage.getMessageText());
//			Da li sam ja taj kome je upucena poruka?
			if(AppInfo.myInfo.getId() == recieverId) {
				AppInfo.timestampedStandardPrint("This connect message is for me.");
//				Stavljam original sendera u listu svojih komsija i saljem njemu
//				direktno poruku sa svojim podacima, kako bi stavio i on mene u listu svojih
				AppInfo.myInfo.addNeighbour(originalSender);
				
//				TODO: saljem poruku
				ConnectionCallbackMessage posMessage = new ConnectionCallbackMessage(AppInfo.myInfo, originalSender);
				MessageUtil.sendMessage(posMessage);
			}else {
				AppInfo.timestampedStandardPrint("This connect message is not ment for me.");

//				Nije meni upucena poruka. Nadji najpodobnijeg za dalje slanje
				NodeInfo receiver = checkTarget(recieverId);
				if(receiver != null) {
					AppInfo.timestampedStandardPrint("Receiver for this message should be " + receiver.getId());
//					Nasli smo najboljeg, salji njemu
					ConnectionMessage message2 = new ConnectionMessage(originalSender, AppInfo.myInfo, receiver, recieverId+"");
					MessageUtil.sendMessage(message2);
				}else {
//					Doslo je do greske
					AppInfo.timestampedErrorPrint("Greska pri pronalazenju najboljeg za dalje slanje connect poruke");
				}
				
			}
			
		} else {
			AppInfo.timestampedErrorPrint("Join handler got: " + clientMessage);
		}
	}
	
	
	public NodeInfo checkTarget(int recieverId) {
		int[] recieverIdBase3 = Functions.decToBase3(recieverId);
		int[] myIdBase3 = AppInfo.myInfo.getIdBase3();
//		Da li sam 0?
		if(myIdBase3[Constants.ID_MAX_DIGITS-1]==0) {
//			Jesam 0. Da li je on moj komsija?
			if(isHeMyNeighbour(recieverId) != null) {
//				Jeste, saljem njemu
				return isHeMyNeighbour(recieverId);
			}else {
//				Nije. Da li imam nekog iz tog komsiluka?
				ArrayList<NodeInfo> neighbourhood = getNeighbourhood(recieverId);
				if(neighbourhood.size() > 0) {
//					Imam. Saljem najblizem njemu
					return closestInNeighbourhood(neighbourhood, recieverId);
				}else {
//					Nemam. Saljem najstarijoj nuli
					return minNodeId(getZeroNeighbors());
				}
			}
			
		}else {
//			Nisam 0, saljem poruku svojoj nuli
			ArrayList<NodeInfo> zeroNeighbours = getZeroNeighbors();
			if(zeroNeighbours.size() == 1) {
				return zeroNeighbours.get(0);
			}
			else {
				AppInfo.timestampedErrorPrint("Nemam ni jednog nula komsiju, a ja sam list???");
				return null;
			}
		}
	}
	
	public NodeInfo closestInNeighbourhood(ArrayList<NodeInfo> neighbors , int id) {
		int minDistance = Integer.MAX_VALUE;
		NodeInfo closest = null;
		for (NodeInfo node : neighbors) {
			int distance = java.lang.Math.abs(node.getId() - id);
			if(distance < minDistance) {
				minDistance = distance;
				closest = node;
			}
		}
		return closest;
	}
	
	public NodeInfo minNodeId(ArrayList<NodeInfo> nodes) {
		NodeInfo minNode = null;
		int minId = Integer.MAX_VALUE;
		for (NodeInfo node : nodes) {
			if(node.getId()<=minId) {
				minId = node.getId();
				minNode = node;
			}
		}
		return minNode;
	}
	
	public ArrayList<NodeInfo> getNeighbourhood(int id){
		int[] idBase3 = Functions.decToBase3(id);
		ArrayList<NodeInfo> result = new ArrayList<NodeInfo>();
		
		for (NodeInfo node : AppInfo.myInfo.getNeighbors()) {
			int[] neighborsId = node.getIdBase3();
			for(int i = 0; i < neighborsId.length; i++) {
				if(neighborsId[i] != 0 || idBase3[i] != 0) {
					if(neighborsId[i] == idBase3[i]) {
						result.add(node);
					}else {
						break;
					}
				}
			}
//			TODO:Ispitati slucaj kad je u komsiluku 0: specijalni slucaj
//			Ipak verovatno ne treba, jer se salje najstarijoj nuli.
		}
		return result;
	}
	
	public NodeInfo isHeMyNeighbour(int receiverId) {
		for (NodeInfo neigh : AppInfo.myInfo.getNeighbors()) {
			if(neigh.getId() == receiverId)
				return neigh;
		}
		return null;
	}
	
	public ArrayList<NodeInfo> getZeroNeighbors() {
		
		ArrayList<NodeInfo> result = new ArrayList<NodeInfo>();
		CopyOnWriteArrayList<NodeInfo> neighbors = AppInfo.myInfo.getNeighbors();
		for(int i = 0; i < neighbors.size(); i++) {
			int[] neighbourId = neighbors.get(i).getIdBase3();
			if(neighbourId[Constants.ID_MAX_DIGITS-1] == 0) {
				result.add(neighbors.get(i));
			}
		}
		return result;
	}
	

}
