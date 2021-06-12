package br.com.redewalker.engine.bungee;

import br.com.redewalker.engine.WEngine;
import br.com.redewalker.engine.sistema.chat.Canal;
import br.com.redewalker.engine.sistema.punicoes.Punicao;
import br.com.redewalker.engine.sistema.punicoes.PunicoesType;
import br.com.redewalker.engine.sistema.usuario.Usuario;
import br.com.redewalker.engine.sistema.vip.VIP;
import br.com.redewalker.engine.utils.Title;
import br.com.redewalker.engine.utils.WalkersUtils;
import br.net.fabiozumbi12.MinEmojis.Fanciful.FancyMessage;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class WalkerBungee implements PluginMessageListener, Listener {
	
	@Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("bungee:walkerengine")) return;
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        ArrayList<String> sts = new ArrayList<String>();
        try {
        	sts.add(in.readUTF());
        	sts.add(in.readUTF());
        	if (sts.get(0).equals("chat")) {
        		sts.add(in.readUTF());
        		sts.add(in.readUTF());
        	}else if (sts.get(0).equals("punicoes")) {
        		sts.add(in.readUTF());
        		sts.add(in.readUTF());
        		sts.add(in.readUTF());
        	}else if (sts.get(0).equals("vip")) {
        		sts.add(in.readUTF());
        	}
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (sts.get(0).equals("chat")) {
        	Canal chan = WEngine.get().getChatManager().getCanal(sts.get(2));
            if (chan == null || !chan.isBungee()) return;
            if (chan.getDistancia() == 0) {
            	for (Player receiver : Bukkit.getServer().getOnlinePlayers()) {
            		Usuario usuario = WEngine.get().getUsuarioManager().getUsuario(receiver.getName());
            		if (usuario != null && usuario.hasPermission("walker.chat.chat"+chan.getNome())) {
            			WalkersUtils.performCommand(receiver, Bukkit.getConsoleSender(), "tellraw " + receiver.getName() + " " + sts.get(3));
            		}
                }
            }
            Bukkit.getConsoleSender().sendMessage("Mensagem bungee para o canal " + chan.getNome() + " do server: " + sts.get(1));
        }else if (sts.get(0).equals("punicoes")) {
        	PunicoesType tipo = PunicoesType.getByNome(sts.get(4));
        	Punicao punicao = WEngine.get().getPunicoesManager().getPunicao(Long.parseLong(sts.get(2)));
        	if (sts.get(1).equalsIgnoreCase(WEngine.get().getServerType().toString())) return;
            for (Player receiver : Bukkit.getServer().getOnlinePlayers()) {
            	if (sts.get(3).equalsIgnoreCase(receiver.getName()) && (tipo.equals(PunicoesType.BAN) || tipo.equals(PunicoesType.IPBAN) || tipo.equals(PunicoesType.KICK))) {
            		receiver.kickPlayer(WEngine.get().getPunicoesManager().getMsgPunido(punicao));
            		continue;
            	}
        		Usuario usuario = WEngine.get().getUsuarioManager().getUsuario(receiver.getName());
        		if (usuario != null && usuario.hasPermission("walker.punir") && usuario.isVerMsgPunicao()) {
        			receiver.sendMessage(WEngine.get().getPunicoesManager().getMsg(punicao));
        		}
            }
        }else if (sts.get(0).equals("vip")) {
        	VIP vip = WEngine.get().getVipManager().getVIP(Long.parseLong(sts.get(2)));
        	if (sts.get(1).equalsIgnoreCase(WEngine.get().getServerType().toString())) new Title(WEngine.get().getGruposManager().getGrupo(vip.getTipo()).getTag().toString().toUpperCase(), "§f"+vip.getUsuario().getNickOriginal()+" tornou-se VIP!").broadcast();
        }
    }
	
	public static void sendBungeeVip(String id, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("vip");
        out.writeUTF(server);
        out.writeUTF(id);
        Player p = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        try {
            p.sendPluginMessage(WEngine.get(), "bungee:walkerengine", out.toByteArray());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
	
	public static void sendBungeeChat(Canal ch, FancyMessage text) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("chat");
        out.writeUTF(WEngine.get().getServerType().toString());
        out.writeUTF(ch.getNome());
        out.writeUTF(text.toJSONString());
        Player p = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        try {
            p.sendPluginMessage(WEngine.get(), "bungee:walkerengine", out.toByteArray());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
	
	public static void sendBungeePunicao(String id, String jogador, String tipo) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("punicoes");
        out.writeUTF(WEngine.get().getServerType().toString());
        out.writeUTF(id);
        out.writeUTF(jogador);
        out.writeUTF(tipo);
        Player p = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        try {
            p.sendPluginMessage(WEngine.get(), "bungee:walkerengine", out.toByteArray());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
