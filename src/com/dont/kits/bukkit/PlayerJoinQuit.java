package com.dont.kits.bukkit;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.dont.kits.Main;
import com.dont.kits.database.DataManager;
import com.dont.kits.models.Storable;
import com.dont.kits.models.User;

public class PlayerJoinQuit implements Listener {

    private final Main main;
    private final DataManager manager;

    public PlayerJoinQuit(Main main) {
	this.main = main;
	this.manager = main.getDataManager();
	Bukkit.getPluginManager().registerEvents(this, main);
	main.debug(this.getClass().getSimpleName() + " carregado");
    }

    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent e) {
	if (!e.getLoginResult().equals(Result.ALLOWED))
	    return;
	if (manager.cache.containsKey(e.getName()))
	    return;
	if (manager.exists(e.getName())) {
	    User user = manager.get(e.getName(), User.class);
	    manager.cache.put(e.getName(), user);
	    main.debug("Pegando player " + e.getName() + " do mysql");
	} else {
	    manager.cache.put(e.getName(), new User(e.getName(), new HashMap<>()));
	    main.debug("Criando novo player no mysql " + e.getName());
	}
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
	if (!manager.cache.containsKey(e.getPlayer().getName()))
	    return;
	User user = (User) manager.cache.get(e.getPlayer().getName());
	manager.insert(user, true);
	manager.cache.remove(user.getName());
	user = null;
    }


}
