package com.br.gabrielsilva.prismamc.lobby.listener;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import com.br.gabrielsilva.prismamc.commons.core.data.category.DataCategory;
import com.br.gabrielsilva.prismamc.commons.core.data.type.DataType;
import com.br.gabrielsilva.prismamc.commons.core.group.Groups;
import com.br.gabrielsilva.prismamc.commons.bukkit.BukkitMain;
import com.br.gabrielsilva.prismamc.commons.bukkit.account.BukkitPlayer;
import com.br.gabrielsilva.prismamc.commons.bukkit.api.bossbar.BossBarAPI;
import com.br.gabrielsilva.prismamc.commons.bukkit.api.itembuilder.ItemBuilder;
import com.br.gabrielsilva.prismamc.commons.bukkit.api.title.TitleAPI;
import com.br.gabrielsilva.prismamc.commons.bukkit.custom.events.PlayerGriefEvent;
import com.br.gabrielsilva.prismamc.commons.bukkit.custom.events.ScoreboardChangeEvent;
import com.br.gabrielsilva.prismamc.commons.bukkit.custom.events.ScoreboardChangeEvent.ChangeType;
import com.br.gabrielsilva.prismamc.commons.bukkit.custom.events.UpdateEvent;
import com.br.gabrielsilva.prismamc.commons.bukkit.custom.events.UpdateEvent.UpdateType;
import com.br.gabrielsilva.prismamc.commons.bukkit.utils.BungeeUtils;
import com.br.gabrielsilva.prismamc.commons.core.Core;
import com.br.gabrielsilva.prismamc.lobby.Lobby;
import com.br.gabrielsilva.prismamc.lobby.commands.ServerCommand;

public class GeneralListeners implements Listener {
	@EventHandler
	public void openGuis(PlayerInteractEvent event) {
		if (event.getAction() == Action.PHYSICAL) {
			event.setCancelled(true);
			return;
		}
		if (!event.getAction().name().contains("RIGHT")) {
			return;
		}
		
		Player player = event.getPlayer();

		if (player.getItemInHand().getType().equals(Material.COMPASS)) {
			Lobby.getManager().getInventoryManager().getGamesInventory().open(player);
		} else if (player.getItemInHand().getType().equals(Material.SKULL_ITEM)) {
			player.performCommand("acc");
		}
	}
	
	@EventHandler
	public void onGrief(PlayerGriefEvent event) {
		event.setCancelled(false);
	}
	
	@EventHandler
	public void update(UpdateEvent event) {
		if (event.getType() != UpdateType.SEGUNDO) {
			return;
		}
		
		Core.getServersHandler().updateAllServers();
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		player.updateInventory();
		
		player.teleport(Lobby.getSpawn());
		
		Lobby.getManager().getScoreboardManager().createSideBar(player);
		
		TitleAPI.enviarTitulos(player, "§b§lKOMBO", "§fServidores de Minecraft", 1, 3, 5);
        BossBarAPI.sendBossBar(player, "Acesse nossa §6§lLOJA!");
		
		player.getInventory().setItem(3, 
				new ItemBuilder().material(Material.COMPASS).name("§eGames").build());
		
		player.getInventory().setItem(5, 
				new ItemBuilder().material(Material.SKULL_ITEM).durability(3).headName(player.getName()).name("§ePerfil").build());
		
		if (!player.getGameMode().equals(GameMode.ADVENTURE)) {
			player.setGameMode(GameMode.ADVENTURE);
		}
		
        Lobby.runLater(() -> {
        	if (player.isOnline()) {
        		BukkitPlayer bukkitPlayer = BukkitMain.getManager().getDataManager().getBukkitPlayer(player.getUniqueId());
        		
        		if (bukkitPlayer.getData(DataType.GRUPO).getGrupo().getNivel() > Groups.MEMBRO.getNivel()) {
        			player.setAllowFlight(true);
        			player.setFlying(true);
        		}
        		
	        	if (bukkitPlayer.getDataHandler().categoryHasCache(DataCategory.PRISMA_PLAYER)) {
	        		bukkitPlayer.getDataHandler().load(DataCategory.PRISMA_PLAYER);
	        	} else {
	        		bukkitPlayer.getDataHandler().sendCache(DataCategory.PRISMA_PLAYER);
	        	}
        		Lobby.getManager().getScoreboardManager().updatePosition(player);
        	}
        }, 20L);
	}
	
	@EventHandler
	public void onChangeScoreboard(ScoreboardChangeEvent event) {
		if (event.getChangeType() == ChangeType.DESATIVOU) {
			event.setCancelled(true);
		} else {
			Lobby.getManager().getScoreboardManager().createSideBar(event.getPlayer());
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		final UUID uuid = event.getPlayer().getUniqueId();
		if (ServerCommand.autorizados.contains(uuid)) {
			ServerCommand.autorizados.remove(uuid);
		}
	}
	
	@EventHandler
	public void spread(BlockSpreadEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onFood(FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onChuva(WeatherChangeEvent event) {
		if (event.toWeatherState()) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		event.setCancelled(true);
		if (event.getCause().equals(DamageCause.VOID)) {
			event.getEntity().teleport(Lobby.getSpawn());
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void ignite(BlockIgniteEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onEntitySpawn(CreatureSpawnEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void drop(PlayerDropItemEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void pickup(PlayerPickupItemEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void spawn(ItemSpawnEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onBreak(BlockBreakEvent event) {
		if ((event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) && (ServerCommand.autorizados.contains(event.getPlayer().getUniqueId()))) {
			event.setCancelled(false);
		} else {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		if ((event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) && (ServerCommand.autorizados.contains(event.getPlayer().getUniqueId()))) {
			event.setCancelled(false);
		} else {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void Interact(PlayerInteractEvent event) {
		if ((event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) && (ServerCommand.autorizados.contains(event.getPlayer().getUniqueId()))) {
			event.setCancelled(false);
		} else {
			event.setCancelled(true);
		}
	}

	public static void performInteract(Player player, String name) {
		if (name.equalsIgnoreCase("Hardcore-Games")) {
			Lobby.getManager().getInventoryManager().getHungerGamesInventory().open(player);
		} else if (name.equalsIgnoreCase("KitPvP")) {
			if (!Core.getServersHandler().getNetworkServer("kitpvp").isOnline()) {
				player.sendMessage("§cEste servidor nao esta online.");
				return;
			}
			player.sendMessage("§aConectando...");
			BungeeUtils.redirecionar(player, "KitPvP");
		} else if (name.equalsIgnoreCase("Gladiator")) {
			if (!Core.getServersHandler().getNetworkServer("gladiator").isOnline()) {
				player.sendMessage("§cEste servidor nao esta online.");
				return;
			}
			player.sendMessage("§aConectando...");
			BungeeUtils.redirecionar(player, "Gladiator");
		}
	}
}