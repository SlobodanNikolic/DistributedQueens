package concurrent;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import helpers.Constants;
import sun.nio.cs.ext.TIS_620;

public class Token implements Serializable{

	public ConcurrentLinkedQueue<Integer> tokenRequests = new ConcurrentLinkedQueue<Integer>();
	public Integer[] lastRequestNumber = new Integer[Constants.MAX_NODES + 1];
	public TokenStatus status = TokenStatus.IDLE;
	public AtomicBoolean tokenAvailable = new AtomicBoolean(true);
	
	public Token() {
		
	}
	
	public Token(boolean available) {
		this.tokenAvailable = new AtomicBoolean(available);
	}
	
	public Token(Token t2) {
		this.tokenRequests = t2.tokenRequests;
		this.lastRequestNumber = t2.lastRequestNumber;
		this.status = t2.status;
		this.tokenAvailable = t2.tokenAvailable;
	}
}
