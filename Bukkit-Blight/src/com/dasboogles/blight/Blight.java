package com.dasboogles.blight;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class Blight implements Runnable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final ArrayList<Block> activeBlightBlocks = new ArrayList<Block>();
	private Logger log = Logger.getLogger("minecraft");
	private Random rand = new Random();
	private BlockFace[] faces = {
			BlockFace.NORTH, 
			BlockFace.EAST, 
			BlockFace.SOUTH, 
			BlockFace.WEST, 
			BlockFace.DOWN, 
			BlockFace.UP,
			BlockFace.NORTH_EAST,
			BlockFace.NORTH_WEST,
			BlockFace.SOUTH_EAST,
			BlockFace.SOUTH_WEST};
	public ArrayList<Material> includedList = new ArrayList<Material>();
	public Material infectedBlockMaterial = Material.SPONGE;
	public Material exhaustedBlockMaterial = Material.NETHERRACK;
	public int maximumActiveBlocks = 100;
	public int infectionProbability = 40;
	
    public void advanceBlight() {
    	clampActiveBlightBlocks(maximumActiveBlocks);
    	
    	ArrayList<Block> blightBlocks = new ArrayList<Block>(activeBlightBlocks.size());
    	blightBlocks.addAll(activeBlightBlocks);
    	
    	for(int activeBlightBlock = 0; activeBlightBlock < activeBlightBlocks.size(); activeBlightBlock++) {
    		exhaustBlock(activeBlightBlocks.get(activeBlightBlock));
    	}
    	
    	for(int blightBlock = 0; blightBlock < blightBlocks.size(); blightBlock++) {
    		for(BlockFace face : faces) {
    			if(face != BlockFace.SELF) {
    				Block otherBlock = blightBlocks.get(blightBlock).getRelative(face);
        			if(includedList.contains(otherBlock.getType()) 
        					&& rand.nextInt(100) < infectionProbability) {
        				this.infectBlock(otherBlock);
        			}	
    			}
    		}
    		exhaustBlock(blightBlocks.get(blightBlock));
    	}
    }
    
    public void infectBlock(Block block) {
    	block.setType(infectedBlockMaterial);
    	activeBlightBlocks.add(block);
    }
    
    public void exhaustBlock(Block block) {
    	block.setType(exhaustedBlockMaterial);
    	activeBlightBlocks.remove(block);
    }
    
    private void clampActiveBlightBlocks(int maxBlightBlocks) {
    	int activeBlightBlocksSize = activeBlightBlocks.size();
    	if(activeBlightBlocksSize > maxBlightBlocks) {
    		exhaustBlock(activeBlightBlocks.get(rand.nextInt(activeBlightBlocksSize)));
    		clampActiveBlightBlocks(maxBlightBlocks);
    	}
    }

	@Override
	public void run() {
		advanceBlight();
	}
	
	public boolean saveToDisk(String filename) {
		return false;
	}
	
	public boolean restoreFromDisk(String filename) {
		return false;
	}
}
