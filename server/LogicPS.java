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
import objects.Publication;
import objects.Topic;

public class LogicPS implements Runnable {
	private CountDownLatch latch;
	private Map<UUID, IClientPS> activeClts;
	private Map<UUID, String> registeredClts;
	private Map<String, Set<UUID>> subscriptions;
	private Map<String, Topic> topics;
	private Map<UUID, Long> lastUpdate;

	public LogicPS(CountDownLatch l) {
		this.latch = l;
		lastUpdate = Collections.synchronizedMap(new HashMap<>());
		topics = Collections.synchronizedMap(new HashMap<>());
		subscriptions = Collections.synchronizedMap(new HashMap<>());
		activeClts = Collections.synchronizedMap(new HashMap<>());
		registeredClts = Collections.synchronizedMap(new HashMap<>());
	}
	
	public boolean loginClt(IClientPS c) throws RemoteException {
		synchronized (registeredClts) {
			if (registeredClts.containsKey(c.getUuid()) && registeredClts.get(c.getUuid()).equals("") ) { //change "" for hashed password
				synchronized (activeClts) {
					activeClts.put(c.getUuid(), c);
					Log.getLogger().info((LogicPS.class.getName() + " - client with UUID: " + c.getUuid() + " logged in"));
					updateClientN(c);			
					return true;
				}
			}
		}
		return false;
	}

	public boolean logoutClt(IClientPS c) throws RemoteException {
		activeClts.remove(c.getUuid());
		lastUpdate.put(c.getUuid(), c.getCurrentTick());
		return false;
	}
	
	private void updateClientN(IClientPS c) throws RemoteException {
		if (lastUpdate.get(c.getUuid()) != null ) {
			long l = lastUpdate.get(c.getUuid());
			long maxTick = lastUpdate.get(c.getUuid());
			synchronized (topics.values()) {
				for (Topic t: topics.values()) {
					synchronized (t) {
						if(subscriptions.get(t.getTopicName()).contains(c.getUuid())) { //check if client is subscribed to topic
							synchronized (t.getPubs()) { 
								for(Publication p: t.getPubs()) {
									synchronized (p) {
										if(p.getTick() > l) {
											c.notify(p, t.getTopicName());
											maxTick = Math.max(maxTick, p.getTick());
										}
									}
								}	
							}
						}
					}
				}
			}
			synchronized (lastUpdate) {
				lastUpdate.put(c.getUuid(), maxTick);
			}
		}
	}
	
	public void subscribeTo(IClientPS c, String tn) throws RemoteException {
		if (subscriptions.get(tn) == null) {
			Topic t = new Topic(tn);
			synchronized (topics) {
				topics.put(tn, t);
			}
			subscriptions.put(tn, new HashSet<UUID>());
			Log.getLogger().info((LogicPS.class.getName() + " - client with UUID: " + c.getUuid() + " create topic: " + tn));
		}
		subscriptions.get(tn).add(c.getUuid());
		synchronized (subscriptions.get(tn)) {
			for (UUID u: subscriptions.get(tn) ) {
				if (activeClts.get(u) != null)
					activeClts.get(u).notify(c, tn); //notify to users this subscription 
			}
			notifyOnSubscribe(c, tn);
		}
		Log.getLogger().info((LogicPS.class.getName() + " - client with UUID: " + c.getUuid() + " subscribed to topic: " + tn));
		Log.getLogger().fine(LogicPS.class.getName() + " - users subscribed to topic: " + tn + ": " +  subscriptions.get(tn).toString());
	}
	
	private void notifyOnSubscribe(IClientPS c, String tn) throws RemoteException { //checar con Toño
		for (Publication p: topics.get(tn).getPubs()) {
			c.notify(p, tn);
		}
	}
	
	public boolean registerClt(IClientPS c) throws RemoteException {
		synchronized (registeredClts) {
			if (!registeredClts.containsKey(c.getUuid())) {
				registeredClts.put(c.getUuid(), ""); //change "" for hashed password
				Log.getLogger().info((LogicPS.class.getName() + " - client with UUID: " + c.getUuid() + " registered"));
				synchronized (lastUpdate) {
					lastUpdate.put(c.getUuid(), (long) 0);
				}
				return true;
			}
		}
		return false;
	}
	
	public void publish(IClientPS c, String msg, String tn) throws RemoteException {
		if ( topics.get(tn) != null) {
			Publication p = new Publication(c.getUuid(), msg);
			topics.get(tn).getPubs().add(p);
			for (IClientPS ic: activeClts.values()) {
				ic.notify(p, tn);
			}
		}
	}
	
	@Override
	public void run() {
		Log.getLogger().info((LogicPS.class.getName() + " - Ready"));
		latch.countDown();
		while (true) {
			try {
				Thread.sleep(1000*5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
