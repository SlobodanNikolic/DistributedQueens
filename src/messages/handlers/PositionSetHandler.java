package messages.handlers;

import app.AppInfo;
import bootstrap.BootstrapConfig;
import bootstrap.BootstrapWorker;
import messages.Message;
import messages.MessageType;
import messages.MessageUtil;
import messages.NodeCountMessage;
import messages.PositionSetMessage;
import node.NodeInfo;

public class PositionSetHandler implements MessageHandler {

	private Message clientMessage;
	private BootstrapWorker bootstrap;
	
	public PositionSetHandler(Message clientMessage, BootstrapWorker bootstrap) {
		this.clientMessage = clientMessage;
		this.bootstrap = bootstrap;
	}

	@Override
	public void run() {
//		TODO: Hendleri za poruke treba da imaju lockove takodje, da ne mogu da prekidaju jedan drugog
//		Nego samo jedan hendler u isto vreme
		
		if (clientMessage.getMessageType() == MessageType.POSITION_SET) {
			AppInfo.timestampedStandardPrint("Got a POSITION SET message from " + clientMessage.getSenderInfo().getId());
			AppInfo.timestampedStandardPrint(clientMessage.toString());

			if(AppInfo.myInfo.isBootstrap()) {
//				Da li sam ja bootstrap
				PositionSetMessage message = (PositionSetMessage)clientMessage;
				NodeInfo setNode = message.getSenderInfo();
				AppInfo.timestampedStandardPrint("Got a position set message from " + message.getSenderInfo().getId());

				bootstrap.addNode(setNode);
				for (NodeInfo node: bootstrap.getNodes()) {
					NodeCountMessage nodeCountMess = new NodeCountMessage(AppInfo.myInfo, node, bootstrap.getNodeCount()+"");
					MessageUtil.sendMessage(nodeCountMess);
				}
				AppInfo.joinLock.set(true);
			}
			else {
				AppInfo.timestampedErrorPrint("Ja nisam bootstrap, a stigla mi je position set poruka");
			}
			
		} else {
			AppInfo.timestampedErrorPrint("Ping handler got: " + clientMessage);
		}
	}

}
