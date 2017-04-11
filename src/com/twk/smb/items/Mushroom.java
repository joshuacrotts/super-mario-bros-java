package com.twk.smb.items;

import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.joshuacrotts.standards.StandardGameObject;
import com.joshuacrotts.standards.StandardID;
import com.twk.smb.entities.Block;
import com.twk.smb.entities.Player;
import com.twk.smb.main.Game;

public class Mushroom extends StandardGameObject{

	private boolean active = false;
	private boolean inBoxState = true;
	
	private double gravity = 1;
	private boolean falling = false;
	
	private Game game;
	private StandardGameObject block;
	
	private Player player;

	
	public Mushroom(int x, int y, StandardGameObject b, Player player, Game game){
		super(x, y, StandardID.Powerup);
		
		this.game = game;
		this.block = b;
		this.player = player;
		
		try{
			this.setCurrentSprite(ImageIO.read(new File("Resources/sprites/items/mushroom0.png")));
		}catch(IOException e){
			e.printStackTrace();
		}
		
		this.setWidth(this.getCurrentSprite().getWidth());
		this.setHeight(this.getCurrentSprite().getHeight());
		
		this.setVelY(1);
		this.setVelX(1);
		
		this.setActive(true);
	}

	@Override
	public void tick() {
		
		if(active){
			
			if(this.getBounds().intersects(this.block.getBounds())){
				this.inBoxState = true;
			}else{
				this.inBoxState = false;
			}
			
			if(inBoxState){
				this.setY(this.getY() - this.getVelY());
				return;
			}else{
				this.setX(this.getX() + this.getVelX());
				this.setY(this.getY() + this.getVelY());
				
				this.fall();
				this.checkCollision();
				
				if(this.getBounds().intersects(this.player.getBounds())){
					this.player.setBig(true);
					game.handler.removeEntity(this);
				}
			}
		}
		
	}

	@Override
	public void render(Graphics2D g2) {

		g2.drawImage(this.getCurrentSprite(), this.getX(), this.getY(), null);
		
	}
	
	private void checkCollision(){
		for(int i = 0; i < Game.blockHandler.getBlocks().size(); i++){
			if(Game.blockHandler.getBlocks().get(i).getId() == StandardID.Obstacle){

				StandardGameObject b = (Block) Game.blockHandler.getBlocks().get(i);

				
				if(this.getBottomBounds().intersects(b.getTopBounds())){
					this.setVelY(0);
					this.setY(b.getY() - b.getHeight());
				}
				
				if(this.getLeftBounds().intersects(b.getRightBounds())){
					this.setVelX(-this.getVelX());
					this.setX(b.getX() + b.getWidth());
				}
				
				if(this.getRightBounds().intersects(b.getLeftBounds())){
					this.setVelX(-this.getVelX());
					this.setX(b.getX() - b.getWidth());
				}
				
			}
		}
	}

	private void fall(){
		if(falling)
			this.setVelY((int) (this.getVelY() + gravity));
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isInBoxState() {
		return inBoxState;
	}

	public void setInBoxState(boolean inBoxState) {
		this.inBoxState = inBoxState;
	}

	public boolean isFalling() {
		return falling;
	}

	public void setFalling(boolean falling) {
		this.falling = falling;
	}
}
