package server;

import interfaces.IClientPS;
import interfaces.IServerPS;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.InvalidKeyException;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SealedObject;

public class ServerPS implements IServerPS, Runnable{

	private static final int PORT_RMI = 1099;
	private LogicPS lps;

	public ServerPS(LogicPS lps) throws RemoteException {
		super();
		this.lps = lps;
	}
	
	@Override
	public void publish(IClientPS c, String msg, SealedObject cmsg, String tn) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, ClassNotFoundException, IOException {
		lps.publish(c, msg, cmsg, tn);
	}

	@Override
	public void subscribe(IClientPS c, String tn) throws RemoteException {
		lps.subscribeTo(c, tn);
	}

	@Override
	public void unsubscribe(IClientPS c, String tn) throws RemoteException {
		lps.unsubscribe(c, tn);
	}

	@Override
	public boolean register(IClientPS c, String s) throws RemoteException {
		return lps.registerClt(c, s);
	}

	@Override
	public boolean login(IClientPS c, String s) throws RemoteException {
		return lps.loginClt(c, s);
	}
	
	@Override
	public boolean logout(IClientPS c) throws RemoteException {
		return lps.logoutClt(c);
	}
	
	@Override
	public void run() {
        try {
            LocateRegistry.createRegistry(PORT_RMI);
            Policy.setPolicy( new Policy() {
                @Override
                public PermissionCollection
                    getPermissions(CodeSource codesource) {
                    Permissions perms = new Permissions();
                    perms.add(new AllPermission());
                    return(perms);
                }
                @Override
                public void refresh(){
                    // no need to refresh
                }
            });
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }
            
            String name = ServerPS.class.getName();
            IServerPS stub = (IServerPS) UnicastRemoteObject.exportObject(this, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            Log.getLogger().info(ServerPS.class.getName() + " - Ready");
            
        } catch (RemoteException ex) {
            Logger.getLogger(ServerPS.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
}
