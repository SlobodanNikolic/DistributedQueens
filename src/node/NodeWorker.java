package node;

import java.io.Serializable;
import java.util.ArrayList;

import app.AppInfo;
import helpers.Functions;
import messages.MessageUtil;
import messages.ResultReportMessage;
import messages.StartNowMessage;

public class NodeWorker implements Runnable, Serializable{

	boolean running = false;
	private int startRange =0;
	private int endRange = 0;
	private int tableSize = 0;
	
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
		int i = startRange + tablesDone;
		
		while(running){
			
			if(i > endRange) {
	//			TODO: gotov posao
//				wrap it up i salji poruku svima
				
				if(AppInfo.nodeCount > 1) {
					String resultString = "";
					for (Integer res : possibleTables) {
						resultString+=res+",";
					}
					if(resultString.charAt(resultString.length()-1)==',' && resultString.length() > 0) {
						resultString = resultString.substring(0, resultString.length()-1);
					}
					
//					Salji poruku sa rezultatom ostalima
					for(int j = 0; j < AppInfo.nodeCount; j++) {
						if(j==AppInfo.myInfo.getId())
							continue;
						else {
							int s = -1;
							int e = -1;
							
							ResultReportMessage message = new ResultReportMessage(AppInfo.myInfo, AppInfo.myInfo, 
									AppInfo.myInfo.getNeighbors().get(0), j+","+tableSize+","+resultString);
							MessageUtil.sendMessage(message);
						}
					}
					
				}
				finished();

			}else {
				if(checkTable(i)) {
//					Postavka je dobra. Stavi i u listu mogucih
					possibleTables.add(i);
				}
				else {
//					Postavka nije dobra. Stavi i u listu nemogucih
					impossibleTables.add(i);
				}
				i++;
				tablesDone++;
			}
		
		}
		return;
	}
	
	public String arrayToString(int[] array) {
		String result = "";
		for (int i : array) {
			result+=i;
		}
		return result;
	}
	
	public String matrixToString(int[][] matrix, int matrixSize) {
		String res = "";
		for(int i = 0; i < matrixSize; i++) {
			for(int j = 0; j < matrixSize; j++) {
				res+=matrix[i][j] + ", "; 
			}
			res+=System.lineSeparator();
		}
		return res;
	}
	
	public boolean checkTable(int table) {
		
		int[] tableBaseN = Functions.decToBaseN(table, tableSize);
		int[][] realTable = new int[tableSize][tableSize];
				
		AppInfo.timestampedStandardPrint("Checking table: " + table + ": " + " for table size " + tableSize);
		
//		Popunjavamo tablu kraljicama (1)
		for(int i = 0; i < tableBaseN.length; i++) {
			realTable[tableBaseN[i]][i] = 1;
		}
		
		AppInfo.timestampedStandardPrint(arrayToString(tableBaseN));
		
		AppInfo.timestampedStandardPrint(matrixToString(realTable, tableSize));
		
//		Trazimo kraljicu po kraljicu i za svaku proveravamo da li postoji neka kraljica na njenom putu
		for(int i = 0; i < tableSize; i++) {
			for(int j = 0; j < tableSize; j++) {
				if(realTable[i][j]==1) {
					if(checkHorizontal(i, j, realTable)
							|| checkVertical(i, j, realTable)
							|| checkDiagonal(i, j, realTable)) {
						
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	public double getPercentage() {
		int totalTableCount = endRange - startRange + 1;
		double percentage = (100 * (double)tablesDone) / (double)totalTableCount;
		
		return percentage;
	}
	
	public Boolean checkHorizontal(int i, int j, int[][] table) {
//		ka levo
		for(int p = j-1; p >= 0; p--) {
			if(table[i][p]==1)
				return true;
		}
//		ka desno
		for(int p = j+1; p < tableSize; p++) {
			if(table[i][p]==1)
				return true;
		}
		
		return false;
	}
	
	public Boolean checkVertical(int i, int j, int[][] table) {
		
//		ka gore
		for(int p = i-1; p >= 0; p--) {
			if(table[p][j]==1)
				return true;
		}
//		ka dole
		for(int p = i+1; p < tableSize; p++) {
			if(table[p][j]==1)
				return true;
		}
		
		return false;
	}
	
	public Boolean checkDiagonal(int i, int j, int[][] table) {
		
//		gore levo
		for(int w = j-1, h = i-1; w>=0 && h>=0; w--, h--) {
			if(table[h][w]==1)
				return true;
		}
		
//		gore desno
		for(int w = j+1, h = i-1; w<tableSize && h>=0; w++, h--) {
			if(table[h][w]==1)
				return true;
		}
//		dole levo
		for(int w = j-1, h = i+1; w>=0 && h<tableSize; w--, h++) {
			if(table[h][w]==1)
				return true;
		}
//		dole desno
		for(int w = j+1, h = i+1; w<tableSize && h<tableSize; w++, h++) {
			if(table[h][w]==1)
				return true;
		}
		
		return false;
	}
	
	public void finished() {
//		Stavi dokle si stigao u mapu rezultata
		running = false;
		QueensResult res = new QueensResult(startRange, endRange, tableSize, (int)getPercentage(), tablesDone, QueenStatus.DONE, possibleTables);
		AppInfo.myInfo.addResult(res, AppInfo.myInfo.getId());
		AppInfo.myInfo.setWorker(null);
//		TODO: poslati svima poruku sa rezultatom
		AppInfo.myInfo.checkAwaiting(tableSize);
		return;
	}

	public void pause() {
//		TODO: implementirati lepi stop
//		Stavi dokle si stigao u mapu rezultata
		running = false;
		QueensResult res = new QueensResult(startRange, endRange, tableSize, (int)getPercentage(), tablesDone, QueenStatus.PAUSED, possibleTables);
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
