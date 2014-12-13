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
		s += "buflistclean\n";
		for(ServerBuffer b: buffers) {
			s += String.format("buflist %s\n", b.getName());
		}
		return s;
	}
	
	private String genClientsList()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("clientlistclean\n");
		for(ServerClient sc: clients) {
			sb.append(String.format("clientlist %s <%s>\n", sc.isOper() ? "o" : "n", sc.getName()));
		}
		return sb.toString();
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
		b.flip();
		while(b.hasRemaining()) {
			try { s.write(b); }
			catch(IOException e) {}
		}
	}
	
	private void broadcast(String str, ServerClient except)
	{
		for(ServerClient sc: clients) if(sc != except) sendString(sc.sock, str);
	}
	
	private void clientCommand(ServerClient c, String ccmd)
	{
		Scanner ss = new Scanner(ccmd);
		if(!ss.hasNext()) return;
		String cmdname = ss.next();
		switch(cmdname) {
			case "ping":
				sendString(c.sock, "pong");
				break;
			case "name":
				if(ss.hasNext())
				{
					String newname = ss.next();
					broadcast(String.format("rename %s %s", c.getNick(), newname), c);
					c.setName(newname);
				}
				break;
			case "getf":
				if(ss.hasNext())
				{
					String fname = ss.next();
					ServerBuffer buf = null;
					for(ServerBuffer b: buffers)
					{
						if(b.getName().equals(fname)) buf = b;
					}
					if(buf == null) break;
					c.addSubscription(fname);
					sendString(c.sock, buf.getSendCommand());
				}
				break;
			case "closef":
				if(ss.hasNext())
				{
					String fname = ss.next();
					c.unsubscribe(fname);
				}
				break;
		}
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
			try { selector.select(); }
			catch(IOException e) { System.out.println(e.toString()); return; }
			Set<SelectionKey> selected = selector.selectedKeys();
			Iterator<SelectionKey> it = selected.iterator();
			while(it.hasNext()) {
				SelectionKey key = it.next();
				it.remove();
				if(key.isAcceptable()) {
					SocketChannel clientsock;
					try { clientsock = ((ServerSocketChannel)key.channel()).accept(); }
					catch(IOException e) { System.out.println(e.toString()); continue; }
					if(clientsock == null) continue;
					try { clientsock.configureBlocking(false); }
					catch(IOException e) { System.out.println(e.toString()); continue; }
					ServerClient client = new ServerClient();
					client.sock = clientsock;
					clients.add(client);
					try { clientsock.register(selector, SelectionKey.OP_READ, client); }
					catch(IOException e) { System.out.println(e.toString()); continue; }
					sendString(clientsock, genBufList());
					sendString(clientsock, genClientsList());
				}
				if(key.isReadable()) {
					SocketChannel sock = (SocketChannel)key.channel();
					ServerClient client = (ServerClient)key.attachment();
					byte buf[] = new byte[1500];
					ByteBuffer b = ByteBuffer.wrap(buf);
					int bread;
					b.clear();
					try { bread = sock.read(b); }
					catch(Exception e) { System.out.println(e.toString()); continue; }
					if(bread > 0)
					{
						String ss = new String(buf, 0, bread);
						client.buf.append(ss);
						checkClientBuffer(client);
					}
					if(bread < 0)
					{
						clients.remove(client);
						key.cancel();
						try { sock.close(); }
						catch(Exception e) { System.out.println(e.toString()); }
					}
				}
			}
		}
	}
}
