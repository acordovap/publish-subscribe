package objects;

import java.io.Serializable;
import java.util.UUID;

public class Publication implements Serializable {
	private static final long serialVersionUID = 7979957901712193717L;
	private UUID uuidPublisher;
	private String msg;
	private long tick;
	static Long clk = (long) 1;
	
	public UUID getUuidPublisher() {
		return uuidPublisher;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public long getClk() {
		synchronized (clk) {
			return clk;
		}
	}
	
	public long getTick() {
		return tick;
	}

	public Publication(UUID uuidPublisher, String msg) {
		super();
		this.uuidPublisher = uuidPublisher;
		this.msg = msg;
		synchronized (clk) {
			this.tick = clk++;
		}
	}
	
}
