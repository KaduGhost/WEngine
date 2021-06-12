package br.com.redewalker.engine.comandos;

import br.com.redewalker.engine.WEngine;
import br.com.redewalker.engine.apis.MensagensAPI;
import br.com.redewalker.engine.sistema.grupo.Grupo;
import br.com.redewalker.engine.sistema.grupo.GrupoType;
import br.com.redewalker.engine.sistema.server.Servers;
import br.com.redewalker.engine.sistema.usuario.Usuario;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

public class ComandoStaff extends BukkitCommand {

	public ComandoStaff(String name) {
		super(name);
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		Usuario j = WEngine.get().getUsuarioManager().getUsuario(sender.getName());
		if (j instanceof Usuario && !j.hasPermission("walker.staff.setar")) {
			MensagensAPI.semPermissaoComando(sender);
			return true;
		}
		if (args.length == 0) {
			sender.sendMessage("");
			sender.sendMessage("  §6Comandos de staff");
			sender.sendMessage(" §e/staff §7 - para ver os comandos edição de staff.");
			sender.sendMessage(" §e/staff <jogador>§7 - para ver os grupos do jogador.");
			sender.sendMessage(" §e/staff <jogador> remover (server/rede)§7 - para remover o jogador da staff.");
			sender.sendMessage(" §e/staff <jogador> <grupo> (server/rede)§7 - para definir o grupo do jogador.");
			sender.sendMessage("");
			return true;
		}
		Usuario j2 = WEngine.get().getUsuarioManager().getUsuario(args[0]);
		if (!(j2 instanceof Usuario)) {
			MensagensAPI.jogadorNaoEncontrado(sender);
			return false;
		}
		if (args.length == 1) {
			Grupo gp = WEngine.get().getGruposManager().getGrupo(j2.getGrupoIn());
			Grupo gp2 = WEngine.get().getGruposManager().getGrupo(j2.getGrupoOut());
			sender.sendMessage("");
			sender.sendMessage(" §6Jogador "+j2.getNickOriginal()+":");
			sender.sendMessage(" §eO grupo é: "+gp.getTag().getCor()+gp.getNome()+".");
			sender.sendMessage(" §eO grupo tag é: "+gp2.getTag().getCor()+gp2.getNome()+".");
			sender.sendMessage("");
			return true;
		}else if (args.length == 2) {
			if (args[1].equalsIgnoreCase("remover")) {
				try {
					WEngine.get().getStaffManager().removerStaff(j2, WEngine.get().getServerType(), j);
				} catch (Exception e) {
					MensagensAPI.mensagemErro(e.getMessage(),sender);
				}
				return true;
			}
			if (!GrupoType.isExists(args[1])) {
				MensagensAPI.grupoNaoEncontrado(sender);
				return true;
			}
			GrupoType gp = GrupoType.getTipo(args[1]);
			try {
				WEngine.get().getStaffManager().setStaff(j2, WEngine.get().getServerType(), gp, j);
			} catch (Exception e) {
				MensagensAPI.mensagemErro(e.getMessage(),sender);
			}
			return true;
		}else if (args.length == 3) {
			if (j instanceof Usuario && !j.hasPermission("walker.staff.setar.gerente")) {
				MensagensAPI.semPermissaoComando(sender);
				return true;
			}
			Servers server = Servers.getTipo(args[2]);
			if (args[2].equalsIgnoreCase("rede")) server = Servers.Rede;
			if (server==null) {
				MensagensAPI.mensagemErro("Servidor não encontrado", sender);
				return true;
			}
			if (args[1].equalsIgnoreCase("remover")) {
				try {
					WEngine.get().getStaffManager().removerStaff(j2, server, j);
				} catch (Exception e) {
					MensagensAPI.mensagemErro(e.getMessage(),sender);
				}
				return true;
			}
			if (!GrupoType.isExists(args[1])) {
				MensagensAPI.grupoNaoEncontrado(sender);
				return true;
			}
			GrupoType gp = GrupoType.getTipo(args[1]);
			try {
				WEngine.get().getStaffManager().setStaff(j2, server, gp, j);
			} catch (Exception e) {
				MensagensAPI.mensagemErro(e.getMessage(),sender);
			}
			return true;
		}
		return false;
	}

}
