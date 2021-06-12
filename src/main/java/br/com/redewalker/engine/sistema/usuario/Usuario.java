package br.com.redewalker.engine.sistema.usuario;

import br.com.redewalker.engine.WEngine;
import br.com.redewalker.engine.sistema.grupo.GrupoType;
import br.com.redewalker.engine.sistema.grupo.GrupoType.CargoType;
import br.com.redewalker.engine.sistema.server.Servers;
import br.com.redewalker.engine.sistema.server.Servers.ServerType;
import br.com.redewalker.engine.sistema.staff.Staff;
import br.com.redewalker.engine.sistema.vip.VIP;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Usuario {
	
	private String nickOriginal;
	
	public Usuario(String nick) {
		this.nickOriginal = nick;
	}
	
	public String getChatFocus() {
		return WEngine.get().getConexaoManager().getUsuarioConnection().getString(nickOriginal, "chatfocus");
	}

	public void setChatFocus(String chatFocus) {
		WEngine.get().getConexaoManager().getUsuarioConnection().setString(nickOriginal, "chatfocus", chatFocus);
	}
	
	public boolean hasPermission(String permission) {
		return /*permissions.contains(permission.toLowerCase()) ||*/ hasPermissionOP() || WEngine.get().getGruposManager().hasPermission(getGrupoIn(), permission) || WEngine.get().getGruposManager().hasPermission(getGrupoOut(), permission) /*|| (getPlayer() instanceof Player && (getPlayer().hasPermission(permission)))*/;
	}
	
	private boolean hasPermissionOP() {
		return /*permissions.contains("*") ||*/ WEngine.get().getGruposManager().hasPermission(getGrupoIn(), "*") || WEngine.get().getGruposManager().hasPermission(getGrupoOut(), "*") /*|| (getPlayer() instanceof Player && (getPlayer().hasPermission("*") || getPlayer().isOp()))*/;
	}
	
	public void atualizarGrupo() {
		boolean istaff = WEngine.get().getStaffManager().isStaff(getNickOriginal());
		boolean isvip = WEngine.get().getVipManager().isVIP(this);
		if (!isvip && getGrupoIn().isVIP()) setGrupoIn(GrupoType.Membro);
		else if (isvip) {
			VIP vip = WEngine.get().getVipManager().getVIPAtivo(getNickOriginal(), WEngine.get().getServerType());
			if (vip != null) {
				setGrupoIn(vip.getTipo());
				return;
			}
			else if (vip == null && WEngine.get().getServerType().getTipo().equals(ServerType.Lobby)) setGrupoIn(GrupoType.VIPLobby);
		}
		if (!istaff && getGrupoIn().isStaff()) setGrupoIn(GrupoType.Membro); 
		else if (istaff) {
			Staff s = WEngine.get().getStaffManager().getStaffAtivoNoServer(this, WEngine.get().getServerType());
			if (s != null) setGrupoIn(s.getGrupo());
			else setGrupoIn(GrupoType.Staff);
		}
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayerExact(nickOriginal);
	}
	
	public void setGrupoIn(GrupoType grupo) {
		if (grupo.getTipo().equals(CargoType.Tag)) return;
		WEngine.get().getConexaoManager().getUsuarioConnection().setString(nickOriginal, "grupo", grupo.toString());
	}
	
	public void setGrupoOut(GrupoType grupo) {
		if (!grupo.getTipo().equals(CargoType.Tag)) return;
		WEngine.get().getConexaoManager().getUsuarioConnection().setString(nickOriginal, "grupoout", grupo.toString());
	}
	
	public GrupoType getGrupoIn() {
		return GrupoType.getTipo(WEngine.get().getConexaoManager().getUsuarioConnection().getString(nickOriginal, "grupo"));
	}
	
	public GrupoType getGrupoOut() {
		return GrupoType.getTipo(WEngine.get().getConexaoManager().getUsuarioConnection().getString(nickOriginal, "grupoout"));
	}
	
	public void setVerMsgPunicao(boolean set) {
		WEngine.get().getConexaoManager().getUsuarioConnection().setBoolean(nickOriginal, "msgban", set);
	}
	
	public boolean isVerMsgPunicao() {
		return WEngine.get().getConexaoManager().getUsuarioConnection().getBoolean(nickOriginal, "msgban");
	}
	
	public void setLogado(boolean set) {
		WEngine.get().getConexaoManager().getUsuarioConnection().setBoolean(nickOriginal, "logado", set);
	}
	
	public boolean isLogado() {
		return WEngine.get().getConexaoManager().getUsuarioConnection().getBoolean(nickOriginal, "logado");
	}
	
	private void setOnlineInServer(String set) {
		WEngine.get().getConexaoManager().getUsuarioConnection().setString(nickOriginal, "onlineserver", set);
	}
	
	public boolean isOnlineInServer() {
		String s = WEngine.get().getConexaoManager().getUsuarioConnection().getString(nickOriginal, "onlineserver");
		if (!s.equalsIgnoreCase("nenhum") && Servers.getTipo(s) != null) return Servers.getTipo(s).equals(WEngine.get().getServerType());
		return false;
	}
	
	public void setOnline(boolean set) {
		if (!set) setOnlineInServer("nenhum");
		else setOnlineInServer(WEngine.get().getServerType().toString());
		WEngine.get().getConexaoManager().getUsuarioConnection().setBoolean(nickOriginal, "online", set);
	}
	
	public boolean isOnline() {
		return WEngine.get().getConexaoManager().getUsuarioConnection().getBoolean(nickOriginal, "online");
	}
	
	public void setTell(boolean set) {
		WEngine.get().getConexaoManager().getUsuarioConnection().setBoolean(nickOriginal, "tell", set);
	}
	
	public boolean isTell() {
		return WEngine.get().getConexaoManager().getUsuarioConnection().getBoolean(nickOriginal, "tell");
	}
	
	public void getServer() {
		
	}
	
	public void setServer() {
		
	}
	
	public double getCash() {
		return WEngine.get().getConexaoManager().getUsuarioConnection().getDouble(nickOriginal, "cash");
	}

	public void setCash(double cash) {
		WEngine.get().getConexaoManager().getUsuarioConnection().setDouble(nickOriginal, "cash", cash);
	}
	
	public String getNickOriginal() {
		return nickOriginal;
	}
	
	public boolean isVanish() {
		return WEngine.get().getConexaoManager().getUsuarioConnection().getBoolean(nickOriginal, "vanish");
	}

	public void setVanish(boolean set) {
		WEngine.get().getConexaoManager().getUsuarioConnection().setBoolean(nickOriginal, "vanish", set);
	}
	
}
