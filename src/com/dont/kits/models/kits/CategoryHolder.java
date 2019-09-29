package com.dont.kits.models.kits;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class CategoryHolder implements InventoryHolder{

    private KitCategory category;
    private boolean preview;
    
    public  CategoryHolder(KitCategory category, boolean preview) {
	this.category = category;
	this.preview = preview;
    }
    
    @Override
    public Inventory getInventory() {
	// TODO Auto-generated method stub
	return null;
    }

    /**
     * @return the category
     */
    public KitCategory getCategory() {
        return category;
    }

    /**
     * @return the preview
     */
    public boolean isPreview() {
        return preview;
    }

}
