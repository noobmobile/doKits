package com.dont.kits.models.kits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale.Category;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import com.dont.kits.Main;
import com.dont.kits.utils.ItemBuilder;
import com.dont.kits.utils.ItemSerializer;

public class KitsManager {

    private final HashMap<String, Kit> kits;
    private final HashMap<Integer, KitCategory> categories;
    private final Inventory inventory;
    public static ItemStack backItem;

    public KitsManager(Main main) {
	String name = main.getConfig().getString("MenuKits.Nome");
	int size = main.getConfig().getInt("MenuKits.Tamanho");
	backItem = new ItemBuilder(Material.valueOf(main.getConfig().getString("Voltar.Material")))
		.setName(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("Voltar.Nome")))
		.toItemStack();
	inventory = Bukkit.createInventory(null, size, name);
	kits = new HashMap<>();
	categories = new HashMap<>();
	for (String key : main.getConfig().getConfigurationSection("Kits").getKeys(false)) {
	    String mkey = "Kits." + key + ".";

	    String permission = main.getConfig().getString(mkey + "Permissao");
	    long delay = main.getConfig().getLong(mkey + "Delay");
	    String itemsBase64 = main.getConfig().getString(mkey + "Itens");
	    ItemStack icon = new ItemBuilder(Material.valueOf(main.getConfig().getString(mkey + "Icone.Material")))
		    .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
		    .setName(ChatColor.translateAlternateColorCodes('&',
			    main.getConfig().getString(mkey + "Icone.Nome")))
		    .setLore(main.getConfig().getStringList(mkey + "Icone.Lore")).toItemStack();
	    List<ItemStack> items = new ArrayList<>();
	    if (itemsBase64.equals("nulo"))
		itemsBase64 = ItemSerializer.itemStackArrayToBase64(new ItemStack[] { new ItemStack(Material.APPLE) });
	    try {
		for (ItemStack it : ItemSerializer.itemStackArrayFromBase64(itemsBase64))
		    items.add(it);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    Kit kit = new Kit(key, permission, icon, delay, items);
	    kits.put(key, kit);
	}

	for (String key : main.getConfig().getConfigurationSection("Categorias").getKeys(false)) {
	    String mkey = "Categorias." + key + ".";
	    ItemStack icon = new ItemBuilder(Material.valueOf(main.getConfig().getString(mkey + "Icone.Material")))
		    .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
		    .setName(ChatColor.translateAlternateColorCodes('&',
			    main.getConfig().getString(mkey + "Icone.Nome")))
		    .setLore(main.getConfig().getStringList(mkey + "Icone.Lore")).toItemStack();
	    int tamanho = main.getConfig().getInt(mkey + "Tamanho");
	    int slot = main.getConfig().getInt(mkey + "Slot");
	    HashMap<Integer, Kit> kits = new HashMap<>();
	    for (String kit : main.getConfig().getStringList(mkey + "Kits")) {
		String[] array = kit.split(":");
		kits.put(Integer.valueOf(array[0]), this.kits.get(array[1]));
	    }
	    KitCategory category = new KitCategory(key, icon, slot, tamanho, kits);
	    categories.put(slot, category);
	    inventory.setItem(slot, icon);
	}

    }

    /**
     * @return the inventory
     */
    public Inventory getInventory() {
	return inventory;
    }

    /**
     * @return the kits
     */
    
    @Deprecated
    public Optional<KitCategory> getCategoryByName(String name) {
	return categories.values().stream().filter(c -> c.getNome().equals(name)).findFirst();
    }
    
    public HashMap<String, Kit> getKits() {
	return kits;
    }

    /**
     * @return the categories
     */
    public HashMap<Integer, KitCategory> getCategories() {
	return categories;
    }

}
