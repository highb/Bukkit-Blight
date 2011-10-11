package com.dasboogles.blight;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class Blight implements Runnable {
	private ArrayList<Block> activeBlightBlocks = new ArrayList<Block>();
	private Logger log = Logger.getLogger("minecraft");
	private Random rand = new Random();
	public ArrayList<BlockFace> faces = new ArrayList<BlockFace>();
	public ArrayList<Material> includedList = new ArrayList<Material>();
	public Material infectedBlockMaterial = Material.SPONGE;
	public Material exhaustedBlockMaterial = Material.NETHERRACK;
	public int maximumActiveBlocks = 100;
	public int infectionProbability = 40;
	
	public Blight(String includedMaterials, String infectableFaces, String infectedBlock, String exhaustedBlock, int maximumActiveBlocks, int infectionProbability) {
		String[] includedMaterialsArray = includedMaterials.split(",");
    	for(String matString : includedMaterialsArray) {
			Material materialToAdd = Material.getMaterial(matString.toUpperCase());
			if(materialToAdd != null) {
				includedList.add(materialToAdd);
			}
			else {
				log.warning("Blight: Material name " + matString + " provided in config file was invalid. Check that it exists in the list here: http://jd.bukkit.org/apidocs/org/bukkit/Material.html");
			}
    	}
    	
    	String[] infectableFacesArray = infectableFaces.split(",");
    	for(String faceString : infectableFacesArray) {
			try {
				BlockFace faceToAdd = BlockFace.valueOf(faceString);
				faces.add(faceToAdd);
			}
			catch(IllegalArgumentException ex) {
				log.warning("Blight: Face name " + faceString + " provided in config file was invalid. Check that it exists in the list here: http://jd.bukkit.org/apidocs/org/bukkit/block/BlockFace.html");
			}
			catch(NullPointerException ex) {
				
			}
    	}
    	
    	Material infectedBlockMaterial = Material.getMaterial(infectedBlock);
    	if(infectedBlockMaterial != null) {
    		this.infectedBlockMaterial = infectedBlockMaterial;
    	}
    	
    	Material exhaustedBlockMaterial = Material.getMaterial(exhaustedBlock);
    	if(exhaustedBlockMaterial != null) {
    		this.exhaustedBlockMaterial = exhaustedBlockMaterial;
    	}
    	
    	this.maximumActiveBlocks = maximumActiveBlocks;
    	this.infectionProbability = infectionProbability;
	}
	
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
	
	public boolean saveLocationsToDisk(String filename) {
		return false;
	}
	
	public boolean restoreLocationsFromDisk(String filename) {
		return false;
	}
}
