package br.com.redewalker.engine.apis;

import br.com.redewalker.engine.sistema.usuario.Usuario;
import org.bukkit.entity.Player;

public class VanishAPI {
	
	public static boolean usuarioCanSeeVanish(Usuario j) {
		return j.hasPermission("walker.vanish.see");
	}
	
	public static boolean UsuarioIsOnline(Usuario j, Usuario j2) {
		Player p = j.getPlayer();
		if (p instanceof Player && (!j.isVanish() || usuarioCanSeeVanish(j2))) return true;
		return false;
	}

}