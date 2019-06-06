package commands;

import app.AppInfo;
import messages.JoinMessage;
import messages.Message;
import messages.MessageUtil;
import messages.PingMessage;
import messages.StartNowMessage;
import messages.TokenRequest;
import node.NodeInfo;
import node.NodeWorker;
import node.QueenStatus;
import node.QueensResult;

public class StartNowCommand implements Command {
	
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
//				TODO: Treba pauzirati zadatak koji se trenutno izvrsava i proveriti da li vec imamo neki rezultat
//				Za ovaj zadatak. Ako imamo, treba nastaviti od njega, ako ne, novi
				if(AppInfo.myInfo.getWorker()!=null) {
					AppInfo.myInfo.getWorker().pause();
				}
				QueensResult existingResult = AppInfo.myInfo.checkForResult(boardSize);
				if(existingResult != null) {
//					Vec imamo neki rezultat za to, treba da ga prosledimo novom workeru
//					koji ce da nastavi dalje.
					NodeWorker worker = new NodeWorker(existingResult);
					Thread workerThread = new Thread(worker);
					workerThread.start();
					AppInfo.myInfo.setWorker(worker);
					int messagesSentCount = 0;
					
//					Salji poruku ostalima da startuju
					for(int i = 0; i < AppInfo.nodeCount; i++) {
						if(i==AppInfo.myInfo.getId())
							continue;
						else {
							int s = -1;
							int e = -1;
							StartNowMessage message = new StartNowMessage(AppInfo.myInfo, AppInfo.myInfo, AppInfo.myInfo.getNeighbors().get(0), ""+s+","+e+","+i);
							MessageUtil.sendMessage(message);
							messagesSentCount++;
							AppInfo.myInfo.setAwaiting(boardSize, messagesSentCount);
						}
					}
				}
				else {
	//				Ako je novi zadatak
					int totalCombinations = (int) java.lang.Math.pow(boardSize, boardSize);
					int jobSize = totalCombinations/AppInfo.nodeCount;
					int currentPointer = 0;
					int nodeAssigned = 0;
					int startNumber = currentPointer;
					int endNumber = currentPointer+jobSize;
					
					if(endNumber >= totalCombinations) {
						endNumber = totalCombinations-1;
					}
					
	//				TODO: Dodeliti prvo sebi zadatak
	//				Postavljamo bool da je job running
					AppInfo.myInfo.setJobRunning(boardSize);
	//				Postavljamo result u mapu rezultata
					QueensResult res = new QueensResult(startNumber, endNumber, boardSize, 0, 0, QueenStatus.WORKING);
					AppInfo.myInfo.addResult(res, AppInfo.myInfo.getId());
					
					NodeWorker worker = new NodeWorker(startNumber, endNumber, 0, boardSize);
					Thread workerThread = new Thread(worker);
					workerThread.start();
					AppInfo.myInfo.setWorker(worker);
					
					currentPointer+=jobSize;
					int messagesSentCount = 0;

					
					while(currentPointer < totalCombinations) {
						
//						Ako sam ja slucajno node koji treba da radi, preskoci me
						if(nodeAssigned==AppInfo.myInfo.getId()) {
							nodeAssigned++;
						}
						
						startNumber = currentPointer;
						endNumber = currentPointer + jobSize;
						
						if(endNumber >= totalCombinations) {
							endNumber = totalCombinations-1;
						}
						
						currentPointer+=jobSize;
						
						StartNowMessage message = new StartNowMessage(AppInfo.myInfo, AppInfo.myInfo, AppInfo.myInfo.getNeighbors().get(0), ""+startNumber+","+endNumber+","+nodeAssigned);
						MessageUtil.sendMessage(message);
						messagesSentCount++;
						AppInfo.myInfo.setAwaiting(boardSize, messagesSentCount);
					}
				}
			}
		}
	}

	@Override
	public String commandName() {
		return "startnow";
	}


	@Override
	public void execute(String args) {
		
		int boardSize = Integer.parseInt(args);
		Thread t = new Thread(new StartWorker(boardSize));
		t.start();
		
	}

}
