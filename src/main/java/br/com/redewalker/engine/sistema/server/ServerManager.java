package br.com.redewalker.engine.sistema.server;

import br.com.redewalker.engine.WEngine;
import br.com.redewalker.engine.sistema.grupo.GrupoType;

import java.util.ArrayList;
import java.util.EnumSet;

public class ServerManager {
	
	private Servers server;
	
	public ServerManager(Servers tipo) {
		this.server = tipo;
		WEngine.get().getConexaoManager().getConexaoServersDAO().checkTable();
		WEngine.get().getConexaoManager().getConexaoServersDAO().saveAllServers(getAllServer());
	}
	
	public ArrayList<Server> getAllServersLobby() {
		ArrayList<Server> servers = new ArrayList<Server>();
		for (Server s : getAllServer()) {
			if (s.getServer().getTipo().equals(Servers.ServerType.Lobby)) servers.add(s);
		}
		return servers;
	}
	
	public Servers getServerType() {
		return server;
	}
	
	public Server getServer() {
		return new Server(server);
	}
	
	public Server getServer(Servers server) {
		return new Server(server); 
	}
	
	public ArrayList<Server> getAllServer() {
		ArrayList<Server> servers = new ArrayList<Server>();
		for (Servers gp : EnumSet.allOf(Servers.class)) {
			servers.add(new Server(gp));
		}
		return servers;
	}
	
	public static boolean isExists(String nome) {
		for (GrupoType gp : EnumSet.allOf(GrupoType.class)) {
			if (nome.equalsIgnoreCase(gp.toString())) return true;
		}
		return false;
	}
	
	

}
