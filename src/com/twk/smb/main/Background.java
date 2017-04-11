package com.twk.smb.main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Background {

	private BufferedImage bg;
	
	private String fileLocation;
	
	public Background(String fileLocation){
		this.fileLocation = fileLocation;
		
		try{
			this.bg = ImageIO.read(new File(this.fileLocation));
		}catch(Exception e){
			System.err.println("Error! Could not find or locate Background. Check file path.");
			e.printStackTrace();
		}
	}
	
	public void render(Graphics2D g2){
		g2.drawImage(this.bg, -1000, -300, null);
	}
	
	public String getFileLocation(){
		return this.fileLocation;
	}
}
