package com.br.gabrielsilva.prismamc.lobby;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import com.br.gabrielsilva.prismamc.commons.bukkit.BukkitMain;
import com.br.gabrielsilva.prismamc.commons.bukkit.api.server.ServerAPI;
import com.br.gabrielsilva.prismamc.commons.bukkit.commands.BukkitCommandFramework;
import com.br.gabrielsilva.prismamc.commons.bukkit.manager.config.PluginConfig;
import com.br.gabrielsilva.prismamc.commons.core.Core;
import com.br.gabrielsilva.prismamc.commons.core.utils.loaders.CommandLoader;
import com.br.gabrielsilva.prismamc.commons.core.utils.loaders.ListenerLoader;
import com.br.gabrielsilva.prismamc.lobby.manager.Manager;
import com.br.gabrielsilva.prismamc.lobby.manager.npc.NPCManager;

import lombok.Getter;
import lombok.Setter;

public class Lobby extends JavaPlugin {

	@Getter @Setter
	private static Lobby instance;
	
	@Getter @Setter
	private static Location spawn;
	
	@Getter @Setter
	private static Manager manager;
	
	public void onLoad() {
		setInstance(this);
		saveDefaultConfig();
	}
	
	public void onEnable() {
		Core.getServersHandler().init();
		
		BukkitMain.getManager().enableHologram();
		setManager(new Manager());
		
		PluginConfig.createLoc(getInstance(), "spawn");
		
		setSpawn(PluginConfig.getNewLoc(getInstance(), "spawn"));
		
		ListenerLoader.loadListenersBukkit(getInstance(), "com.br.gabrielsilva.prismamc.lobby.listener");
		
		new CommandLoader(new BukkitCommandFramework(getInstance())).
		loadCommandsFromPackage("com.br.gabrielsilva.prismamc.lobby.commands");
		new NPCManager(this);
		
		ServerAPI.registerAntiAbuser();
	}
	
	public static void console(String msg) {
		Bukkit.getConsoleSender().sendMessage("[Lobby] " + msg);
	}
	
	public static void runAsync(Runnable runnable) {
		Bukkit.getScheduler().runTaskAsynchronously(getInstance(), runnable);	
	}
	
	public static void runLater(Runnable runnable) {
		runLater(runnable, 5);
	}
	
	public static void runLater(Runnable runnable, long ticks) {
		Bukkit.getScheduler().runTaskLater(getInstance(), runnable, ticks);	
	}
}