package utils;

/** @author andrius */
public class User implements UserInt {
	private String nick, realname;
	
	@Override
	public String getNick() { return nick; }
	@Override
	public void setNick(String n) { nick = n; }
	@Override
	public String getName() { return realname; }
	@Override
	public void setName(String n) { realname = n; }
}
