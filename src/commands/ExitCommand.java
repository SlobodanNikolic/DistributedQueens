package commands;

import app.AppInfo;
import messages.ExitRequestMessage;
import messages.JoinMessage;
import messages.Message;
import messages.MessageUtil;
import messages.PingMessage;
import node.NodeInfo;

public class ExitCommand implements Command {
	
	private class ExitWorker implements Runnable {
		
		public ExitWorker() {
			
		}
		
		@Override
		public void run() {
			
			Message exitMessage = new ExitRequestMessage();		
			MessageUtil.sendMessage(exitMessage);
				
		}
	}

	@Override
	public String commandName() {
		return "exit";
	}


	@Override
	public void execute(String args) {
		
		Thread t = new Thread(new ExitWorker());
		t.start();
		
	}

}
