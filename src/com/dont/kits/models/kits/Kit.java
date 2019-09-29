package com.dont.kits.models.kits;

import java.util.List;

import org.bukkit.inventory.ItemStack;

public class Kit {

    private String nome, permissao;
    private ItemStack icone;
    private long delay;
    private List<ItemStack> items;
    public Kit(String nome, String permissao, ItemStack icone, long delay, List<ItemStack> items) {
	super();
	this.nome = nome;
	this.permissao = permissao;
	this.icone = icone;
	this.delay = delay;
	this.items = items;
    }
    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }
    /**
     * @return the permissao
     */
    public String getPermissao() {
        return permissao;
    }
    /**
     * @return the icone
     */
    public ItemStack getIcone() {
        return icone;
    }
    /**
     * @return the delay
     */
    public long getDelay() {
        return delay;
    }
    /**
     * @return the items
     */
    public List<ItemStack> getItems() {
        return items;
    }
    @Override
    public String toString() {
	return "Kit [nome=" + nome + ", permissao=" + permissao + ", icone=" + icone + ", delay=" + delay + ", items="
		+ items + "]";
    }
    
    
    
}
