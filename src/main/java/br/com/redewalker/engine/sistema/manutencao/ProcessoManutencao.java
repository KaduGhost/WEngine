package br.com.redewalker.engine.sistema.manutencao;

import br.com.redewalker.engine.WEngine;
import br.com.redewalker.engine.utils.Title;
import org.bukkit.Bukkit;

public class ProcessoManutencao implements Manutencao{
	
	private ManutencaoType tipo;
	
	public ProcessoManutencao(ManutencaoType tipo) {
		this.tipo = tipo;
	}
	
	public void run() {
		new Title("§4"+tipo.getNome(), "§aAtivada").broadcast();
		Bukkit.getConsoleSender().sendMessage("§4"+getTipo().getNome()+"\n§cServidor em manutenção");
		WEngine.get().getManutencaoManager().setLiberado(false);
		WEngine.get().getManutencaoManager().setProcesso(false);
	}
	
	public void reverse() {
		new Title("§4"+tipo.getNome(), "§cDesativada").broadcast();;
		WEngine.get().getManutencaoManager().setLiberado(true);
	}
	
	public String getMensagemKick() {
		return "§cServidor entrou em manutenção!";
	}
	
	public ManutencaoType getTipo() {
		return tipo;
	}
	
	public boolean isIgnoreBypassKick() {
		return false;
	}

}
