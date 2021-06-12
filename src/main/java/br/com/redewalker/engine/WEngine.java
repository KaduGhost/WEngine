package br.com.redewalker.engine;

import br.com.redewalker.engine.comandos.*;
import br.com.redewalker.engine.conexao.ConexaoManager;
import br.com.redewalker.engine.configuracao.ConfigManager;
import br.com.redewalker.engine.listeners.PunicoesListeners;
import br.com.redewalker.engine.listeners.ReportsListeners;
import br.com.redewalker.engine.listeners.UsuarioListeners;
import br.com.redewalker.engine.sistema.chat.ChatManager;
import br.com.redewalker.engine.sistema.comando.ComandosManager;
import br.com.redewalker.engine.sistema.grupo.GrupoManager;
import br.com.redewalker.engine.sistema.manutencao.ManutencaoManager;
import br.com.redewalker.engine.sistema.placeholder.PlaceholderAPI;
import br.com.redewalker.engine.sistema.punicoes.PunicoesManager;
import br.com.redewalker.engine.sistema.reports.ReportManager;
import br.com.redewalker.engine.sistema.server.Servers;
import br.com.redewalker.engine.sistema.staff.StaffManager;
import br.com.redewalker.engine.sistema.usuario.UsuarioManager;
import br.com.redewalker.engine.sistema.vanish.VanishManager;
import br.com.redewalker.engine.sistema.vip.VIPManager;
import br.com.redewalker.engine.bungee.WalkerBungee;
import br.com.redewalker.engine.listeners.ComandosListeners;
import br.com.redewalker.engine.sistema.placeholder.PlaceHookWalkers;
import br.com.redewalker.engine.sistema.server.ServerManager;
import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class WEngine extends JavaPlugin {
	
	private static WEngine instance;
	private UsuarioManager usuarioManager;
	private ConexaoManager conexaoAPI;
	private ConfigManager config;
	private GrupoManager grupoManager;
	private StaffManager staffManager;
	private ChatManager chatManager;
	private PlaceholderAPI placeholder;
	private ComandosManager comandosManager;
	private VanishManager vanishManager;
	private PunicoesManager punicoesManager;
	private ReportManager reportManager;
	private ManutencaoManager manutencao;
	private ServerManager servers;
	private VIPManager vip;
	//private VoarManager voarManager;
	private boolean isUseProtocolLib;
	private String tag;
	private Servers server;
	private static CommandSender console = Bukkit.getConsoleSender();
	
	public static WEngine get() {
		return instance;
	}

	
	@Override
	public void onEnable() {
		instance = this;
		tag = "("+getName()+") ";
		config = new ConfigManager(this);
		server = Servers.getTipo(config.getConfigPrincipal().getServerNome());
		console.sendMessage(server.toString());
		conexaoAPI = new ConexaoManager(getConfigManager().getConfigPrincipal());
		usuarioManager = new UsuarioManager();
		grupoManager = new GrupoManager(config.getConfigPrincipal());
		staffManager = new StaffManager();
		chatManager = new ChatManager(getConfigManager().getConfigChat());
		comandosManager = new ComandosManager(getConfigManager().getConfigComando());
		vanishManager = new VanishManager();
		punicoesManager = new PunicoesManager(getConfigManager().getConfigPrincipal());
		reportManager = new ReportManager();
		servers = new ServerManager(server);
		manutencao = new ManutencaoManager(config.getConfigPrincipal());
		vip = new VIPManager();
//		voarManager = new VoarManager(getConfigManager().getConfigPrincipal());
		placeholder = new PlaceholderAPI();
		if (!checkHooks()) {
			console.sendMessage("§c"+tag+" Server desligando por não encontrar plugins necessários para seu funcionamento.");
			Bukkit.shutdown();
			return;
		}
		try {placeholder.registerPlaceHolder(new PlaceHookWalkers("walkers"));} catch (Exception e) {e.printStackTrace();}
		
		registrarEventos();
		registrarComandos();
		
		getServer().getMessenger().registerOutgoingPluginChannel(this, "bungee:walkerengine");
        getServer().getMessenger().registerIncomingPluginChannel(this, "bungee:walkerengine", new WalkerBungee());
        
        //getServer().getMessenger().registerOutgoingPluginChannel(this, "bungee:walkerpunicoes");
        //getServer().getMessenger().registerIncomingPluginChannel(this, "bungee:walkerpunicoes", new WalkerPunicaoBungee());
        
		console.sendMessage("§a"+tag+" Plugin iniciado com sucesso!");
		new BukkitRunnable() {
			public void run() {
				for (String user : WEngine.get().getUsuarioManager().getAllUsuarios().keySet()) {
					WEngine.get().getUsuarioManager().getUsuario(user).atualizarGrupo();
				}
			}
		}.runTaskTimerAsynchronously(WEngine.get(), 0, 20*300);
	}
	
	@Override
	public void onDisable() {
		grupoManager.desligar();
		comandosManager.save();
		ProtocolLibrary.getProtocolManager().removePacketListeners(this);
		console.sendMessage("§a"+tag+" Plugin desligado com sucesso!");
		instance = null;
	}
	
	public boolean checkHooks() {
		try {
			if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
	        	isUseProtocolLib = getServer().getPluginManager().isPluginEnabled("ProtocolLib");
	        	return isUseProtocolLib;
	        }
	    }catch (Exception exception){exception.printStackTrace();}
		return false;
	}
	
	public Servers getServerType() {
		return server;
	}
	
