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
		if(AppInfo.getInstance().getMyInfo().isBootstrap())
			nodeType = "Bootstrap";
		else
			nodeType = "Node";
		AppInfo.getInstance().timestampedStandardPrint(nodeType + " info: " + AppInfo.getInstance().getMyInfo().getIp() +
				" : " + AppInfo.getInstance().getMyInfo().getPort());
	}

}
