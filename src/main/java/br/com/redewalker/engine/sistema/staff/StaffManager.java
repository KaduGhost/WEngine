package br.com.redewalker.engine.sistema.staff;

import br.com.redewalker.engine.WEngine;
import br.com.redewalker.engine.apis.MensagensAPI;
import br.com.redewalker.engine.eventos.UsuarioAlteradoStaffEvent;
import br.com.redewalker.engine.eventos.UsuarioEntrouStaffEvent;
import br.com.redewalker.engine.eventos.UsuarioRemovidoStaffEvent;
import br.com.redewalker.engine.sistema.grupo.GrupoType;
import br.com.redewalker.engine.sistema.grupo.GrupoType.CargoServerType;
import br.com.redewalker.engine.sistema.server.Servers;
import br.com.redewalker.engine.sistema.server.Servers.ServerType;
import br.com.redewalker.engine.sistema.usuario.Usuario;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class StaffManager implements Listener {
	
	public StaffManager() {
		WEngine.get().getConexaoManager().getStaffConnection().checkTable();
		Bukkit.getServer().getPluginManager().registerEvents(this, WEngine.get());
	}
	
	public void removerStaff(Usuario usuario, Servers server, Usuario autor) throws Exception {
		if (autor instanceof Usuario && !autor.hasPermission("walker.staff.setar")) throw new Exception("Você não tem permissão para alterar o cargo de algum jogador");
		Staff s = getStaffNoServer(usuario, Servers.Rede);
		if (s != null) {
			if (autor instanceof Usuario && (!autor.hasPermission("walker.staff.setar.gerente"))) throw new Exception("Você não tem permissão para alterar o cargo de algum jogador na rede");
			if (autor instanceof Usuario && (s.getGrupo().getPrioridade() >= autor.getGrupoIn().getPrioridade())) throw new Exception("Você não pode alterar o cargo de um superior");
			UsuarioRemovidoStaffEvent event = new UsuarioRemovidoStaffEvent(usuario, s.getGrupo(), autor);
			WEngine.get().getConexaoManager().getStaffConnection().deleteStaff(usuario.getNickOriginal());
			WEngine.get().getServer().getPluginManager().callEvent(event);
		}else {
			if (server.getTipo().equals(ServerType.Lobby)) throw new Exception("Você não pode atribuir cargo exclusivo de servidor no lobby");
			s = getStaffNoServer(usuario, server);
			if (s != null) {
				if (autor instanceof Usuario && (s.getGrupo().getPrioridade() >= autor.getGrupoIn().getPrioridade())) throw new Exception("Você não pode alterar o cargo de um superior");
				UsuarioRemovidoStaffEvent event = new UsuarioRemovidoStaffEvent(usuario, s.getGrupo(), autor);
				WEngine.get().getConexaoManager().getStaffConnection().deleteStaff(s.getId());
				WEngine.get().getServer().getPluginManager().callEvent(event);
			}
		}
		usuario.atualizarGrupo();
	}
	
	public void setStaff(Usuario usuario, Servers server, GrupoType grupo, Usuario autor) throws Exception {
		if (!grupo.isStaff() || grupo.equals(GrupoType.Staff)) throw new Exception("Este cargo não faz parte da equipe do servidor");
		if (autor instanceof Usuario && !autor.hasPermission("walker.staff.setar")) throw new Exception("Você não tem permissão para alterar o cargo de algum jogador");
		if (grupo.getPrioridade() >= autor.getGrupoIn().getPrioridade()) throw new Exception("Você não tem permissão para definir cargos superiores ou igual ao seu");
		if (autor instanceof Usuario && autor.getGrupoIn().getPrioridade() <= grupo.getPrioridade()) throw new Exception("Você não pode alterar o cargo de um superior");
		if (grupo.getServer().equals(CargoServerType.Rede)) {
			if (autor instanceof Usuario && (!autor.hasPermission("walker.staff.setar.gerente"))) throw new Exception("Você não tem permissão para alterar o cargo de algum jogador na rede");
			Staff s = getStaffNoServer(usuario, Servers.Rede);
			if (s != null) {
				if (autor instanceof Usuario && (s.getGrupo().getPrioridade() >= autor.getGrupoIn().getPrioridade())) throw new Exception("Você não pode alterar o cargo de um superior");
				UsuarioAlteradoStaffEvent event = new UsuarioAlteradoStaffEvent(usuario, s.getGrupo(),grupo, autor);
				WEngine.get().getConexaoManager().getStaffConnection().modifyStaff(s.getId(), grupo, Servers.Rede);
				WEngine.get().getServer().getPluginManager().callEvent(event);
			}else {
				WEngine.get().getConexaoManager().getStaffConnection().deleteStaff(usuario.getNickOriginal());
				WEngine.get().getConexaoManager().getStaffConnection().createStaff(usuario.getNickOriginal(), grupo, Servers.Rede);
				WEngine.get().getServer().getPluginManager().callEvent(new UsuarioEntrouStaffEvent(usuario, grupo, autor));
			}
		}else {
			if (server.getTipo().equals(ServerType.Lobby)) throw new Exception("Você não pode atribuir cargo exclusivo de servidor no lobby");
			Staff s = getStaffNoServer(usuario, Servers.Rede);
			if (s != null) {
				if (autor instanceof Usuario && (s.getGrupo().getPrioridade() >= autor.getGrupoIn().getPrioridade())) throw new Exception("Você não pode alterar o cargo de um superior");
				WEngine.get().getConexaoManager().getStaffConnection().deleteStaff(s.getId());
			}
			s = getStaffNoServer(usuario, server);
			if (s != null) {
				if (autor instanceof Usuario && (s.getGrupo().getPrioridade() >= autor.getGrupoIn().getPrioridade())) throw new Exception("Você não pode alterar o cargo de um superior");
				UsuarioAlteradoStaffEvent event = new UsuarioAlteradoStaffEvent(usuario, s.getGrupo(),grupo, autor);
				WEngine.get().getConexaoManager().getStaffConnection().modifyStaff(s.getId(), grupo, server);
				WEngine.get().getServer().getPluginManager().callEvent(event);
			}else {
				WEngine.get().getConexaoManager().getStaffConnection().createStaff(usuario.getNickOriginal(), grupo, server);
				WEngine.get().getServer().getPluginManager().callEvent(new UsuarioEntrouStaffEvent(usuario, grupo, autor));
			}
		}
		usuario.atualizarGrupo();
	}
	
	public boolean isStaffNoServer(Usuario usuario, Servers server) {
		return WEngine.get().getConexaoManager().getStaffConnection().isStaffServer(usuario.getNickOriginal(), server);
	}
	
	public Staff getStaffNoServer(Usuario usuario, Servers server) {
		return WEngine.get().getConexaoManager().getStaffConnection().getStaff(usuario.getNickOriginal(), server);
	}
	
	public Staff getStaffAtivoNoServer(Usuario usuario, Servers server) {
		Staff s = getStaffNoServer(usuario, Servers.Rede);
		if (s != null) return s;
		return WEngine.get().getConexaoManager().getStaffConnection().getStaff(usuario.getNickOriginal(), server);
	}
	
	
	@EventHandler
	void aoEntrarStaff(UsuarioEntrouStaffEvent e) {
		MensagensAPI.mensagemSucesso("Você definiu o cargo "+ WEngine.get().getGruposManager().getNomeGrupo(e.getGrupo())+" ao jogador "+e.getJogador().getNickOriginal(), e.getAutor().getPlayer());
		MensagensAPI.mensagemSucesso("Você agora faz parte da equipe do servidor, bem vindo :D", e.getJogador().getPlayer());
	}
	
	@EventHandler
	void aoSairStaff(UsuarioRemovidoStaffEvent e) {
		MensagensAPI.mensagemSucesso("Você removeu o jogador "+e.getJogador().getNickOriginal()+" da equipe do servidor", e.getAutor().getPlayer());
		MensagensAPI.mensagemSucesso("Você foi removido da equipe do servidor", e.getJogador().getPlayer());
	}
	
	@EventHandler
	void aoAlterarStaff(UsuarioAlteradoStaffEvent e) {
		MensagensAPI.mensagemSucesso("Você definiu o cargo "+ WEngine.get().getGruposManager().getNomeGrupo(e.getGrupoNovo())+" ao jogador "+e.getJogador().getNickOriginal(), e.getAutor().getPlayer());
		MensagensAPI.mensagemSucesso("Seu cargo na equipe do servidor foi alterado. :D", e.getJogador().getPlayer());
	}
	
	
	
	
	
	
	/*public void removeStaff(Usuario usuario, Servers server, Usuario autor) {
		if (autor instanceof Usuario && (!autor.hasPermission("walker.staff.setar") || !WalkerEngine.get().getGruposManager().isSuperior(autor, usuario))) return;
		if (server.getTipo().equals(ServerType.Lobby)) {
			if (autor instanceof Usuario && !autor.hasPermission("walker.staff.setar.gerente")) return;
			WalkerEngine.get().getConexaoManager().getStaffConnection().deleteStaff(usuario.getNickOriginal());
		}else if (isStaffInServer(usuario.getNickOriginal(), server)) {
			WalkerEngine.get().getConexaoManager().getStaffConnection().deleteStaff(server, usuario.getNickOriginal());
		}
		usuario.atualizarGrupo();
	}
	
	public boolean setStaff(Usuario usuario, GrupoType cargo, Servers server, Usuario autor) {
		if (!cargo.getTipo().equals(CargoType.Staff) || cargo.equals(GrupoType.Staff)) return false;
		if (autor instanceof Usuario && (!autor.hasPermission("walker.staff.setar") || !WalkerEngine.get().getGruposManager().isSuperior(autor, usuario))) return false;
		if (server.getTipo().equals(ServerType.Lobby)) {
			if (autor instanceof Usuario && (!autor.hasPermission("walker.staff.setar.gerente") || cargo.getServer().equals(CargoServerType.Server))) return false;
			Staff staff = WalkerEngine.get().getStaffManager().getStaffInServer(usuario,Servers.Rede);
			if (staff instanceof Staff) {
				WalkerEngine.get().getConexaoManager().getStaffConnection().modifyStaff(staff.getId(), , cargo);
			}
			else WalkerEngine.get().getConexaoManager().getStaffConnection().createStaff(usuario.getNickOriginal(), cargo, Servers.Rede);
		}else {
			Staff staff = WalkerEngine.get().getConexaoManager().getStaffConnection().getStaff(usuario.getNickOriginal(), server);
			if (staff instanceof Staff) staff.setGrupo(cargo);
			else WalkerEngine.get().getConexaoManager().getStaffConnection().createStaff(usuario.getNickOriginal(), cargo, server);
		}
		usuario.atualizarGrupo();
		return true;
	}*/
	
	public HashMap<Integer, Staff> getAllStaffs() {
		return WEngine.get().getConexaoManager().getStaffConnection().getAllStaffs();
	}
	
	public HashMap<Integer, Staff> getAllStaffsServer(String server) {
		return WEngine.get().getConexaoManager().getStaffConnection().getAllStaffsServer(server);
	}
	
	/*public boolean isStaffInServer(String nick, Servers server) {
		return WalkerEngine.get().getConexaoManager().getStaffConnection().isStaffServer(nick, Servers.Rede) || WalkerEngine.get().getConexaoManager().getStaffConnection().isStaffServer(nick, server);
	}
	
	public Staff getStaffInServer(Usuario usuario, Servers server) {
		Staff s = WalkerEngine.get().getConexaoManager().getStaffConnection().getStaff(usuario.getNickOriginal(), Servers.Rede);
		if (s instanceof Staff) return s;
		return WalkerEngine.get().getConexaoManager().getStaffConnection().getStaff(usuario.getNickOriginal(), server);
		
	}*/
	
	public boolean isStaff(String nick) {
		return WEngine.get().getConexaoManager().getStaffConnection().isStaff(nick);
	}
	
}
