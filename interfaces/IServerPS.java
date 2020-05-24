package interfaces;

import java.rmi.RemoteException;

public interface IServerPS extends java.rmi.Remote {
    void publish() throws RemoteException;
    void subscribe() throws RemoteException;
    void unsubscribe() throws RemoteException;
    boolean register(IClientPS cps) throws RemoteException;
}
