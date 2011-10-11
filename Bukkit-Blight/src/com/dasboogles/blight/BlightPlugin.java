package com.dasboogles.blight;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.config.Configuration;

/**
 * Sample plugin for Bukkit
 *
 * Boilerplate code obtained from:
 * https://github.com/Bukkit/SamplePlugin/tree/master/src/main/java/com/dinnerbone/bukkit/sample
 *
 * @author boogles
 * 
 */
public class BlightPlugin extends JavaPlugin {
	 	private final BlightPlayerListener playerListener = new BlightPlayerListener(this);
	    private final BlightBlockListener blockListener = new BlightBlockListener(this);
	    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
	    protected static Configuration CONFIG;
	    
	    private Blight blight;
	    
	    
	    private Logger log = Logger.getLogger("minecraft");

	    public Blight getBlight() {
			return blight;
	    }
	    
	    // NOTE: There should be no need to define a constructor any more for more info on moving from
	    // the old constructor see:
	    // http://forums.bukkit.org/threads/too-long-constructor.5032/

	    public void onDisable() {
	        // TODO: Place any custom disable code here

	        // NOTE: All registered events are automatically unregistered when a plugin is disabled

	        // EXAMPLE: Custom code, here we just output some info so we can check all is well
	    	PluginDescriptionFile pdfFile = this.getDescription();
	        log.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is disabled!");
	    }

	    public void onEnable() {
	        // TODO: Place any custom enable code here including the registration of any events
	    	// Setup config
	    	CONFIG = getConfiguration();
	    	String includedMaterials = CONFIG.getString("included", "" + Material.DIRT.toString() + "," 
	    			+ Material.GRASS.toString() + "," 
	    			+ Material.WOOD.toString() + ","
	    			+ Material.AIR.toString());
	    	String infectableFaces = CONFIG.getString("infectableFaces", "" + BlockFace.NORTH 
	    			+ "," + BlockFace.EAST 
	    			+ "," + BlockFace.SOUTH 
	    			+ "," + BlockFace.WEST 
	    			+ "," + BlockFace.DOWN 
	    			+ "," + BlockFace.UP
	    			+ "," + BlockFace.NORTH_EAST
	    			+ "," + BlockFace.NORTH_WEST
	    			+ "," + BlockFace.SOUTH_EAST
	    			+ "," + BlockFace.SOUTH_WEST);
	    	String infectedBlockString = CONFIG.getString("infectedBlockMaterial", Material.NETHERRACK.toString());
	    	String exhaustedBlockString = CONFIG.getString("exhaustedBlockMaterial", Material.NETHERRACK.toString());
	    	int maximumActiveBlocksCap = CONFIG.getInt("maximumActiveBlocksCap", 10);
	    	int infectionProbability = CONFIG.getInt("infectionProbability", 50);
	    	int infectionFrequency = CONFIG.getInt("infectionFrequency", 200);
	    	CONFIG.save();
	    	
	    	blight = new Blight(includedMaterials, infectableFaces, infectedBlockString,
	    			exhaustedBlockString, maximumActiveBlocksCap, infectionProbability);
	    	
	        // Register our events
	        PluginManager pm = getServer().getPluginManager();
	        pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
	        pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Normal, this);
	        pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Priority.Normal, this);
	        pm.registerEvent(Event.Type.BLOCK_PHYSICS, blockListener, Priority.Normal, this);
	        pm.registerEvent(Event.Type.BLOCK_CANBUILD, blockListener, Priority.Normal, this);

	        BukkitScheduler scheduler = getServer().getScheduler();
	        scheduler.scheduleSyncRepeatingTask(this, blight, 1, infectionFrequency);
	        
	        // Register our commands
	        getCommand("blight").setExecutor(new BlightCommand(this));

	        // EXAMPLE: Custom code, here we just output some info so we can check all is well
	        PluginDescriptionFile pdfFile = this.getDescription();
	        log.info( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
	    }
}
