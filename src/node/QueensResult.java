package node;

public class QueensResult {
	
	private int startRange;
	private int endRange;
	private int tableSize;
	private int percentDone;
	private int tablesDone;
	private QueenStatus status = QueenStatus.NOT_STARTED;
	
	public QueensResult(int startRange, int endRange, int tableSize, int percentDone, int tablesDone, QueenStatus status) {
		this.startRange = startRange;
		this.endRange = endRange;
		this.tableSize = tableSize;
		this.percentDone = percentDone;
		this.tablesDone = tablesDone;
		this.status = status;

	}
	
	public QueensResult() {
		
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

	public int getTableSize() {
		return tableSize;
	}

	public void setTableSize(int tableSize) {
		this.tableSize = tableSize;
	}

	public int getPercentDone() {
		return percentDone;
	}

	public void setPercentDone(int percentDone) {
		this.percentDone = percentDone;
	}

	public int getTablesDone() {
		return tablesDone;
	}

	public void setTablesDone(int tablesDone) {
		this.tablesDone = tablesDone;
	}

	public QueenStatus getStatus() {
		return status;
	}

	public void setStatus(QueenStatus status) {
		this.status = status;
	}
	
	
}