package br.com.redewalker.engine.sistema.punicoes;

import br.com.redewalker.engine.WEngine;

public class Punicao implements Comparable<Punicao> {
	
	private long id;
	
	public Punicao(long id) {
		this.id = id;
	}
	
	public void setID(long id) {
		this.id = id;
	}
	
	public boolean isAvaliacao() {
		return WEngine.get().getConexaoManager().getConexaoPunicoesDAO().getBoolean(id, "avaliacao");
	}
	
	public void setAvaliacao(boolean set) {
		WEngine.get().getConexaoManager().getConexaoPunicoesDAO().setBoolean(id, "despunidor", set);
	}
	
	public void setAvaliador(String set) {
		WEngine.get().getConexaoManager().getConexaoPunicoesDAO().setString(id, "avaliador", set);
	}
	
	public void setDespunidor(String set) {
		WEngine.get().getConexaoManager().getConexaoPunicoesDAO().setString(id, "despunidor", set);
	}
	
	public String getAvaliador() {
		return WEngine.get().getConexaoManager().getConexaoPunicoesDAO().getString(id, "avaliador");
	}
	
	public String getDespunidor() {
		return WEngine.get().getConexaoManager().getConexaoPunicoesDAO().getString(id, "despunidor");
	}
	
	public boolean hasAvaliador() {
		return !getAvaliador().equalsIgnoreCase("n");
	}
	
	public boolean hasDespunidor() {
		return !getDespunidor().equalsIgnoreCase("n");
	}
	
	public String getComentario() {
		return WEngine.get().getConexaoManager().getConexaoPunicoesDAO().getString(id, "comentario");
	}
	
	public boolean isAvaliado() {
		return WEngine.get().getConexaoManager().getConexaoPunicoesDAO().getBoolean(id, "avaliado");
	}
	
	public void setAvaliado(boolean set) {
		WEngine.get().getConexaoManager().getConexaoPunicoesDAO().setBoolean(id, "avaliado", set);
	}
	
	public boolean isPermanente() {
		return getAte() == 0;
	}
	
	public boolean isAtivo() {
		return WEngine.get().getConexaoManager().getConexaoPunicoesDAO().getBoolean(id, "ativo");
	}
	
	public void setAtivo(boolean set) {
		WEngine.get().getConexaoManager().getConexaoPunicoesDAO().setBoolean(id, "ativo", set);
	}
	
	public long getID() {
		return id;
	}

	public long getData() {
		return WEngine.get().getConexaoManager().getConexaoPunicoesDAO().getLong(id, "data");
	}

	public long getAte() {
		return WEngine.get().getConexaoManager().getConexaoPunicoesDAO().getLong(id, "ate");
	}

	public String getAutor() {
		return WEngine.get().getConexaoManager().getConexaoPunicoesDAO().getString(id, "autor");
	}

	public String getProva() {
		return WEngine.get().getConexaoManager().getConexaoPunicoesDAO().getString(id, "prova");
	}

	public PunicaoMotivoType getMotivo() {
		return PunicaoMotivoType.getByNome(WEngine.get().getConexaoManager().getConexaoPunicoesDAO().getString(id, "motivo"));
	}

	public PunicoesType getTipo() {
		return PunicoesType.getByNome(WEngine.get().getConexaoManager().getConexaoPunicoesDAO().getString(id, "tipo"));
	}

	public String getJogador() {
		return WEngine.get().getConexaoManager().getConexaoPunicoesDAO().getString(id, "jogador");
	}

	@Override
	public int compareTo(Punicao o) {
		if (id < o.getID()) return -1;
		else if (id > o.getID()) return 1;
		else return 0;
	}

}
