package com.dont.kits.models.kits;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.dont.kits.utils.ItemBuilder;

public class KitCategory {

    private String nome;
    private ItemStack icone;
    private int slot, size;
    private HashMap<Integer, Kit> kits;
    //private final Inventory inventory;
    public KitCategory(String nome, ItemStack icone, int slot, int size, HashMap<Integer, Kit> kits) {
	super();
	this.nome = nome;
	this.icone = icone;
	this.slot = slot;
	this.size = size;
	this.kits = kits;
	/*inventory = Bukkit.createInventory(new CategoryHolder(this), size, nome);
	for (Entry<Integer, Kit> entry : kits.entrySet()) {
	    inventory.setItem(entry.getKey(), entry.getValue().getIcone());
	}
	inventory.setItem(size-1, KitsManager.backItem);*/
    }
    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }
    /**
     * @return the icone
     */
    public ItemStack getIcone() {
        return icone;
    }
    /**
     * @return the slot
     */
    public int getSlot() {
        return slot;
    }
    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }
    /**
     * @return the kits
     */
    public HashMap<Integer, Kit> getKits() {
        return kits;
    }
    @Override
    public String toString() {
	return "KitCategory [nome=" + nome + ", icone=" + icone + ", slot=" + slot + ", size=" + size + ", kits=" + kits
		+ "]";
    }
    /**
     * @return the inventory
     */
 /*   public Inventory getInventory() {
        return inventory;
    }
   */ 
    
    
}
