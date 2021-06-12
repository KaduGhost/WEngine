package br.com.redewalker.engine.conexao;

import br.com.redewalker.engine.sistema.usuario.Usuario;
import br.com.redewalker.engine.WEngine;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class ConexaoUsuarioDAO extends Conexao {
	
	public ConexaoUsuarioDAO(String nome, String pk) {
		super(nome,pk);
	}
	
	public void checkTable() {
		String sql = "create table if not exists "+getNome()+"(nick varchar(16), grupo varchar(50) not null default 'membro', grupoout varchar(50) not null default 'membro', cash double default 0, primary key(nick));";
		try {
			PreparedStatement st = WEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.execute();
			st = WEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement("select * from "+getNome());
			ResultSet rs =st.executeQuery();
			if (!WEngine.get().getConexaoManager().getConexaoAPI().hasColumn(rs, "logado")) {
				st = WEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement("ALTER TABLE `"+getNome()+"` ADD `logado` tinyint default 0 AFTER `cash`");
				st.executeUpdate();
			}
			if (!WEngine.get().getConexaoManager().getConexaoAPI().hasColumn(rs, "vanish")) {
				st = WEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement("ALTER TABLE `"+getNome()+"` ADD `vanish` tinyint default 0 AFTER `logado`");
				st.executeUpdate();
			}
			if (!WEngine.get().getConexaoManager().getConexaoAPI().hasColumn(rs, "online")) {
				st = WEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement("ALTER TABLE `"+getNome()+"` ADD `online` tinyint default 0 AFTER `logado`");
				st.executeUpdate();
			}
			if (!WEngine.get().getConexaoManager().getConexaoAPI().hasColumn(rs, "onlineserver")) {
				st = WEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement("ALTER TABLE `"+getNome()+"` ADD `onlineserver` varchar(50) not null default 'nenhum' AFTER `online`");
				st.executeUpdate();
			}
			if (!WEngine.get().getConexaoManager().getConexaoAPI().hasColumn(rs, "chatfocus")) {
				st = WEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement("ALTER TABLE `"+getNome()+"` ADD `chatfocus` varchar(50) not null default 'default' AFTER `online`");
				st.executeUpdate();
			}
			if (!WEngine.get().getConexaoManager().getConexaoAPI().hasColumn(rs, "tell")) {
				st = WEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement("ALTER TABLE `"+getNome()+"` ADD `tell` tinyint default 1 AFTER `chatfocus`");
				st.executeUpdate();
			}
			if (!WEngine.get().getConexaoManager().getConexaoAPI().hasColumn(rs, "msgban")) {
				st = WEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement("ALTER TABLE `"+getNome()+"` ADD `msgban` tinyint default 1 AFTER `tell`");
				st.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public long getQuantUsuariosOnline() {
		String sql = "select * from "+getNome()+" where online = ?";
		try {
			PreparedStatement st = WEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setBoolean(1, true);
			ResultSet rs = st.executeQuery();
			rs.last();
			return rs.getRow();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public boolean createUsuario(String nick) {
		String sql = "insert into "+getNome()+"(nick,grupo,grupoout,cash,logado,vanish,online) values(?,?,?,?,?,?,?)";
		try {
			PreparedStatement st = WEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setString(1, nick);
			st.setString(2, "membro");
			st.setString(3, "membro");
			st.setDouble(4, 0);
			st.setBoolean(5, false);
			st.setBoolean(6, false);
			st.setBoolean(7, false);
			st.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public HashMap<String, Usuario> getAllUsuarios() {
		HashMap<String, Usuario> jogadores = new HashMap<>();
		String sql = "select * from "+getNome();
		try {
			PreparedStatement st = WEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Usuario j = new Usuario(rs.getString("nick"));
				jogadores.put(j.getNickOriginal().toLowerCase(), j);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return jogadores;
	}
	
	public Usuario getUsuario(String nick) {
		String sql = "select * from "+getNome()+" where nick = ?";
		try {
			PreparedStatement st = WEngine.get().getConexaoManager().getConexaoAPI().getConexao().prepareStatement(sql);
			st.setString(1, nick);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				return new Usuario(rs.getString("nick"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
