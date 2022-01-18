package com.br.gabrielsilva.prismamc.lobby.manager.inventory;

import com.br.gabrielsilva.prismamc.lobby.manager.inventory.inventorys.GamesInventory;
import com.br.gabrielsilva.prismamc.lobby.manager.inventory.inventorys.HungerGamesInventory;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InventoryManager {

	private GamesInventory gamesInventory;
	private HungerGamesInventory hungerGamesInventory;
	
	public void init() {
		setGamesInventory(new GamesInventory("Games", 3));
		setHungerGamesInventory(new HungerGamesInventory("Salas", 4));
	}
}