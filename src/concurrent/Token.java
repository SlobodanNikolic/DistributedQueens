package concurrent;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class Token implements Serializable{

	ConcurrentLinkedQueue<Integer> tokenRequests = new ConcurrentLinkedQueue<Integer>();
	CopyOnWriteArrayList<Integer> lastRequestNumber = new CopyOnWriteArrayList<Integer>();
	
	public Token() {
		
	}
}
