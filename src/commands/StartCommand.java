package commands;

import app.AppInfo;
import messages.JoinMessage;
import messages.Message;
import messages.MessageUtil;
import messages.PingMessage;
import messages.TokenRequest;
import node.NodeInfo;

public class StartCommand implements Command {
	
	private class StartWorker implements Runnable {
		
		private int boardSize;
		
		public StartWorker(int boardSize) {
			this.boardSize = boardSize;
		}
		
		@Override
		public void run() {
			if(AppInfo.myInfo.getJobsRunning().containsKey(boardSize)) {
				AppInfo.timestampedErrorPrint("A calculation for this board size has already been started");
			}else {
				for(int i = 0; i < AppInfo.nodeCount; i++) {
					if(AppInfo.myInfo.getId()!=i) {
						AppInfo.myInfo.incrementRequestCounter();
						TokenRequest requestMessage = new TokenRequest(AppInfo.myInfo, AppInfo.myInfo, i, AppInfo.myInfo.getNeighbors().get(0));
						MessageUtil.sendMessage(requestMessage);
					}
				}
			}
		}
	}

	@Override
	public String commandName() {
		return "start";
	}


	@Override
	public void execute(String args) {
		
		int boardSize = Integer.parseInt(args);
		Thread t = new Thread(new StartWorker(boardSize));
		t.start();
		
	}

}
