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
import messages.Message;
import messages.MessageType;
import messages.MessageUtil;
import messages.PositionSetMessage;
import messages.SwitchPlacesMessage;
import node.NodeInfo;

public class ExitAcceptHandler implements MessageHandler {

	private Message clientMessage;
	
	public ExitAcceptHandler(Message clientMessage) {
		this.clientMessage = clientMessage;
	}

	@Override
	public void run() {
		
		if (clientMessage.getMessageType() == MessageType.EXIT_ACCEPT) {
			ExitAcceptedMessage message = (ExitAcceptedMessage)clientMessage;
			
//			Kasnije cemo trio broadcastom da obavestimo sve ostale o tome da je jedan cvor manje u sistemu
			int recieverId = AppInfo.nodeCount-1;
			AppInfo.timestampedStandardPrint("Reciever for this switch places message is the last node: " + recieverId);
			
//			Koristimo standardni nacin provere kome treba da posaljemo poruku
			NodeInfo receiver = checkTarget(recieverId);
			if(receiver != null) {
				AppInfo.timestampedStandardPrint("Receiver for this message should be " + receiver.getId());
//				Nasli smo najboljeg, salji njemu
				SwitchPlacesMessage message2 = new SwitchPlacesMessage(AppInfo.myInfo, AppInfo.myInfo, receiver, recieverId+"");
				MessageUtil.sendMessage(message2);
			}else {
//				Doslo je do greske
				AppInfo.timestampedErrorPrint("Greska pri pronalazenju najboljeg za dalje slanje connect poruke");
			}
								
		} else {
			AppInfo.timestampedErrorPrint("Ping handler got: " + clientMessage);
		}
	}
	
//	Saljemo poruke svim svojim susedima da treba da raskinu veze sa nama
//	for (NodeInfo neighbour : AppInfo.myInfo.getNeighbors()) {
//		ConnectionBrakeMessage conBreakMess = new ConnectionBrakeMessage(AppInfo.myInfo, AppInfo.myInfo, neighbour);
//		MessageUtil.sendMessage(conBreakMess);
//		
//	}
	
	
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
