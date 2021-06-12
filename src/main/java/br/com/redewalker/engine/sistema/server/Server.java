package br.com.redewalker.engine.sistema.server;

import br.com.redewalker.engine.WEngine;

public class Server {
	
	private Servers server;
	
	public Server(Servers server) {
		this.server = server;
	}
	
	public Servers getServer() {
		return server;
	}
	
	public void setManutencao(boolean set) {
		WEngine.get().getConexaoManager().getConexaoServersDAO().setBoolean(server.toString(), "manutencao", set);
	}
	
	public boolean isManutencao() {
		return WEngine.get().getConexaoManager().getConexaoServersDAO().getBoolean(server.toString(), "manutencao");
	}
	
}
