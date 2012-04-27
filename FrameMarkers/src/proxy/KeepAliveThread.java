package proxy;

public class KeepAliveThread implements Runnable {
	private final ConnectionManager connectionManager;
	private Thread thread;

	KeepAliveThread(ConnectionManager connectionManager) {
		super();
		this.connectionManager = connectionManager;
	}

	public void start() {
		thread = new Thread(this);
		thread.setDaemon(true);
		thread.start();
	}

	public void stop() {
		thread.interrupt();
	}

	@Override
	public void run() {
		try {
			while (true) {
				connectionManager.sendKeepAlive();
				Thread.sleep(500);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
