package br.com.redewalker.engine.sistema.comando.inventario;

import br.com.redewalker.engine.WEngine;
import br.com.redewalker.engine.sistema.usuario.Usuario;
import br.com.redewalker.engine.utils.WalkersUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InventarioMenu {
	
	public InventarioMenu(Player p) {
		Usuario j = WEngine.get().getUsuarioManager().getUsuario(p.getName());
		Inventory inv  = Bukkit.createInventory(p, 9*6, "§8Comandos - Menu§7");
		inv.setItem(11, WalkersUtils.WalkersItens.getItem("cabecamenu", j.getNickOriginal()));
		inv.setItem(15, WalkersUtils.WalkersItens.getItemComandos("info", null));
		inv.setItem(38, WalkersUtils.WalkersItens.getItemComandos("configs", null));
		inv.setItem(39, WalkersUtils.WalkersItens.getItemComandos("listacomandos", null));
		inv.setItem(41, WalkersUtils.WalkersItens.getItemComandos("listapermitidos", null));
		inv.setItem(42, WalkersUtils.WalkersItens.getItemComandos("listabloqueados", null));
		inv.setItem(53, WalkersUtils.WalkersItens.getItem("sair",null));
		p.openInventory(inv);
	}

}
