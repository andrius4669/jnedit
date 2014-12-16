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
		StringBuilder s = new StringBuilder();
		s.append("buflistclean\n");
		for(ServerBuffer b: buffers) {
			s.append(String.format("buflist %s\n", b.getName()));
		}
		return s.toString();
	}
	
	private String genClientsList()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("clientlistclean\n");
		for(ServerClient sc: clients) {
			if(sc.getNick() != null) sb.append(String.format("clientlist %s %s\n", sc.isOper() ? "o" : "n", sc.getNick()));
		}
		return sb.toString();
	}
	
	public boolean PrepareServer(int port)
	{
		try {
			/* make socket */
			servsock = ServerSocketChannel.open();
		} catch(Exception e) {
			servsock = null; selector = null;
			System.out.printf("Failure in opening server socket channel: %s\n", e.toString());
			return false;
		}
		try {
			/* bind it */
			servsock.bind(new InetSocketAddress(port));
		} catch(Exception e) {
			servsock = null; selector = null;
			System.out.printf("Failure in binding server socket: %s\n", e.toString());
			return false;
		}
		try {
			/* make it non-blocking */
			servsock.configureBlocking(false);
		} catch(Exception e) {
			servsock = null; selector = null;
			System.out.printf("Failure in making server socket non-blocking: %s\n", e.toString());
			return false;
		}
		try {
			/* make selector obj */
			selector = Selector.open();
		} catch(Exception e) {
			servsock = null; selector = null;
			System.out.printf("Failure in opening selector: %s\n", e.toString());
			return false;
		}
		try {
			/* register socket to selector for accepting connections */
			servsock.register(selector, SelectionKey.OP_ACCEPT);
		} catch(Exception e) {
			servsock = null; selector = null;
			System.out.printf("Failure in registering to selector: %s\n", e.toString());
			return false;
		}
		return true;
	}
	
	public void sendString(SocketChannel s, String str)
	{
		//ByteBuffer b = ByteBuffer.allocate(500000);
		
		ByteBuffer b = ByteBuffer.wrap(str.getBytes());
		//b.clear();
		//b.put(str.getBytes());
		//b.flip();
		while(b.hasRemaining()) {
			try { s.write(b); }
			catch(IOException e) { System.out.printf("write: %s\n", e.toString()); break; }
		}
	}
	
	private void broadcast(String str, ServerClient except)
	{
		for(ServerClient sc: clients) if(sc != except) sendString(sc.sock, str);
	}
	
	private void broadcastln(String str, ServerClient except) {
		for(ServerClient sc: clients) if(sc != except) {
			sendString(sc.sock, str);
			sendString(sc.sock, "\n");
		}
	}
	
	private ServerBuffer findBuf(String name) {
		for(ServerBuffer buf: buffers) if(buf.getName().equals(name)) return buf;
		return null;
	}
	
	private void clientCommand(ServerClient c, String ccmd)
	{
		if(ccmd.length() <= 0) return;
		int space = ccmd.indexOf(' ');
		String cmdname = ccmd.substring(0, space >= 0 ? space : ccmd.length()).toLowerCase();
		if(cmdname.length() <= 0) return;
		String arg = ccmd.substring(space >= 0 ? space + 1 : ccmd.length());
		switch(cmdname) {
			case "ping":
			{
				if(arg.length() > 0) sendString(c.sock, String.format("pong %s\n", arg));
				else sendString(c.sock, "pong\n");
				break;
			}
			case "name":
			{
				Scanner ss = new Scanner(arg);
				if(ss.hasNext())
				{
					String newname = ss.next();
					if(c.getNick() != null)
						broadcast(String.format("rename %s %s\n", c.getNick(), newname), c);
					else
						broadcast(String.format("join %s\n", newname), c);
					c.setNick(newname);
				}
				break;
			}
			case "wall":
				broadcast(String.format("wall %s\n", arg), c);
				break;
			case "getf":
			case "makef":
			{
				Scanner ss = new Scanner(arg);
				if(ss.hasNext())
				{
					String fname = ss.next();
					ServerBuffer buf = findBuf(fname);
					if(buf == null)
					{
						if(!cmdname.equals("makef")) break;
						buf = new ServerBuffer(fname);
						buffers.add(buf);
						broadcast(String.format("makef %s\n", fname), null);
					}
					c.addSubscription(fname);
					sendString(c.sock, buf.getSendCommand());
				}
				break;
			}
			case "closef":
				c.unsubscribe(arg);
				break;
			case "efclean":
			{
				if(!c.isSubscribing(arg)) break;
				ServerBuffer buf = findBuf(arg);
				if(buf == null) break;
				buf.clearText();
				broadcastln(ccmd, c);
				break;
			}
			case "efadd":
			{
				Scanner ss = new Scanner(arg);
				if(!ss.hasNext()) break;
				String fname = ss.next();
				if(!c.isSubscribing(fname)) break;
				ServerBuffer buf = findBuf(fname);
				if(buf == null) break;
				if(!ss.hasNext()) break;
				String text = ss.next();
				buf.parseEscapedText(text);
				broadcastln(ccmd, c);
				break;
			}
			case "efsend":
			{
				Scanner ss = new Scanner(arg);
				if(!ss.hasNext()) break;
				String fname = ss.next();
				if(!c.isSubscribing(fname)) break;
				ServerBuffer buf = findBuf(fname);
				if(buf == null) break;
				if(!ss.hasNext()) break;
				String text = ss.next();
				buf.clearText();
				buf.parseEscapedText(text);
				broadcastln(ccmd, c);
				break;
			}
			case "efins":
			{
				Scanner ss = new Scanner(arg);
				if(!ss.hasNext()) break;
				String fname = ss.next();
				if(!c.isSubscribing(fname)) break;
				ServerBuffer buf = findBuf(fname);
				if(buf == null) break;
				if(!ss.hasNext()) break;
				int inspos = ss.nextInt();
				if(!ss.hasNext()) break;
				String text = ss.next();
				buf.unescapeAndInsert(inspos, text);
				broadcastln(ccmd, c);
				break;
			}
			case "efdel":
			{
				Scanner ss = new Scanner(arg);
				if(!ss.hasNext()) break;
				String fname = ss.next();
				if(!c.isSubscribing(fname)) break;
				ServerBuffer buf = findBuf(fname);
				if(buf == null) break;
				if(!ss.hasNext()) break;
				int delpos = ss.nextInt();
				if(!ss.hasNext()) break;
				int dellen = ss.nextInt();
				buf.remove(delpos, dellen);
				broadcastln(ccmd, c);
				break;
			}
			case "rmf":
			{
				ServerBuffer buf = findBuf(arg);
				buffers.remove(buf);
				if(buf != null) broadcastln(ccmd, c);
				break;
			}
		}
	}
	
	void clientLeft(ServerClient c) {
		if(c.getNick() != null) broadcast(String.format("leave %s\n", c.getNick()), c);
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
			catch(IOException e) { System.out.printf("select: %s\n", e.toString()); return; }
			Set<SelectionKey> selected = selector.selectedKeys();
			Iterator<SelectionKey> it = selected.iterator();
			while(it.hasNext()) {
				SelectionKey key = it.next();
				it.remove();
				if(!key.isValid()) {
					key.cancel();
					break;
				}
				if(key.isReadable()) {
					SocketChannel sock = (SocketChannel)key.channel();
					ServerClient client = (ServerClient)key.attachment();
					if(!sock.isConnected()) {
						clientLeft(client);
						clients.remove(client);
						key.cancel();
						try { sock.close(); }
						catch(Exception e) { System.out.println(e.toString()); }
						break;
					}
					byte buf[] = new byte[1500];
					ByteBuffer b = ByteBuffer.wrap(buf);
					int bread;
					b.clear();
					try { bread = sock.read(b); }
					catch(Exception e) {
						System.out.printf("bad read, disconnecting (%s)\n", e.toString());
						clientLeft(client);
						clients.remove(client);
						key.cancel();
						try { sock.close(); }
						catch(Exception ee) { System.out.println(ee.toString()); }
						break;
					}
					if(bread > 0)
					{
						String ss = new String(buf, 0, bread);
						client.buf.append(ss);
						checkClientBuffer(client);
					}
					if(bread < 0)
					{
						clientLeft(client);
						clients.remove(client);
						key.cancel();
						try { sock.close(); }
						catch(Exception e) { System.out.println(e.toString()); }
						break;
					}
				}
				if(key.isAcceptable()) {
					SocketChannel clientsock;
					try { clientsock = ((ServerSocketChannel)key.channel()).accept(); }
					catch(IOException e) { System.out.printf("accept: %s\n", e.toString()); continue; }
					if(clientsock == null) break;
					try { clientsock.configureBlocking(false); }
					catch(IOException e) { System.out.printf("cs.cblk: %s\n", e.toString()); continue; }
					try { clientsock.socket().setKeepAlive(true); } catch(Exception e) {}
					try { clientsock.socket().setTcpNoDelay(true); } catch(Exception e) {}
					ServerClient client = new ServerClient();
					client.sock = clientsock;
					clients.add(client);
					try { clientsock.register(selector, SelectionKey.OP_READ, client); }
					catch(IOException e) { System.out.printf("register: %s\n", e.toString()); break; }
					sendString(clientsock, genBufList());
					sendString(clientsock, genClientsList());
				}
			}
		}
	}
}
