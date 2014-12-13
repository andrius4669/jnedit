package server;

public class Main {
	private void cmain(String args[]) {
		ServerState s = new ServerState();
		s.PrepareServer(61337);
		s.ServerLoop();
	}
	
	public static void main(String args[]) {
		(new Main()).cmain(args);
	}
}
