package server;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class ServerState {
	final ArrayList<ServerBuffer> buffers = new ArrayList<>();
	final ArrayList<ServerClient> clients = new ArrayList<>();
	public ServerSocketChannel servsock;
	public Selector selector;
	
	private String genBufList()
	{
		String s = new String();
		s += "buflist clean\n";
		for(ServerBuffer b: buffers) {
			s += String.format("buflist %s\n", b.getName());
		}
		return s;
	}
	
	
	
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
			/* register socket to selector for accepting connections */
			servsock.register(selector, SelectionKey.OP_ACCEPT);
		} catch(Exception e) {
			servsock = null;
			selector = null;
			return false;
		}
		return true;
	}
	
	public void sendString(SocketChannel s, String str)
	{
		ByteBuffer b = ByteBuffer.allocate(1500);
		b.clear();
		b.put(str.getBytes());
		try { s.write(b); }
		catch(IOException e) {}
	}
	
	void clientCommand(ServerClient c, String cmd)
	{
		System.out.println(cmd);
	}
	
	public void checkClientBuffer(ServerClient c)
	{
		StringBuffer b = c.buf;
		int index;
		boolean needcheck;
		for(needcheck = true; needcheck;)
		{
			needcheck = false;
			for(index = 0; index < b.length(); index++)
			{
				if(b.charAt(index) == '\n')
				{
					String cmd = b.substring(0, index);
					b.delete(0, index+1);
					clientCommand(c, cmd);
					needcheck = true;
					break;
				}
			}
		}
	}
	
	public void ServerLoop()
	{
		for(;;) {
			try { if(selector.select() <= 0) continue; } catch(IOException e) { return; }
			Set<SelectionKey> selected = selector.selectedKeys();
			Iterator<SelectionKey> it = selected.iterator();
			while(it.hasNext()) {
				SelectionKey key = it.next();
				if(key.isAcceptable()) {
					SocketChannel clientsock;
					try { clientsock = ((ServerSocketChannel)key.channel()).accept(); }
					catch(IOException e) { continue; }
					if(clientsock == null) continue;
					try { clientsock.configureBlocking(false); }
					catch(IOException e) { continue; }
					ServerClient client = new ServerClient();
					client.sock = clientsock;
					clients.add(client);
					try { clientsock.register(selector, SelectionKey.OP_READ, client); }
					catch(IOException e) { continue; }
					sendString(clientsock, genBufList());
				}
				else if(key.isReadable()) {
					SocketChannel sock = (SocketChannel)key.channel();
					ServerClient client = (ServerClient)key.attachment();
					ByteBuffer b = ByteBuffer.allocate(1500);
					int bread;
					b.clear();
					try { bread = sock.read(b); }
					catch(Exception e) { continue; }
					if(bread > 0) client.buf.append(b.asCharBuffer());
					else if(bread < 0)
					{
						clients.remove(client);
						key.cancel();
						try { sock.close(); }
						catch(Exception e) {}
					}
					checkClientBuffer(client);
				}
			}
		}
	}
}
