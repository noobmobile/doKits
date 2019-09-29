package com.dont.kits.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bukkit.Bukkit;

import com.dont.kits.models.Storable;
import com.google.gson.Gson;

public class DataManager {

    private final DataSource sql;
    private final Gson gson;
    public final HashMap<String, Storable> cache;
    private final ExecutorService executor;

    public DataManager(DataSource sql) {
	this.sql = sql;
	gson = new Gson();
	cache = new HashMap<>();
	executor = Executors.newFixedThreadPool(5);
    }

    public boolean exists(String key) {
	try {
	    PreparedStatement stm = sql.getConnection().prepareStatement("SELECT * FROM `" + sql.tableName + "` WHERE `key` = ?");
	    stm.setString(1, key);
	    return stm.executeQuery().next();
	} catch (SQLException e) {
	    e.printStackTrace();
	    return false;
	}
    }

    
    public <T extends Storable> T get(String key, Class<? extends T> clazz) {
	try {
	    PreparedStatement stm = sql.getConnection().prepareStatement("SELECT * FROM `" + sql.tableName + "` WHERE `key` = ?");
	    stm.setString(1, key);
	    ResultSet rs = stm.executeQuery();
	    if (rs.next()) {
		String json = rs.getString("json");
		return gson.fromJson(json, clazz);
	    }
	    return null;
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public void insert(Storable storable, boolean async) {
	Runnable insert = () -> {
	    try {
		String json = gson.toJson(storable);
		Bukkit.getConsoleSender().sendMessage("§dInserindo "+storable.getClass().getSimpleName()+" "+storable.getName()+" na tabela em "+(async ? "a" : "")+"sync");
		PreparedStatement stm = sql.getConnection().prepareStatement("INSERT OR REPLACE INTO `" + sql.tableName + "`(`key`, `json`) VALUES (?,?)");
		stm.setString(1,  storable.getName());
		stm.setString(2, json);
		stm.executeUpdate();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	};
	if (async) executor.submit(insert);
	else insert.run();
    }

}
