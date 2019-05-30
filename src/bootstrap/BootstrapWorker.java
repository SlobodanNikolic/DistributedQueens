package bootstrap;

import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;

import node.NodeInfo;

public class BootstrapWorker implements Runnable{

	private ArrayList<NodeInfo> chordNodes = new ArrayList<NodeInfo>();
	boolean running = false;
	
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
	
	public NodeInfo addNode(NodeInfo newCookie) {
//		Q: ovde treba napraviti hash?
//		Q: da li treba da bude zakljucano dodavanje i ostalo,
//		sve dok se cvor ne smesti?
		String dataForHex = newCookie.getIp()+newCookie.getPort();
		String hashValue = DigestUtils.sha1Hex(dataForHex);
		newCookie.setId(hashValue);
		
		chordNodes.add(newCookie);
		
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
