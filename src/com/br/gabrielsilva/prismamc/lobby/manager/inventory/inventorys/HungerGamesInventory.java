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
import com.br.gabrielsilva.prismamc.commons.bukkit.utils.BungeeUtils;
import com.br.gabrielsilva.prismamc.commons.core.Core;
import com.br.gabrielsilva.prismamc.commons.core.server.types.HungerGamesServer;
import com.br.gabrielsilva.prismamc.commons.core.server.types.Stages;
import com.br.gabrielsilva.prismamc.commons.core.utils.system.DateUtils;

public class HungerGamesInventory extends MenuInventory {

	private MenuClickHandler menuClickHandler;
	
	public HungerGamesInventory(String title, int rows) {
		super(title, rows);
		
		updateInventory();
		
		this.menuClickHandler = new MenuClickHandler() {
			 
			public void onClick(Player player, Inventory inv, ClickType type, ItemStack stack, int slot) {
				int salaID = Integer.valueOf(stack.getItemMeta().getDisplayName().split("#")[1]);
				
				 player.closeInventory();
				 
				 HungerGamesServer hungerGamesServer = Core.getServersHandler().getHungerGamesServer("hg" + salaID);
				 
				 if (!hungerGamesServer.isOnline()) {
					 player.sendMessage("§cEsta sala está offline.");
					 return;
				 }
				 if (hungerGamesServer.getEstagio() == Stages.FIM || hungerGamesServer.getEstagio() == Stages.CARREGANDO) {
					 if (hungerGamesServer.getEstagio() == Stages.FIM) {
						 player.sendMessage("§cO jogo está sendo encerrado.");
					 } else {
						 player.sendMessage("§cO servidor está carregando.");
					 }
					 return;
				 }
				 boolean continuar = true;
				 if (hungerGamesServer.getVivos() >= 80) {
					 if (!player.hasPermission("hungergames.entrar")) {
						 player.sendMessage("§cOs Slots para membros acabaram, compre VIP e tenha slot reservado.");
						 continuar = false;
					 }
				 }
				 if (!continuar) {
					 return;
				 }
				 player.sendMessage("§aConectando...");
				 BungeeUtils.redirecionar(player, "HG" + salaID);
			}
		};
		
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
		int ID = 11;
		
		for (int i = 1; i <= Core.getServersHandler().getSalasHG(); i++) {
			 if (ID == 16) {
				 ID = 20;
			 }
			 HungerGamesServer hgs = Core.getServersHandler().getHungerGamesServer("hg" + i);
			 int durability = 8;
			 String[] lore = null;
			 
			 if (!hgs.isOnline()) {
				 durability = 8;
				 lore = new String[] {
				 "§8Sala offline."};
			 } else {
				 if (hgs.getEstagio() == Stages.OFFLINE) {
					 durability = 8;
					 lore = new String[] {
					 "§8Sala offline."};
				 } else if (hgs.getEstagio() == Stages.CARREGANDO) {
					 durability = 14;
					 lore = new String[] {
					 "§8Sala carregando."};
				 } else if (hgs.getEstagio() == Stages.FIM) {
					 durability = 8;
					 lore = new String[] {
					 "§8Sala em encerramento."};
				 } else if (hgs.getEstagio() == Stages.PREJOGO) {
					 durability = 10;
					 lore = new String[] {
					 "",
					 "§fIniciando em: §a" + DateUtils.formatarSegundos2(hgs.getTempo()),
					 "§fJogadores: §a" + hgs.getVivos(),
					 ""};
				 } else if (hgs.getEstagio() == Stages.INVENCIBILIDADE) {
					 durability = 11;
					 lore = new String[] {
					 "",
					 "§fInvencibilidade acaba em: §e" + DateUtils.formatarSegundos2(hgs.getTempo()),
					 "§fJogadores: §e" + hgs.getVivos(),
					 ""};
				 } else if (hgs.getEstagio() == Stages.JOGANDO) {
					 durability = 1;
					 lore = new String[] {
					 "",
					 "§fTempo: §c" + DateUtils.formatarSegundos2(hgs.getTempo()),
					 "§fJogadores: §c" + hgs.getVivos(),
					 ""};
				 }
			 }
			 setItem(ID, new ItemBuilder().material(Material.INK_SACK).durability(durability).name("§aSala #" + hgs.getServerID()).lore(lore).
				amount(hgs.getServerID()).build(), menuClickHandler);
			 ID++;
		}
	}
}
