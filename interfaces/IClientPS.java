package interfaces;

import java.rmi.RemoteException;

public interface IClientPS extends java.rmi.Remote {
    void ntfy() throws RemoteException;
}
