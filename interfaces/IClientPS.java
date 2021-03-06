package interfaces;

import java.rmi.RemoteException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.UUID;
import objects.Publication;

public interface IClientPS extends java.rmi.Remote {
	UUID getUuid() throws RemoteException; 
    void notify(Publication p, String tn) throws RemoteException;
    void notify(IClientPS c, String tn) throws RemoteException;
    long getCurrentTick() throws RemoteException;
    PublicKey getPublicKey() throws RemoteException;
    void updateAllTopics(ArrayList<String> s) throws RemoteException;
    void updateSubscriptedTopics(ArrayList<String> s) throws RemoteException;
}
