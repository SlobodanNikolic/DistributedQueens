package node;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import messages.Message;
import messages.MessageUtil;
import messages.handlers.JoinResponseHandler;
import messages.handlers.MessageHandler;
import messages.handlers.NullHandler;
import messages.handlers.PingHandler;


public class NodeListener extends SimpleListener implements Runnable {

	private final ExecutorService threadPool = Executors.newWorkStealingPool();
		
	private Boolean working = true;
	private NodeWorker worker;
	
	public NodeListener(NodeWorker worker) {
		this.worker = worker;
	}

	@Override
	public void run() {
		
		ServerSocket listenerSocket = null;
		try {
			listenerSocket = new ServerSocket(Integer.parseInt(NodeConfig.getInstance().getPort()));
			
			System.out.println("Node:" + listenerSocket.getInetAddress().toString());
		} catch (IOException e) {
			NodeConfig.getInstance().timestampedErrorPrint("Couldn't open listener socket on: " + NodeConfig.getInstance().getPort());
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
				case JOIN_RESPONSE:
					messageHandler = new JoinResponseHandler(message);
					break;
				}
				
				threadPool.submit(messageHandler);
			} catch (SocketTimeoutException timeoutEx) {
				//Uncomment the next line to see that we are waking up every second.
//				AppConfig.timedStandardPrint("Waiting...");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void stop() {
		this.working = false;
	}
}
