package interfaces;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SealedObject;

public interface IServerPS extends java.rmi.Remote {
	public static final String ALG = "RSA";
	public static final String ENCODE = "UTF-8";
	public static final int KEYLENGTH = 2048;
    void publish(IClientPS c, String msg, SealedObject cmsg, String tn) throws RemoteException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, ClassNotFoundException, IOException;
    void subscribe(IClientPS c, String tn) throws RemoteException;
    void unsubscribe(IClientPS c, String tn) throws RemoteException;
    boolean login(IClientPS c) throws RemoteException;
    boolean logout(IClientPS c) throws RemoteException;
    boolean register(IClientPS c) throws RemoteException;
}
