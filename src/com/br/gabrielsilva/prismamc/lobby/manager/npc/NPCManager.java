package com.br.gabrielsilva.prismamc.lobby.manager.npc;


import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.br.gabrielsilva.prismamc.commons.core.Core;
import com.br.gabrielsilva.prismamc.lobby.listener.GeneralListeners;
import com.comphenix.protocol.ProtocolLibrary;

import lombok.Getter;
import net.jitse.npclib.NPCLib;
import net.jitse.npclib.api.NPC;
import net.jitse.npclib.api.events.NPCInteractEvent;
import net.jitse.npclib.api.skin.Skin;
import net.jitse.npclib.api.state.NPCSlot;
import protocolsupport.ProtocolSupport;
import protocolsupport.api.ProtocolSupportAPI;

@Getter
public class NPCManager implements Listener {
	
	private JavaPlugin plugin;
	private NPCLib npcLib;
	private Location location;
	
	private NPC hgNPC;
	private NPC pvpNPC;
	private NPC gladiatorNPC;
	
	
	public NPCManager(JavaPlugin plugin) {
		this.plugin = plugin;
		
		npcLib = new NPCLib(plugin);
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
		
		/*
		 * Hardcore Games NPC - Configuration
		 */
		
		location = new Location(Bukkit.getWorld("world"), 11, 71, 25, -179, 0);
		hgNPC = npcLib.createNPC(Arrays.asList("§6§lCLIQUE PARA JOGAR!", 
				"§bHardcore Games", "§e" + Core.getServersHandler().getAmountHungerGames() + " jogadores."));
		hgNPC.setSkin(new Skin("ewogICJ0aW1lc3RhbXAiIDogMTY0MTQyMjkyOTQxMSwKICAicHJvZmlsZUlkIiA6ICJmODA0ZWQzMTU3NGY0MTE4Yjc5YWJmYzI5ZmRlMmQ4ZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJwaXRlcmx1Y2FzIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzQ1OWRhOGFlZTcxNTlhY2VlMmYyYjU0OTQ3YWNlZjhkMmU3MmRmMjA0NTM3YjM4MTU0M2U4OWJjY2Q1N2VlMyIKICAgIH0KICB9Cn0=", 
				"BiVQce6j8xykFcBy1TF9uVRFbCqWKyiKgPgnaYiDvKTjmeCVFX5Konk2VWshwMUYR66k5GD6F6hSACyhTUDyl03PtA+X9q1T7w0lXt0RTizRSHVNJG188UE/+isZdufs1hRbYYIljQ2pWdVswdvnOlMzBtplqSoH89Ysc/rU67u0+XKaNeeDF15+bsd9axmGyxeCTzOZLxobcdBy6DcuXLGXIYNX3wQHrPVnCmF4+UkeZjySTLVc/hNHuUqrErBFEu45yUa6G49sJ60mFstliKSvnuFxaC7eufF76/h+UT/3UWnc126VQa8vOkM0rzGUE867rSirOo1YIXeYxSMhm+7kGLj0v7FuRuuizD0jI1UCaBRVKfSB2WkF5LYZ3HxxuOiBOL/TXG3ygvE6Bd0IpNb+td52TSVLuvxep8MPHuCEYi3PUUxN3NirUWUgtGEYQPADUHm0xgJWIiNGpu+I32Ugy66Op4YLGe2MIni450KRnZ8wamqIiepRLNYPbZuwkhdzg/H64y6Ji0dP2ztsXPBCglIyX/XWL9FhVSawOsAis4QDVJibLsJVK/h1NEYaIqdRBThnh+NB74ghJRd01ZttRs45+MGTLalRqvpnesj++xfblHS746P8CC51yTAUcgXhO5DBCOGUELJ/Okj46h++x/a/9TDHJKuncLiFhMc="));
		hgNPC.setItem(NPCSlot.MAINHAND, new ItemStack(Material.MUSHROOM_SOUP));
		hgNPC.setLocation(location);
		hgNPC.create();
		
		/*
		 * PvP NPC - Configuration
		 */
		
		location = new Location(Bukkit.getWorld("world"), 4, 71, 25, -179, 0);
		pvpNPC = npcLib.createNPC(Arrays.asList("§6§lCLIQUE PARA JOGAR!", 
				"§bKitPvP", "§e" + Core.getServersHandler().getNetworkServer("kitpvp").getOnlines() + " jogadores."));
		pvpNPC.setSkin(new Skin("ewogICJ0aW1lc3RhbXAiIDogMTY0MTQyNDI4MzE3NywKICAicHJvZmlsZUlkIiA6ICI3ZGE3MTNjOWIxZDY0NmFkODRiMjk0OGEyZmM5MDViMiIsCiAgInByb2ZpbGVOYW1lIiA6ICJNdXNocm9vbSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8zNzg1MWI1ZTQwNmMzMTcwZTFmODhiOWM4MDA0OTA2NzZmOGM0MWYyODUxNmMwNWFlZThkYjJkNmQzOWJhODNmIgogICAgfQogIH0KfQ==", 
				"b98+DfcChgOyOVM0ddt+Z7vftrS4e0NHXFXcVMMfYPIRbhzGDViY4SQC0fOyLURpcE0upCyfOWvIihhXiW9/PMEDPLNPom6PNzyzLRqR88/WQxEKknCeeaVsF9dcWqszxjga2UIIN1iDri0hoMDLGl9TMLsmUBkrvhDIbld6KY8cZzNGlq7d2c0jOZL9r0bKPVfaaNvlnpbmi0hN61mbtSL3Cclm5r8BSkelf42b8mGPTt91s+wvifHZTuaxrSZiVtD4w8FCYw4Vl322dz9NSPn1hgm7eQpg/3MqKP4QwGQ8EQS6yrtKjZYx0r6cMgusSQS0jfNNWtC+h0Llu4iQeia6b/J/M3Tts8VRCrpNNjMTVhxSn1NKCkmR5O7yAlT6Y1x6rLSgjvYDBdUSIAqms4XUCBOEYMjEH4NYXkM7rXyq9Tg9tWfIlF97UXwpAxAlNGY+36sFLTiIuQq/xQQVabUr4U/GoUf46+JgglvI0yJH81G2Il2fFFFKTuqZeAyo3i8SPQ3o2bXmnhuTrMzzn1PKQwcmh9MQAdiKB+Y6lASuYy1oBA0Ycv3pJTLNrSMHUKBwOz6kq/2dyYEXiSE/GZndDSFZUjJYNGoJfIVHCGMmGS8rZdBPDSFCcydXceESJnkdaWdT+0uCzR7U9b6rGpQ0b2bNnbVwr+YyZl4CE+U="));
		pvpNPC.setItem(NPCSlot.MAINHAND, new ItemStack(Material.DIAMOND_SWORD));
		pvpNPC.setLocation(location);
		pvpNPC.create();
		
		/*
		 * Gladiator NPC - Configuration
		 */
		
		location = new Location(Bukkit.getWorld("world"), -4, 71, 26.628, -179, 0);
 		gladiatorNPC = npcLib.createNPC(Arrays.asList("§6§lCLIQUE PARA JOGAR!", 
				"§bGladiator", "§e" + Core.getServersHandler().getAmountHungerGames() + " jogadores."));
		gladiatorNPC.setSkin(new Skin("ewogICJ0aW1lc3RhbXAiIDogMTY0MTQyNDQwMzE5MSwKICAicHJvZmlsZUlkIiA6ICI4N2M3YWUzN2IyZDQ0NmFmOTFmMDJhZmU2MjhmOTBiZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJTaWxlbnRfTXVzaHJvb20iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmM3MGE3N2ZiMGQ4YTM1NTI0NGVhMzZlOGE2NDcxZGIwMTNhMjQ2ZmE2OTY0MjhkNDUwMzNlNDI0ZjA2NDY1OCIKICAgIH0KICB9Cn0=", 
				"hE+bYsrqoYgveph92mlQWUgWHJeaJGK/0Z8T5xZxjKPi7NfOcFiFhW+k7HjQ89bA8XKKf+4l/CqKw0/Q8x8ga7AptTNzGtVt0OQzjR2UaF03KkD75bYTWj7l858pbdBKBe5Frcopa/M8PLIS930d/dx6vJdyiiZEgO8noCcXwyFgKbf5d+eGVk0fjlRtcgLRf3/51HESAAE41ovF6uPSyFoxmVYV9mykJIY7X9bvzjp1ImJndJzFBKRRzGeSafOQ2iGMKn6GjdcQ4Kjwg37PsM+pNMbzW92NtD5OwXvdOBFMEipStIlYHOAlN4cLl5P0HKf7ychGoI1fI2mqZQTFtAAe8t1Z6lcOYI5eTJ2r1Lq+Yw2EQ3v6A8TXg+4qd2YSgfA1u+q2D9ktzhIksVk50cT6QmkN1G0xKigwZCMPwIDQVOE0nUEmfwKdcSirSkMbuKouANKEVoccvhTwF0/7FtInHWS85rfEvzB4sFB/Q2VWSFBZjI4lH02TBJ8db8DLm5hWF9Sm2Yr1JaLT+o/CBk3G0btkC60XNnkn5D2kmlYCpwvn2h7WOOPBveiauhXHF9fkz6TvvLa9EuCE23+pfljVUWFDSY/eXdD11ha71V6u0KormD7Dl98pdWjZnmzACuzO0Bj0v+cCQiERRg9lSZ07XufDVA2UmvcgOwDFtX0="));
		gladiatorNPC.setItem(NPCSlot.MAINHAND, new ItemStack(Material.IRON_FENCE));
		gladiatorNPC.setLocation(location);
		gladiatorNPC.create();
		
		new BukkitRunnable() {
			@Override
			public void run() {
				update();
			}
		}.runTaskTimer(plugin, 20L, 20L);
	}
	
