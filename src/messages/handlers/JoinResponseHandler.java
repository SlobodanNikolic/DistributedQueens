package messages.handlers;

import app.AppInfo;
import bootstrap.BootstrapConfig;
import bootstrap.BootstrapWorker;
import messages.JoinMessage;
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

	@Override
	public void run() {
		if (clientMessage.getMessageType() == MessageType.JOIN_RESPONSE) {
			
			if(!AppInfo.getInstance().getMyInfo().isBootstrap()) {
				
//				Got a join response, with NodeInfo of the node that we need to contact
				NodeInfo nodeToContact = clientMessage.getOriginalSenderInfo();
				
				if(nodeToContact != null) {
//					TODO:
//					Vec je postojao neko u chordu, kontaktiraj njega da ti kaze gde ces
					System.out.println("Got a join response. Node to contact ip: " + nodeToContact.getIp()
					+ " port: " + nodeToContact.getPort());
					
					PositionMessage positionMessage = new PositionMessage(nodeToContact);
					MessageUtil.sendMessage(positionMessage);
				}
				else {
//					TODO:
//					Ti si prvi u chordu, uradi sta treba
//					Treba postaviti tabelu suseda na prazno (nemamo susede)
//					A to je vec postavljeno
					System.out.println("Got a join response. First in chord!");
				}				
				
			}
	
		} else {
			AppInfo.getInstance().timestampedErrorPrint("Join handler got: " + clientMessage);
		}
	}
	

}