//	public VoarManager getVoarManager() {
//		return voarManager;
//	}
	
	public VIPManager getVipManager() {
		return vip;
	}
	
	public ServerManager getServerManager() {
		return servers;
	}
	
	public ManutencaoManager getManutencaoManager() {
		return manutencao;
	}
	
	public ReportManager getReportManager() {
		return reportManager;
	}
	
	public PunicoesManager getPunicoesManager() {
		return punicoesManager;
	}
	
	public FileConfiguration getConfig() {
		return config.getConfigPrincipal().get();
	}
	
	public VanishManager getVanishManager() {
		return vanishManager;
	}
	
	public ComandosManager getComandosManager() {
		return comandosManager;
	}
	
	public PlaceholderAPI getPlaceholderAPI() {
		return placeholder;
	}
	
	public ChatManager getChatManager() {
		return chatManager;
	}
	
	public StaffManager getStaffManager() {
		return staffManager;
	}
	
	public GrupoManager getGruposManager() {
		return grupoManager;
	}
	
	public ConfigManager getConfigManager() {
		return config;
	}
	
	public ConexaoManager getConexaoManager() {
		return conexaoAPI;
	}
	
	public String getTag() {
		return tag;
	}
	
	public UsuarioManager getUsuarioManager() {
		return usuarioManager;
	}
	
	public void registrarEventos() {
		Bukkit.getServer().getPluginManager().registerEvents(new UsuarioListeners(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new ComandosListeners(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new PunicoesListeners(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new ReportsListeners(), this);
	}
	
	public void registrarComandos() {
		comandosManager.registrarComando(new ComandoCash("cash"), "walker.membro", 0);
		comandosManager.registrarComando(new ComandoGrupo("grupo"), "walker.grupos.admin", 0);
		comandosManager.registrarComando(new ComandoStaff("staff"), "walker.staff", 0);
		comandosManager.registrarComando(new ComandoBroadcast("broadcast"), "walker.broadcast.admin", 0);
		comandosManager.registrarComando(new ComandoHat("hat"), "walker.hat", 0);
		comandosManager.registrarComando(new ComandoMatar("matar"), "walker.matar", 0);
		comandosManager.registrarComando(new ComandoTimeSet("timeset"), "walker.timeset", 0);
		comandosManager.registrarComando(new ComandoLimparChat("limparchat"), "walker.limparchat", 0);
		comandosManager.registrarComando(new ComandoSkull("skull"), "walker.skull", 0);
		comandosManager.registrarComando(new ComandoTell("tell"), "walker.membro", 0);
		comandosManager.registrarComando(new ComandoR("r"), "walker.membro", 0);
		comandosManager.registrarComando(new ComandoCmdBlock("cmdblock"), "walker.cmdblock", 0);
		comandosManager.registrarComando(new ComandoPunir("punir"), "walker.punir", 0);
		//comandosManager.registrarComando(new ComandoPunir("punir"), "punir", 0);
		//comandosManager.registrarComando(new ComandoMotd("motd"), "motd", 0);
		//comandosManager.registrarComando(new ComandoManutencao("manutencao"), "manutencao.admin", 0);
		comandosManager.registrarComando(new ComandoReports("reports"), "walker.reports", 0);
		comandosManager.registrarComando(new ComandoReport("reportar"), "walker.membro", 30);
		comandosManager.registrarComando(new ComandoVanish("vanish"), "walker.vanish", 30);
		//comandosManager.registrarComando(new ComandoEditItem("edititem"), "edititem", 0);
		//comandosManager.registrarComando(new ComandoVoar("voar"), "voar", 0);
		comandosManager.registrarComando(new ComandoCrashar("crashar"), "walker.crashar", 0);
		comandosManager.registrarComando(new ComandoDivulgar("divulgar"), "walker.divulgar", 60*30);
		//comandosManager.registrarComando(new ComandoEditarItem("editaritem"), "editaritem", 0);
		//comandosManager.registrarComando(new ComandoEditarPlaca("editarplaca"), "editarplaca", 0);
		comandosManager.registrarComando(new ComandoExecutarSom("executarsom"), "walker.executarsom", 0);
		comandosManager.registrarComando(new ComandoSpeed("speed"), "walker.speed", 0);
		comandosManager.registrarComando(new ComandoSudo("sudo"), "walker.sudo", 0);
		comandosManager.registrarComando(new ComandoTitle("title"), "walker.title", 0);
		comandosManager.registrarComando(new ComandoManutencao("manutencao"), "walker.manutencao.admin", 0);
		comandosManager.registrarComando(new ComandoVIPs("vips"), "walker.vips.admin", 0);
		comandosManager.registrarComando(new ComandoDarVip("darvip"), "walker.darvip", 0);
		comandosManager.registrarComando(new ComandoSetVip("setvip"), "walker.setvip", 0);
	}

}
