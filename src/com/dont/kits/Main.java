package com.dont.kits;

import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.dont.kits.bukkit.CriarKitCommand;
import com.dont.kits.bukkit.EditarKitCommand;
import com.dont.kits.bukkit.KitCommand;
import com.dont.kits.bukkit.PlayerJoinQuit;
import com.dont.kits.database.AutoSave;
import com.dont.kits.database.DataManager;
import com.dont.kits.database.DataSource;
import com.dont.kits.models.Storable;
import com.dont.kits.models.User;
import com.dont.kits.models.kits.KitsManager;

public class Main extends JavaPlugin {

    private DataManager manager;
    private DataSource dataSource;
    private KitsManager kitManager;

    public void onEnable() {
	saveDefaultConfig();
	debug("ativado, by don't");

	this.dataSource = new DataSource();
	manager = new DataManager(dataSource);

	try {
	    dataSource.open();
	} catch (SQLException e) {
	    debug("nao foi possivel ativar o mysql: §f" + e.getMessage());
	    Bukkit.getPluginManager().disablePlugin(this);
	    return;
	}

	for (Player player : Bukkit.getOnlinePlayers()) {
	    if (manager.exists(player.getName())) {
		User user = manager.get(player.getName(), User.class);
		manager.cache.put(player.getName(), user);
		debug("Pegando player " + player.getName() + " do mysql");
	    } else {
		manager.cache.put(player.getName(), new User(player.getName(), new HashMap<>()));
		debug("Criando novo player no mysql " + player.getName());
	    }
	}
	kitManager = new KitsManager(this);
	setup();
    }

    private void setup() {
	new AutoSave(this);
	new PlayerJoinQuit(this);
	new KitCommand(this);
	new EditarKitCommand(this);
	new CriarKitCommand(this);
    }

    public void onDisable() {
	for (Storable storable : manager.cache.values()) {
	    manager.insert(storable, false);
	}
	dataSource.close();
    }

    public void debug(String msg) {
	Bukkit.getConsoleSender().sendMessage("§d[" + getName() + "] " + msg);
    }

    public DataManager getDataManager() {
	return manager;
    }

    /**
     * @return the kitManager
     */
    public KitsManager getKitManager() {
	return kitManager;
    }

    /**
     * @param kitManager the kitManager to set
     */
    public void reload() {
	this.kitManager = null;
	this.kitManager = new KitsManager(this);
    }

}
