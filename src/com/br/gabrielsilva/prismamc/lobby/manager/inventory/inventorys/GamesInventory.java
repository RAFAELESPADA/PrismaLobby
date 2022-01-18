package com.br.gabrielsilva.prismamc.lobby.manager.inventory.inventorys;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.br.gabrielsilva.prismamc.commons.bukkit.api.itembuilder.ItemBuilder;
import com.br.gabrielsilva.prismamc.commons.bukkit.api.menu.ClickType;
import com.br.gabrielsilva.prismamc.commons.bukkit.api.menu.MenuClickHandler;
import com.br.gabrielsilva.prismamc.commons.bukkit.api.menu.MenuInventory;
import com.br.gabrielsilva.prismamc.commons.bukkit.api.menu.MenuUpdateHandler;
import com.br.gabrielsilva.prismamc.commons.core.Core;
import com.br.gabrielsilva.prismamc.lobby.listener.GeneralListeners;

public class GamesInventory extends MenuInventory {

	public GamesInventory(String title, int rows) {
		super(title, rows);
		
		updateInventory();
		
		setUpdateHandler(new MenuUpdateHandler() {
			public void onUpdate(Player player, MenuInventory arg1) {
				updateInventory();
			}
		});
	}
	
	public void open(Player p) {
		updateInventory();
		
		super.open(p);
	}
	
	private void updateInventory() {
		setItem(11, new ItemBuilder().material(Material.MUSHROOM_SOUP).name("§9§lHARDCORE-GAMES").lore(new String[] {
		"",
		"§7Salas de Hardcore-Games",
		"§7Um battle royale onde s§ um sai campeão!",
		"§7Venha jogar nosso §6§lSINGLE-KIT",
		"",
		"§fJogadores online: §b" + Core.getServersHandler().getAmountHungerGames(),
		""}).build(), new MenuClickHandler() {
			public void onClick(Player player, Inventory inv, ClickType type, ItemStack stack, int slot) {
				player.closeInventory();
				
				GeneralListeners.performInteract(player, "Hardcore-Games");
			}
		});
		
		setItem(13, new ItemBuilder().material(Material.DIAMOND_SWORD).name("§9§lKitPvP").lore(new String[] {
		"",
		"§7Servidor de KitPvP com sopa e sem armadura.",
		"§7Em um estilo mais Hardcore, simulando um HG",
		"",
		"§fJogadores online: §b" + Core.getServersHandler().getNetworkServer("kitpvp").getOnlines(),
		""}).build(), new MenuClickHandler() {
			public void onClick(Player player, Inventory inv, ClickType type, ItemStack stack, int slot) {
				player.closeInventory();
				
				GeneralListeners.performInteract(player, "KitPvP");
			}
		});
		
		setItem(15, new ItemBuilder().material(Material.IRON_FENCE).name("§9§lGLADIATOR").lore(new String[] {
		"",
		"§7Servidor de Gladiator",
		"§7Treine seu controle de inventario neste modo",
		"",
		"§fJogadores online: §b" + Core.getServersHandler().getNetworkServer("gladiator").getOnlines(),
		""}).build(), new MenuClickHandler() {
			public void onClick(Player player, Inventory inv, ClickType type, ItemStack stack, int slot) {
				player.closeInventory();
				
				GeneralListeners.performInteract(player, "Gladiator");
			}
		});
	}
}