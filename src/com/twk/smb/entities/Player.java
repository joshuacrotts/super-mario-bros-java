package com.twk.smb.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.joshuacrotts.standards.StandardAnimator;
import com.joshuacrotts.standards.StandardGameObject;
import com.joshuacrotts.standards.StandardID;
import com.twk.smb.items.Mushroom;
import com.twk.smb.main.Game;

public class Player extends StandardGameObject implements KeyListener {

	// Sprites for items, etc.
	private BufferedImage blankQuestion;

	// Still Sprites for small mario
	private BufferedImage sStillSpriteR; //Standing still right
	private BufferedImage sStillSpriteL; // Standing still left
	private BufferedImage sJumpingR;
	private BufferedImage sJumpingL;

	// Animated Sprites (Moving state) for small mario
	private ArrayList<BufferedImage> sRSprites = new ArrayList<BufferedImage>(); //Small & going right
	private ArrayList<BufferedImage> sLSprites = new ArrayList<BufferedImage>(); //Small & going left
	// Animators for small mario
	private StandardAnimator sRAnimator;
	private StandardAnimator sLAnimator;

	// Still sprites for big mario
	private BufferedImage bStillSpriteR = null; //Standing still right
	private BufferedImage bStillSpriteL = null; // Standing still left
	private BufferedImage bJumpingR = null; //Jumping right 
	private BufferedImage bJumpingL = null; //Jumping left

	// Animated Sprites (Moving state) for big mario
	private ArrayList<BufferedImage> bRSprites = new ArrayList<BufferedImage>(); //Small & going right
	private ArrayList<BufferedImage> bLSprites = new ArrayList<BufferedImage>(); //Small & going left

	// Animators for big mario
	private StandardAnimator bRAnimator;
	private StandardAnimator bLAnimator;

	// Key codes
	private boolean right = false;
	private boolean left = false;

	// Player states
	private enum Direction  {Left, Right};
	private Direction playerFacing;
	public boolean airborne = true;
	public boolean falling = true;
	public boolean jumping = false;
	public boolean movingHorizontal = false;
	public boolean attacking = false;
	public boolean down = false;
	public boolean isBig = false;

	// Physics (jumping and falling)
	private double gravity = 2;

	//Actual game states (health, powerup, etc)
	public static int[] powerUps = {0,1,2,3,4,5};

	private Game game;

	public Player(int x, int y, Game game) {
		super(x, y, StandardID.Player);

		this.game = game;

		this.loadSprites();
		this.loadAnimators();

		this.playerFacing = Direction.Right;
		this.currentSprite = sStillSpriteR;

		super.setWidth((byte) currentSprite.getWidth());
		super.setHeight((byte) currentSprite.getHeight());

		this.game.handler.addEntity(this);
	}

	public void tick(){
		this.setX(this.getX() + this.getVelX());
		this.setY(this.getY() + this.getVelY());

		this.fall();
		this.checkCollision();
		this.checkVelocities();

		if(jumping){

			if(!this.isBig){
				if(this.playerFacing == Direction.Left)
					this.setCurrentSprite(this.sJumpingL);
				else
					this.setCurrentSprite(this.sJumpingR);
			}else{
				if(this.playerFacing == Direction.Left)
					this.setCurrentSprite(this.bJumpingL);
				else
					this.setCurrentSprite(this.bJumpingR);
			}
		}


		if(!this.isBig){
			if(!this.isMoving() && this.playerFacing == Direction.Left)
				this.setCurrentSprite(sStillSpriteL);
			if(!this.isMoving() && this.playerFacing == Direction.Right)
				this.setCurrentSprite(sStillSpriteR);
		}else{
			if(!this.isMoving() && this.playerFacing == Direction.Left)
				this.setCurrentSprite(bStillSpriteL);
			if(!this.isMoving() && this.playerFacing == Direction.Right)
				this.setCurrentSprite(bStillSpriteR);
		}
		
		if(this.getCurrentSprite() == null)
			this.setCurrentSprite(bStillSpriteR);
		
		//System.out.println(this.bStillSpriteR);
		
		try{
			this.setWidth((byte) currentSprite.getWidth());
			this.setHeight((byte) currentSprite.getHeight());
		}catch(NullPointerException e){
			//System.exit(0);
			System.out.println(this.getCurrentSprite());
			System.out.println("Left: "+bStillSpriteL);
			System.out.println("Right: "+bStillSpriteR);
		}

	}

	private void checkVelocities(){
		if(left && !right){
			if(!this.isBig){
				this.bLAnimator.setAnimating(false);
				this.sLAnimator.setAnimating(true);
				this.sLAnimator.animate();
			}
			else{
				this.sLAnimator.setAnimating(false);
				this.bLAnimator.setAnimating(true);
				this.bLAnimator.animate();
			}
			this.setVelX(-5);
			this.movingHorizontal = true;
		}

		if(right && !left){
			if(!this.isBig){
				this.bRAnimator.setAnimating(false);
				this.sRAnimator.setAnimating(true);
				this.sRAnimator.animate();
			}
			else{
				this.sLAnimator.setAnimating(false);
				this.bRAnimator.setAnimating(true);
				this.bRAnimator.animate();
				
			}
			this.setVelX(5);
			this.movingHorizontal = true;
		}

		if(!left && !right){
			this.setVelX(0);
			this.movingHorizontal = false;
		}

	}

