package bootstrap;

import java.util.ArrayList;
import java.util.Random;

import node.NodeInfo;

public class BootstrapWorker implements Runnable{

	private ArrayList<NodeInfo> chordNodes = new ArrayList<NodeInfo>();

	public BootstrapWorker() {
		
	}
	
	public NodeInfo randomNode() {
		Random rand = new Random();
		int randomInt = rand.nextInt(chordNodes.size());
		return chordNodes.get(randomInt);
	}
	
	public int getNodeCount() {
		return chordNodes.size();
	}
	
	public void addNode(NodeInfo newCookie) {
		chordNodes.add(newCookie);
//		Q: ovde treba napraviti hash?
		
	}
	
	public String makeAHash(NodeInfo newCookie) {
		return null;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	
}
