package commands;

import app.AppInfo;
import bootstrap.BootstrapWorker;
import node.NodeInfo;

public class InfoCommand implements Command {

	private BootstrapWorker bootstrap;	
	
	public InfoCommand(BootstrapWorker bootstrap) {
		this.bootstrap = bootstrap;
	}
	
	@Override
	public String commandName() {
		return "info";
	}

	@Override
	public void execute(String args) {
		String nodeType = "";
		String message = "";
		
		if(AppInfo.myInfo.isBootstrap()) {
			nodeType = "Bootstrap";
			message += "Nodes in network: " + bootstrap.getNodeCount() + ".";
		}
		else {
			nodeType = "Node";
			message += "My id is: " + AppInfo.myInfo.getId() + ". \nMy base3 id is + " + AppInfo.myInfo.getIdBase3().toString()
					+ ". My neighbours: ";
			for (NodeInfo node : AppInfo.myInfo.getNeighbors()) {
				message += node.getId() + " : " + node.getIdBase3().toString() + "; ";
			}
		}
		AppInfo.timestampedStandardPrint(nodeType + " info: " + AppInfo.myInfo.getIp() +
				" : " + AppInfo.myInfo.getPort());
		AppInfo.timestampedStandardPrint(message);
	}

}