	private void checkCollision(){
		for(int i = 0; i < Game.blockHandler.getBlocks().size(); i++){
			if(Game.blockHandler.getBlocks().get(i).getId() == StandardID.Obstacle ||
					Game.blockHandler.getBlocks().get(i).getId() == StandardID.Weapon ||
					Game.blockHandler.getBlocks().get(i).getId() == StandardID.Powerup){

				StandardGameObject b = (Block) Game.blockHandler.getBlocks().get(i);

				if(this.getBottomBounds().intersects(b.getTopBounds())){
					this.setVelY(0);
					if(!this.isBig)
						this.setY(b.getY() - b.getHeight());
					else
						this.setY(b.getY() - b.getHeight() * 2);
					System.out.println(b.getY()-b.getHeight());
					this.setJumping(false);
				}

				if(this.getTopBounds().intersects(b.getBottomBounds())){
					this.setVelY(-this.getVelY());
					this.setY(b.getY() + b.getHeight());

					if(b.id == StandardID.Weapon && b.isInteractable()){
						Game.blockHandler.getBlocks().get(i).getAnimation().setAnimating(false);
						Game.blockHandler.getBlocks().get(i).setCurrentSprite(blankQuestion);
						Game.blockHandler.getBlocks().get(i).setInteractable(false);

						game.handler.addEntity(new Mushroom(b.getX(), b.getY(), b, this, this.game));

					}
				}

				if(this.getLeftBounds().intersects(b.getRightBounds())){
					this.setVelX(0);
					this.setX(b.getX() + b.getWidth());
				}

				if(this.getRightBounds().intersects(b.getLeftBounds())){
					this.setVelX(0);
					this.setX(b.getX() - b.getWidth());
				}



			}
		}
	}

	private void fall(){
		if(falling)
			this.setVelY((int) (this.getVelY() + gravity));
	}

	public void render(Graphics2D g2) {

		g2.drawImage(this.currentSprite, this.getX(), this.getY(), null);

		//	g2.setColor(Color.red);
		//	g2.draw(this.getBottomBounds());
	}


	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();

		// Movement keys and attacking keys
		switch (keyCode) {

		case KeyEvent.VK_W:
			if(!this.isJumping()){
				this.setVelY(-20);
				this.setJumping(true);
				break;
			}
		case KeyEvent.VK_S:
			down = true;
			break;
		case KeyEvent.VK_A:
			playerFacing = Direction.Left;
			left = true;		
			break;
		case KeyEvent.VK_D:
			playerFacing = Direction.Right;
			right = true;
			break;
		case KeyEvent.VK_SPACE:
			attacking = true;
			break;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();

		switch (keyCode) {

		case KeyEvent.VK_S:
			down = false;
			break;
		case KeyEvent.VK_A:
			left = false;
			break;
		case KeyEvent.VK_D:
			right = false;
			break;
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	private void loadSprites() {
		for (int i = 0; i < 4; i++) {
			try {
				this.sLSprites.add(ImageIO.read(new File("Resources/sprites/mario/sLeft/sLeft"+i+".png")));
				this.sRSprites.add(ImageIO.read(new File("Resources/sprites/mario/sRight/sRight"+i+".png")));
				
				this.bLSprites.add(ImageIO.read(new File("Resources/sprites/mario/bLeft/bLeft"+i+".png")));
				this.bRSprites.add(ImageIO.read(new File("Resources/sprites/mario/bRight/bRight"+i+".png")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			//Instantiates the small still sprites
			this.sStillSpriteL = sLSprites.get(0);
			this.sStillSpriteR = sRSprites.get(0);
			this.sJumpingR = ImageIO.read(new File("Resources/sprites/mario/sJump/sJump0.png"));
			this.sJumpingL = ImageIO.read(new File("Resources/sprites/mario/sJump/sJump1.png"));

			//Instantiates the big still sprites
			this.bStillSpriteL = bLSprites.get(0);
			this.bStillSpriteR = bRSprites.get(0);
			this.bJumpingR = ImageIO.read(new File("Resources/sprites/mario/bJump/bJump1.png"));
			this.bJumpingL = ImageIO.read(new File("Resources/sprites/mario/bJump/bJump0.png"));

			this.blankQuestion = ImageIO.read(new File("Resources/sprites/question/blank0.png"));


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loadAnimators(){
		this.sRAnimator = new StandardAnimator(this.sRSprites, (byte) 30, this);
		this.sLAnimator = new StandardAnimator(this.sLSprites, (byte) 30, this);
		this.bRAnimator = new StandardAnimator(this.bRSprites, (byte) 20, this);
		this.bLAnimator = new StandardAnimator(this.bLSprites, (byte) 20, this);
	}

	private boolean isMoving() {
		if (velX == 0 && velY == 0 && !left && !right) {
			return false;
		}
		return true;
	}

	public boolean isJumping() {
		return jumping;
	}

	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}

	public boolean isFalling() {
		return falling;
	}

	public void setFalling(boolean falling) {
		this.falling = falling;
	}

	public Rectangle getBounds() {
		return new Rectangle(this.getX(), this.getY(), currentSprite.getWidth(), currentSprite.getHeight());
	}
	@Override
	public Rectangle getBottomBounds(){
		return new Rectangle(this.getX(), this.getY() + (this.getHeight()/2), this.getWidth(), this.getHeight()/2);
	}

	public Direction getDirection() {
		return playerFacing;
	}

	public boolean isBig() {
		return isBig;
	}

	public void setBig(boolean isBig) {
		this.isBig = isBig;
	}


}