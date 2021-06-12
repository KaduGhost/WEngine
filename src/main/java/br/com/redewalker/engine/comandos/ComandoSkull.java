package br.com.redewalker.engine.comandos;

import br.com.redewalker.engine.WEngine;
import br.com.redewalker.engine.apis.MensagensAPI;
import br.com.redewalker.engine.sistema.usuario.Usuario;
import br.com.redewalker.engine.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class ComandoSkull extends BukkitCommand {

	public ComandoSkull(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		Usuario j = WEngine.get().getUsuarioManager().getUsuario(sender.getName());
		if (!(sender instanceof Player)) {
			MensagensAPI.apenasJogadores(sender);
			return false;
		}
		if (args.length != 1) {
			MensagensAPI.formatoIncorreto("skull <jogador>", sender);
			return false;
		}
		if (!j.hasPermission("walker.skull")) {
			MensagensAPI.semPermissaoComando(sender);
			return false;
		}
		String nick = args[0];
		Player p = j.getPlayer();
		p.getInventory().addItem(new ItemBuilder(Material.SKULL_ITEM, 3).setOwner(nick).fazer());
		MensagensAPI.mensagemSucesso("Você pegou a cabeça do "+ args[0], sender);
 		return false;
	}

}
