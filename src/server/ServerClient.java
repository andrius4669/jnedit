package server;

import java.nio.channels.*;
import java.util.*;
import utils.*;

public class ServerClient extends User {
	private boolean sysoper;
	public SocketChannel sock;
	final public StringBuffer buf = new StringBuffer();
	final private ArrayList<String> subscriptions = new ArrayList<>();
	
	public boolean isOper() { return sysoper; }
	public void setOper(boolean o) { sysoper = o; }
	
	public void addSubscription(String s) {
		for(String ss: subscriptions) if(ss.equals(s)) return;
		subscriptions.add(s);
	}
	
	public void unsubscribe(String s) {
		for(int i = subscriptions.size()-1; i >= 0; i--) {
			if(subscriptions.get(i).equals(s)) subscriptions.remove(i);
		}
	}
	
	public boolean isSubscribing(String s) {
		for(String ss: subscriptions) if(ss.equals(s)) return true;
		return false;
	}
}
