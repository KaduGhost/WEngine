package br.com.redewalker.engine.sistema.reports.inventarios;

import br.com.redewalker.engine.WEngine;
import br.com.redewalker.engine.sistema.usuario.Usuario;
import br.com.redewalker.engine.utils.WalkersUtils.WalkersItens;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class InventarioReportMenu {
	
	public InventarioReportMenu(Player p) {
		Usuario j = WEngine.get().getUsuarioManager().getUsuario(p.getName());
		if (!(j instanceof Usuario) || !j.hasPermission("walker.reports")) return;
		Inventory inv = Bukkit.createInventory(p, 9*3, "§c§8Reports - Menu§e");
		inv.setItem(10, WalkersItens.getCabeca(j.getNickOriginal(), Arrays.asList(), p.getName()));
		inv.setItem(13, WalkersItens.getItemReports("cabecareports",null,null));
		inv.setItem(15, WalkersItens.getItemReports("emanalise",null,null));
		p.openInventory(inv);
	}

}
