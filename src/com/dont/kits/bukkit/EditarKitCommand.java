package com.dont.kits.bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import com.dont.kits.utils.ItemSerializer;
import com.dont.kits.utils.Time;

public class EditarKitCommand implements CommandExecutor {

    private final Main main;
    private final DataManager manager;

    public EditarKitCommand(Main main) {
	this.main = main;
	this.manager = main.getDataManager();
	main.getCommand("editarkit").setExecutor(this);
	main.debug(this.getClass().getSimpleName() + " carregado");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
	if (sender instanceof Player && sender.hasPermission("criar.kits")) {
	    Player player = (Player) sender;
	    if (args.length != 1) {
		player.sendMessage("§eUtilize /editarkit <nomeDoKit>");
		player.sendMessage(
			"§eOs itens do kit serão os itens do seu inventário");
		return false;
	    }

	    if (!main.getKitManager().getKits().containsKey(args[0])) {
		player.sendMessage("§eNão existe nenhum kit com esse nome!");
		return false;
	    }

	    String nome = args[0];
	    String items = ItemSerializer.itemStackArrayToBase64(player.getInventory().getContents());
	    
	    main.getConfig().set("Kits."+nome+".Itens", items);
	    main.saveConfig();
	    main.reload();
	    
	    player.sendMessage("§eKit " + nome + " editado com sucesso");
	}
	return false;
    }

}
