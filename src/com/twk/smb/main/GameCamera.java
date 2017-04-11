package com.twk.smb.main;

import com.joshuacrotts.standards.StandardGameObject;

public class GameCamera {

	private float x;
	private float y;
	
	private Game game;
	
	public GameCamera(float x, float y, Game game){
		this.game = game;
		this.x = x;
		this.y = y;
		
	}
	
	public void tick(StandardGameObject player){
		this.x = -player.getX() + this.game.getWindow().returnWidth()/2;
		this.y = -player.getY() + this.game.getWindow().returnHeight()/2;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}
	
	
}
