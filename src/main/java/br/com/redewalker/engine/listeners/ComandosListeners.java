package br.com.redewalker.engine.listeners;

import br.com.redewalker.engine.WEngine;
import br.com.redewalker.engine.sistema.comando.inventario.InventarioComandosType;
import br.com.redewalker.engine.sistema.usuario.Usuario;
import br.com.redewalker.engine.utils.WalkersUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ComandosListeners implements Listener {
	
	@EventHandler
	void aoClicar(InventoryClickEvent e) {
		if (e.getCurrentItem() == null) return;
		Inventory inv = e.getInventory();
		InventarioComandosType tipo = getTipoInventario(inv.getName());
		if (tipo == null) return;
		e.setCancelled(true);
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || !e.getClickedInventory().equals(e.getInventory())) return;
		Player p = (Player) e.getWhoClicked();
		Usuario j = WEngine.get().getUsuarioManager().getUsuario(p.getName());
		ItemStack item = e.getCurrentItem();
		ItemMeta meta = item.getItemMeta();
		int pagina = 1;
		if (meta.hasDisplayName()) {
			if (item.equals(WalkersUtils.WalkersItens.getItem("sair",null))) {
				p.closeInventory();
				return;
			}else if (item.equals(WalkersUtils.WalkersItens.getItem("voltar",null))) {
				WEngine.get().getComandosManager().abrirInventario(p, InventarioComandosType.MENU, pagina);
				return;
			}
		}
		ClickType click = e.getClick();
		switch (tipo) {
		case MENU:
			if (item.equals(WalkersUtils.WalkersItens.getItemComandos("configs", j))) WEngine.get().getComandosManager().abrirInventario(p, InventarioComandosType.CONFIG, pagina);
			else if (item.equals(WalkersUtils.WalkersItens.getItemComandos("listacomandos", j))) WEngine.get().getComandosManager().abrirInventario(p, InventarioComandosType.LISTACOMANDOS, 1);
			else if (item.equals(WalkersUtils.WalkersItens.getItemComandos("listapermitidos", j))) WEngine.get().getComandosManager().abrirInventario(p, InventarioComandosType.LISTAPERMITIDOS, 1);
			else if (item.equals(WalkersUtils.WalkersItens.getItemComandos("listabloqueados", j))) WEngine.get().getComandosManager().abrirInventario(p, InventarioComandosType.LISTABLOQUEADOS, 1);
			return;
		case CONFIG:
			if (item.equals(WalkersUtils.WalkersItens.getItemComandos("registrados", j))) WEngine.get().getComandosManager().setSomenteRegistrados(!WEngine.get().getComandosManager().isSomenteRegistrados());
			else if (item.equals(WalkersUtils.WalkersItens.getItemComandos("lista", j))) {
				if (click.equals(ClickType.RIGHT)) WEngine.get().getComandosManager().setBloquear(!WEngine.get().getComandosManager().isBloquear());
				else WEngine.get().getComandosManager().setLista(!WEngine.get().getComandosManager().isLista());
			}
			WEngine.get().getComandosManager().abrirInventario(p, InventarioComandosType.CONFIG, pagina);
			return;
		case LISTACOMANDOS:
			pagina = getPagina(inv.getItem(49));
			String comando = getComandoNoNome(item.getItemMeta().getDisplayName());
			if (checkTrocarPagina(p, item, pagina, tipo) || !isComando(item.getItemMeta().getDisplayName())) return;
			if (click.equals(ClickType.RIGHT)) {
				if (WEngine.get().getComandosManager().containsListaPermitir(comando)) WEngine.get().getComandosManager().removeListaPermitir(comando);
				else WEngine.get().getComandosManager().addListaPermitir(comando);
			}else {
				if (WEngine.get().getComandosManager().containsListaBloqueio(comando)) WEngine.get().getComandosManager().removeListaBloqueio(comando);
				else WEngine.get().getComandosManager().addListaBloqueio(comando);
			}
			WEngine.get().getComandosManager().abrirInventario(p, InventarioComandosType.LISTACOMANDOS, pagina);
			return;
		case LISTABLOQUEADOS:
			pagina = getPagina(inv.getItem(49));
			comando = getComandoNoNome(item.getItemMeta().getDisplayName());
			if (checkTrocarPagina(p, item, pagina, tipo) || !isComando(item.getItemMeta().getDisplayName())) return;
			WEngine.get().getComandosManager().removeListaBloqueio(comando);
			WEngine.get().getComandosManager().abrirInventario(p, InventarioComandosType.LISTABLOQUEADOS, pagina);
			return;
		case LISTAPERMITIDOS:
			pagina = getPagina(inv.getItem(49));
			comando = getComandoNoNome(item.getItemMeta().getDisplayName());
			if (checkTrocarPagina(p, item, pagina, tipo) || !isComando(item.getItemMeta().getDisplayName())) return;
			WEngine.get().getComandosManager().removeListaPermitir(comando);
			WEngine.get().getComandosManager().abrirInventario(p, InventarioComandosType.LISTAPERMITIDOS, pagina);
			return;
		default:
			return;
		}
	}
	
	private boolean isComando(String nome) {
		return nome.startsWith("§c§e");
	}
	
	private String getComandoNoNome(String nome) {
		return nome.replace("§c§e", "").replaceAll(" ", "");
	}
	
	
	private boolean checkTrocarPagina(Player p, ItemStack item, int paginaatual, InventarioComandosType tipo) {
		if (item.equals(WalkersUtils.WalkersItens.getItem("proximapagina",null))) {
			WEngine.get().getComandosManager().abrirInventario(p, tipo, paginaatual+1);
			return true;
		}
		else if (item.equals(WalkersUtils.WalkersItens.getItem("paginaanterior",null))) {
			WEngine.get().getComandosManager().abrirInventario(p, tipo, paginaatual-1);
			return true;
		}
		return false;
	}
	
	private int getPagina(ItemStack item) {
		return Integer.parseInt(WalkersUtils.removeColorsOfString(item.getItemMeta().getDisplayName().split(":")[1].replace(" ", "")));
	}
	
	private InventarioComandosType getTipoInventario(String nome) {
		if (nome.equals("§8Comandos - Config§7")) return InventarioComandosType.CONFIG;
		else if (nome.equals("§8Comandos - Menu§7")) return InventarioComandosType.MENU;
		else if (nome.equals("§8Comandos - Lista§7")) return InventarioComandosType.LISTACOMANDOS;
		else if (nome.equals("§8Bloqueados - Lista§7")) return InventarioComandosType.LISTABLOQUEADOS;
		else if (nome.equals("§8Permitidos - Lista§7")) return InventarioComandosType.LISTAPERMITIDOS;
		return null;
	}

}
