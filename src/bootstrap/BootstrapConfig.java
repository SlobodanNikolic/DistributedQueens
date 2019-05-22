package bootstrap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import app.AppInfo;
import node.NodeInfo;
import sun.security.x509.IPAddressName;

public class BootstrapConfig {
	
	public static volatile BootstrapConfig instance = null;
	
	private String ip="";
	private String port="";

	
	public static BootstrapConfig getInstance() {
		if(instance == null) {
			synchronized(BootstrapConfig.class) {
                if (instance == null) {
                    instance = new BootstrapConfig();
                }
            }
		}
		return instance;
	}
	
	private BootstrapConfig() {
		
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
	
	

	public void readConfig(String configName){
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(new File(configName)));
			this.ip = properties.getProperty("ip");
			this.port = properties.getProperty("port");
			NodeInfo info = new NodeInfo(this.ip, this.port, -1, true);
			AppInfo.getInstance().setMyNodeInfo(info);
			
		} catch (IOException e) {
			timestampedErrorPrint("Couldn't open properties file. Exiting...");
			System.exit(0);
		}
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


	
}
