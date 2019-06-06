package node;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import messages.Message;
import messages.MessageUtil;
import messages.handlers.ConnectionBreakHandler;
import messages.handlers.ConnectionBrokenHandler;
import messages.handlers.ConnectionCallbackHandler;
import messages.handlers.ConnectionHandler;
import messages.handlers.DataChangeConfirmHandler;
import messages.handlers.DataChangeHandler;
import messages.handlers.ExitAcceptHandler;
import messages.handlers.ExitGrantHandler;
import messages.handlers.JoinResponseHandler;
import messages.handlers.MessageHandler;
import messages.handlers.NodeCountHandler;
import messages.handlers.NullHandler;
import messages.handlers.PingHandler;
import messages.handlers.ResultReportHandler;
import messages.handlers.StartNowHandler;
import messages.handlers.SwitchPlacesHandler;
import messages.handlers.TokenRequestHandler;


public class NodeListener extends SimpleListener implements Runnable {

	private final ExecutorService threadPool = Executors.newCachedThreadPool();
		
	private Boolean working = true;
	private CLIParser cli;
	
	public void setCli(CLIParser cli) {
		this.cli = cli;
	}
	
	public NodeListener() {

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
				case CONNECT:
					messageHandler = new ConnectionHandler(message);
					break;
				case CONNECTION_CALLBACK:
					messageHandler = new ConnectionCallbackHandler(message);
					break;
				case TOKEN_REQUEST:
					messageHandler = new TokenRequestHandler(message);
					break;
				case NODE_COUNT:
					messageHandler = new NodeCountHandler(message);
					break;
				case EXIT_ACCEPT:
					messageHandler = new ExitAcceptHandler(message);
					break;
				case SWITCH:
					messageHandler = new SwitchPlacesHandler(message);
					break;
				case CONNECTION_BREAK:
					messageHandler = new ConnectionBreakHandler(message);
					break;
				case DATA_CHANGE:
					messageHandler = new DataChangeHandler(message);
					break;
				case DATA_CHANGE_CONFIRM:
					messageHandler = new DataChangeConfirmHandler(message);
					break;
				case CONNECTION_BROKEN:
					messageHandler = new ConnectionBrokenHandler(message);
					break;
				case EXIT_GRANTED:
					messageHandler = new ExitGrantHandler(message, this);
					break;
				case RESULT_REPORT:
					messageHandler = new ResultReportHandler(message);
					break;
				case START_NOW:
					messageHandler = new StartNowHandler(message);
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
		this.threadPool.shutdown();
		this.working = false;
	}

	public Boolean getWorking() {
		return working;
	}

	public void setWorking(Boolean working) {
		this.working = working;
	}


	public CLIParser getCli() {
		return cli;
	}
}
