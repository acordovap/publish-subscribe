package clients;

import interfaces.IClientPS;
import interfaces.IServerPS;
import objects.Publication;
import server.ServerPS;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.UUID;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;

public class ClientPS extends UnicastRemoteObject implements IClientPS, Serializable {
	
	private static final long serialVersionUID = 6924681544523586806L;
	private UUID uuid;
	private long currentTick;
	private KeyPair pair;
	private Cipher cipher;
	
	public ClientPS() throws RemoteException, NoSuchAlgorithmException, NoSuchPaddingException {
		super();
		currentTick = 0;
		uuid = UUID.randomUUID();
		KeyPairGenerator kg = KeyPairGenerator.getInstance(IServerPS.ALG);
		kg.initialize(IServerPS.KEYLENGTH);
		pair = kg.generateKeyPair();
		cipher = Cipher.getInstance(IServerPS.ALG);
	}

	public ClientPS(UUID uuid) throws RemoteException, NoSuchAlgorithmException, NoSuchPaddingException {
		super();
		currentTick = 0;
		this.uuid = uuid;
		KeyPairGenerator kg = KeyPairGenerator.getInstance(IServerPS.ALG);
		kg.initialize(IServerPS.KEYLENGTH);
		pair = kg.generateKeyPair();
		cipher = Cipher.getInstance(IServerPS.ALG);
	}

	public PublicKey getPublicKey() {
		return pair.getPublic();
	}

	public long getCurrentTick() {
		return currentTick;
	}

	public void setCurrentTick(long currentTick) {
		this.currentTick = currentTick;
	}

	@Override
	public void notify(IClientPS c, String tn) throws RemoteException {
		System.out.println("UUID: " + c.getUuid() + "\tsubscribed to topic: " + tn);
	}
	
	@Override
	public void notify(Publication p, String tn) throws RemoteException {
		if (p.getTick() > getCurrentTick()) {
			setCurrentTick(p.getTick());
			System.out.println("UUID: " + p.getUuidPublisher() + "\tpublished on topic: " + tn + " " + "\tthe message: " + p.getMsg());
		}
	}

	@Override
	public UUID getUuid() throws RemoteException {
		return uuid;
	}
	
	protected SealedObject getCMsg(String msg) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
		cipher.init(Cipher.ENCRYPT_MODE, pair.getPrivate());
		return new SealedObject(msg, cipher);  
	}
	
	public static void main(String[] args) throws NotBoundException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, ClassNotFoundException {
		ClientPS c1 = new ClientPS();
		ClientPS c2 = new ClientPS();
		
        String saddress = "127.0.0.1"; // server's IP address
        IServerPS server = (IServerPS) LocateRegistry.getRegistry(saddress).lookup( ServerPS.class.getName());
        
        server.register(c1, "");
        server.login(c1, "");
        server.subscribe(c1, "topic1");
        server.publish(c1, "msg1", c1.getCMsg("msg1"), "/topic1");
        server.logout(c1);
        
        server.register(c2, "");
        server.login(c2, "");
        server.subscribe(c2, "topic1");
        server.publish(c2, "msg2", c2.getCMsg("msg2"), "/topic1");
        
        server.login(c1, "");
        
        while (true) {
        	//TODO: implement UI
        }
		
	}

	@Override
	public void updateAllTopics(ArrayList<String> s) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateSubscriptedTopics(ArrayList<String> s) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

}
