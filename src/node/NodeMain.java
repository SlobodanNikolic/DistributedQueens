package node;

import app.AppInfo;
import bootstrap.BootstrapConfig;
import bootstrap.BootstrapListener;

public class NodeMain {
	
	public static void main(String[] args) {
		
		NodeConfig.getInstance().readConfig("resources/node_properties.properties");
		
		System.out.println("Node: ip: " + AppInfo.getInstance().getMyInfo().getIp());
			
		NodeListener listener = new NodeListener();
		Thread listenerThread = new Thread(listener);
		listenerThread.start();
		
		CLIParser parser = new CLIParser(listener);
		Thread parserThread = new Thread(parser);
		parserThread.start();
	
	}
	
	
}
