package br.com.redewalker.engine.sistema.punicoes.inventarios;

import br.com.redewalker.engine.sistema.usuario.Usuario;
import br.com.redewalker.engine.WEngine;
import br.com.redewalker.engine.apis.MensagensAPI;
import br.com.redewalker.engine.sistema.punicoes.PunicaoMotivoType;
import br.com.redewalker.engine.sistema.punicoes.PunicoesType;
import br.com.redewalker.engine.utils.ItemBuilder;
import br.com.redewalker.engine.utils.WalkersUtils.WalkersItens;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class InventarioPunir {
	
	public InventarioPunir(Player p, String j2) {
		Usuario j = WEngine.get().getUsuarioManager().getUsuario(p.getName());
		Usuario u2 = WEngine.get().getUsuarioManager().getUsuario(j2);
		boolean registrado = u2 instanceof Usuario;
		if (!(j instanceof Usuario) || !j.hasPermission("walker.punir")) return;
		if (registrado && j.getGrupoIn().getPrioridade() <= u2.getGrupoIn().getPrioridade()) {
			MensagensAPI.mensagemErro("Você não pode punir alguém com cargo maior ou igual ao seu", p);
			return;
		}
		Inventory inv = Bukkit.createInventory(p, 9*4, "§c§8Punir - Motivos§e");
		List<Integer> slots = getSlotsMenu();
		int cont = 0;
		for (PunicaoMotivoType tipo : EnumSet.allOf(PunicaoMotivoType.class)) {
			if (tipo.equals(PunicaoMotivoType.OUTRO) || WEngine.get().getPunicoesManager().jogadorPodePunir(j, tipo.getPunicao(), tipo.getTempo())) inv.setItem(slots.get(cont), getItem(tipo));
			cont++;
		}
		inv.setItem(4, WalkersItens.getItemPunicao("cabecamotivo", j2, null));
		if (WEngine.get().getPunicoesManager().jogadorPodePunir(j, PunicoesType.KICK, 0)) inv.setItem(35, WalkersItens.getItemPunicao("kickarjogador", j2, null));
		p.openInventory(inv);
	}
	
	private ItemStack getItem(PunicaoMotivoType tipo) {
		return new ItemBuilder(Material.EMPTY_MAP).setName("§e"+tipo.getDescricao()).fazer();
	}
	
	private List<Integer> getSlotsMenu() {
		return Arrays.asList(9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26);
	}

}
