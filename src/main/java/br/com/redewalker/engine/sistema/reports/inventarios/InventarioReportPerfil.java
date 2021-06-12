package br.com.redewalker.engine.sistema.reports.inventarios;

import br.com.redewalker.engine.WEngine;
import br.com.redewalker.engine.sistema.reports.Report;
import br.com.redewalker.engine.sistema.reports.ReportPerfil;
import br.com.redewalker.engine.sistema.usuario.Usuario;
import br.com.redewalker.engine.utils.WalkersUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InventarioReportPerfil {
	
	public InventarioReportPerfil(Player p, int pagina, ReportPerfil perfil) {
		Usuario j = WEngine.get().getUsuarioManager().getUsuario(p.getName());
		if (!(j instanceof Usuario) || !j.hasPermission("walker.reports")) return;
		List<Report> perfis = perfil.getReports();
		Collections.sort(perfis, Collections.reverseOrder());
		int paginas = 0;
		if (perfis.size()%45 == 0) paginas = perfis.size()/45;
		else paginas = perfis.size()/45+1;
		Inventory inv = Bukkit.createInventory(p, 9*6, "§c§8Reports - "+perfil.getJogador());
		for (int i = 0; i < paginas; i++) {
			if (pagina-1 != i) continue;
			int quantItens = perfis.size()-i*45;
			for (int f = 0; f < 45; f++) {
				if (quantItens == f) break;
				inv.setItem(f, getItem(perfis.get(i*45+f), j));
			}
		}
		inv.setItem(53, WalkersUtils.WalkersItens.getItem("voltar",null));
		inv.setItem(49, WalkersUtils.WalkersItens.getPaginaAtual(pagina));
		inv.setItem(46, WalkersUtils.WalkersItens.getItemReports("analisar", perfil, perfil.getJogador()));
		if (pagina < paginas) inv.setItem(50, WalkersUtils.WalkersItens.getItem("proximapagina",null));
		if (pagina > 1) inv.setItem(48, WalkersUtils.WalkersItens.getItem("paginaanterior",null));
		p.openInventory(inv);
	}
	
	private ItemStack getItem(Report punicao, Usuario j) {
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("");
		lore.add(" §fReportado§7: "+punicao.getReportado());
		lore.add(" §fMotivo§7: "+ WEngine.get().getReportManager().getReportMotivo(punicao.getMotivo()).getNome());
		lore.add(" §fComentario:§7: "+punicao.getComentario());
		lore.add(" §fData§7: "+WalkersUtils.formatedData(punicao.getData()).replace(" ", " ás "));
		lore.add("");
		Usuario autor = WEngine.get().getUsuarioManager().getUsuario(punicao.getAutor());
		if (autor instanceof Usuario) return WalkersUtils.WalkersItens.getCabeca(WEngine.get().getGruposManager().getGrupo(autor.getGrupoIn()).getTag().toString()+autor.getNickOriginal(), lore, punicao.getAutor());
		else return WalkersUtils.WalkersItens.getCabeca(punicao.getAutor(), lore, punicao.getAutor());
	}

}
