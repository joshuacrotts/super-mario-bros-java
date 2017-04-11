package com.twk.smb.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.joshuacrotts.standards.StandardAnimator;
import com.joshuacrotts.standards.StandardGameObject;
import com.joshuacrotts.standards.StandardID;
import com.twk.smb.main.Game;

public class Block extends StandardGameObject {

	public static boolean drawBounds;

	// For debugging
	private boolean collision = false;

	private Player p;

	private Game game;
	
	public Block(int x, int y, String fileLocation, Game game, StandardID id, boolean interactable) {
		super(x, y, id);
		this.game = game;
		this.p = game.player;
		try {
			this.currentSprite = ImageIO.read(new File(fileLocation));
		} catch (IOException e) {
			System.err.println("Error! Could not load in Block Image");
		}

		this.setInteractable(interactable);
		
		this.setWidth((byte) this.currentSprite.getWidth());
		this.setHeight((byte) this.currentSprite.getHeight());
	}
	
	public Block(int x, int y, Game game, StandardID id, boolean interactable) {
		super(x, y, id);
		this.game = game;
		this.p = game.player;

		this.setInteractable(interactable);

	}

	@Override
	public void tick() {
		
	}

	public void render(Graphics2D g2) {

		//Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(super.currentSprite, super.getX(), super.getY(), null);
		// this.tick();
		
	//	g2.setColor(Color.blue);
	//	g2.draw(this.getBounds());

	}

	public Rectangle getBounds() {
		return new Rectangle(super.getX(), super.getY(), this.getWidth(), this.getHeight());
	}
	
	public StandardAnimator getAnimation(){
		return null;
	}
}