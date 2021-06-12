package br.com.redewalker.engine.sistema.punicoes.inventarios;

import br.com.redewalker.engine.WEngine;
import br.com.redewalker.engine.sistema.punicoes.Punicao;
import br.com.redewalker.engine.sistema.punicoes.PunicoesType;
import br.com.redewalker.engine.sistema.usuario.Usuario;
import br.com.redewalker.engine.utils.WalkersUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Collections;
import java.util.List;

public class InventarioPunirHBanidos {
	
	public InventarioPunirHBanidos(int pagina, Player p) {
		Usuario j = WEngine.get().getUsuarioManager().getUsuario(p.getName());
		if (!(j instanceof Usuario) || !j.hasPermission("walker.punir")) return;
		List<Punicao> punicoes = WEngine.get().getPunicoesManager().getHistoricoPunicoes(PunicoesType.BAN);
		Collections.sort(punicoes, Collections.reverseOrder());
		int paginas = 0;
		if (punicoes.size()%45 == 0) paginas = punicoes.size()/45;
		else paginas = punicoes.size()/45+1;
		Inventory inv = Bukkit.createInventory(p, 9*6, "§c§8Historico - Banidos§e");
		for (int i = 0; i < paginas; i++) {
			if (pagina-1 != i) continue;
			int quantItens = punicoes.size()-i*45;
			for (int f = 0; f < 45; f++) {
				if (quantItens == f) break;
				inv.setItem(f, WalkersUtils.WalkersItens.getItemPunicao("punicaohistorico", p.getName(), punicoes.get(i*45+f)));
			}
		}
		inv.setItem(53, WalkersUtils.WalkersItens.getItem("voltar",null));
		inv.setItem(49, WalkersUtils.WalkersItens.getPaginaAtual(pagina));
		if (pagina < paginas) inv.setItem(50, WalkersUtils.WalkersItens.getItem("proximapagina",null));
		if (pagina > 1) inv.setItem(48, WalkersUtils.WalkersItens.getItem("paginaanterior",null));
		p.openInventory(inv);
	}
	
}