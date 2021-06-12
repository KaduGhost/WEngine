package br.com.redewalker.engine.sistema.vip;

import br.com.redewalker.engine.WEngine;
import br.com.redewalker.engine.sistema.grupo.GrupoType;
import br.com.redewalker.engine.sistema.server.Servers;
import br.com.redewalker.engine.sistema.usuario.Usuario;

public class VIP implements Comparable<VIP> {
	
	private long id;
	
	public VIP(long id) {
		this.id = id;
	}
	
	public long getID() {
		return id;
	}
	
	public Servers getServer() {
		return Servers.getTipo(WEngine.get().getConexaoManager().getConexaoVIPDAO().getString(id, "server"));
	}
	
	public boolean isPermanente() {
		return WEngine.get().getConexaoManager().getConexaoVIPDAO().getLong(id, "tempo") == 0L;
	}
	
	public boolean isAtivo() {
		return WEngine.get().getConexaoManager().getConexaoVIPDAO().isAtivo(id);
	}
	
	public void setAtivo(boolean set) {
		WEngine.get().getConexaoManager().getConexaoVIPDAO().setBoolean(id, "ativo", set);
		long tempo = getTempoRestante();
		if (!set && tempo > 0L) setTempoRestante((tempo-System.currentTimeMillis())/1000L);
		else if (set && tempo > 0L) {
			setTempoRestante((tempo*1000L)+System.currentTimeMillis());
			setInicio();
		}
	}
	
	public long getInicio() {
		return WEngine.get().getConexaoManager().getConexaoVIPDAO().getLong(id, "dia");
	}
	
	public void setInicio() {
		WEngine.get().getConexaoManager().getConexaoVIPDAO().setLong(id, "dia", System.currentTimeMillis());
	}
	
	public boolean isExpirado() {
		return WEngine.get().getConexaoManager().getConexaoVIPDAO().isExpirado(id);
	}
	
	public Usuario getUsuario() {
		return WEngine.get().getUsuarioManager().getUsuario(WEngine.get().getConexaoManager().getConexaoVIPDAO().getString(id, "nick"));
	}
	
	public GrupoType getTipo() {
		return GrupoType.getTipo(WEngine.get().getConexaoManager().getConexaoVIPDAO().getString(id, "vip"));
	}
	
	public String getRecebidoPor() {
		return WEngine.get().getConexaoManager().getConexaoVIPDAO().getString(id, "recebido");
	}
	
	public long getTempoRestante() {
		return WEngine.get().getConexaoManager().getConexaoVIPDAO().getLong(id, "tempo");
	}
	
	public void setTempoRestante(long valor) {
		if (valor < 0) valor = -1L;
		WEngine.get().getConexaoManager().getConexaoVIPDAO().setLong(id, "tempo", valor);
	}
	
	public String getRemovidoPor() {
		return WEngine.get().getConexaoManager().getConexaoVIPDAO().getString(id, "removido");
	}
	
	public void setRemovidoPor(String valor) {
		WEngine.get().getConexaoManager().getConexaoVIPDAO().setString(id, "removido", valor);
	}
	
	@Override
	public int compareTo(VIP o) {
		if (id < o.getID()) return -1;
		else if (id > o.getID()) return 1;
		else return 0;
	}
	
}
