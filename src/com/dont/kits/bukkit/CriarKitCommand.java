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

public class CriarKitCommand implements CommandExecutor {

    private final Main main;
    private final DataManager manager;

    public CriarKitCommand(Main main) {
	this.main = main;
	this.manager = main.getDataManager();
	main.getCommand("criarkit").setExecutor(this);
	main.debug(this.getClass().getSimpleName() + " carregado");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
	if (sender instanceof Player && sender.hasPermission("criar.kits")) {
	    Player player = (Player) sender;
	    if (args.length != 5) {
		player.sendMessage(
			"§eUtilize /criarkit <nomeDoKit> <permissao> <delay> <categoria> <slot (0 para automático)>");
		player.sendMessage(
			"§eO ícone que será utilizado será o item na sua mão e os itens do kit serão os itens do seu inventário");
		player.sendMessage("§eMas você poderá alterar através do /editarkit");
		return false;
	    }

	    if (main.getKitManager().getKits().containsKey(args[0])) {
		player.sendMessage("§eJá existe um kit com esse nome!");
		return false;
	    }

	    String nome = args[0];
	    String permissao = args[1];
	    long delay = 0;
	    try {
		delay = Long.valueOf(args[2]);
	    } catch (Exception e) {
		player.sendMessage("§eVocê inseriu um delay inválido");
		return false;
	    }
	    if (player.getInventory().getItemInHand() == null
		    || player.getInventory().getItemInHand().getType().equals(Material.AIR)) {
		player.sendMessage("§eÍcone na mão inválido");
		return false;
	    }

	    Optional<KitCategory> optCategory = main.getKitManager().getCategoryByName(args[3]);
	    if (!optCategory.isPresent()) {
		player.sendMessage("§eVocê inseriu uma categoria inválida");
		return false;
	    }
	    KitCategory category = optCategory.get();

	    int slot = 0;
	    try {
		slot = Integer.valueOf(args[4]);
	    } catch (Exception e) {
		player.sendMessage("§eVocê inseriu um slot inválido");
		return false;
	    }

	    if (slot == 0) {
		for (int i = 0; i < category.getSize(); i++) {
		    if (i < 9 || i == 17 || i == 26 || i == 35 || i == 44 || i == 53 || i % 9 == 0)
			continue;
		    if (category.getKits().containsKey(i))
			continue;
		    slot = i;
		    break;
		}

		int size = 27 + plus(category.getKits().size());
		main.getConfig().set("Categorias." + category.getNome() + ".Tamanho", size);

	    }

	    List<String> categoryKits = main.getConfig().getStringList("Categorias." + category.getNome() + ".Kits");
	    categoryKits.add(slot + ":" + nome);

	    String iconMaterial = player.getInventory().getItemInHand().getType().toString();
	    String iconNome = player.getInventory().getItemInHand().getItemMeta().hasDisplayName()
		    ? player.getInventory().getItemInHand().getItemMeta().getDisplayName().replace("§", "&")
		    : player.getInventory().getItemInHand().getType().name();
	    List<String> iconLore = new ArrayList<>();
	    if (player.getInventory().getItemInHand().getItemMeta().hasLore()) {

		for (String lore : player.getInventory().getItemInHand().getItemMeta().getLore())
		    iconLore.add(lore.replace("§", "&"));
	    } else {
		iconLore.add("nulo");
	    }
	    String itens = ItemSerializer.itemStackArrayToBase64(player.getInventory().getContents());
	    String mkey = "Kits." + nome + ".";
	    main.getConfig().set(mkey + "Permissao", permissao);
	    main.getConfig().set(mkey + "Delay", delay);
	    main.getConfig().set(mkey + "Icone.Material", iconMaterial);
	    main.getConfig().set(mkey + "Icone.Nome", iconNome);
	    main.getConfig().set(mkey + "Icone.Lore", iconLore);
	    main.getConfig().set(mkey + "Itens", itens);
	    main.getConfig().set("Categorias." + category.getNome() + ".Kits", categoryKits);
	    main.saveConfig();
	    main.reload();
	    player.sendMessage("§eKit " + nome + " criado com sucesso");
	}
	return false;
    }

    private int plus(int i) {
	if (i < 7)
	    return 0;
	if (i < 14)
	    return 9;
	if (i < 21)
	    return 18;
	return 27;
    }

}
