package br.com.redewalker.engine.sistema.placeholder;

import br.com.redewalker.engine.WEngine;
import br.com.redewalker.engine.sistema.chat.Canal;
import br.com.redewalker.engine.sistema.grupo.Grupo;
import br.com.redewalker.engine.sistema.usuario.Usuario;

import java.text.NumberFormat;

public class PlaceHookWalkers extends PlaceholderHook {

	public PlaceHookWalkers(String nome) throws Exception {
		super(nome);
	}

	@Override
	public String checkPlaceholder(String pattern, Usuario j, Canal canal) {
		String formato = pattern.toLowerCase();
		Grupo grupo = WEngine.get().getGruposManager().getGrupo(j.getGrupoIn());
		Grupo gpout = WEngine.get().getGruposManager().getGrupo(j.getGrupoOut());
		switch (formato) {
		case "%grupo_cor%":
			return grupo.getTag().getCor().toString();
		case "%grupo_tag%":
			return grupo.getTag().getTag();
		case "%grupo_tag_full%":
			return grupo.getTag().toString();
		case "%grupo_nome%":
			return grupo.getNome();
		case "%grupoout_tag%":
			return gpout.getTag().toString();
		case "%grupoout_tag_full%":
			return gpout.getTag().toString();
		case "%grupoout_nome%":
			return gpout.getNome();
		case "%jogador_nick%":
			return j.getNickOriginal();
		case "%jogador_tag%":
			return grupo.getTag().getTag();
		case "%jogador_tag_full%":
			return grupo.getTag().toString();
		case "%server_tag%":
			return WEngine.get().getChatManager().getChatTag("{server}").getTag();
		case "%server_tag_full%":
			return WEngine.get().getChatManager().getChatTag("{server}").getCor()+ WEngine.get().getChatManager().getChatTag("{server}").getTag();
		case "%server_nome%":
			return WEngine.get().getServerType().toString();
		case "%server_cor%":
			return WEngine.get().getChatManager().getChatTag("{server}").getCor();
		case "%jogador_cash%":
			return NumberFormat.getInstance().format(j.getCash());
		case "%chat_tag%":
			return canal.getTag().toString();
		case "%chat_tag_cor%":
			return canal.getTag().getCor().toString();
		default:
			return formato;
		}
	}

}
