package node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.sun.org.apache.xpath.internal.operations.Bool;

import app.AppInfo;
import concurrent.Token;
import helpers.Constants;

public class NodeInfo implements Serializable{
	

	private String ip;
	private String port;
	private String hash;
	private int limit;
	private int id;
	private int[] idBase3 = new int[Constants.ID_MAX_DIGITS];
	private boolean isBootstrap = false;
	private NodeInfo exitNode;
	
	private int requestsSentCount = 0;
	private Token token = new Token(false);
	
	private CopyOnWriteArrayList<NodeInfo> neighbors = new CopyOnWriteArrayList<NodeInfo>();
	
	
	private NodeWorker worker = null;

	private Integer[] sequenceCounter = new Integer[Constants.MAX_NODES + 1];
	private CopyOnWriteArrayList<Integer> neighborsToContact = new CopyOnWriteArrayList<Integer>();
	private CopyOnWriteArrayList<Integer> nodesForDataChange = new CopyOnWriteArrayList<Integer>();
	private CopyOnWriteArrayList<Integer> nodesForConnectionBrake = new CopyOnWriteArrayList<Integer>();

	private ConcurrentHashMap<Integer, Boolean> jobsRunning = new ConcurrentHashMap<Integer, Boolean>();
	private ConcurrentHashMap<Integer, Integer> awaitingResults = new ConcurrentHashMap<Integer, Integer>();

//	Prvi integer je velicina table, drugi je id cvora 
	private ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, QueensResult>> results = 
			new ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, QueensResult>>();
	
	
	
	
	
	public NodeInfo() {
		for(int i = 0; i < Constants.MAX_NODES; i++) {
			sequenceCounter[i]=0;
		}
	}
	
	public void setAwaiting(int tableSize, int count) {
		awaitingResults.put(tableSize, count);
	}
	
	public QueensResult checkForResult(int tableSize) {
		if(results.get(tableSize) == null)
			return null;
		return results.get(tableSize).get(AppInfo.myInfo.getId());
	}
	
	public void addResult(QueensResult result, int nodeId) {
		ConcurrentHashMap<Integer, QueensResult> resultsForSize = results.get(result.getTableSize());
		if(resultsForSize == null) {
			resultsForSize = new ConcurrentHashMap<Integer, QueensResult>();
		}
		resultsForSize.put(nodeId, result);
		results.put(result.getTableSize(), resultsForSize);
	}
	
	public void editResult(QueensResult result, int nodeId) {
		
	}
	
	public void addJobRunning(int tableSize) {
		jobsRunning.put(tableSize, true);
	}
	
	public ConcurrentHashMap<Integer, Boolean> getJobsRunning() {
		return jobsRunning;
	}
	
	public void setJobRunning(int boardSize) {
		for (Map.Entry<Integer, Boolean> entry : jobsRunning.entrySet()) {
			jobsRunning.put(entry.getKey(), false);
		}
		jobsRunning.put(boardSize, true);
	}
	
	public void setJobNotRunning(int boardSize) {
		
		jobsRunning.put(boardSize, false);
	}
	
	public int incrementRequestCounter() {
		requestsSentCount++;
		sequenceCounter[id]++;
		return requestsSentCount;
	}

	public void setJobsRunning(ConcurrentHashMap<Integer, Boolean> jobsRunning) {
		this.jobsRunning = jobsRunning;
	}
	
	public void addJob(int tableSize) {
		if(jobsRunning.containsKey(tableSize)) {
			
		}else {
			jobsRunning.put(tableSize, true);
		}
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
	
	public void addNeighbourToContact(int neighbourId) {
		neighborsToContact.add(neighbourId);
	}
	
	public void removeNeighbourToContact(int neighbourId) {
		
		for(int i = 0; i < neighborsToContact.size(); i++) {
			if(neighborsToContact.get(i) == neighbourId) {
				neighborsToContact.remove(i);
			}
		}
	}
	
	public void addRequest() {
		requestsSentCount++;
	}

	public void addDataChangeNode(int neighbourId) {
		nodesForDataChange.add(neighbourId);
	}
	
	public void removeDataChangeNode(int neighbourId) {
		
		for(int i = 0; i < nodesForDataChange.size(); i++) {
			if(nodesForDataChange.get(i) == neighbourId) {
				nodesForDataChange.remove(i);
			}
		}
	}
	
	public void addConnBreakNode(int neighbourId) {
		nodesForConnectionBrake.add(neighbourId);
	}
	
	public void removeConnBreakNode(int neighbourId) {
		
		for(int i = 0; i < nodesForConnectionBrake.size(); i++) {
			if(nodesForConnectionBrake.get(i) == neighbourId) {
				nodesForConnectionBrake.remove(i);
			}
		}
	}
	
	public void addNeighbour(NodeInfo n) {
	
		neighbors.add(n);
	}
	
	public void removeNeighbour(NodeInfo n) {
		
		for(int i = 0; i < neighbors.size(); i++) {
			if(neighbors.get(i).getId() == n.getId()) {
				neighbors.remove(i);
			}
		}
	}
	
	public void changeNeighbourData(NodeInfo oldData, NodeInfo newData) {
		for (NodeInfo neighbour : neighbors) {
			if(neighbour.getId() == oldData.getId()) {
				neighbour.setIp(newData.getIp());
				neighbour.setPort(newData.getPort());
			}
		}
	}
	
	public void clearNeighbors() {
		neighbors.clear();
	}
	
	public void setExitNode(NodeInfo exitNode) {
		this.exitNode = exitNode;
	}
	
	public NodeInfo getExitNode() {
		return this.exitNode;
	}
	
	
	public NodeWorker getWorker() {
		return worker;
	}

	public void setWorker(NodeWorker worker) {
		this.worker = worker;
	}

	public ConcurrentHashMap<Integer, Integer> getAwaitingResults() {
		return awaitingResults;
	}

	public void setAwaitingResults(ConcurrentHashMap<Integer, Integer> awaitingResults) {
		this.awaitingResults = awaitingResults;
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

	public int[] getIdBase3() {
		return idBase3;
	}

	public void setIdBase3(int[] idBase3) {
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

	public CopyOnWriteArrayList<NodeInfo> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(CopyOnWriteArrayList<NodeInfo> neighbors) {
		this.neighbors = neighbors;
	}

	public Integer[] getSequenceCounter() {
		return sequenceCounter;
	}

	public void setSequenceCounter(Integer[] sequenceCounter) {
		this.sequenceCounter = sequenceCounter;
	}

	public CopyOnWriteArrayList<Integer> getNeighborsToContact() {
		return neighborsToContact;
	}

	public void setNeighborsToContact(CopyOnWriteArrayList<Integer> neighborsToContact) {
		this.neighborsToContact = neighborsToContact;
	}
	
	public CopyOnWriteArrayList<Integer> getDataChangeNodes() {
		return nodesForDataChange;
	}

	public void setDataChangeNodes(CopyOnWriteArrayList<Integer> nodesForDataChange) {
		this.nodesForDataChange = nodesForDataChange;
	}
	
	public CopyOnWriteArrayList<Integer> getConnBreakNodes() {
		return nodesForConnectionBrake;
	}

	public void setConnBreakNodes(CopyOnWriteArrayList<Integer> nodesForConnBreak) {
		this.nodesForConnectionBrake = nodesForConnBreak;
	}
}
