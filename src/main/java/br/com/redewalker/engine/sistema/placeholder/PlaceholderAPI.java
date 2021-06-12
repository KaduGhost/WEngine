package br.com.redewalker.engine.sistema.placeholder;

import br.com.redewalker.engine.WEngine;
import br.com.redewalker.engine.sistema.chat.Canal;
import br.com.redewalker.engine.sistema.usuario.Usuario;

import java.util.HashMap;

public class PlaceholderAPI {
	
	private HashMap<String, PlaceholderHook> placeholders;
	
	public PlaceholderAPI() {
		placeholders = new HashMap<String, PlaceholderHook>();
	}
	
	public void registerPlaceHolder(PlaceholderHook place) throws Exception {
		if (exists(place.getNome())) throw new Exception("Já existe uma instancia de placeholdehook com esse nome");
		placeholders.put(place.getNome().toLowerCase(), place);
	}
	
	public void unregisterPlaceHolder(String name) throws Exception {
		if (!exists(name)) throw new Exception("Não existe uma instancia de placeholdehook com esse nome");
		placeholders.remove(name.toLowerCase());
	}
	
	public String checkPlaceholder(String pattern, Usuario j, Canal canal) {
		String format = pattern.toLowerCase().replace("%", "");
		String[] parts = format.split("_");
		if (parts.length == 0) return pattern.toLowerCase();
		for (String key : placeholders.keySet()) {
			String s = placeholders.get(key).checkPlaceholder(pattern, j, canal);
			if (!s.equals(pattern)) return s;
		}
		return pattern;
		/*if (exists(parts[0])) {
			return placeholders.get(parts[0]).checkPlaceholder(pattern, j, canal);
		} else return placeholders.get("walkers").checkPlaceholder(pattern, j, canal);*/
	}
	
	public String checkPlaceholders(String string, Usuario j, Canal canal) {
		String retu = "";
		String formato = string;
		String latestTag = "";
		for (char c : formato.toCharArray()) {
			if (String.valueOf(c).equals("%")) {
				if (latestTag.contains("%")) {
					latestTag+=c;
					retu+= WEngine.get().getPlaceholderAPI().checkPlaceholder(latestTag, j, canal);
					latestTag = "";
				} else latestTag+=c;
			} else {
				if (latestTag.length() != 0) latestTag+=c;
				else retu+=c;
			}
		}
		return retu;
	}
	
	private boolean exists(String nome) {
		return placeholders.containsKey(nome.toLowerCase());
	}

}
