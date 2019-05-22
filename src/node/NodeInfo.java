package node;

import java.io.Serializable;

public class NodeInfo implements Serializable{
	
	private String ip;
	private String port;
	private String hash;
	private int limit;
	private String id;
	private boolean isBootstrap = false;

	
	public NodeInfo() {
		
	}
	
	public NodeInfo(String ip, String port, int limit, boolean b) {
		// TODO Auto-generated constructor stub
		this.ip = ip;
		this.port = port;
		this.limit = limit;
		this.isBootstrap = b;
		this.hash = "no_hash";
		this.id = "no_id";
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}
