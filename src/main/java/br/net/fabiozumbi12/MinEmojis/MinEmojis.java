package br.net.fabiozumbi12.MinEmojis;

import br.net.fabiozumbi12.MinEmojis.Fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.rpapi.ResourcePackAPI;

import java.util.*;

public class MinEmojis extends JavaPlugin implements Listener {

	public static Server serv;
	public static PluginDescriptionFile pdf;
	public static PluginManager pm;
	public static MinEmojis plugin;
	static HashMap<List<String>, String> emojis = new HashMap<List<String>, String>();
	static List<String> DeclinedPlayers = new ArrayList<String>();
	public static MEConfig config;
	private static List<String> installing = new ArrayList<String>();
	private HashMap<String,HashMap<Integer,String>> signPlayers = new HashMap<String,HashMap<Integer,String>>();

	public void onEnable() {
		try {			
			
			Plugin p = Bukkit.getPluginManager().getPlugin("ResourcePackApi");
			boolean RPAPI = p != null && p.isEnabled();	    	
			plugin = this;
			serv = getServer();
	        pdf = getDescription();
	        pm = serv.getPluginManager();
	        AddEmojis();
	        config = new MEConfig(plugin);
	        
	        if (getBukkitVersion() >= 188){
	        	if (RPAPI){
	        		MELogger.warning("ResourcePackApi detected but after version 1.8.8 is not necessary. You can remove securelly!");
	        	}
	        	pm.registerEvents(new MEListener188(), this); 
	        } else {	        	
	        	if (!RPAPI){
	        		MELogger.severe("Not found the dependency ResourcePackAPI required for version < 1.8.8!");
	    			Bukkit.getPluginManager().disablePlugin(this);
	    			return;
	        	}
	        	pm.registerEvents(new MEListenerRPA(), this); 
	        }
	        
	        MELogger.sucess(pdf.getFullName()  + " enabled. ("+getBukkitVersion()+")");

			Bukkit.getPluginManager().registerEvents(this, this);
		} catch (Exception e) {
			e.printStackTrace();
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
	}
	
	private void AddEmojis() {
		emojis.put(new ArrayList(Arrays.asList(":blush:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":smile:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":rage:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":anguished:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":heart:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":broken_heart:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":heart_eyes:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":kissing_heart:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":sob:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":sunglasses:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":unamused:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":zzz:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":dizzy_face:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":expressionless:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":wink:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":worried:")), "???");

		emojis.put(new ArrayList(Arrays.asList(":stuck_out_tongue_closed_eyes:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":yum:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":open_mouth:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":flushed:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":confused:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":sweat:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":star:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":cupid:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":shit:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":thumbsup:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":thumbsdown:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":punch:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":ok_hand:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":raised_hands:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":kiss:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":eyes:")), "???");
		
		emojis.put(new ArrayList(Arrays.asList(":trollface:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":scream:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":smiling_imp:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":imp:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":innocent:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":musical_note:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":star2:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":sparkles:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":boom:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":blue_heart:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":fire:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":point_up:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":point_down:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":point_left:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":point_right:")), "???");
		emojis.put(new ArrayList(Arrays.asList(":pray:")), "???");
	}


	public void onDisable() {		
		config.saveAll();
		MELogger.severe(pdf.getFullName() + " disabled.");
	}	
		
	public static boolean isInstalling(Player p){		
		return installing.contains(p.getName());
	}
	
	public static boolean delInstalling(Player p){		
		return installing.remove(p.getName());
	}
	
	private int getBukkitVersion(){
    	String name = Bukkit.getServer().getClass().getPackage().getName();
		String v = name.substring(name.lastIndexOf('.') + 1) + ".";
    	String[] version = v.replace('_', '.').split("\\.");
		
		int lesserVersion = 0;
		try {
			lesserVersion = Integer.parseInt(version[2]);
		} catch (NumberFormatException ex){				
		}
		return Integer.parseInt((version[0]+version[1]).substring(1)+lesserVersion);
    }
	
	@Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args){
		List<String> tab = new ArrayList<String>();
		if (cmd.getName().equalsIgnoreCase("MinEmojis") && args.length == 1){			
	    	for (List<String> emojis:MinEmojis.emojis.keySet()){
	    		for (String emoji:emojis){
	    			if (emoji.startsWith(":") && emoji.endsWith(":")){
		    			tab.add(emoji);
		    		}
	    		}	    		
	    	}
		}    	
		return tab;     	
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (args.length == 0){
			sender.sendMessage(config.getLangString("plugin-tag")+"??aHelp Page:");
			if (sender.hasPermission("minemojis.install")){
				sender.sendMessage(config.getLangString("commands.install").replace("{cmd}", lbl));
			}
			if (sender.hasPermission("minemojis.download")){
				sender.sendMessage(config.getLangString("commands.download").replace("{cmd}", lbl));
			}
			if (sender.hasPermission("minemojis.enable")){
				sender.sendMessage(config.getLangString("commands.enable").replace("{cmd}", lbl));
			}
			if (sender.hasPermission("minemojis.list")){
				sender.sendMessage(config.getLangString("commands.list").replace("{cmd}", lbl));
			}
            if (sender.hasPermission("minemojis.setsign")){
            	sender.sendMessage(config.getLangString("commands.setsign").replace("{cmd}", lbl));
			}
			sender.sendMessage(ChatColor.GRAY+"?? "+ChatColor.ITALIC + MinEmojis.pdf.getFullName() + ", developed by FabioZumbi12!");
		}
		if (args.length == 1){
			if ((args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("enable")) && sender.hasPermission("minemojis.enable")){
				if (DeclinedPlayers.contains(sender.getName())){
					DeclinedPlayers.remove(sender.getName());					
				}
				sender.sendMessage(config.getLangString("plugin-tag")+config.getLangString("emojis.enabled"));
			}
			if ((args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("disable")) && sender.hasPermission("minemojis.enable")){
				if (!DeclinedPlayers.contains(sender.getName())){
					DeclinedPlayers.add(sender.getName());					
				}        		
				sender.sendMessage(config.getLangString("plugin-tag")+config.getLangString("emojis.disabled"));
			}
			if (args[0].equalsIgnoreCase("reload") && sender.hasPermission("minemojis.reload")){
				config.reload();
        		sender.sendMessage(ChatColor.AQUA + MinEmojis.pdf.getFullName() + " reloaded!");
			}						
			if (args[0].equalsIgnoreCase("install") && sender instanceof Player && sender.hasPermission("minemojis.install")){
				Player p = (Player) sender;
				if (getBukkitVersion() >= 188){
					p.setResourcePack("https://dl.dropboxusercontent.com/s/mggt0usjvrrvgj5/MinEmojis.zip");
				} else {
					ResourcePackAPI.setResourcepack(p, "https://dl.dropboxusercontent.com/s/mggt0usjvrrvgj5/MinEmojis.zip", "minemojis");	
				}
				installing.add(p.getName());
			}
			if (args[0].equalsIgnoreCase("download") && sender instanceof Player && sender.hasPermission("minemojis.download")){
				Player p = (Player) sender;
				if (config.getList("download-help-lines") != null && config.getList("download-help-lines").size() > 0){
					for (String line:config.getList("download-help-lines")){
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
					}
				}					
			}
			if (args[0].equalsIgnoreCase("list") && sender instanceof Player && sender.hasPermission("minemojis.list")){
				Iterator<List<String>> emojits = MinEmojis.emojis.keySet().iterator();
				sender.sendMessage(config.getLangString("plugin-tag")+config.getLangString("default-color")+" Emojis:");
				try {
                  	 Class.forName("com.google.gson.JsonParser");
                  	FancyMessage message = new FancyMessage();
    				
    				while (emojits.hasNext()){	
    					List<String> shortcuts = emojits.next();
    					String[] emoji = shortcuts.toArray(new String[shortcuts.size()]);
    					String shortcut = "";
    					for (String shortc:emoji){
    						shortcut = shortcut+" "+config.getLangString("default-color")+"|??r "+shortc;
    					}
    					shortcut = shortcut.substring(4);
    					message.text(config.getLangString("default-color")+"|??r"+MinEmojis.emojis.get(shortcuts)+config.getLangString("default-color")+"|??r")
    					.tooltip(MinEmojis.config.getLangString("shortcut")+shortcut)
    					.then(" ");
    				}    				
    				message.send(sender);
                  	} catch( ClassNotFoundException e ) {
                  		String send = "";
        				while (emojits.hasNext()){	
        					List<String> shortcuts = emojits.next();
        					String[] emoji = shortcuts.toArray(new String[shortcuts.size()]);
        					send = send+"|"+MinEmojis.emojis.get(shortcuts)+" = "+emoji+"|";
        				}
        				sender.sendMessage(send);
                  	}				
			}
			for (List<String> emojis:MinEmojis.emojis.keySet()){
				if (emojis.contains(args[0]) && (sender.hasPermission("minemojis."+args[0]) || sender.hasPermission("minemojis.all"))){
					sender.sendMessage(config.getLangString("plugin-tag")+" "+args[0]+"??r = "+MinEmojis.emojis.get(emojis));
				}
			}
		}		
				
		if (args.length >= 3){
			//emoji setsign <line> <text>
			if (args[0].equalsIgnoreCase("setsign") && sender instanceof Player && sender.hasPermission("minemojis.setsign")){
				Player p = (Player) sender;
				int line = 0;
				try {
					line = Integer.valueOf(args[1]);
					if (line < 1 || line > 4){
						sender.sendMessage(config.getLangString("plugin-tag")+config.getLangString("setsign.nolines"));
						return true;
					}
				} catch (NumberFormatException e){
					sender.sendMessage(config.getLangString("plugin-tag")+config.getLangString("setsign.usage").replace("{cmd}", lbl));
					return true;
				}
				StringBuilder message = new StringBuilder();
				for (String arg:args){
					if (arg.equals(args[0]) || arg.equals(args[1])){
						continue;
					}
					message.append(arg+" ");
				}
				String msg = formatEmoji(p, message.toString().substring(0,message.toString().length()-1), true);
				HashMap<Integer,String> pmsg = new HashMap<Integer,String>();
				if (signPlayers.containsKey(p.getName())){
					pmsg = signPlayers.get(p.getName());
				}
				pmsg.put(line, msg);
				signPlayers.put(p.getName(), pmsg);
				sender.sendMessage(config.getLangString("plugin-tag")+config.getLangString("setsign.setline-to").replace("{line}", ""+line).replace("{text}", ChatColor.translateAlternateColorCodes('&', msg)));
			}
		}
		return true;
	}	
	
	private String formatEmoji(Player p, String msg, boolean sign){
		for (List<String> emojis:MinEmojis.emojis.keySet()){
			for (String emoji:emojis){
				if (msg.contains(emoji) && (p.hasPermission("minemojis.emoji."+emoji.replace(":", "")) || p.hasPermission("minemojis.emoji.all"))){
					String emof = MinEmojis.emojis.get(emojis);
					msg = msg.replace(emoji, sign ? "&f"+emof+"&r":emof);				
				}
			}			
		}
		return msg;
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onInteractSign(PlayerInteractEvent e){
		Player p = e.getPlayer();
		Block b = e.getClickedBlock();
		if (signPlayers.containsKey(p.getName())){
			if (b.getState() instanceof Sign){
				Sign s = (Sign) b.getState();
				for (Integer line:signPlayers.get(p.getName()).keySet()){
					s.setLine(line-1, ChatColor.translateAlternateColorCodes('&', signPlayers.get(p.getName()).get(line)));				
				}				
				s.update();
			} else {
				p.sendMessage(config.getLangString("plugin-tag")+config.getLangString("setsign.no-sign"));
			}
			signPlayers.remove(p.getName());
		}		
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onSignChange(SignChangeEvent e){
		Player p = e.getPlayer();
		if (p.hasPermission("minemojis.placesign") && !MinEmojis.DeclinedPlayers.contains(p.getName())){
			for (int i = 0; i < e.getLines().length; i++){
				e.setLine(i, ChatColor.translateAlternateColorCodes('&', formatEmoji(p, e.getLine(i), true)));
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent e){
		Player p = e.getPlayer();
		String chat = e.getMessage();
		
		if (MinEmojis.DeclinedPlayers.contains(p.getName())){
			return;
		}
		e.setMessage(formatEmoji(p, chat, false));
	}
}
