package node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;

import concurrent.Token;
import helpers.Constants;

public class NodeInfo implements Serializable{
	

	private String ip;
	private String port;
	private String hash;
	private int limit;
	private int id;
	private Integer[] idBase3 = new Integer[Constants.ID_MAX_DIGITS];
	private boolean isBootstrap = false;
	private int requestsSentCount = 0;
	private Token token = new Token(false);
	private ArrayList<Integer> neighbors = new ArrayList<Integer>();
		
	private Integer[] sequenceCounter = new Integer[Constants.MAX_NODES + 1];
	
	public NodeInfo() {
		
	}
	
	public NodeInfo(String ip, String port, int limit, boolean b) {
		// TODO Auto-generated constructor stub
		this.ip = ip;
		this.port = port;
		this.limit = limit;
		this.isBootstrap = b;
		this.hash = "no_hash";
		this.id = -1;
	}
	
	public void setSequence(int id) {
		sequenceCounter[id]++;
	}
	
	public int getSequenceNumber(int id) {
		return sequenceCounter[id];
	}

	public boolean isBootstrap() {
		return isBootstrap;
	}

	public void setBootstrap(boolean isBootstrap) {
		this.isBootstrap = isBootstrap;
	}

	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}
	
	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer[] getIdBase3() {
		return idBase3;
	}

	public void setIdBase3(Integer[] idBase3) {
		this.idBase3 = idBase3;
	}

	public int getRequestsSentCount() {
		return requestsSentCount;
	}

	public void setRequestsSentCount(int requestsSentCount) {
		this.requestsSentCount = requestsSentCount;
	}


	public Integer[] getRequestsReceived() {
		return sequenceCounter;
	}

	public void setRequestsReceived(Integer[] requestsReceived) {
		this.sequenceCounter = requestsReceived;
	}
	

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public ArrayList<Integer> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(ArrayList<Integer> neighbors) {
		this.neighbors = neighbors;
	}

	public Integer[] getSequenceCounter() {
		return sequenceCounter;
	}

	public void setSequenceCounter(Integer[] sequenceCounter) {
		this.sequenceCounter = sequenceCounter;
	}
}