	public void update() {
		hgNPC.setText(Arrays.asList("§6§lCLIQUE PARA JOGAR!", 
				"§bHardcore Games", "§e" + Core.getServersHandler().getAmountHungerGames() + " jogadores."));
		pvpNPC.setText(Arrays.asList("§6§lCLIQUE PARA JOGAR!", 
				"§bKitPvP", "§e" + Core.getServersHandler().getNetworkServer("kitpvp").getOnlines() + " jogadores."));
		gladiatorNPC.setText(Arrays.asList("§6§lCLIQUE PARA JOGAR!", 
				"§bGladiator", "§e" + Core.getServersHandler().getNetworkServer("gladiator").getOnlines() + " jogadores."));
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if (ProtocolSupportAPI.getProtocolVersion(event.getPlayer()).getId() < 47)
			return;
	
		new BukkitRunnable() {
			@Override
			public void run() {
				getHgNPC().show(event.getPlayer());
				getPvpNPC().show(event.getPlayer());
				getGladiatorNPC().show(event.getPlayer());
			}
		}.runTaskLater(plugin, 10L);
	}
	
	@EventHandler
	public void onClick(NPCInteractEvent event) {
		NPC npc = event.getNPC();
		Player player = event.getWhoClicked();
		if (npc.equals(hgNPC)) {
			GeneralListeners.performInteract(player, "Hardcore-Games");
		} else if (npc.equals(pvpNPC)) {
			GeneralListeners.performInteract(player, "KitPvP");
		} else if (npc.equals(gladiatorNPC)) {
			GeneralListeners.performInteract(player, "Gladiator");
		}
	}

}
