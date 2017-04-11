package com.twk.smb.main;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.twk.smb.entities.Block;

public class BlockHandler {

	private ArrayList<Block> blocks;

	public static Game game;
	
	public BlockHandler(Game game){
		this.game = game;
		this.blocks = new ArrayList<Block>();
	}

	public void tick(){
		for(int i = 0; i<blocks.size(); i++){
			blocks.get(i).tick();
		}
	}

	public void render(Graphics2D g2){
		for (int i = 0; i < blocks.size(); i++){
			blocks.get(i).render(g2);
		}
	}
	
	public void add(Block b){
		this.blocks.add(b);
	}
	
	public ArrayList<Block> getBlocks(){
		return blocks;
	}
}