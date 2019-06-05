package node;

import app.AppInfo;
import bootstrap.BootstrapConfig;
import bootstrap.BootstrapListener;

public class NodeMain {
	
	public static void main(String[] args) {
		
		NodeConfig.getInstance().readConfig("resources/node_properties.properties");
		
		System.out.println("Node: ip: " + AppInfo.myInfo.getIp());
		
		NodeWorker worker = new NodeWorker();
		Thread workerThread = new Thread(worker);
		workerThread.start();
			
		NodeListener listener = new NodeListener(worker);
		Thread listenerThread = new Thread(listener);
		listenerThread.start();
		
		CLIParser parser = new CLIParser(listener, worker);
		Thread parserThread = new Thread(parser);
		parserThread.start();
		
		listener.setCli(parser);
		
	}
	
	
}
