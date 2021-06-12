package br.com.redewalker.engine.comandos;

import br.com.redewalker.engine.WEngine;
import br.com.redewalker.engine.apis.MensagensAPI;
import br.com.redewalker.engine.sistema.usuario.Usuario;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class ComandoR extends BukkitCommand {

	public ComandoR(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		Usuario j = WEngine.get().getUsuarioManager().getUsuario(sender.getName());
		if (!(sender instanceof Player)) {
			MensagensAPI.apenasJogadores(sender);
			return false;
		}
		if (args.length == 0) {
			MensagensAPI.formatoIncorreto("r <msg>", sender);
			return false;
		}
		String msg = "";
		for (int i = 0; i < args.length; i++) {
			msg+=args[i]+" ";
		}
		ComandoTell.enviarMsg(j, msg);
		return false;
	}

}
