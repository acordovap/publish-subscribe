package server;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {
	private static Logger logger = Logger.getLogger(Log.class.getName());
	public static Logger getLogger(){
	    return logger;
	}
	
	public static void log(Level level, String msg){
	    getLogger().log(level, msg);
	    System.out.println(msg);
	}
	
}
