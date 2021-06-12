package br.com.redewalker.engine.sistema.manutencao;

public interface Manutencao {
	
	public void run();
	
	public void reverse();
	
	public ManutencaoType getTipo();
	
	public String getMensagemKick();
	
	public boolean isIgnoreBypassKick();
	
}
