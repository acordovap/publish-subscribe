package interfaces;

import java.rmi.RemoteException;
import java.util.UUID;

public interface IClientPS extends java.rmi.Remote {
	UUID getUuid() throws RemoteException; 
    void ntfy() throws RemoteException;
}
