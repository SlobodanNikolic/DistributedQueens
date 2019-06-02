package bootstrap;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import app.AppInfo;
import messages.Message;
import messages.MessageUtil;
import messages.handlers.JoinHandler;
import messages.handlers.MessageHandler;
import messages.handlers.NullHandler;
import messages.handlers.PingHandler;
import messages.handlers.PositionSetHandler;
import node.SimpleListener;


public class BootstrapListener extends SimpleListener implements Runnable{

	private final ExecutorService threadPool = Executors.newWorkStealingPool();
	private BootstrapWorker bootstrap;	
	
	private Boolean working = true;
	
	public BootstrapListener(BootstrapWorker bootstrap) {
		this.bootstrap = bootstrap;
	}

	@Override
	public void run() {
		
		ServerSocket listenerSocket = null;
		try {
			listenerSocket = new ServerSocket(Integer.parseInt(AppInfo.myInfo.getPort()));
			
			System.out.println(listenerSocket.getInetAddress().toString());
		} catch (IOException e) {
			BootstrapConfig.getInstance().timestampedErrorPrint("Couldn't open listener socket on: " + BootstrapConfig.getInstance().getPort());
			System.exit(0);
		}
		
		
		while (working) {
			try {

				/*
				 * This blocks for up to 1s, after which SocketTimeoutException is thrown.
				 */
				Socket clientSocket = listenerSocket.accept();
				
				//GOT A MESSAGE! <3
				Message message = MessageUtil.readMessage(clientSocket);	
				
				MessageHandler messageHandler = new NullHandler(message);
				
				/*
				 * Each message type has it's own handler.
				 * If we can get away with stateless handlers, we will,
				 * because that way is much simpler and less error prone.
				 */
				switch (message.getMessageType()) {
				case PING:
					messageHandler = new PingHandler(message);
					break;
				case JOIN:
					messageHandler = new JoinHandler(message, bootstrap);
					break;
				case POSITION_SET:
					messageHandler = new PositionSetHandler(message, bootstrap);
					break;
				}
				
				threadPool.submit(messageHandler);
			} catch (SocketTimeoutException timeoutEx) {
				//Uncomment the next line to see that we are waking up every second.
				AppInfo.timestampedStandardPrint("Waiting...");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void stop() {
		this.working = false;
	}
}
