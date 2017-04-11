package com.twk.smb.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import com.joshuacrotts.standards.StandardAnimator;
import com.joshuacrotts.standards.StandardID;
import com.twk.smb.main.Game;

public class ItemBlock extends Block {

	private BufferedImage[] sprites = null;

	private StandardAnimator questionAnimation;
	
	private Game game;

	public ItemBlock(int x, int y, StandardID id, Game game) {
		super(x, y, game, id, true);

		this.game = game;

		sprites = new BufferedImage[3];
		try{
			for(int i = 0; i < sprites.length; i++){
				sprites[i] = ImageIO.read(new File("Resources/sprites/question/q"+i+".png"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		this.questionAnimation = new StandardAnimator(new ArrayList<BufferedImage>(Arrays.asList(this.sprites)), 30, this);
		
		this.currentSprite = sprites[0];
		
		this.setWidth(this.currentSprite.getWidth());
		this.setHeight(this.currentSprite.getHeight());
	}
	
	public void tick(){
		this.questionAnimation.animate();
	}
	
	public void render(Graphics2D g2){
		g2.drawImage(this.currentSprite, this.getX(), this.getY(), null);
		
	//	g2.setColor(Color.yellow);
	//	g2.draw(this.getBounds());
	}
	
	@Override
	public StandardAnimator getAnimation(){
		return this.questionAnimation;
	}
	
	
}
