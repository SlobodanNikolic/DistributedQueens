package messages;

import java.io.Serializable;
import java.util.List;

import com.sun.corba.se.impl.orbutil.graph.Node;

import node.NodeInfo;


public interface Message extends Serializable {

	NodeInfo getSenderInfo();
	/**
	 * Information about the original sender. If <code>makeMeASender</code> is invoked
	 * on this object, this attribute will not be changed.
	 */
	NodeInfo getOriginalSenderInfo();
	
	/**
	 * If a servent uses <code>makeMeASender</code> when resending a message, it will
	 * be added to this list. So we can use this to see how this message got to us.
	 */
//	List<NodeInfo> getRoute();
	
	/**
	 * Information about the receiver of the message.
	 */
	NodeInfo getReceiverInfo();
	
	/**
	 * Message type. Mainly used to decide which handler will work on this message.
	 */
	MessageType getMessageType();
	
	/**
	 * The body of the message. Use this to see what your neighbors have sent you.
	 */
	String getMessageText();
	
	/**
	 * An id that is unique per servent. Combined with servent id, it will be unique
	 * in the system.
	 */
	int getMessageId();

	/**
	 * Alters the message and returns a new copy with everything intact, except
	 * the current node being added to the route list.
	 */
	void makeMeASender();
	
	/**
	 * Alters the message and returns a new copy with everything intact, except
	 * the receiver being changed to the one with the specified <code>id</code>.
	 */
	Message changeReceiver(NodeInfo newReceiver);
	
	/**
	 * This method is invoked by the frameworks sender code. It is invoked
	 * exactly before the message is being sent. If the message was held up
	 * by an event or a queue, this ensures that we perform the effect as
	 * we are sending the message.
	 */
	void sendEffect();
	
	Object getResponseObject();
}
