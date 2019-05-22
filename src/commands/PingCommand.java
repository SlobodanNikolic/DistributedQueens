package commands;

import app.AppInfo;
import messages.Message;
import messages.MessageUtil;
import messages.PingMessage;
import node.NodeInfo;

public class PingCommand implements Command {
	
	private class PingWorker implements Runnable {
		
		private NodeInfo originalSender;
		private NodeInfo sender;
		private NodeInfo receiver;
		
		public PingWorker(NodeInfo originalSender, NodeInfo sender, NodeInfo receiver) {
			this.originalSender = originalSender;
			this.receiver = receiver;
			this.sender = sender;
		}
		
		@Override
		public void run() {
			
			Message pingMessage = new PingMessage(originalSender, sender, receiver);		
			MessageUtil.sendMessage(pingMessage);
				
		}
	}

	@Override
	public String commandName() {
		return "ping";
	}


	@Override
	public void execute(String args) {
		
		String[] argsArray = args.split(" ");
		NodeInfo receiverInfo = new NodeInfo(argsArray[0], argsArray[1], 20, true);
		
		Thread t = new Thread(new PingWorker(AppInfo.getInstance().getMyInfo(),
				AppInfo.getInstance().getMyInfo(), receiverInfo));
		
		t.start();
		
	}

}
