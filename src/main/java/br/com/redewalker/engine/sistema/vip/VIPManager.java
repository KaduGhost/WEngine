package br.com.redewalker.engine.sistema.vip;

import br.com.redewalker.engine.listeners.VIPsListeners;
import br.com.redewalker.engine.sistema.server.Servers;
import br.com.redewalker.engine.sistema.usuario.Usuario;
import br.com.redewalker.engine.sistema.vip.inventario.*;
import br.com.redewalker.engine.WEngine;
import br.com.redewalker.engine.bungee.WalkerBungee;
import br.com.redewalker.engine.sistema.grupo.GrupoType;
import br.com.redewalker.kaduzin.engine.sistema.vip.inventario.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class VIPManager {
	
	public VIPManager() {
		WEngine.get().getConexaoManager().getConexaoVIPDAO().checkTable();
		new BukkitRunnable() {
			@Override
			public void run() {
				checkAllVipsAtivos();
			}
		}.runTaskTimer(WEngine.get(), 0, 20*60*15);
		Bukkit.getServer().getPluginManager().registerEvents(new VIPsListeners(), WEngine.get());
	}
	
	public void setVip(Usuario p, GrupoType vip, int dias, String recebido, Servers server) {
		if (!vip.isVIP() || vip.equals(GrupoType.VIPLobby)) return;
		long tempo = 86400*dias;
		VIP vip1 = WEngine.get().getConexaoManager().getConexaoVIPDAO().createVIP(p.getNickOriginal(), vip.toString(), server.toString(), tempo, false, System.currentTimeMillis(), recebido, dias);
		ativarVIP(vip1);
		WalkerBungee.sendBungeeVip(vip1.getID()+"", vip1.getServer().toString());
	}
	
	public void darItens(Usuario p, GrupoType vip) {
		if (!vip.isVIP() || vip.equals(GrupoType.VIPLobby)) return;
		  ArrayList<String> cmds = WEngine.get().getConfigManager().getConfigPrincipal().getCmds(vip.toString().toLowerCase());
		  for (String cmd : cmds) {
			  if (cmd.contains(";")) {
				  String[] cc = cmd.split(";");
				  Random r = new Random();
				  if (Integer.parseInt(cc[0]) >= r.nextInt(100)) Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cc[1].replace("@jogador", p.getNickOriginal()));
			  }else Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("@jogador", p.getNickOriginal()));
		  }
	}
	
	public void abrirInventario(Player p, InventarioVipsType tipo, VIP vip, Servers server, int pagina, Usuario j2, String st) {
		switch (tipo) {
		case MENU:
			new InventarioVIPMenu(p, tipo);
			break;
		case HISTORICOVIPS:
			new InventarioVIPsHistorico(pagina, p, server, tipo);
			break;
		case ESCOLHERSERVIDOR:
			new InventarioVIPEscolherServidor(pagina, p, st);
			break;
		case LISTAVIPSSERVER:
			new InventarioVIPsLista(pagina, p, server, tipo);
			break;
		case LISTAVIPS:
			new InventarioVIPsByUsuarioLista(pagina, p, server, j2, tipo);
			break;
		default:
			break;
		}
	}
	
	public boolean containsVIPPermanente(Usuario user, GrupoType vip, Servers server) {
		return WEngine.get().getConexaoManager().getConexaoVIPDAO().containsPermanente(user.getNickOriginal(), vip.toString(), server.toString());
	}

	public void ativarVIP(VIP id) {
		VIP atv = getVIPAtivo(id.getUsuario().getNickOriginal(), id.getServer());
		if (atv instanceof VIP && id.getID() == atv.getID()) return;
		if (atv instanceof VIP) atv.setAtivo(false);
		id.setAtivo(true);
	}
	
	public void desativarVIP(VIP id) {
		id.setAtivo(false);
		VIP vip = getVIPAtivo(id.getUsuario().getNickOriginal(), id.getServer());
		if (!(vip instanceof VIP)) setNextVIP(id.getUsuario(), id.getServer());
	}
	
	public void removerVip(VIP id, String remove) {
		id.setAtivo(false);
		id.setTempoRestante(-1);
		id.setRemovidoPor(remove);
	}
	
	public void checkAllVipsAtivos() {
		ArrayList<VIP> vips = WEngine.get().getConexaoManager().getConexaoVIPDAO().getAllVipsAtivos();
		for (VIP vip : vips) {
			long tempo = vip.getTempoRestante();
			if (tempo < 0 || tempo < System.currentTimeMillis()) desativarVIP(vip);
		}
	}
	
	private void setNextVIP(Usuario user, Servers server) {
		ArrayList<VIP> vips = getVIPsParaAtivar(user, server);
		if (!vips.isEmpty()) ativarVIP(vips.get(0));
	}
	
	public boolean isVIP(Usuario user) {
		return WEngine.get().getConexaoManager().getConexaoVIPDAO().isVIP(user.getNickOriginal());
	}
	
	public VIP getVIPAtivo(String usuario, Servers server) {
		VIP vip = WEngine.get().getConexaoManager().getConexaoVIPDAO().getVipOnlyAtivo(usuario, server.toString());
		if (vip instanceof VIP) desativarVIP(vip);
		return WEngine.get().getConexaoManager().getConexaoVIPDAO().getVipAtivo(usuario, server.toString());
	}
	
	public VIP getVIP(long id) {
		return WEngine.get().getConexaoManager().getConexaoVIPDAO().getVip(id);
	}
	
	public ArrayList<VIP> getVIPsParaAtivar(Usuario user, Servers server) {
		return WEngine.get().getConexaoManager().getConexaoVIPDAO().getVipsParaAtivar(user.getNickOriginal(), server.toString());
	}
	
	public ArrayList<VIP> getVIPsValidos(Servers server) {
		return WEngine.get().getConexaoManager().getConexaoVIPDAO().getVipsValidosByServer(server);
	}
	
	public ArrayList<VIP> getVIPs(Servers server) {
		return WEngine.get().getConexaoManager().getConexaoVIPDAO().getVips(server);
	}
	
	public HashMap<String, ArrayList<VIP>> getVIPsWithKey(Servers server) {
		return WEngine.get().getConexaoManager().getConexaoVIPDAO().getVipsWithKey(server);
	}
	
	public ArrayList<VIP> getVIPsValidosByJogador(Usuario user, Servers server) {
		return WEngine.get().getConexaoManager().getConexaoVIPDAO().getVipsValidosByJogador(user, server);
	}
	
	public ArrayList<VIP> getVIPs(Usuario user, Servers server) {
		return WEngine.get().getConexaoManager().getConexaoVIPDAO().getVips(user.getNickOriginal(), server.toString());
	}

}
