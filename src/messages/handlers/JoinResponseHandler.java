package messages.handlers;

import java.util.ArrayList;

import app.AppInfo;
import bootstrap.BootstrapConfig;
import bootstrap.BootstrapWorker;
import concurrent.Token;
import helpers.Functions;
import messages.JoinMessage;
import messages.JoinResponseMessage;
import messages.Message;
import messages.MessageType;
import messages.MessageUtil;
import messages.PositionSetMessage;
import messages.ConnectionMessage;
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
//			when its finished, so the bootstrap join lock can be unlocked
			
			JoinResponseMessage message = (JoinResponseMessage)clientMessage;
			Token token = (Token)message.getResponseObject();
			
			if(token != null) {
//				Ako smo dobili token, ne treba nikog da kontaktiramo, prvi smo.
				AppInfo.timestampedStandardPrint("We got a join response with " + ((Token)message.getResponseObject()));
				token.tokenAvailable.set(true);
				AppInfo.myInfo.setToken(token);
				if(message.getId()!=-1) {
					AppInfo.myInfo.setId(message.getId());
					AppInfo.myInfo.setIdBase3(Functions.decToBase3(AppInfo.myInfo.getId()));
				}
				else {
					AppInfo.timestampedErrorPrint("Got id -1");
				}
//				Saljemo poruku bootstrapu da smo se smestili
				PositionSetMessage posSetMess = new PositionSetMessage(AppInfo.myInfo, AppInfo.bootstrapInfo);
				MessageUtil.sendMessage(posSetMess);
			}
			else {
//				Ako ne, nismo dobili token, nego random cvor i njemu prosledjujemo broadcast poruku za connect
//				Za svakog s kim treba da se konektujemo, po jednu poruku
				AppInfo.timestampedStandardPrint("We got a join response with a contact " + message.getOriginalSenderInfo().getId());
				
				if(message.getId() != -1) {
					AppInfo.myInfo.setId(message.getId());
					AppInfo.myInfo.setIdBase3(Functions.decToBase3(AppInfo.myInfo.getId()));
					AppInfo.timestampedStandardPrint("My new id is: " + message.getId());
				}else {
					AppInfo.timestampedErrorPrint("Got id -1");
				}
				
				NodeInfo contact = message.getOriginalSenderInfo();
//				TODO: send a connection message to all the nodes we need to connect to
				AppInfo.myInfo.setNeighborsToContact(Functions.whoToContact(AppInfo.myInfo.getId()));
				AppInfo.timestampedStandardPrint("Who to contact: " + Functions.whoToContact(AppInfo.myInfo.getId()));
				
				for (Integer nei : AppInfo.myInfo.getNeighborsToContact()) {
					ConnectionMessage connMess = new ConnectionMessage(AppInfo.myInfo, AppInfo.myInfo, contact, nei+"");
					MessageUtil.sendMessage(connMess);
				}
//				Sada cekamo da nas komsije kontaktiraju. Oni ce upisati nase podatke, a nama poslati svoje.
//				Nakon toga mi saljemo PositionSet poruku bootstrapu.
			}
	
		} else {
			AppInfo.timestampedErrorPrint("Join handler got: " + clientMessage);
		}
	}
	

}
