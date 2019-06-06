package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;

import node.NodeInfo;


public class AppInfo {
	
	public static NodeInfo myInfo;
	public static NodeInfo bootstrapInfo;
	public static int sentMessagesCount = 0;
	public static int receivedMessagesCount = 0;
	public static int nodeCount = 0;
	
	public static final AtomicBoolean joinLock = new AtomicBoolean(true);
	
	
	public AppInfo() {
		
	}
	
	public void setMyNodeInfo(NodeInfo info) {
		myInfo = info;
	}

	/**
	 * Print a message to stdout with a timestamp
	 * @param message message to print
	 */
	public static void timestampedStandardPrint(String message) {
		DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Date now = new Date();
		
		System.out.println(timeFormat.format(now) + " - " + message);
	}
	
	/**
	 * Print a message to stderr with a timestamp
	 * @param message message to print
	 */
	public static void timestampedErrorPrint(String message) {
		DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Date now = new Date();
		
		System.err.println(timeFormat.format(now) + " - " + message);
	}

	
}
