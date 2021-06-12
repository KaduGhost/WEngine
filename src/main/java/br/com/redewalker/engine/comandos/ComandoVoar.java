package br.com.redewalker.engine.comandos;

import br.com.redewalker.engine.WEngine;
import br.com.redewalker.engine.apis.MensagensAPI;
import br.com.redewalker.engine.sistema.usuario.Usuario;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class ComandoVoar extends BukkitCommand {

	public ComandoVoar(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		if (!(sender instanceof Player)) {
			MensagensAPI.apenasJogadores(sender);
			return false;
		}
		Usuario j = WEngine.get().getUsuarioManager().getUsuario(sender.getName());
		if (!j.hasPermission("walker.voar")) {
			MensagensAPI.semPermissaoComando(sender);
			return false;
		}
		if (args.length == 0) {
			if (!WEngine.get().getVoarManager().isPodeVoar(j)) {
				MensagensAPI.mensagemErro("Você não pode voar nesta localização", sender);
				return false;
			}
			WEngine.get().getVoarManager().mudarVoar(j,j);
			return true;
		}else if (args.length == 1) {
			if (!j.hasPermission("walker.voar.admin")) {
				MensagensAPI.semPermissaoComando(sender);
				return false;
			}
			Usuario j2 = WEngine.get().getUsuarioManager().getUsuario(sender.getName());
			if (!(j2 instanceof Usuario) || !(j2.getPlayer() instanceof Player)) {
				MensagensAPI.jogadorOffline(sender);
				return false;
			}
			WEngine.get().getVoarManager().mudarVoar(j2,j);
			return true;
		}
		MensagensAPI.formatoIncorreto("voar (jogador)", sender);
		return false;
	}

}
