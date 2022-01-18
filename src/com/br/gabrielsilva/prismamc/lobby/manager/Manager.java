package com.br.gabrielsilva.prismamc.lobby.manager;

import com.br.gabrielsilva.prismamc.lobby.manager.hologram.HologramManager;
import com.br.gabrielsilva.prismamc.lobby.manager.inventory.InventoryManager;
import com.br.gabrielsilva.prismamc.lobby.manager.scoreboard.ScoreBoardManager;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Manager {

	private InventoryManager inventoryManager;
	private ScoreBoardManager scoreboardManager;
	private HologramManager hologramManager;
	
	public Manager() {
		setInventoryManager(new InventoryManager());
		getInventoryManager().init();
		
		setScoreboardManager(new ScoreBoardManager());
		getScoreboardManager().init();
		
		setHologramManager(new HologramManager());
		getHologramManager().init();
	}
}