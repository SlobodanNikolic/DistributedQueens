package commands;

import app.AppInfo;

public class InfoCommand implements Command {

	@Override
	public String commandName() {
		return "info";
	}

	@Override
	public void execute(String args) {
		String nodeType = "";
		if(AppInfo.myInfo.isBootstrap())
			nodeType = "Bootstrap";
		else
			nodeType = "Node";
		AppInfo.timestampedStandardPrint(nodeType + " info: " + AppInfo.myInfo.getIp() +
				" : " + AppInfo.myInfo.getPort());
	}

}
