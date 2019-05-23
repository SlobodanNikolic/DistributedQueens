package commands;

import app.AppInfo;
import messages.JoinMessage;
import messages.Message;
import messages.MessageUtil;
import messages.PingMessage;
import node.NodeInfo;

public class JoinCommand implements Command {
	
	private class JoinWorker implements Runnable {
		
		public JoinWorker() {
			
		}
		
		@Override
		public void run() {
			
			Message joinMessage = new JoinMessage();		
			MessageUtil.sendMessage(joinMessage);
				
		}
	}

	@Override
	public String commandName() {
		return "join";
	}


	@Override
	public void execute(String args) {
		
		Thread t = new Thread(new JoinWorker());
		t.start();
		
	}

}
