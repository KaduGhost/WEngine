package br.com.redewalker.engine.comandos;

import br.com.redewalker.engine.apis.MensagensAPI;
import br.com.redewalker.engine.sistema.usuario.Usuario;
import br.com.redewalker.engine.WEngine;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.IntStream;

public class ComandoLimparChat extends BukkitCommand {

	public ComandoLimparChat(String name) {
		super(name);
		setAliases(Arrays.asList("lchat", "limparc", "lc"));
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		Usuario j = WEngine.get().getUsuarioManager().getUsuario(sender.getName());
		if (sender instanceof Player && !j.hasPermission("walker.limparchat")) {
			MensagensAPI.semPermissaoComando(sender);
			return false;
		}
		if (args.length == 0) {
			if (j.hasPermission("walker.limparchat")) {
				MensagensAPI.formatoIncorreto("limparchat <all/jogador>", sender);
				return false;
			}else {
				IntStream.range(0, 120).forEach(r-> sender.sendMessage(" "));
				return false;
			}
		}else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("all") && j.hasPermission("walker.limparchat")) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					Usuario u = WEngine.get().getUsuarioManager().getUsuario(p.getName());
					if (!u.hasPermission("walker.limparchat.bypass")) IntStream.range(0, 120).forEach(r-> p.sendMessage(" "));
				}
				for (Player p : Bukkit.getServer().getOnlinePlayers()) {
					Usuario j2 = WEngine.get().getUsuarioManager().getUsuario(p.getName());
					if (j2.hasPermission("walker.limparchat")) MensagensAPI.mensagemSucesso("Chat limpo pelo: " + sender.getName(), sender);
				}
				Bukkit.broadcastMessage("§aCHAT LIMPO!");
			}else {
				Player p2 = Bukkit.getPlayer(args[0]);
				if (!(p2 instanceof Player)) {
					MensagensAPI.jogadorNaoEncontrado(sender);
					return false;
				}
				IntStream.range(0, 120).forEach(r-> p2.sendMessage(" "));
				sender.sendMessage("§aCHAT DO JOGADOR "+p2.getName()+" LIMPO!");
				return false;
			}
			
		}
		MensagensAPI.formatoIncorreto("limparchat <all/jogador>", sender);
		return false;
	}

}
