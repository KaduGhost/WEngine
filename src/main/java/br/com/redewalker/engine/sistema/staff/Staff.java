package br.com.redewalker.engine.sistema.staff;

import br.com.redewalker.engine.WEngine;
import br.com.redewalker.engine.sistema.grupo.GrupoType;
import br.com.redewalker.engine.sistema.server.Servers;
import br.com.redewalker.engine.sistema.usuario.Usuario;

public class Staff {
	
	private int id;
	
	public Staff(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void setGrupo(GrupoType grupo) {
		WEngine.get().getConexaoManager().getStaffConnection().modifyStaff(id, grupo, getServer());
	}
	
	public GrupoType getGrupo() {
		return WEngine.get().getConexaoManager().getStaffConnection().getGrupo(id);
	}
	
	public Usuario getUsuario() {
		return WEngine.get().getUsuarioManager().getUsuario(WEngine.get().getConexaoManager().getStaffConnection().getString(id, "nick"));
	}
	
	public Servers getServer() {
		return Servers.getTipo(WEngine.get().getConexaoManager().getStaffConnection().getString(id, "server"));
	}

}
