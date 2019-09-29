package com.dont.kits.bukkit;

import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.dont.kits.Main;
import com.dont.kits.database.DataManager;
import com.dont.kits.models.User;
import com.dont.kits.models.kits.CategoryHolder;
import com.dont.kits.models.kits.Kit;
import com.dont.kits.models.kits.KitCategory;
import com.dont.kits.models.kits.KitsManager;
import com.dont.kits.utils.ItemBuilder;
import com.dont.kits.utils.Time;

public class KitCommand implements Listener, CommandExecutor {

    private final Main main;
    private final DataManager manager;

    public KitCommand(Main main) {
	this.main = main;
	this.manager = main.getDataManager();
	Bukkit.getPluginManager().registerEvents(this, main);
	main.getCommand("kit").setExecutor(this);
	main.debug(this.getClass().getSimpleName() + " carregado");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
	if (sender instanceof Player) {
	    Player player = (Player) sender;
	    player.openInventory(main.getKitManager().getInventory());
	    player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
	}
	return false;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
	if (e.getCurrentItem() == null || e.getSlotType().equals(SlotType.OUTSIDE))
	    return;
	if (e.getInventory().getName().equals(main.getKitManager().getInventory().getName())) {
	    e.setCancelled(true);
	    Player player = (Player) e.getWhoClicked();
	    if (main.getKitManager().getCategories().containsKey(e.getSlot())) {
		player.closeInventory();
		KitCategory category = main.getKitManager().getCategories().get(e.getSlot());
		User user = (User) manager.cache.get(player.getName());
		user.openCategory((Player) player, category);
	    }
	} else if (e.getInventory().getHolder() instanceof CategoryHolder) {
	    e.setCancelled(true);
	    Player player = (Player) e.getWhoClicked();
	    CategoryHolder holder = (CategoryHolder) e.getInventory().getHolder();
	    KitCategory category = holder.getCategory();
	    User user = (User) manager.cache.get(player.getName());
	    if (e.getSlot() == e.getInventory().getSize() - 1) {
		player.closeInventory();
		if (holder.isPreview())
		    user.openCategory(player, category);
		else
		    player.openInventory(main.getKitManager().getInventory());
	    } else if (category.getKits().containsKey(e.getSlot()) && !holder.isPreview()) {
		Kit kit = category.getKits().get(e.getSlot());
		boolean hasPerm = player.hasPermission(kit.getPermissao());
		boolean isOnDelay = user.isOnDelay(kit);
		if (e.isLeftClick()) {
		    if (!hasPerm) {
			player.sendMessage("§eVocê não tem permissão para isso!");
		    } else {
			if (isOnDelay) {
			    long remaining = user.getRemainingDelay(kit);
			    player.sendMessage(
				    "§eVocê só poderá pegar este kit em: §f" + Time.getRemainingTime(remaining / 1000));
			} else {
			    user.putDelay(kit);
			    player.closeInventory();
			    user.openCategory((Player) player, category);
			    ((Player) player).playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
			    for (ItemStack item : kit.getItems()) {
				if (item == null)
				    continue;
				player.getInventory().addItem(item);
			    }
			}
		    }
		} else {
		    player.closeInventory();
		    Inventory inventory = Bukkit.createInventory(new CategoryHolder(category, true), 54,
			    "Kit " + kit.getNome());
		    int lastIndex = 0;

		    for (int i = 0; i < 54; i++) {
			if (i < 9 || i == 17 || i == 26 || i == 35 || i == 44 || i == 53 || i % 9 == 0)
			    continue;
			if (lastIndex >= kit.getItems().size())
			    break;
			inventory.setItem(i, kit.getItems().get(lastIndex));
			lastIndex++;
		    }
		    inventory.setItem(inventory.getSize() - 1, KitsManager.backItem);
		    player.openInventory(inventory);
		}
	    }
	}
    }

}
