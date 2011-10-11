package com.dasboogles.blight;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.Material;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPhysicsEvent;

/**
 * Blight block listener
 * @author boogles
 */
public class BlightBlockListener extends BlockListener {
	 @SuppressWarnings("unused")
	private final BlightPlugin plugin;

	    public BlightBlockListener(final BlightPlugin plugin) {
	        this.plugin = plugin;
	    }

	    @Override
	    public void onBlockPhysics(BlockPhysicsEvent event) {

	    }

	    @Override
	    public void onBlockCanBuild(BlockCanBuildEvent event) {

	    }
}
