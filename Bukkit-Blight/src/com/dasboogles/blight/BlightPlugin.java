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
	    	String includedMaterials = CONFIG.getString("included", "" + Material.DIRT.toString() + "," + Material.GRASS.toString() + "," + Material.WOOD.toString());
	    	CONFIG.save();
	    	
	    	blight = new Blight();
	    	
	    	String[] includedMaterialsArray = includedMaterials.split(",");
	    	for(String matString : includedMaterialsArray) {
    			Material materialToAdd = Material.getMaterial(matString.toUpperCase());
    			if(materialToAdd != null) {
    				blight.includedList.add(materialToAdd);
    			}
    			else {
    				log.warning("Blight: Material name " + matString + " provided in config file was invalid. Check that it exists in the list here: http://jd.bukkit.org/apidocs/org/bukkit/Material.html");
    			}
	    	}

	    	
	    	
	        // Register our events
	        PluginManager pm = getServer().getPluginManager();
	        pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
	        pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Normal, this);
	        pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Priority.Normal, this);
	        pm.registerEvent(Event.Type.BLOCK_PHYSICS, blockListener, Priority.Normal, this);
	        pm.registerEvent(Event.Type.BLOCK_CANBUILD, blockListener, Priority.Normal, this);

	        BukkitScheduler scheduler = getServer().getScheduler();
	        //scheduler.scheduleSyncRepeatingTask(this, blight, 1, 1000);
	        
	        
	        // Register our commands
	        getCommand("blight").setExecutor(new BlightCommand(this));

	        // EXAMPLE: Custom code, here we just output some info so we can check all is well
	        PluginDescriptionFile pdfFile = this.getDescription();
	        log.info( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
	    }
	    
		private boolean saveBlightToDisk(String filename) {
		    try {
		    	FileOutputStream blightFile = new FileOutputStream(filename);
		    	ObjectOutputStream out = new ObjectOutputStream(blightFile);
		    	out.writeObject(blight);
		    	out.close();
		    	return true;
		    }
		    catch(IOException exception) {
		    	exception.printStackTrace();
		    	return false;
		    }
		}
	    
	    private boolean restoreBlightFromDisk(String filename) {
	    	try {
				FileInputStream blightFileInput = new FileInputStream(filename);
				ObjectInputStream in = new ObjectInputStream(blightFileInput);
				blight = (Blight) in.readObject();
				return true;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
	    	
	    }
}
