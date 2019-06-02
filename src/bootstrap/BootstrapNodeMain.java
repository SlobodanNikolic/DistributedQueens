package bootstrap;

import java.util.ArrayList;

import app.AppInfo;
import concurrent.Token;
import node.CLIParser;
import node.NodeInfo;

public class BootstrapNodeMain {
	
	
	public static void main(String[] args) {
		
		BootstrapConfig.getInstance().readConfig("resources/bootstrap_properties.properties");
		
		System.out.println("BootstrapNodeMain: ip: " + AppInfo.myInfo.getIp());
		
		AppInfo.myInfo.setToken(new Token(true));
		
		BootstrapWorker worker = new BootstrapWorker();
		Thread workerThread = new Thread(worker);
		workerThread.start();
		
		BootstrapListener listener = new BootstrapListener(worker);
		Thread listenerThread = new Thread(listener);
		listenerThread.start();
		
		CLIParser parser = new CLIParser(listener, worker);
		Thread parserThread = new Thread(parser);
		parserThread.start();
		
		
	}
}
