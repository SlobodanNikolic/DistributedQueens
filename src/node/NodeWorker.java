package node;

public class NodeWorker implements Runnable{

	boolean running = false;
	
	@Override
	public void run() {
		running = true;
		while(running) {
//			Vrti se
		}
	}

	public void stop() {
		running = false;
	}
}
