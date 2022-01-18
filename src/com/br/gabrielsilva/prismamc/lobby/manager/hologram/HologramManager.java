package com.br.gabrielsilva.prismamc.lobby.manager.hologram;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.br.gabrielsilva.prismamc.commons.bukkit.custom.events.UpdateEvent;
import com.br.gabrielsilva.prismamc.commons.bukkit.custom.events.UpdateEvent.UpdateType;
import com.br.gabrielsilva.prismamc.commons.bukkit.hologram.types.ClanHologram;
import com.br.gabrielsilva.prismamc.commons.bukkit.hologram.types.PlayerRankingHologram;
import com.br.gabrielsilva.prismamc.lobby.Lobby;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HologramManager {

	private PlayerRankingHologram hologram;
	private ClanHologram clanHologram;
	
	public void init() {
		setHologram(new PlayerRankingHologram(Lobby.getInstance(), "ranking"));
		setClanHologram(new ClanHologram(Lobby.getInstance(), "clans"));

		getClanHologram().create();
		getHologram().create();
		
		registerListener();
	}

	public void registerListener() {
		Bukkit.getServer().getPluginManager().registerEvents(new Listener() {
			
			int minutos = 0;
			
			@EventHandler
			public void updateEvent(UpdateEvent event) {
				if (event.getType() != UpdateType.MINUTO) {
					return;
				}
				
				minutos++;
				
				if (minutos == 10) {
					synchronized(this) {
						getClanHologram().updateValues();
						getHologram().updateValues();
					}
					
					minutos = 0;
				}
			}
		}, Lobby.getInstance());
	}
}