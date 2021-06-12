package br.com.redewalker.engine.sistema.punicoes.inventarios;

import br.com.redewalker.engine.WEngine;
import br.com.redewalker.engine.sistema.usuario.Usuario;
import br.com.redewalker.engine.utils.WalkersUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class InventarioPunirMenu {
	
	public InventarioPunirMenu(Player p) {
		Usuario j = WEngine.get().getUsuarioManager().getUsuario(p.getName());
		if (!(j instanceof Usuario) || !j.hasPermission("walker.punir")) return;
		Inventory inv = Bukkit.createInventory(p, 9*5, "§c§8Punições - Walkers§e");
		inv.setItem(3, WalkersUtils.WalkersItens.getItemPunicao("punirjogador", null, null));
		inv.setItem(4, WalkersUtils.WalkersItens.getCabeca(j.getNickOriginal(), Arrays.asList(), p.getName()));
		inv.setItem(5, WalkersUtils.WalkersItens.getItemPunicao("despunirjogador", null, null));
		inv.setItem(22, WalkersUtils.WalkersItens.getItemPunicao("listaavaliados", j.getNickOriginal(), null));
		inv.setItem(19, WalkersUtils.WalkersItens.getItemPunicao("listabanidos", null, null));
		inv.setItem(20, WalkersUtils.WalkersItens.getItemPunicao("listamutados", null, null));
		inv.setItem(24, WalkersUtils.WalkersItens.getItemPunicao("historicobanidos", null, null));
		inv.setItem(25, WalkersUtils.WalkersItens.getItemPunicao("historicomutados", null, null));
		inv.setItem(34, WalkersUtils.WalkersItens.getItemPunicao("historicokicks", null, null));
		inv.setItem(39, WalkersUtils.WalkersItens.getItemPunicao("listameusbanidos", null, null));
		inv.setItem(41, WalkersUtils.WalkersItens.getItemPunicao("listameusmutados", null, null));
		p.openInventory(inv);
	}

}
