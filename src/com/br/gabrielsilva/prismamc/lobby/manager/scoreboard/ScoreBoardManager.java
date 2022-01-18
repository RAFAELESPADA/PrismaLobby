package com.br.gabrielsilva.prismamc.lobby.manager.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.br.gabrielsilva.prismamc.commons.bukkit.BukkitMain;
import com.br.gabrielsilva.prismamc.commons.bukkit.account.BukkitPlayer;
import com.br.gabrielsilva.prismamc.commons.bukkit.api.scoreboard.Sidebar;
import com.br.gabrielsilva.prismamc.commons.bukkit.api.scoreboard.animation.WaveAnimation;
import com.br.gabrielsilva.prismamc.commons.bukkit.custom.events.UpdateEvent;
import com.br.gabrielsilva.prismamc.commons.bukkit.custom.events.UpdateEvent.UpdateType;
import com.br.gabrielsilva.prismamc.commons.core.Core;
import com.br.gabrielsilva.prismamc.commons.core.connections.mysql.MySQLManager;
import com.br.gabrielsilva.prismamc.commons.core.data.DataHandler;
import com.br.gabrielsilva.prismamc.commons.core.data.type.DataType;
import com.br.gabrielsilva.prismamc.commons.core.rank.PlayerRank;
import com.br.gabrielsilva.prismamc.commons.core.tags.Tag;
import com.br.gabrielsilva.prismamc.commons.core.utils.string.StringUtils;
import com.br.gabrielsilva.prismamc.lobby.Lobby;

public class ScoreBoardManager {

	private WaveAnimation waveAnimation;
	private String text = "";
	
	public void init() {
		waveAnimation = new WaveAnimation("KOMBO ", "§f§l", "§b§l", "§3§l", 3);
		text = waveAnimation.next();
		
		registerListener();
		
		Bukkit.getScheduler().runTaskTimer(Lobby.getInstance(), new Runnable() {
			public void run() {
				text = waveAnimation.next();
				
				for (Player onlines : Bukkit.getOnlinePlayers()) {
					 if (onlines == null) {
						 continue;
					 }
					 if (!onlines.isOnline()) {
						 continue;
					 }
					 if (onlines.isDead()) {
						 continue;
					 }
				 	 Scoreboard score = onlines.getScoreboard();
					 if (score == null) {
						 continue;
					 }
					 Objective objective = score.getObjective(DisplaySlot.SIDEBAR);
					 if (objective == null) {
						 continue;
					 }
					 objective.setDisplayName(text);
				}
			}
		}, 40, 2L);
	}
	
	public void updatePosition(Player player) {
		final BukkitPlayer bukkitPlayer = BukkitMain.getManager().getDataManager().getBukkitPlayer(player.getUniqueId());
		
		Sidebar sideBar = bukkitPlayer.getSideBar();
		
		if (sideBar == null) {
			return;
		}
		if (sideBar.isHided()) {
			return;
		}
		
		sideBar.setText(10, "Ranking Global: §a#" + MySQLManager.getPlayerPositionRanking(bukkitPlayer.getNick()));
	}
	
	public void updateScoreboard(Player player) {
		final BukkitPlayer bukkitPlayer = BukkitMain.getManager().getDataManager().getBukkitPlayer(player.getUniqueId());
		
		Sidebar sideBar = bukkitPlayer.getSideBar();
		
		if (sideBar == null) {
			return;
		}
		if (sideBar.isHided()) {
			return;
		}
		
		final DataHandler dataHandler = 
				BukkitMain.getManager().getDataManager().getBukkitPlayer(player.getUniqueId()).getDataHandler();
		
		final Tag tag = dataHandler.getData(DataType.GRUPO).getGrupo().getTag();
		final PlayerRank playerRank = bukkitPlayer.getRank();
		
		sideBar.setText(12, "Cargo: " + tag.getColor() + "§l" + tag.getNome());
		sideBar.setText(11, "Liga: §7[" + playerRank.getCor() + playerRank.getSimbolo() + "§7] " + playerRank.getCor()  + playerRank.getNome());
		
		sideBar.setText(8, "XP: §b" + StringUtils.reformularValor(dataHandler.getInt(DataType.XP)));
		sideBar.setText(7, "Moedas: §e" + StringUtils.reformularValor(dataHandler.getData(DataType.COINS).getInt()));
		
		sideBar.setText(5, "Clan: " + (dataHandler.getData(DataType.CLAN).getString().equalsIgnoreCase("Nenhum") ? 
				"§cN/A" : "§a" + dataHandler.getData(DataType.CLAN).getString()));
		
		sideBar.setText(3, "Jogadores: §a" + Core.getServersHandler().getGlobalCount());
	}
	
	public void createSideBar(Player player) {
		BukkitPlayer bukkitPlayer = BukkitMain.getManager().getDataManager().getBukkitPlayer(player.getUniqueId());
		
		Sidebar sideBar = bukkitPlayer.getSideBar();
		if (sideBar == null) {
			bukkitPlayer.setSideBar(sideBar = new Sidebar(player.getScoreboard()));
			sideBar.show();
		}
		if (sideBar.isHided())
			return;
		
		sideBar.hide();
		sideBar.show();
	
		sideBar.setTitle("§b§lKOMBO§f§lPVP");
		sideBar.setText(13, "");
		sideBar.setText(12, "Cargo: §7MEMBRO");
		sideBar.setText(11, "Liga: ...");
		sideBar.setText(10, "Ranking Global: §a#" + MySQLManager.getPlayerPositionRanking(bukkitPlayer.getNick()));
		sideBar.setText(9, "");
		sideBar.setText(8, "XP: §b0");
		sideBar.setText(7, "Moedas: §b0");
		sideBar.setText(6, "");
		sideBar.setText(5, "Clan: §cN/A");
		sideBar.setText(4, "");
		sideBar.setText(3, "Jogadores: §a0");
		sideBar.setText(2, "");
		sideBar.setText(1, "§bkombopvp.net");
		updateScoreboard(player);
	}
	
	private void registerListener() {
		Bukkit.getServer().getPluginManager().registerEvents(new Listener() {
			
			@EventHandler
			public void update(UpdateEvent event) {
				if (event.getType() != UpdateType.SEGUNDO) {
					return;
				}
				
				for (Player onlines : Bukkit.getOnlinePlayers()) {
					 updateScoreboard(onlines);
				}
			}
			
		}, Lobby.getInstance());
	}
}