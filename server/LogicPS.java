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
	private Map<String, Set<UUID>> topicClts;
	private Map<String, Topic> topics;
	private Map<UUID, Long> lastPublication;

	public LogicPS(CountDownLatch l) {
		this.latch = l;
		lastPublication = Collections.synchronizedMap(new HashMap<>());
		topics = Collections.synchronizedMap(new HashMap<>());
		topicClts = Collections.synchronizedMap(new HashMap<>());
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

	private void updateClientN(IClientPS c) throws RemoteException {
		if (lastPublication.get(c.getUuid()) != null ) {
			long l = lastPublication.get(c.getUuid());
			long maxTick = lastPublication.get(c.getUuid());
			synchronized (topics.values()) {
				for (Topic t: topics.values()) {
					synchronized (t) {
						if(topicClts.get(t.getTopicName()).contains(c.getUuid())) { //check if client is subscribed to topic
							synchronized (t.getPubs()) { 
								for(Publication p: t.getPubs()) {
									synchronized (p) {
										if(p.getTick() > l) {
											c.notify(p, t);
											maxTick = Math.max(maxTick, p.getTick());
										}
									}
								}	
							}
						}
					}
				}
			}
			synchronized (lastPublication) {
				lastPublication.put(c.getUuid(), maxTick);
			}
		}
	}
	
	public void subscribeTo(IClientPS c, String tn) throws RemoteException {
		if (topicClts.get(tn) == null) {
			Topic nt = new Topic(tn);
			synchronized (topics) {
				topics.put(tn, nt);
			}
			topicClts.put(tn, new HashSet<UUID>());
			Log.getLogger().info((LogicPS.class.getName() + " - client with UUID: " + c.getUuid() + " create topic: " + tn));
		}
		topicClts.get(tn).add(c.getUuid());
		synchronized (topicClts.get(tn)) {
			for (UUID u: topicClts.get(tn) ) {
				activeClts.get(u).notify(c, topics.get(tn)); //notify to users this subscription 
			}
			notifyOnSubscribe(c, tn);
		}
		Log.getLogger().info((LogicPS.class.getName() + " - client with UUID: " + c.getUuid() + " subscribed to topic: " + tn));
		Log.getLogger().fine(LogicPS.class.getName() + " - users subscribed to topic: " + tn + ": " +  topicClts.get(tn).toString());
	}
	
	private void notifyOnSubscribe(IClientPS c, String tn) throws RemoteException { //checar con Toño
		for (Publication p: topics.get(tn).getPubs()) {
			c.notify(p, topics.get(tn));
		}
	}
	
	public boolean registerClt(IClientPS c) throws RemoteException {
		synchronized (registeredClts) {
			if (!registeredClts.containsKey(c.getUuid())) {
				registeredClts.put(c.getUuid(), ""); //change "" for hashed password
				Log.getLogger().info((LogicPS.class.getName() + " - client with UUID: " + c.getUuid() + " registered"));
				synchronized (lastPublication) {
					lastPublication.put(c.getUuid(), (long) 0);
				}
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
			try {/*
				synchronized (activeClts) {
					for (IClientPS c: activeClts) { //must check for activeClts with pending Msgs
						//c.ntfy();
					}
				}*/
				Thread.sleep(1000*5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
