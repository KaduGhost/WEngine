package br.com.redewalker.engine.sistema.vip.inventario;

import br.com.redewalker.engine.sistema.usuario.Usuario;
import br.com.redewalker.engine.WEngine;
import br.com.redewalker.engine.utils.WalkersUtils.WalkersItens;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class InventarioVIPMenu {
	
	public InventarioVIPMenu(Player p, InventarioVipsType tipo) {
		Usuario j = WEngine.get().getUsuarioManager().getUsuario(p.getName());
		if (!(j instanceof Usuario) || !j.hasPermission("walker.vips.admin")) return;
		Inventory inv = Bukkit.createInventory(p, 9*5, "§c§8VIPS - Walker§e");
		inv.setItem(4, WalkersItens.getCabeca(j.getNickOriginal(), Arrays.asList(), p.getName()));
		inv.setItem(19, WalkersItens.getItemVIP("listavips", null, null, tipo, null));
		inv.setItem(20, WalkersItens.getItemVIP("listavipsall", null, null, tipo, null));
		inv.setItem(24, WalkersItens.getItemVIP("historicovips", null, null, tipo, null));
		inv.setItem(25, WalkersItens.getItemVIP("historicovipsall", null, null, tipo, null));
		p.openInventory(inv);
	}

}
