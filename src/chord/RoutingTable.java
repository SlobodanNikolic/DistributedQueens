package chord;

import java.util.HashMap;

import node.NodeInfo;

public class RoutingTable {
	
	HashMap<String, NodeInfo> neighbors = new HashMap<String, NodeInfo>(160);
	
	public RoutingTable() {
		
	}
	
	public NodeInfo getNeighbor(String id) {
		return neighbors.get(id);
	}
	
}
