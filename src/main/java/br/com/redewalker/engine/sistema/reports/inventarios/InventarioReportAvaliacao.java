package br.com.redewalker.engine.sistema.reports.inventarios;

import br.com.redewalker.engine.sistema.usuario.Usuario;
import br.com.redewalker.engine.WEngine;
import br.com.redewalker.engine.sistema.reports.ReportPerfil;
import br.com.redewalker.engine.utils.WalkersUtils.WalkersItens;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InventarioReportAvaliacao {
	
	public InventarioReportAvaliacao(Player p, ReportPerfil report) {
		Usuario j = WEngine.get().getUsuarioManager().getUsuario(p.getName());
		if (!(j instanceof Usuario) || !j.hasPermission("walker.reports")) return;
		Inventory inv = Bukkit.createInventory(p, 9*5, "§c§8Avaliação - Report§e");
		inv.setItem(13, WalkersItens.getItemReports("cabecaavaliar", report,null));
		inv.setItem(31, WalkersItens.getItemReports("teleportaraojogador",null,null));
		inv.setItem(29, WalkersItens.getItemReports("finalizaravaliacao",null,null));
		inv.setItem(33, WalkersItens.getItemReports("cancelaravaliacao",null,null));
		p.openInventory(inv);
	}

}
