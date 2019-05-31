package bootstrap;

import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;

import concurrent.Token;
import node.NodeInfo;

public class BootstrapWorker implements Runnable{

	private ArrayList<NodeInfo> nodes = new ArrayList<NodeInfo>();
	boolean running = false;
	
	public BootstrapWorker() {
		
	}
	
	
	public NodeInfo randomNode() {
		Random rand = new Random();
		int randomInt = rand.nextInt(nodes.size());
		return nodes.get(randomInt);
	}
	
	public int getNodeCount() {
		return nodes.size();
	}
	
	public NodeInfo addNode(NodeInfo newCookie) {
//		Q: ovde treba napraviti hash?
//		Q: da li treba da bude zakljucano dodavanje i ostalo,
//		sve dok se cvor ne smesti?
		
				
		return newCookie;
	}
	
	public String makeAHash(String whatToHash) {
		return DigestUtils.sha1Hex(whatToHash);
	}
	
	@Override
	public void run() {
		running = true;
		// TODO Auto-generated method stub
		while(running) {
//			Vrti se
		}
		
	}

	
}
