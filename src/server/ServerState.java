package server;

import java.net.*;
import java.nio.channels.*;
import java.util.*;

public class ServerState {
	ArrayList<ServerBuffer> buffers;
	ArrayList<ServerClient> clients;
	public ServerSocketChannel servsock;
	public Selector selector;
	
	public void PrepareServer(int port)
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
		}
	}
	
	public void ServerLoop()
	{
		
	}
}
