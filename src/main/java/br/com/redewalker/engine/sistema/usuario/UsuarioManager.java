package br.com.redewalker.engine.sistema.usuario;

import br.com.redewalker.engine.WEngine;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class UsuarioManager {
	
	public UsuarioManager() {
		WEngine.get().getConexaoManager().getUsuarioConnection().checkTable();
	}
	
	public int getQuantidadeUsuariosOnlineNoServerBypassVanish() {
		return Bukkit.getOnlinePlayers().size();
	}
	
	public long getQuantidadeUsuariosOnlineBypassVanish() {
		return WEngine.get().getConexaoManager().getUsuarioConnection().getQuantUsuariosOnline();
	}
	
	public long getQuantidadeUsuariosOnlineNoServer() {
		int cont = 0;
		for (Player p : Bukkit.getOnlinePlayers()) {
			Usuario j = getUsuario(p.getName());
			if (!j.isVanish()) cont++;
		}
		return cont;
	}
	
	public HashMap<String, Usuario> getAllUsuarios() {
		return WEngine.get().getConexaoManager().getUsuarioConnection().getAllUsuarios();
	}
	
	public Usuario getUsuario(String nick) {
		if (!exists(nick)) return null;
		return WEngine.get().getConexaoManager().getUsuarioConnection().getUsuario(nick);
	}
	
	public Usuario createUsuario(String p) {
		if (exists(p)) return null;
		WEngine.get().getConexaoManager().getUsuarioConnection().createUsuario(p);
		return getUsuario(p);
	}
	
	public boolean exists(String nick) {
		return WEngine.get().getConexaoManager().getUsuarioConnection().exists("nick", nick);
	}
	
}
