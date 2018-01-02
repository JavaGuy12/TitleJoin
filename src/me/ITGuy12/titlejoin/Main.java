package me.ITGuy12.titlejoin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle.EnumTitleAction;
import net.minecraft.server.v1_12_R1.PlayerConnection;

public class Main extends JavaPlugin implements Listener {

	
	 boolean useActionBar;
	 boolean useTitle;
	 String titleText;
	 String subtitleText;
	 String actionBarText;
	
	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		
		this.saveDefaultConfig();
		useActionBar = getConfig().getBoolean("actionbar.use-action-bar");
		useTitle = getConfig().getBoolean("title.use-title");
		actionBarText = getConfig().getString("actionbar.action-bar-message");
		titleText = getConfig().getString("title.title-message");
		subtitleText = getConfig().getString("title.sub-title-message");
		
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		   // Setting up the strings
			String title = ChatColor.translateAlternateColorCodes('&', titleText);
			String subtitle = ChatColor.translateAlternateColorCodes('&', subtitleText);
			String actionBar = ChatColor.translateAlternateColorCodes('&', actionBarText);
			
			title = title.replaceAll("%player%", e.getPlayer().getName());
			title = title.replaceAll("%online%", Integer.toString(Bukkit.getOnlinePlayers().size()));
			subtitle = subtitle.replaceAll("%player%", e.getPlayer().getName());
			subtitle = subtitle.replaceAll("%online%", Integer.toString(Bukkit.getOnlinePlayers().size()));
			actionBar = actionBar.replaceAll("%player%", e.getPlayer().getName());
			actionBar = actionBar.replaceAll("%online%", Integer.toString(Bukkit.getOnlinePlayers().size()));
				
					//Player Variables
		        CraftPlayer craftplayer = (CraftPlayer) e.getPlayer();
		        PlayerConnection connection = craftplayer.getHandle().playerConnection;
		       
		        //JSON code for the messages
		        IChatBaseComponent titleJSON = ChatSerializer.a("{\"text\": \"" + title + "\"}\"");
		        IChatBaseComponent subtitleJSON = ChatSerializer.a("{\"text\": \"" + subtitle + "\"}\"");
		        IChatBaseComponent actionBarJSON = ChatSerializer.a("{\"text\": \"" + actionBar + "\"}\"");
		        
		        //Packets
		        PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(EnumTitleAction.TITLE, titleJSON, 1, 3, 1);
		        PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, subtitleJSON);
		        PacketPlayOutTitle actionBarPacket = new PacketPlayOutTitle(EnumTitleAction.ACTIONBAR, actionBarJSON);
		        
		        //Sending the Packets
		        if(useTitle) {
		        		connection.sendPacket(titlePacket);
		        		connection.sendPacket(subtitlePacket);
		        }
		        
		        if(useActionBar) {
		        		connection.sendPacket(actionBarPacket);
		        }
		        
		        }
	}
