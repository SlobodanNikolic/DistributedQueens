package bootstrap;

import app.AppInfo;
import node.CLIParser;

public class BootstrapNodeMain {
	public static void main(String[] args) {
		
		BootstrapConfig.getInstance().readConfig("resources/bootstrap_properties.properties");
		
		System.out.println("BootstrapNodeMain: ip: " + AppInfo.getInstance().getMyInfo().getIp());
		
		BootstrapListener listener = new BootstrapListener();
		Thread listenerThread = new Thread(listener);
		listenerThread.start();
		
		CLIParser parser = new CLIParser(listener);
		Thread parserThread = new Thread(parser);
		parserThread.start();
		
		
	}
}
