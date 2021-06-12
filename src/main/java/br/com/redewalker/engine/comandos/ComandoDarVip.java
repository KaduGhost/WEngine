package br.com.redewalker.engine.comandos;

import br.com.redewalker.engine.sistema.usuario.Usuario;
import br.com.redewalker.engine.WEngine;
import br.com.redewalker.engine.apis.MensagensAPI;
import br.com.redewalker.engine.sistema.grupo.Grupo;
import br.com.redewalker.engine.sistema.grupo.GrupoType;
import br.com.redewalker.engine.sistema.server.Servers;
import br.com.redewalker.engine.sistema.server.Servers.ServerType;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComandoDarVip extends BukkitCommand {
	
	public ComandoDarVip(String name) {
		super(name);
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		if (sender instanceof Player) {
			Usuario j = WEngine.get().getUsuarioManager().getUsuario(sender.getName());
			if (!j.hasPermission("walker.darvip")) {
				MensagensAPI.semPermissaoComando(sender);
				return false;
			}
		}
		if (args.length != 4) {
			MensagensAPI.formatoIncorreto("darvip <jogador> <vip> <dias> <server>", sender);
			return false;
		}
		Usuario j2 = WEngine.get().getUsuarioManager().getUsuario(args[0]);
		if (!(j2 instanceof Usuario)) {
			MensagensAPI.jogadorNaoEncontrado(sender);
			return false;
		}
		if (!GrupoType.isExists(args[1])) {
			MensagensAPI.grupoNaoEncontrado(sender);
			return true;
		}
		GrupoType gp = GrupoType.getTipo(args[1]);
		if (!gp.isVIP() || gp.equals(GrupoType.VIPLobby)) {
			sender.sendMessage(MensagensAPI.getErro()+"Esse grupo não é um grupo vip.");
			return false;
		}
		Grupo grupo = WEngine.get().getGruposManager().getGrupo(gp);
		Servers server = Servers.getTipo(args[3]);
		if (server == null) {
			MensagensAPI.mensagemErro("Este servidor não existe", sender);
			return false;
		}
		if (server.getTipo().equals(ServerType.Lobby)) {
			MensagensAPI.mensagemErro("Você não pode dar vip em servidor Lobby", sender);
			return false;
		}
		Pattern pt = Pattern.compile("[^0-9]");
		Matcher m = pt.matcher(args[2]);
	    boolean b = m.find();
		if (b) {
	    	sender.sendMessage(MensagensAPI.getErro() + "Digite um tempo em númerico em dias.");
	    	return false;
	    }
		if (WEngine.get().getVipManager().containsVIPPermanente(j2, gp, server)) {
			sender.sendMessage(MensagensAPI.getErro()+"Este jogador já tem um vip permanente do tipo "+grupo.getTag().toString()+MensagensAPI.getErro()+" ativo no servidor "+server.toString());
			return false;
		}
		int dias = Integer.parseInt(args[2]);
		WEngine.get().getVipManager().setVip(j2, gp, dias, sender.getName(), server);
		if (dias == 0) sender.sendMessage("§aVocê deu um vip "+grupo.getTag().toString()+"§a de duração permanente ao jogador '"+j2.getNickOriginal()+"'.");
		else sender.sendMessage("§aVocê deu um vip "+grupo.getTag().toString()+"§a com duração de "+dias+" dias ao jogador '"+j2.getNickOriginal()+"'.");
		WEngine.get().getVipManager().darItens(j2, gp);
		return false;
	}

}
