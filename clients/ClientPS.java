package clients;

import interfaces.IClientPS;
import interfaces.IServerPS;
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
	
	public ClientPS() throws RemoteException {
		super();
		uuid = UUID.randomUUID();
	}

	public ClientPS(UUID uuid) throws RemoteException {
		super();
		this.uuid = uuid;
	}

	@Override
	public void ntfy() throws RemoteException {
		System.out.println("Hi");
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
        while (true) {
        	
        }
		
	}

}
