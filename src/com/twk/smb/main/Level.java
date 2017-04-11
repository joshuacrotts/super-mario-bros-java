package com.twk.smb.main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Level {
	
	public final int WIDTH;
	public final int HEIGHT;
	
	private int x;
	private int y;
	
	private String fileLocation;
	private String bgFileLocation;
	
	private BufferedImage bgImage;
	private BufferedImage image;
	
	public Level(String fileLocation, String bgImage, int width,  int height){
		this.x = 0;
		this.y = 0;
		this.WIDTH = width;
		this.HEIGHT = height;
		
		this.fileLocation = fileLocation;
		this.bgFileLocation = bgImage;
		
		try{
			this.image = ImageIO.read(new File(fileLocation));
			this.bgImage = ImageIO.read(new File(bgFileLocation));
		}catch(IOException e){
			e.printStackTrace();
		}
		
	}

	public void tick(){

	}
	
	public void render(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(this.bgImage, 0, 0, null);
	}
	
	public int getX() {
		return x;
	}

	public void setX(short x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(short y) {
		this.y = y;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	
}