package br.com.redewalker.engine.sistema.chat;

import br.com.redewalker.engine.WEngine;
import br.com.redewalker.engine.sistema.usuario.Usuario;
import br.net.fabiozumbi12.MinEmojis.Fanciful.FancyMessage;
import org.bukkit.ChatColor;

import java.util.ArrayList;

public class ChatUtils {
	
	public static String colorirMsg(Usuario j, String msg, String corPrincipal) {
		if (j.hasPermission("walker.chat.cor")) {
			String colors = "0123456789abcdefABCDEF";
			for (char c : colors.toCharArray()) {
				String cor = String.valueOf(c).toLowerCase();
				if (msg.contains("&"+cor)) msg = msg.replace("&"+cor, "§"+cor);
			}
		}
		if (j.hasPermission("walker.chat.formato")) {
			String colors = "krlmno";
			for (char c : colors.toCharArray()) {
				String cor = String.valueOf(c).toLowerCase();
				if (msg.contains("&"+cor) && !"&k".equalsIgnoreCase("&"+cor)) msg = msg.replace("&"+cor, "§"+cor);
			}
		}
		return corPrincipal+msg;
	}
	
	public static FancyMessage formatarMensagem(Canal canal, Usuario j, String msg, Usuario recebedor) {
		FancyMessage fancy = new FancyMessage("");
		String formato = canal.getFormato();
		String latest = "";
		String latestTag = "";
		String ultimoadicionado = "";
		for (char c : formato.toCharArray()) {
			if (String.valueOf(c).equals("{")) {
				latestTag+=c;
				if (ultimoadicionado.endsWith(" ") && latest.startsWith(" ")) latest = latest.replaceFirst(" ", "");
				fancy.then(latest);
				ultimoadicionado=latest;
				latest = "";
			} else {
				if (String.valueOf(c).equals("}")) {
					latestTag+=c;
					CanalTag ct = WEngine.get().getChatManager().getChatTag(latestTag);
					if (ct != null) {
						if (ct.isPlayerCanUse(j.getNickOriginal())) {
							String add = WEngine.get().getPlaceholderAPI().checkPlaceholders(ct.getCor(), j, canal)+ WEngine.get().getPlaceholderAPI().checkPlaceholders(ct.getTag(), j, canal);
							if (!checkVazia(add)) {
								if (ultimoadicionado.endsWith(" ") && latest.startsWith(" ")) latest = latest.replaceFirst(" ", "");
								fancy.then(add);
								ultimoadicionado = "";
								if (ct.isExecuteComando()) fancy.command(WEngine.get().getPlaceholderAPI().checkPlaceholders(ct.getExecute(), j, canal));
								else if (ct.isSugestao()) fancy.command(WEngine.get().getPlaceholderAPI().checkPlaceholders(ct.getSuggest(), j, canal));
								else if (ct.isUrl()) fancy.command(WEngine.get().getPlaceholderAPI().checkPlaceholders(ct.getUrl(), j, canal));
								if (ct.isHover()) {
									ArrayList<String> arr = new ArrayList<String>();
									for (String s : ct.getOrdemHovers()) {
										if (ct.containsHover(s)) {
											if (j.hasPermission(s)) for (String s2 : ct.getHover(s)) arr.add(WEngine.get().getPlaceholderAPI().checkPlaceholders(s2, j, canal));
										}
										else {
											if (recebedor.hasPermission(s)) for (String s2 : ct.getHoverRecebedor(s)) arr.add(WEngine.get().getPlaceholderAPI().checkPlaceholders(s2, j, canal));
										}
									}
									fancy.tooltip(arr);
								}
							}
						}
					} else if (latestTag.equalsIgnoreCase("{msg}")) {
						msg = colorirMsg(j, msg, WEngine.get().getPlaceholderAPI().checkPlaceholders("%chat_tag_cor%", j, canal));
						fancy.then(msg);
						ultimoadicionado = "";
						String last = ChatColor.getLastColors(msg);
						String le = last.replace("§", "");
						if (le.equals("")) le = "f";
						fancy.color(ChatColor.getByChar(le));
					} else if (latestTag.equalsIgnoreCase("{chat_cor}")) latest+= WEngine.get().getPlaceholderAPI().checkPlaceholders("%chat_tag_cor%", j, canal);
					else latest+=latestTag;
					latestTag = "";
				} else {
					if (latestTag.length() != 0) latestTag+=c;
					else latest+=c;
				}
			}
		}
		fancy.then(latest);
		return fancy;
	}
	
	private static boolean checkVazia(String add) {
		String colors = "0123456789abcdefABCDEF";
		for (char c : colors.toCharArray()) {
			add=add.replaceAll("§"+c, "");
		}
		add=add.replaceAll(" ", "");
		return add.equals("");
	}
	
	public static String translateStringColor(String color) {
		switch(color.toLowerCase()) {
			case "black": {return ChatColor.BLACK.toString();}
			case "darkblue": {return ChatColor.DARK_BLUE.toString();}
			case "darkgreen": {return ChatColor.DARK_GREEN.toString();}
			case "darkaqua": {return ChatColor.DARK_AQUA.toString();}
			case "darkred": {return ChatColor.DARK_RED.toString();}
			case "darkpurple": {return ChatColor.DARK_PURPLE.toString();}
			case "gold": {return ChatColor.GOLD.toString();}
			case "gray": {return ChatColor.GRAY.toString();}
			case "darkgray": {return ChatColor.DARK_GRAY.toString();}
			case "blue": {return ChatColor.BLUE.toString();}
			case "green": {return ChatColor.GREEN.toString();}
			case "aqua": {return ChatColor.AQUA.toString();}
			case "red": {return ChatColor.RED.toString();}
			case "lightpurple": {return ChatColor.LIGHT_PURPLE.toString();}
			case "yellow": {return ChatColor.YELLOW.toString();}
			default: {return ChatColor.WHITE.toString();}
		}
	}
	
	public static String translateChatColorToStringColor(ChatColor color) {
		switch(color) {
			case BLACK: {return "black";}
			case DARK_BLUE: {return "darkblue";}
			case DARK_GREEN: {return "darkgreen";}
			case DARK_AQUA: {return "darkaqua";}
			case DARK_RED: {return "darkred";}
			case DARK_PURPLE: {return "darkpurple";}
			case GOLD: {return "gold";}
			case GRAY: {return "gray";}
			case DARK_GRAY: {return "darkgray";}
			case BLUE: {return "blue";}
			case GREEN: {return "green";}
			case AQUA: {return "aqua";}
			case RED: {return "red";}
			case LIGHT_PURPLE: {return "lightpurple";}
			case YELLOW: {return "yellow";}
			default: {return "white";}
		}
	}
	
	public static ChatColor getColor(String msg) {
		String colors = "0123456789abcdefABCDEF";
		for (char c : colors.toCharArray()) {
			String cor = String.valueOf(c).toLowerCase();
			if (msg.equals("&"+cor) || msg.equals("§"+cor)) return ChatColor.getByChar(msg.replace("&", "").replace("§", ""));
		}
		return ChatColor.WHITE;
	}

}
