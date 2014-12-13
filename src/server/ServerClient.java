package server;

import java.nio.channels.*;
import utils.*;

public class ServerClient extends User {
	private boolean sysoper;
	public SocketChannel sock;
	final public StringBuffer buf = new StringBuffer();
	
	public boolean isOper() { return sysoper; }
	public void setOper(boolean o) { sysoper = o; }
	
	
}
