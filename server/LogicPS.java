package server;

import java.util.HashSet;
import java.util.concurrent.CountDownLatch;
import clients.ClientPS;

public class LogicPS implements Runnable {

	private CountDownLatch latch;
	private final HashSet<ClientPS> clts;

	public LogicPS(CountDownLatch latch) {
		this.latch = latch;
		clts = new HashSet<>();
	}
	
	public HashSet<ClientPS> getClts() {
		return clts;
	}

	@Override
	public void run() {
		Log.getLogger().info((LogicPS.class.getName() + " - Ready"));
		latch.countDown();
		while (true) {
			try {
				System.out.println(LogicPS.class.getName() + " - Wait");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
