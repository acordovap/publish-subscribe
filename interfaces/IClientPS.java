package interfaces;

import java.rmi.RemoteException;
import java.util.UUID;

import objects.Publication;
import objects.Topic;

public interface IClientPS extends java.rmi.Remote {
	UUID getUuid() throws RemoteException; 
    void notify(Publication p, Topic t) throws RemoteException;
    void notify(IClientPS c, Topic t) throws RemoteException;
}
