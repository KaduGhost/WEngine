package br.com.redewalker.engine.sistema.placeholder;

import br.com.redewalker.engine.sistema.chat.Canal;
import br.com.redewalker.engine.sistema.usuario.Usuario;

public abstract class PlaceholderHook {
	
	private String nome;
	
	public PlaceholderHook(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}
	
	public abstract String checkPlaceholder(String pattern, Usuario j, Canal canal);

}
