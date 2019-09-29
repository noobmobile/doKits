package com.dont.kits.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.dont.kits.Main;


public class DataSource {

    private Connection connection;
    public final String tableName = "do.modelo";
    public DataSource() {

    }
    
    public void open() throws SQLException {
	this.connection = DriverManager
			.getConnection("jdbc:sqlite:" + Main.getPlugin(Main.class).getDataFolder().getPath() + "/database.db");
	createTables();
	
    }
    
    public void close() {
	try {
	    connection.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }
 
    private void createTables() throws SQLException {
	    PreparedStatement stm = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `"+tableName+"`(`key` VARCHAR(16) NOT NULL, `json` TEXT NOT NULL, PRIMARY KEY (`key`))");
	    stm.executeUpdate();
    }
    
    public Connection getConnection() {
	return connection;
    }

    
}
