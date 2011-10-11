package com.dasboogles.blight;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Handler for the /pos BrokeIt command.
 * @author SpaceManiac
 */
public class BlightCommand implements CommandExecutor {
	private final BlightPlugin plugin;

    private Logger log = Logger.getLogger("minecraft");
    
    public BlightCommand(BlightPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;
        
        if(player.hasPermission("blight") || player.isOp()) {
	        if (args.length == 0) {
	            Location location = player.getLocation();
	            player.sendMessage("You are starting a blight at " + location.getBlockX() +"," + location.getBlockY() + "," + location.getBlockZ());
	            World world = location.getWorld();
	            Block blightSeed = world.getBlockAt(location.getBlockX(), location.getBlockY() - 1, location.getBlockZ());
	            plugin.getBlight().infectBlock(blightSeed);
	            return true;
	        } else if (args.length == 1) {
	        	try {
		    		int iterations = Integer.parseInt(args[0]);
		    		for(int i = 0; i < iterations; i++) {
		    			plugin.getBlight().advanceBlight();
		    		}
	        	}
	        	catch (Exception e) {
	        		player.sendMessage("Invalid parameter");
	        	}
	            return true;
	        } else {
	            return false;
	        }
        }
        else {
        	player.sendMessage("You don't have permissions to start a blight! You're a naughty little blighter!");
        	return false;
        }
    }
    

}
