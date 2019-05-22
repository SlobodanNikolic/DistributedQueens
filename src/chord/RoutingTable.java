package chord;

import java.util.HashMap;

public class RoutingTable {
	
	HashMap<String, Data> table = new HashMap<String, Data>(160);
	
	public Data findInTable(String key) {
		if(table.containsKey(key))
			return table.get(key);
		else
			return null;
	}
}
