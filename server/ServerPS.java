package server;

import interfaces.IServerPS;
import java.rmi.RemoteException;

public class ServerPS implements IServerPS{

    public void test() throws RemoteException {
        System.out.println("test");
    }

    public static void main(String[] args) {
        ServerPS sps = new ServerPS();
        try {
            sps.test();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
