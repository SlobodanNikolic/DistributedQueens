package messages;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import com.sun.crypto.provider.AESParameters;

import app.AppInfo;
import bootstrap.BootstrapConfig;
import node.NodeInfo;

/**
 * A default message implementation. This should cover most situations.
 * If you want to add stuff, remember to think about the modificator methods.
 * If you don't override the modificators, you might drop stuff.
 * @author bmilojkovic
 *
 */
public abstract class BasicMessage implements Message {

	private static final long serialVersionUID = -9075856313609777945L;
	private final MessageType type;
	private final NodeInfo originalSenderInfo;
	private final NodeInfo senderInfo;
	private final NodeInfo receiverInfo;
	private final List<NodeInfo> routeList;
	private final String messageText;
	
	private final int messageId;
		
	
	public BasicMessage(NodeInfo originalSender, NodeInfo sender, NodeInfo reciever) {
		this.type = MessageType.PING;
		this.originalSenderInfo = originalSender;
		this.senderInfo = sender;
		this.receiverInfo = reciever;
		this.routeList = new ArrayList<NodeInfo>();
		routeList.add(sender);
		this.messageText = "";
		int id = AppInfo.sentMessagesCount;
		id++;
		this.messageId = id;
		AppInfo.sentMessagesCount = id;
	}
	
	
	public BasicMessage(MessageType type, NodeInfo originalSender, NodeInfo sender, NodeInfo reciever) {
		this.type = type;
		this.originalSenderInfo = originalSender;
		this.senderInfo = sender;
		this.receiverInfo = reciever;
		this.routeList = new ArrayList<NodeInfo>();
		routeList.add(sender);
		this.messageText = "";
		int id = AppInfo.sentMessagesCount;
		id++;
		this.messageId = id;
		AppInfo.sentMessagesCount = id;
	}
	
	public BasicMessage(MessageType type, NodeInfo originalSender, NodeInfo sender, NodeInfo reciever, String text) {
		this.type = type;
		this.originalSenderInfo = originalSender;
		this.senderInfo = sender;
		this.receiverInfo = reciever;
		this.routeList = new ArrayList<NodeInfo>();
		routeList.add(sender);
		this.messageText = text;
		int id = AppInfo.sentMessagesCount;
		id++;
		this.messageId = id;
		AppInfo.sentMessagesCount = id;
	}
		
	@Override
	public MessageType getMessageType() {
		return type;
	}

	@Override
	public NodeInfo getOriginalSenderInfo() {
		return originalSenderInfo;
	}

	@Override
	public NodeInfo getReceiverInfo() {
		return receiverInfo;
	}
	
	@Override
	public List<NodeInfo> getRoute() {
		return routeList;
	}
	
	@Override
	public String getMessageText() {
		return messageText;
	}
	
	@Override
	public int getMessageId() {
		return messageId;
	}
	
	
	/**
	 * Used when resending a message. It will not change the original owner
	 * (so equality is not affected), but will add us to the route list, so
	 * message path can be retraced later.
	 */
	@Override
	public void makeMeASender() {
//		TODO: srediti my servent info
		NodeInfo newRouteItem = AppInfo.myInfo;
		routeList.add(newRouteItem);
	}
	
	/**
	 * Change the message received based on ID. The receiver has to be our neighbor.
	 * Use this when you want to send a message to multiple neighbors, or when resending.
	 */
	@Override
	public Message changeReceiver(Integer newReceiverId) {
//		TODO: srediti
//		if (AppConfig.myServentInfo.getNeighbors().contains(newReceiverId)) {
//			NodeInfo newReceiverInfo = AppConfig.getInfoById(newReceiverId);
//			
//			Message toReturn = new BasicMessage(getMessageType(), getOriginalSenderInfo(),
//					newReceiverInfo, isWhite(), getRoute(), getMessageText(), getMessageId());
//			
//			return toReturn;
//		} else {
//			AppConfig.timestampedErrorPrint("Trying to make a message for " + newReceiverId + " who is not a neighbor.");
//			
//			return null;
//		}
		return null;
		
	}
	
	
	
	/**
	 * Comparing messages is based on their unique id and the original sender id.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BasicMessage) {
			BasicMessage other = (BasicMessage)obj;
			
			if (getMessageId() == other.getMessageId() &&
				getOriginalSenderInfo().getId() == other.getOriginalSenderInfo().getId()) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Hash needs to mirror equals, especially if we are gonna keep this object
	 * in a set or a map. So, this is based on message id and original sender id also.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getMessageId(), getOriginalSenderInfo().getId());
	}
	
	/**
	 * Returns the message in the format: <code>[sender_id|message_id|text|type|receiver_id]</code>
	 */
	@Override
	public String toString() {
		return "[" + getOriginalSenderInfo().getId() + "|" + getMessageId() + "|" +
					getMessageText() + "|" + getMessageType() + "|" +
					getReceiverInfo().getId() + "]";
	}


	@Override
	public NodeInfo getSenderInfo() {
		// TODO Auto-generated method stub
		return this.senderInfo;
	}


	@Override
	public void sendEffect() {
		// TODO Auto-generated method stub
		
	}

	

}
