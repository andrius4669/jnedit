package client;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.JFrame;
import utils.*;

/**
 *
 * @author Domas
 */
public class Client{
    EditorGUI editor;
    Thread autocheck = null;
    public Socket csock;
    public OutputStream sockout;
    public InputStream sockin;
    final private StringBuffer inbuf = new StringBuffer();
    final public ArrayList<User> users = new ArrayList<>();
    final public ArrayList<FileBuffer> files = new ArrayList<>();
    final public ArrayList<String> sfiles = new ArrayList<>();	/* subscriptions/files opened by us */
    
    public Client(){
        autocheck = new Thread(new AutoCheck());
        autocheck.start();
        VerificationGUI verGUI = new VerificationGUI(this);
        verGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public boolean isSocketReady() {
	    return csock != null && csock.isConnected() && !csock.isClosed();
    }
    
    public boolean isSubscribing(String name) {
	    for(String s: sfiles) if(s.equals(name)) return true;
	    return false;
    }
    
    public void addSubscription(String name) {
	    for(String s: sfiles) if(s.equals(name)) return;
	    for(FileBuffer f: files) if(f.getName().equals(name)) { sfiles.add(name); return; }
    }
    
    public void unsubscribe(String name) {
	    for(int i = sfiles.size()-1; i >= 0; i--) {
		    String s = sfiles.get(i);
		    if(s.equals(name)) sfiles.remove(s);
	    }
    }
    
    public FileBuffer findBuf(String name) {
	    for(FileBuffer b: files) if(b.getName().equals(name)) return b;
	    return null;
    }
    
    public void mkBuf(String name) {
	    for(FileBuffer b: files) if(b.getName().equals(name)) return;
	    files.add(new FileBuffer(name));
    }
    
    private void parseCommand(String ccmd) {
	    	if(ccmd.length() <= 0) return;
		System.out.printf("dbg:cmd: %s\n", ccmd);
		int space = ccmd.indexOf(' ');
		String cmdname = ccmd.substring(0, space >= 0 ? space : ccmd.length()).toLowerCase();
		if(cmdname.length() <= 0) return;
		String arg = ccmd.substring(space >= 0 ? space + 1 : ccmd.length());
		switch(cmdname) {
			case "ping":
			{
				if(arg.length() > 0) sendString(String.format("pong %s\n", arg));
				else sendString("pong\n");
				break;
			}
			case "wall":
			{
				/* TODO for domas - display somehow (msg window or text in chat window/etc. not nesecary */
				break;
			}
			case "efclean":
			{
				if(!isSubscribing(arg)) break;
				FileBuffer buf = findBuf(arg);
				if(buf == null) break;
				buf.clearText();
                                String openedFile = editor.getOpenedFileName();
				if(openedFile != null && openedFile.equals(buf.getName()))
                                    editor.updateOpenedFile(buf);
				break;
			}
			case "efadd":
			{
				Scanner ss = new Scanner(arg);
				if(!ss.hasNext()) break;
				String fname = ss.next();
				if(!isSubscribing(fname)) break;
				FileBuffer buf = findBuf(fname);
				if(buf == null) break;
				if(!ss.hasNext()) break;
				String text = ss.next();
				buf.parseEscapedText(text);
				String openedFile = editor.getOpenedFileName();
				if(openedFile != null && openedFile.equals(buf.getName()))
                                    editor.updateOpenedFile(buf);
				break;
			}
			case "efsend":
			{
				Scanner ss = new Scanner(arg);
				if(!ss.hasNext()) break;
				String fname = ss.next();
				if(!isSubscribing(fname)) break;
				FileBuffer buf = findBuf(fname);
				if(buf == null) break;
				if(!ss.hasNext()) break;
				String text = ss.next();
				buf.clearText();
				buf.parseEscapedText(text);
				String openedFile = editor.getOpenedFileName();
				if(openedFile != null && openedFile.equals(buf.getName()))
                                    editor.updateOpenedFile(buf);
				break;
			}

			case "efins":
			{
				Scanner ss = new Scanner(arg);
				if(!ss.hasNext()) break;
				String fname = ss.next();
				if(!isSubscribing(fname)) break;
				FileBuffer buf = findBuf(fname);
				if(buf == null) break;
				if(!ss.hasNext()) break;
				int inspos = ss.nextInt();
				if(!ss.hasNext()) break;
				String text = ss.next();
				buf.unescapeAndInsert(inspos, text);
				String openedFile = editor.getOpenedFileName();
				if(openedFile != null && openedFile.equals(buf.getName()))
                                    editor.updateOpenedFile(buf);
				break;
			}
			case "efdel":
			{
				Scanner ss = new Scanner(arg);
				if(!ss.hasNext()) break;
				String fname = ss.next();
				if(!isSubscribing(fname)) break;
				FileBuffer buf = findBuf(fname);
				if(buf == null) break;
				if(!ss.hasNext()) break;
				int delpos = ss.nextInt();
				if(!ss.hasNext()) break;
				int dellen = ss.nextInt();
				buf.remove(delpos, dellen);
				String openedFile = editor.getOpenedFileName();
				if(openedFile != null && openedFile.equals(buf.getName()))
                                    editor.updateOpenedFile(buf);
				break;
			}
			case "leave":
			{
				for(int i = users.size()-1; i >= 0; i--) {
					User u = users.get(i);
					if(u.getNick().equals(arg)) users.remove(i);
                                }
				break;
			}
			case "join":
			{
				/* todo show */
				User u = new User();
				u.setNick(arg);
				users.add(u);
				break;
			}
			case "rename":
			{
				Scanner ss = new Scanner(arg);
				String name1 = ss.next();
				String name2 = ss.next();
				for(User u: users) if(u.getNick().equals(name1)) u.setNick(name2);
				break;
				/* todo show in gui */
			}
			case "clientlistclean":
				users.clear();
				/* todo show in gui */
				break;
			case "clientlist":
			{
				Scanner ss = new Scanner(arg);
				ss.next(); /*ignore; todo for me: process*/
				String name = ss.next();
				User u = new User();
				u.setNick(name);
				users.add(u);
				/* todo for domas: show in gui */
				break;
			}
			case "buflistclean":
				
				sfiles.clear();
				files.clear();
                                editor.updateFileList();
				break;
			case "buflist":
			{
				
				files.add(new FileBuffer(arg));
                                editor.updateFileList();
				break;
			}
			case "makef":
			{
				boolean found = false;
				for(FileBuffer f: files) if(f.getName().equals(arg)) { found = true; break; }
				if(found) break;
				files.add(new FileBuffer(arg));
				editor.updateFileList();
				break;
			}
                        //TO-DO:ANDRIUI - Gauti info kai ištrinamas failas
		}
    }
    
    /* TODO for domas: call these funcs from gui */
    public void openFile(String name) {
	    addSubscription(name);
	    sendString(String.format("getf %s\n", name));
    }
    
    public void createFile(String name) {
	    mkBuf(name);
	    addSubscription(name);
	    sendString(String.format("makef %s\n", name));
    }
    //TO-DO:ANDRIUI failo trinimas
    public void deleteFile(String name) {
	   
    }
    private void checkInputBuffer() {
	    	int index;
		boolean needcheck;
		for(needcheck = true; needcheck;)
		{
			needcheck = false;
			for(index = 0; index < inbuf.length(); index++)
			{
				if(inbuf.charAt(index) == '\n')
				{
					String cmd = inbuf.substring(0, index);
					inbuf.delete(0, index+1);
					parseCommand(cmd);
					needcheck = true;
					break;
				}
			}
		}

    }
    
    public void checkSocket() {
	    for(;;) {
		int av;
		if(sockin == null || !isSocketReady()) return;
		try { av = sockin.available(); } catch(Exception e) { return; }
		if(av <= 0) return;
		byte buf[] = new byte[1500];
		int rd;
		try { rd = sockin.read(buf); } catch(Exception e) { return; }
		if(rd <= 0) return;
		String sr = new String(buf, 0, rd);
		inbuf.append(sr);
		checkInputBuffer();
	    }
    }
    
    public void sendString(String cmd) {
	    if(sockout == null || !isSocketReady()) return;
	    try {
		sockout.write(cmd.getBytes());
		sockout.flush();
	    } catch(Exception e) {}
    }
    
    public void sendStringLn(String cmd) {
	    if(sockout == null || !isSocketReady()) return;
	    try {
		sockout.write((cmd + "\n").getBytes());
		sockout.flush();
	    } catch(Exception e) {}
    }
    
    private boolean setupSocket(String name) {
	    try { csock.setKeepAlive(true); } catch(Exception e) {}
	    try { csock.setTcpNoDelay(true); } catch(Exception e) {}
	    try { sockout = csock.getOutputStream(); } catch(Exception e) { return false; }	/* critical */
	    try { sockin = csock.getInputStream(); } catch(Exception e) { return false; }	/* critical */
	    if(name == null) sendStringLn("name Anonymous");
	    else sendString(String.format("name %s\n", name));
	    return true;
    }
    public boolean connect(){
        editor = new EditorGUI(this, EditorType.VIEW);
	try { csock = new Socket("127.0.0.1", 61337); }
	catch(UnknownHostException e) { csock = null; }
	catch(IOException e) { csock = null; }

        if(isSocketReady() && setupSocket(null)) {
		editor.setVisible(true);
                return true;
	}
        editor.dispose();
        editor = null;
        return false;
    }
    public boolean connect(String username){
        editor = new EditorGUI(this, EditorType.EDIT);
	try { csock = new Socket("127.0.0.1", 61337); }
	catch(UnknownHostException e) { csock = null; }
	catch(IOException e) { csock = null; }

        if(isSocketReady() && setupSocket(username)) {
		editor.setVisible(true);
                return true;
	}
        editor.dispose();
        editor = null;
        return false;
    }
    class AutoCheck implements Runnable{
        public void goCheck(){
            Client.this.checkSocket();
            try {Thread.sleep(5);} catch (Exception e) { };
            goCheck();
        }
        @Override
        public void run() {
            goCheck();
        }  
    }
    public static void main(String[] args) {
        Client client = new Client();
        
        
    }
    
    
}
