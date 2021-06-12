package br.com.redewalker.engine.sistema.vanish.evento;

import br.com.redewalker.engine.sistema.usuario.Usuario;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class JogadorDesativouVanish extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	private Usuario jogador;
	
	public JogadorDesativouVanish(Usuario jogador) {
		this.jogador = jogador;
	}
	
	public Usuario getJogador() {
		return jogador;
	}
	
	public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}