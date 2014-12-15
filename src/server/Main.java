package server;

public class Main {
	private void cmain(String args[]) {
		ServerState s = new ServerState();
		if(!s.PrepareServer(61337)) { System.out.println("Serverio paruosimas nepavyko!!"); return; }
		s.ServerLoop();
	}
	
	public static void main(String args[]) {
		(new Main()).cmain(args);
	}
}
