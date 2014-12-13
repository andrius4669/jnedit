package server;

import utils.*;

/**
 * @author andrius
 */
public class ServerBuffer extends FileBuffer {
	ServerBuffer() { super(); }
	ServerBuffer(String name) { super(name); }
	public String getSendCommand()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("efsend ");
		sb.append(getName());
		sb.append(' ');
		putEscapedText(sb);
		sb.append('\n');
		return sb.toString();
	}
}
