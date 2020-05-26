package clients;

import interfaces.IClientPS;
import interfaces.IServerPS;
import objects.Publication;
import objects.Topic;
import server.ServerPS;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;

public class ClientPS extends UnicastRemoteObject implements IClientPS, Serializable {
	
	private static final long serialVersionUID = 6924681544523586806L;
	private UUID uuid;
	private long currentTick;
	
	public ClientPS() throws RemoteException {
		super();
		uuid = UUID.randomUUID();
	}

	public ClientPS(UUID uuid) throws RemoteException {
		super();
		this.uuid = uuid;
	}

	public long getCurrentTick() {
		return currentTick;
	}

	public void setCurrentTick(long currentTick) {
		this.currentTick = currentTick;
	}

	

	@Override
	public void notify(IClientPS c, Topic t) throws RemoteException {
		System.out.println("Client with UUID: " + c.getUuid() + " subscribed to topic: " + t.getTopicName());
	}
	
	@Override
	public void notify(Publication p, Topic t) throws RemoteException {
		if (p.getTick() > getCurrentTick()) {
			setCurrentTick(p.getTick());
			System.out.println("New publication on topic: " + t.getTopicName() + " from UUID:" + p.getUuidPublisher() + "\n\t" + p.getMsg());
		}
	}

	@Override
	public UUID getUuid() throws RemoteException {
		return uuid;
	}
	
	public static void main(String[] args) throws RemoteException, NotBoundException {
		ClientPS c = new ClientPS();
        String saddress = "127.0.0.1"; // server's ip address
        IServerPS server = (IServerPS) LocateRegistry.getRegistry(saddress).lookup( ServerPS.class.getName());
        
        server.register(c);
        server.login(c);
        server.subscribe(c, "test");
        //server.publish();
        while (true) {
        	
        }
		
	}

}
