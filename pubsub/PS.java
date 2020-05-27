package pubsub;


import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;

import javax.crypto.NoSuchPaddingException;

import server.Log;
import server.LogicPS;
import server.ServerPS;

public class PS {
	
	private static final Level LVL = Level.INFO;
	
    public static void main(String[] args) throws RemoteException, InterruptedException, NoSuchAlgorithmException, NoSuchPaddingException {
    	
    	Log.getLogger().setLevel(LVL);
        CountDownLatch latch = new CountDownLatch(1); 
    	
    	LogicPS lps = new LogicPS(latch);
    	ServerPS sps = new ServerPS(lps);
    	
    	Thread tlps = new Thread(lps);
    	Thread tsps = new Thread(sps);
    	
    	tlps.start();
    	latch.await();
    	tsps.start();
    	
    }
}
