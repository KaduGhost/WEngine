package br.com.redewalker.engine.comandos;

import br.com.redewalker.engine.WEngine;
import br.com.redewalker.engine.apis.MensagensAPI;
import br.com.redewalker.engine.sistema.comando.inventario.InventarioComandosType;
import br.com.redewalker.engine.sistema.usuario.Usuario;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class ComandoCmdBlock extends BukkitCommand {

	public ComandoCmdBlock(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		if(!(sender instanceof Player)) {
			MensagensAPI.apenasJogadores(sender);
			return false;
		}
		Usuario j = WEngine.get().getUsuarioManager().getUsuario(sender.getName());
		if (!j.hasPermission("walker.cmdblock")) {
			MensagensAPI.semPermissaoComando(sender);
			return false;
		}
		if (args.length != 0) {
			MensagensAPI.formatoIncorreto("cmdblock", sender);
			return false;
		}
		WEngine.get().getComandosManager().abrirInventario(j.getPlayer(), InventarioComandosType.MENU, 1);
		return false;
	}

}
