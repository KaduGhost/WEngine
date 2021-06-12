package br.com.redewalker.engine.comandos;

import br.com.redewalker.engine.sistema.usuario.Usuario;
import br.com.redewalker.engine.WEngine;
import br.com.redewalker.engine.apis.MensagensAPI;
import br.com.redewalker.engine.utils.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class ComandoDivulgar extends BukkitCommand {

	public ComandoDivulgar(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		if (!(sender instanceof Player)) {
			MensagensAPI.apenasJogadores(sender);
			return false;
		}
		Usuario j = WEngine.get().getUsuarioManager().getUsuario(sender.getName());
		if (!j.hasPermission("walker.divulgar")) {
			MensagensAPI.semPermissaoComando(sender);
			return false;
		}
		if (args.length == 2) {
			String regexUrl = WEngine.get().getConfigManager().getConfigChat().getAntiIPRegexURL();
			if (!Pattern.compile(regexUrl).matcher(args[1]).find()) {
				MensagensAPI.mensagemErro("Link inválido", sender);
				return false;
			}
			if (args[0].equalsIgnoreCase("live") || args[0].equalsIgnoreCase("video") || args[0].equalsIgnoreCase("outro")) {
				enviarMensagem(args[0].toLowerCase(), args[1], j);
				return true;
			}
		}
		MensagensAPI.formatoIncorreto("divulgar <live/video/outro> <link>", sender);
		return false;
	}
	
	private void enviarMensagem(String tipo, String link, Usuario j) {
		new Title("§r"+link, "§b§l"+j.getNickOriginal()+" ESTA DIVULGANDO!").broadcast();;
		Bukkit.broadcastMessage(" ");
		String divulg = "uma live";
		if (tipo.equalsIgnoreCase("outro")) divulg = "um link";
		else if (tipo.equalsIgnoreCase("video")) divulg = "um video";
		Bukkit.broadcastMessage(" §e"+j.getNickOriginal()+" esta divulgando "+divulg+"!");
		Bukkit.broadcastMessage(String.format(" §eLink: §b§n%s", link));
		Bukkit.broadcastMessage(" ");
	}

}
