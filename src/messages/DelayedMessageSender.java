package messages;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import app.AppInfo;
import bootstrap.BootstrapConfig;
import node.NodeInfo;


/**
 * This worker sends a message asynchronously. Doing this in a separate thread
 * has the added benefit of being able to delay without blocking main or somesuch.
 * 
 * @author bmilojkovic
 *
 */
public class DelayedMessageSender implements Runnable {

	private Message messageToSend;
	
	public DelayedMessageSender(Message messageToSend) {
		this.messageToSend = messageToSend;
	}
	
	public void run() {
		/*
		 * A random sleep before sending.
		 * It is important to take regular naps for health reasons.
		 */
		try {
			Thread.sleep((long)(Math.random() * 1000) + 500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		NodeInfo receiverInfo = messageToSend.getReceiverInfo();
		
		if (MessageUtil.MESSAGE_UTIL_PRINTING) {
			AppInfo.getInstance().timestampedStandardPrint("Sending message " + messageToSend);
		}
		
		try {
			/*
			 * Similar sync block to the one in FifoSenderWorker, except this one is
			 * related to Lai-Yang. We want to be sure that message color is red if we
			 * are red. Just setting the attribute when we were making the message may
			 * have been to early.
			 * All messages that declare their own stuff (eg. LYTellMessage) will have
			 * to override setRedColor() because of this.
			 */
			
			Socket sendSocket = new Socket(receiverInfo.getIp(), Integer.parseInt(receiverInfo.getPort()));
			
			ObjectOutputStream oos = new ObjectOutputStream(sendSocket.getOutputStream());
			oos.writeObject(messageToSend);
			oos.flush();
			
			sendSocket.close();
			
			messageToSend.sendEffect();
//				TODO: srediti
			AppInfo.getInstance().timestampedStandardPrint("Message " + messageToSend + " sent to " + receiverInfo.getIp());
			
		} catch (IOException e) {
			BootstrapConfig.getInstance().timestampedErrorPrint("Couldn't send message: " + messageToSend.toString());
			e.printStackTrace();
		}
	}
	
}
