package messages.handlers;

import app.AppInfo;
import bootstrap.BootstrapConfig;
import messages.Message;

/**
 * This will be used if no proper handler is found for the message.
 * @author bmilojkovic
 *
 */
public class NullHandler implements MessageHandler {

	private final Message clientMessage;
	
	public NullHandler(Message clientMessage) {
		this.clientMessage = clientMessage;
	}
	
	@Override
	public void run() {
		AppInfo.timestampedErrorPrint("Couldn't handle message: " + clientMessage);
	}

}
