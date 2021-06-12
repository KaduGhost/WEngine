package br.com.redewalker.engine.sistema.comando.inventario;

import br.com.redewalker.engine.utils.WalkersUtils.WalkersItens;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InventarioConfiguracao {
	
	public InventarioConfiguracao(Player p) {
		Inventory inv  = Bukkit.createInventory(p, 9*5, "§8Comandos - Config§7");
		inv.setItem(13, WalkersItens.getItemComandos("configsicon", null));
		inv.setItem(29, WalkersItens.getItemComandos("registrados", null));
		inv.setItem(33, WalkersItens.getItemComandos("lista", null));
		inv.setItem(44, WalkersItens.getItem("voltar",null));
		p.openInventory(inv);
	}

}
