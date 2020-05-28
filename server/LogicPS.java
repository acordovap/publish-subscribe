package server;

import java.io.IOException;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import interfaces.IClientPS;
import interfaces.IServerPS;
import objects.Publication;
import objects.Topic;

public class LogicPS implements Runnable {
	private CountDownLatch latch;
	private Map<UUID, IClientPS> activeClts;
	private Map<UUID, String> registeredClts;
	private Map<String, Set<UUID>> subscriptions;
	private Map<String, Topic> topics;
	private Map<UUID, Long> lastUpdate;
	private Cipher cipher;

	public LogicPS(CountDownLatch l) throws NoSuchAlgorithmException, NoSuchPaddingException {
		this.latch = l;
		lastUpdate = Collections.synchronizedMap(new HashMap<>());
		topics = Collections.synchronizedMap(new HashMap<>());
		subscriptions = Collections.synchronizedMap(new HashMap<>());
		activeClts = Collections.synchronizedMap(new HashMap<>());
		registeredClts = Collections.synchronizedMap(new HashMap<>());
		cipher = Cipher.getInstance(IServerPS.ALG);
	}
	
	public boolean loginClt(IClientPS c) throws RemoteException {
		synchronized (registeredClts) {
			if (registeredClts.containsKey(c.getUuid()) && registeredClts.get(c.getUuid()).equals("") ) { //change "" for hashed password
				synchronized (activeClts) {
					activeClts.put(c.getUuid(), c);
					Log.getLogger().info((LogicPS.class.getName() + "\t- client with UUID: " + c.getUuid() + "\tlogged in"));
					c.updateAllTopics(getAllTopics());
					c.updateSubscriptedTopics(getSubscriptedTopics(c));
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
		Log.getLogger().info((LogicPS.class.getName() + "\t- client with UUID: " + c.getUuid() + "\tlogged out"));
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
	
	protected void recursiveSubscribeTo(IClientPS c, String tns) throws RemoteException {
		subscriptions.get(tns).add(c.getUuid());
		
		synchronized (subscriptions.get(tns)) {
			for (UUID u: subscriptions.get(tns) ) {
				if (activeClts.get(u) != null)
					activeClts.get(u).notify(c, tns); //notify to users this subscription 
			}
			notifyOnSubscribe(c, tns);
		}
		Log.getLogger().info((LogicPS.class.getName() + "\t- client with UUID: " + c.getUuid() + "\tsubscribed to topic: " + tns));
		Log.getLogger().fine(LogicPS.class.getName() + "\t- users subscribed to topic: " + tns + ":\t" +  subscriptions.get(tns).toString());
		for (String st : topics.get(tns).getSubtopics()) {
			recursiveSubscribeTo(c, st);
		}
		c.updateSubscriptedTopics(getSubscriptedTopics(c));
	}
	
	public void subscribeTo(IClientPS c, String tnss) throws RemoteException {
		String tns = formatTopicName(tnss);
		if (subscriptions.get("/".concat(tns)) != null) {
			if(!subscriptions.get("/".concat(tns)).contains(c.getUuid())) {
				recursiveSubscribeTo(c, "/".concat(tns));
				return;
			}
		}
		String []tn = tns.split(Topic.SEPARATOR);
		String prefix = new String();
		for (int i = 0; i < tn.length; i++) {
			prefix = prefix+Topic.SEPARATOR+(tn[i]);
			if (subscriptions.get(prefix) == null) {
				Topic t = new Topic(prefix);
				if (i+1 < tn.length) {
					t.addSubtopic(prefix+Topic.SEPARATOR+tn[i+1]);
				}
				synchronized (topics) {
					topics.put(prefix, t);
				}
				subscriptions.put(prefix, new HashSet<UUID>());
				Log.getLogger().info((LogicPS.class.getName() + "\t- client with UUID: " + c.getUuid() + "\tcreate topic: " + prefix));
				subscriptions.get(prefix).add(c.getUuid());
				c.notify(c, prefix);
				Log.getLogger().info((LogicPS.class.getName() + "\t- client with UUID: " + c.getUuid() + "\tsubscribed to topic: " + prefix));
			}
			else { 
				if (i+1 < tn.length) {
					if (subscriptions.get(prefix.concat(Topic.SEPARATOR).concat(tn[i+1])) == null) {
						topics.get(prefix).addSubtopic(prefix+Topic.SEPARATOR+tn[i+1]);
					}
				}
			}
		}
		c.updateSubscriptedTopics(getSubscriptedTopics(c));
		for(IClientPS cc: activeClts.values()) {
			cc.updateAllTopics(getAllTopics());
		}
	}
	
	private void notifyOnSubscribe(IClientPS c, String tn) throws RemoteException { //checar con Toño
		for (Publication p: topics.get(tn).getPubs()) {
			c.notify(p, tn);
		}
	}
	
	public boolean registerClt(IClientPS c) throws RemoteException {
		synchronized (registeredClts) {
			if (!registeredClts.containsKey(c.getUuid())) {
				registeredClts.put(c.getUuid(), ""); // change "" for hashed password
				Log.getLogger().info((LogicPS.class.getName() + "\t- client with UUID: " + c.getUuid() + "\tregistered"));
				synchronized (lastUpdate) {
					lastUpdate.put(c.getUuid(), (long) 0);
				}
				return true;
			}
		}
		return false;
	}
	
	public void publish(IClientPS c, String msg, SealedObject cmsg, String tns) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, ClassNotFoundException, IOException {
		String tn = "/".concat(formatTopicName(tns));
		if(authenticateMsg(msg, cmsg, c.getPublicKey())) {
			if ( topics.get(tn) != null) {
				Publication p = new Publication(c.getUuid(), msg);
				topics.get(tn).getPubs().add(p);
				Log.getLogger().info((LogicPS.class.getName() + "\t- client with UUID: " + c.getUuid() + "\tpublished on topic: " + tn + "\tthe message: " + msg));
				for (IClientPS ic: activeClts.values()) {
					if (subscriptions.get(tn).contains(ic.getUuid()))
						ic.notify(p, tn);
				}
			}
		}
	}
	
	protected boolean authenticateMsg(String msg, SealedObject cmsg, PublicKey k) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, ClassNotFoundException, IOException {
		cipher.init(Cipher.DECRYPT_MODE, k);
		return ((String) cmsg.getObject(cipher)).equals(msg);
	}
	
    public static String formatTopicName(String s) {
    	return "/".concat(s).replaceAll("/+", "/").replaceFirst("/", "");
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

	public ArrayList<String> getAllTopics() {
		return topics.keySet().stream().collect(Collectors.toCollection(ArrayList::new) );
	}

	public ArrayList<String> getSubscriptedTopics(IClientPS c) throws RemoteException {
		ArrayList<String> s = new ArrayList<>();
		for (Map.Entry<String, Set<UUID>> e : subscriptions.entrySet()) {
			if (e.getValue().contains(c.getUuid())) {
				s.add(e.getKey());
			}
		}
		return s;
	}

	public void unsubscribe(IClientPS c, String tn) throws RemoteException {
		String tns = "/".concat(formatTopicName(tn));
		if(subscriptions.get(tns) != null && subscriptions.get(tns).contains(c.getUuid())) {
			subscriptions.get(tns).remove(c.getUuid());
			Log.getLogger().info((LogicPS.class.getName() + "\t- client with UUID: " + c.getUuid() + "\tunsubscribed from topic: " + tns));
			Log.getLogger().fine(LogicPS.class.getName() + "\t- users subscribed to topic: " + tns + ":\t" +  subscriptions.get(tns).toString());
			for (String st : topics.get(tns).getSubtopics()) {
				unsubscribe(c, st);
			}
			c.updateSubscriptedTopics(getSubscriptedTopics(c));
		}
	}

}
