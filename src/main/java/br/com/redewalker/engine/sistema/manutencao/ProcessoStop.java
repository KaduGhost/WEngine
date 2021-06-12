package br.com.redewalker.engine.sistema.manutencao;

import br.com.redewalker.engine.WEngine;
import br.com.redewalker.engine.utils.Title;
import org.bukkit.Bukkit;

public class ProcessoStop implements Manutencao {
	
	private ManutencaoType tipo;
	
	public ProcessoStop(ManutencaoType tipo) {
		this.tipo = tipo;
	}
	
	public void run() {
		new Title("§4"+tipo.getNome(), "§aServidor desligando").broadcast();
		Bukkit.getConsoleSender().sendMessage("§4"+getTipo().getNome()+"\n§cServidor desligando!");
		WEngine.get().getManutencaoManager().setAberto(false);
		WEngine.get().getManutencaoManager().setProcesso(false);
		Bukkit.shutdown();
	}
	
	public void reverse() {
	}
	
	public String getMensagemKick() {
		return "§cO servidor está reiniciando!";
	}
	
	public ManutencaoType getTipo() {
		return tipo;
	}
	
	public boolean isIgnoreBypassKick() {
		return true;
	}

}
