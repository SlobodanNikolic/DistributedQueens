package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;

import node.NodeInfo;


public class AppInfo {
	
public static volatile AppInfo instance = null;
	
	private NodeInfo myInfo;
	private NodeInfo bootstrapInfo;
	private int sentMessagesCount = 0;
	private int receivedMessagesCount = 0;
	
	public static AppInfo getInstance() {
		if(instance == null) {
			synchronized(AppInfo.class) {
                if (instance == null) {
                    instance = new AppInfo();
                }
            }
		}
		return instance;
	}
	
	private AppInfo() {
		
	}
	
	public void setMyNodeInfo(NodeInfo info) {
		myInfo = info;
	}

	/**
	 * Print a message to stdout with a timestamp
	 * @param message message to print
	 */
	public void timestampedStandardPrint(String message) {
		DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Date now = new Date();
		
		System.out.println(timeFormat.format(now) + " - " + message);
	}
	
	/**
	 * Print a message to stderr with a timestamp
	 * @param message message to print
	 */
	public void timestampedErrorPrint(String message) {
		DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Date now = new Date();
		
		System.err.println(timeFormat.format(now) + " - " + message);
	}
	

	public int getSentMessagesCount() {
		return sentMessagesCount;
	}

	public void setSentMessagesCount(int sentMessagesCount) {
		this.sentMessagesCount = sentMessagesCount;
	}

	public int getReceivedMessagesCount() {
		return receivedMessagesCount;
	}

	public void setReceivedMessagesCount(int receivedMessagesCount) {
		this.receivedMessagesCount = receivedMessagesCount;
	}
	
	public NodeInfo getMyInfo() {
		return myInfo;
	}

	public NodeInfo getBootstrapInfo() {
		return bootstrapInfo;
	}

	public void setBootstrapInfo(NodeInfo bootstrapInfo) {
		this.bootstrapInfo = bootstrapInfo;
	}

	public void setMyInfo(NodeInfo myInfo) {
		this.myInfo = myInfo;
	}

	
}
