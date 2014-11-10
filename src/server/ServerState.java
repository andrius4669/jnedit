package server;

import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.util.*;

public class ServerState {
	ArrayList<ServerBuffer> buffers;
	ArrayList<ServerClient> clients;
	public ServerSocketChannel servsock;
	public Selector selector;
	
	public boolean PrepareServer(int port)
	{
		try {
			/* make socket */
			servsock = ServerSocketChannel.open();
			/* bind it */
			servsock.bind(new InetSocketAddress(port));
			/* make it non-blocking */
			servsock.configureBlocking(false);
			/* make selector obj */
			selector = Selector.open();
			/* register socket to selector */
			servsock.register(selector, SelectionKey.OP_ACCEPT);
		} catch(Exception e) {
			servsock = null;
			selector = null;
			return false;
		}
		return true;
	}
	
	public void ServerLoop()
	{
		for(;;) {
			try { if(selector.select() <= 0) continue; } catch(IOException e) { return; }
			Set<SelectionKey> selected = selector.selectedKeys();
			Iterator<SelectionKey> it = selected.iterator();
			while(it.hasNext()) {
				SelectionKey key = it.next();
				
				/* TODO */
			}
		}
	}
}
