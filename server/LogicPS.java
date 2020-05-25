package server;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import interfaces.IClientPS;

public class LogicPS implements Runnable {

	private CountDownLatch latch;
	private Set<IClientPS> activeClts;
	private Map<UUID, String> registeredClts;

	public LogicPS(CountDownLatch l) {
		this.latch = l;
		activeClts = Collections.synchronizedSet(new HashSet<>());
		registeredClts = Collections.synchronizedMap(new HashMap<>());
	}
	
	public boolean loginClt(IClientPS c) throws RemoteException {
		synchronized (registeredClts) {
			if (registeredClts.containsKey(c.getUuid()) && registeredClts.get(c.getUuid()).equals("") ) { //change "" for hashed password
				synchronized (activeClts) {
					activeClts.add(c);
					Log.getLogger().info((LogicPS.class.getName() + " - client with UUID: " + c.getUuid() + " logged in"));
					return true;
				}
			}
		}
		return false;
	}

	public boolean registerClt(IClientPS c) throws RemoteException {
		synchronized (registeredClts) {
			if (!registeredClts.containsKey(c.getUuid())) {
				registeredClts.put(c.getUuid(), ""); //change "" for hashed password
				Log.getLogger().info((LogicPS.class.getName() + " - client with UUID: " + c.getUuid() + " added"));
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void run() {
		Log.getLogger().info((LogicPS.class.getName() + " - Ready"));
		latch.countDown();
		while (true) {
			try {
				synchronized (activeClts) {
					for (IClientPS c: activeClts) { //must check for activeClts with pending Msgs
						c.ntfy();
					}
				}
				Thread.sleep(1000*5);
			} catch (InterruptedException | RemoteException e) {
				e.printStackTrace();
			}
		}
	}

}
