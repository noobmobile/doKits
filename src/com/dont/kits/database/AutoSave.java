package com.dont.kits.database;

import org.bukkit.scheduler.BukkitRunnable;

import com.dont.kits.Main;
import com.dont.kits.models.Storable;

public class AutoSave extends BukkitRunnable{

    private final Main main;
    public AutoSave(Main main) {
	this.main = main;
	runTaskTimerAsynchronously(main, 20l*60*10, 20l*60*10);
    }

    @Override
    public void run() {
	main.debug("Iniciando auto save");
	long before = System.currentTimeMillis();
	int i = 0;
	for (Storable storable : main.getDataManager().cache.values()) {
	    main.getDataManager().insert(storable, true);
	    i++;
	}
	long now = System.currentTimeMillis();
	long total = now-before;
	main.debug("Auto completo, salvo "+i+" objetos em "+total+"ms");
    }
    
    
    
}
