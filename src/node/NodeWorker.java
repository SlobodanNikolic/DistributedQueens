package node;

import java.util.ArrayList;

import app.AppInfo;

public class NodeWorker implements Runnable{

	boolean running = false;
	private int startRange;
	private int endRange;
	private int tableSize;
	
	private boolean working = false;
	private int tablesDone = 0;
	
	private QueensResult previousResult;
	
	ArrayList<Integer> possibleTables = new ArrayList<Integer>();
	ArrayList<Integer> allTables = new ArrayList<Integer>();
	ArrayList<Integer> impossibleTables = new ArrayList<Integer>();

	public NodeWorker(int startRange, int endRange, int tablesDone, int tableSize) {
		this.startRange = startRange;
		this.endRange = endRange;
		this.tablesDone = tablesDone;
		this.tableSize = tableSize;
		working = true;
	}
	
	public NodeWorker(QueensResult previousResult) {
		this.startRange = previousResult.getStartRange();
		this.endRange = previousResult.getEndRange();
		this.tablesDone = previousResult.getTablesDone();
		this.previousResult = previousResult;
		this.tableSize = previousResult.getTableSize();
		working = true;
	}
	
	@Override
	public void run() {
		running = true;
		while(running){
			
			int i = startRange + tablesDone;
			if(i > endRange) {
	//			TODO: gotov posao
			}else {
				
			}
		
		}
		return;
	}
	
	public double getPercentage() {
		int totalTableCount = endRange - startRange + 1;
		double percentage = (100 * (double)tablesDone) / (double)totalTableCount;
		
		return percentage;
	}
	
	public boolean checkTable(int table) {
		
		return false;
	}

	public void pause() {
//		TODO: implementirati lepi stop
//		Stavi dokle si stigao u mapu rezultata
		running = false;
		QueensResult res = new QueensResult(startRange, endRange, tableSize, (int)getPercentage(), tablesDone, QueenStatus.PAUSED);
		AppInfo.myInfo.addResult(res, AppInfo.myInfo.getId());
		AppInfo.myInfo.setWorker(null);
		return;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public int getStartRange() {
		return startRange;
	}

	public void setStartRange(int startRange) {
		this.startRange = startRange;
	}

	public int getEndRange() {
		return endRange;
	}

	public void setEndRange(int endRange) {
		this.endRange = endRange;
	}

	public boolean isWorking() {
		return working;
	}

	public void setWorking(boolean working) {
		this.working = working;
	}

	public int getTablesDone() {
		return tablesDone;
	}

	public void setTablesDone(int tablesDone) {
		this.tablesDone = tablesDone;
	}

	public ArrayList<Integer> getPossibleTables() {
		return possibleTables;
	}

	public void setPossibleTables(ArrayList<Integer> possibleTables) {
		this.possibleTables = possibleTables;
	}

	public ArrayList<Integer> getAllTables() {
		return allTables;
	}

	public void setAllTables(ArrayList<Integer> allTables) {
		this.allTables = allTables;
	}

	public ArrayList<Integer> getImpossibleTables() {
		return impossibleTables;
	}

	public void setImpossibleTables(ArrayList<Integer> impossibleTables) {
		this.impossibleTables = impossibleTables;
	}

	public int getTableSize() {
		return tableSize;
	}

	public void setTableSize(int tableSize) {
		this.tableSize = tableSize;
	}

	public QueensResult getPreviousResult() {
		return previousResult;
	}

	public void setPreviousResult(QueensResult previousResult) {
		this.previousResult = previousResult;
	}
	
	
}
