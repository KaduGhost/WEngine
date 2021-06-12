package br.com.redewalker.engine.comandos;

import br.com.redewalker.engine.WEngine;
import br.com.redewalker.engine.apis.MensagensAPI;
import br.com.redewalker.engine.sistema.punicoes.inventarios.InventarioPunicoesType;
import br.com.redewalker.engine.sistema.usuario.Usuario;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class ComandoPunir extends BukkitCommand {

	public ComandoPunir(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		if (!(sender instanceof Player)) {
			MensagensAPI.apenasJogadores(sender);
			return false;
		}
		Usuario j = WEngine.get().getUsuarioManager().getUsuario(sender.getName());
		if (!j.hasPermission("walker.punir")) {
			MensagensAPI.semPermissaoComando(sender);
			return false;
		}
		if (args.length == 0) {
			WEngine.get().getPunicoesManager().abrirInventario(j.getPlayer(), InventarioPunicoesType.MENU, 1, null);
			return false;
		}else if (args.length == 1) {
			Usuario j2 = WEngine.get().getUsuarioManager().getUsuario(args[0]);
			if (!(j2 instanceof Usuario)) {
				MensagensAPI.jogadorNaoEncontrado(sender);
				return false;
			}
			WEngine.get().getPunicoesManager().abrirInventario(j.getPlayer(), InventarioPunicoesType.PUNIR, 1, j2.getNickOriginal());
			return false;
		}
		MensagensAPI.formatoIncorreto("punir (jogador)", sender);
		return false;
	}

}
