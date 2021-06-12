package br.com.redewalker.engine.sistema.punicoes.inventarios;

import br.com.redewalker.engine.WEngine;
import br.com.redewalker.engine.sistema.punicoes.Punicao;
import br.com.redewalker.engine.sistema.punicoes.PunicoesType;
import br.com.redewalker.engine.sistema.usuario.Usuario;
import br.com.redewalker.engine.utils.WalkersUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InventarioPunirAvaliados {
	
	public InventarioPunirAvaliados(int pagina, Player p) {
		Usuario j = WEngine.get().getUsuarioManager().getUsuario(p.getName());
		if (!(j instanceof Usuario) || !j.hasPermission("walker.punir")) return;
		List<Punicao> punicoes = null;
		if (j.hasPermission("walker.punir.admin")) punicoes = WEngine.get().getPunicoesManager().getPunicoesParaAvaliar();
		else punicoes = WEngine.get().getPunicoesManager().getPunicoesEmAvaliar(j);
		Collections.sort(punicoes);
		int paginas = 0;
		if (punicoes.size()%45 == 0) paginas = punicoes.size()/45;
		else paginas = punicoes.size()/45+1;
		Inventory inv = Bukkit.createInventory(p, 9*6, "§c§8Avaliação - Walkers§e");
		for (int i = 0; i < paginas; i++) {
			if (pagina-1 != i) continue;
			int quantItens = punicoes.size()-i*45;
			for (int f = 0; f < 45; f++) {
				if (quantItens == f) break;
				inv.setItem(f, getItem(punicoes.get(i*45+f), j));
			}
		}
		inv.setItem(53, WalkersUtils.WalkersItens.getItem("voltar",null));
		inv.setItem(49, WalkersUtils.WalkersItens.getPaginaAtual(pagina));
		if (pagina < paginas) inv.setItem(50, WalkersUtils.WalkersItens.getItem("proximapagina",null));
		if (pagina > 1) inv.setItem(48, WalkersUtils.WalkersItens.getItem("paginaanterior",null));
		p.openInventory(inv);
	}
	
	private ItemStack getItem(Punicao punicao, Usuario j) {
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(" §fID§7: " + punicao.getID());
		lore.add(" §fPunido§7: "+punicao.getJogador());
		lore.add(" §fData§7: "+WalkersUtils.formatedData(punicao.getData()).replace(" ", " ás "));
		if (!punicao.getTipo().equals(PunicoesType.KICK)) {
			if (punicao.getAte() == 0) lore.add(" §fDuração§7: Eterno");
			else lore.add(" §fDuração§7: "+WalkersUtils.formatedData(punicao.getAte()).replace(" ", " ás "));
		}
		if (punicao.getProva().equalsIgnoreCase("sem")) lore.add(" §fProva§7: Sem provas");
		else lore.add(" §fProva§7: "+punicao.getProva());
		lore.add(" §fMotivo§7: "+punicao.getComentario());
		Usuario punidor = WEngine.get().getUsuarioManager().getUsuario(punicao.getAutor());
		if (punidor instanceof Usuario) lore.add(" §fAutor§7: "+ WEngine.get().getGruposManager().getGrupo(punidor.getGrupoIn()).getTag().toString()+punidor.getNickOriginal());
		else lore.add(" §fAutor§7: "+punidor.getNickOriginal());
		lore.add("");
		if (j.hasPermission("walker.punir.admin")) {
			lore.add(" §aClique para aceitar a punição.");
			lore.add(" §aClique direito para recusar a punição.");
		}else lore.add(" §aClique direito para revogar a punição.");
		return WalkersUtils.WalkersItens.getCabeca("§ePunição: §l"+punicao.getTipo().getDescricao(), lore, punicao.getJogador());
	}
	
}
