package server;

import utils.*;

/**
 * @author andrius
 */
public class ServerBuffer extends FileBuffer {
	public String getSendCommand()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("sendfile ");
		sb.append(getName());
		sb.append(' ');
		putEscapedText(sb);
		sb.append('\n');
		return sb.toString();
	}
}
