package com.dont.kits.models;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.dont.kits.models.kits.CategoryHolder;
import com.dont.kits.models.kits.Kit;
import com.dont.kits.models.kits.KitCategory;
import com.dont.kits.models.kits.KitsManager;
import com.dont.kits.utils.ItemBuilder;
import com.dont.kits.utils.Time;

public class User implements Storable{

    private String name;
    private HashMap<String, Long> onDelay;
    public User(String name, HashMap<String, Long> onDelay) {
	super();
	this.name = name;
	this.onDelay = onDelay;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @return the onDelay
     */

    public boolean isOnDelay(Kit kit) {
	return System.currentTimeMillis() < (onDelay.getOrDefault(kit.getNome(), 0l) + kit.getDelay());
    }
    
    public long getRemainingDelay(Kit kit) {
	return (onDelay.getOrDefault(kit.getNome(), 0l) + kit.getDelay()) - System.currentTimeMillis();
    }

    public void putDelay(Kit kit) {
	onDelay.put(kit.getNome(), System.currentTimeMillis());
    }
    
    public void openCategory(Player player, KitCategory category) {
	Inventory inventory = Bukkit.createInventory(new CategoryHolder(category, false), category.getSize(),
		category.getNome());
	for (Entry<Integer, Kit> entry : category.getKits().entrySet()) {
	    ItemBuilder clone = new ItemBuilder(entry.getValue().getIcone().clone());
	    boolean hasPerm = player.hasPermission(entry.getValue().getPermissao());
	    boolean isOnDelay = isOnDelay(entry.getValue());

	    if (!hasPerm) {
		clone.addLoreLine("§cVocê não tem permissão para pegar este kit!");
	    } else {
		if (isOnDelay) {
		    long remaining = getRemainingDelay(entry.getValue());
		    clone.addLoreLine("§7Você pode pegar este kit em: §f" + Time.getRemainingTime(remaining / 1000));
		} else {
		    clone.addLoreLine("§7Você pode pegar este kit agora mesmo!");
		}
	    }

	    inventory.setItem(entry.getKey(), clone.toItemStack());
	}
	inventory.setItem(category.getSize() - 1, KitsManager.backItem);
	player.openInventory(inventory);
    }
    
}
