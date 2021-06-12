package br.com.redewalker.engine.sistema.vip.inventario;

import br.com.redewalker.engine.WEngine;
import br.com.redewalker.engine.sistema.server.Servers;
import br.com.redewalker.engine.sistema.usuario.Usuario;
import br.com.redewalker.engine.sistema.vip.VIP;
import br.com.redewalker.engine.utils.WalkersUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Collections;

public class InventarioVIPsByUsuarioLista {
	
	public InventarioVIPsByUsuarioLista(int pagina, Player p, Servers server, Usuario j2, InventarioVipsType tipo) {
		Usuario j = WEngine.get().getUsuarioManager().getUsuario(p.getName());
		if (!(j instanceof Usuario) || !j.hasPermission("walker.vips.admin")) return;
		ArrayList<VIP> vips = WEngine.get().getVipManager().getVIPsValidosByJogador(j2, server);
		Collections.sort(vips, Collections.reverseOrder());
		int paginas = 0;
		if (vips.size()%45 == 0) paginas = vips.size()/45;
		else paginas = vips.size()/45+1;
		Inventory inv = Bukkit.createInventory(p, 9*6, "§b§8VIPs - "+j2.getNickOriginal());
		for (int i = 0; i < paginas; i++) {
			if (pagina-1 != i) continue;
			int quantItens = vips.size()-i*45;
			for (int f = 0; f < 45; f++) {
				if (quantItens == f) break;
				inv.setItem(f, WalkersUtils.WalkersItens.getItemVIP("vip", vips.get(i*45+f), null, tipo, j));
			}
		}
		inv.setItem(53, WalkersUtils.WalkersItens.getItem("voltar",null));
		inv.setItem(49, WalkersUtils.WalkersItens.getPaginaAtualEscondeID(pagina, server.toString()));
		if (pagina < paginas) inv.setItem(50, WalkersUtils.WalkersItens.getItem("proximapagina",null));
		if (pagina > 1) inv.setItem(48, WalkersUtils.WalkersItens.getItem("paginaanterior",null));
		p.openInventory(inv);
	}
	
}