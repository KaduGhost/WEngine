package br.com.redewalker.engine.comandos;

import br.com.redewalker.engine.WEngine;
import br.com.redewalker.engine.apis.MensagensAPI;
import br.com.redewalker.engine.sistema.grupo.Grupo;
import br.com.redewalker.engine.sistema.grupo.GrupoType;
import br.com.redewalker.engine.sistema.usuario.Usuario;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class ComandoGrupo extends BukkitCommand {

	public ComandoGrupo(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		Usuario j = WEngine.get().getUsuarioManager().getUsuario(sender.getName());
		if (j instanceof Usuario && !j.hasPermission("walker.grupos.admin")) {
			MensagensAPI.semPermissaoComando(sender);
			return true;
		}
		if (args.length == 0) {
			sender.sendMessage("");
			sender.sendMessage("  §6Comandos de grupo");
			sender.sendMessage(" §e/grupo §7 - para ver os comandos de ajuda.");
			sender.sendMessage(" §e/grupo <jogador>§7 - para ver os grupos do jogador.");
			sender.sendMessage(" §e/grupo <jogador> <grupo>§7 - para definir o grupo do jogador.");
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
			sender.sendMessage(" §eO grupo out é: "+gp2.getTag().getCor()+gp2.getNome()+".");
			sender.sendMessage("");
			return true;
		}else if (args.length == 2) {
			if (!GrupoType.isExists(args[1])) {
				MensagensAPI.grupoNaoEncontrado(sender);
				return true;
			}
			GrupoType gp = GrupoType.getTipo(args[1]);
			if (sender instanceof Player && !j.getGrupoIn().equals(GrupoType.Master) && (gp.equals(GrupoType.Master))) {
				MensagensAPI.mensagemErro("Apenas Masters podem definir este grupo",sender);
				return true;
			}
			if (sender instanceof Player && j.getGrupoIn().getPrioridade() <= gp.getPrioridade() && (!j.getGrupoIn().equals(GrupoType.Master))) {
				MensagensAPI.mensagemErro("Você só pode definir grupos inferiores ao seu",sender);
				return true;
			}
			//WalkerEngine.get().getGruposManager().setGrupoIn(j2, gp, sender);
			return true;
		}
		return false;
	}

}
