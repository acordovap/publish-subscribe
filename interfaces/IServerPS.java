package interfaces;

import java.rmi.RemoteException;

public interface IServerPS extends java.rmi.Remote {
    void publish(IClientPS c, String msg, String tn) throws RemoteException;
    void subscribe(IClientPS c, String tn) throws RemoteException;
    void unsubscribe() throws RemoteException;
    boolean login(IClientPS c) throws RemoteException;
    boolean logout(IClientPS c) throws RemoteException;
    boolean register(IClientPS c) throws RemoteException;
}
