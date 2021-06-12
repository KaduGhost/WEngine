package br.com.redewalker.engine.comandos;

import br.com.redewalker.engine.sistema.usuario.Usuario;
import br.com.redewalker.engine.WEngine;
import br.com.redewalker.engine.apis.MensagensAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.util.Arrays;

public class ComandoBroadcast extends BukkitCommand {

	public ComandoBroadcast(String name) {
		super(name);
		setAliases(Arrays.asList("bc"));
		setDescription("para falar em forma de anuncio para o server");
	}

	public boolean execute(CommandSender sender, String arg1, String[] args) {
		if (args.length == 0) {
			MensagensAPI.formatoIncorreto("bc <msg>", sender);
			return false;
		}
		Usuario j = WEngine.get().getUsuarioManager().getUsuario(sender.getName());
		if (j instanceof Usuario && !j.hasPermission("walker.broadcast.admin")) {
			MensagensAPI.semPermissaoComando(sender);
			return false;
		}
		String msg = "";
		for (int i = 0; i < args.length; i++) {
			msg = msg + " " + args[i].replace("&", "§");
		}
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage(" §6§lServidor§f: " + msg);
		Bukkit.broadcastMessage("");
		return false;
	}
}
