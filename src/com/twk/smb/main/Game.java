package com.twk.smb.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.joshuacrotts.standards.StandardGame;
import com.joshuacrotts.standards.StandardGameObject;
import com.joshuacrotts.standards.StandardID;
import com.twk.smb.entities.Block;
import com.twk.smb.entities.ItemBlock;
import com.twk.smb.entities.Player;

public class Game extends StandardGame{

	private static final long serialVersionUID = 3196185357401780804L;
	//Grouped objects
	public static GameCamera camera;
	public static Background[] bg;
	public static BlockHandler blockHandler;
	public static Level[] levels;
	
	//Thread information/variables
	private Thread thread;
	private boolean running = false;
	
	//Window and screen information/Debug info
	private boolean consoleFPS = true;
	private boolean titleFPS = true;
	private int levelNum = 0;
	
	//Entity objects
	public Player player;
	
	public Game(int width, int height, String title){
		super(width, height, title);
		
		this.camera = new GameCamera(0,0,this);
		
		this.player = new Player(300,400, this);
		
		this.blockHandler = new BlockHandler(this);
		
		this.levels = new Level[1];
		
		this.bg = new Background[levels.length];
		
		this.loadLevelData();
		
		this.loadImageLevel(levels[levelNum].getImage());
		
		this.addKeyListener(player);
		
		this.start();
	}
	
	private synchronized void start(){
		if(running) return;
		else{
			this.running = true;
			this.thread = new Thread(this);
			this.thread.start();
		}
	}
	
	private synchronized void stop(){
		if(!running) return;
		else{
			this.running = false;
			try{
				this.thread.join();
			}catch(InterruptedException e){
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		System.exit(0);
	}
	
	@Override
	public void run() {
		requestFocus(); //Focuses the click/input on the frame/canvas.
		long lastTime = System.nanoTime(); //The current system's nanotime.
		double ns = 1000000000.0 / 60.0; //Retrieves how many nano-seconds are currently in one tick/update.
		double delta = 0; //How many unprocessed nanoseconds have gone by so far.
		long timer = System.currentTimeMillis();
		int frames = 0; //The frames per second.
		int updates = 0; //The updates per second.
		while (running) {
			
			boolean renderable = false; //Determines if the game should render the actual graphics.
			
			long now = System.nanoTime();//At this point, the current system's nanotime once again.
			delta += (now - lastTime) / ns;
			lastTime = now;
			//If the amount of unprocessed ticks is or goes above one...
			//Also determines if the game should update or not/render. Approximately sixty frames per second.
			while (delta >= 1) {
				this.tick();
				
				delta--;
				updates++;
				
				renderable = true;
			}
			
			if(renderable){
				frames++;
				this.render();
			}
			
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				if(this.titleFPS)
					window.setTitle(window.getTitle() + " | " + updates + " ups, " + frames + " fps");

				if(this.consoleFPS)
					System.out.println(window.getTitle() + " | " + updates + " ups, " + frames + " fps");
				updates = 0;
				frames = 0;
			}
		}
		
		this.stop();
	}
	
	private void tick(){
		handler.tick();
		blockHandler.tick();
		
		for(StandardGameObject o : handler.getEntities()){
			if(o.getId() == StandardID.Player){
				camera.tick(o);
			}
		}
	}
	
	private void render(){
		BufferStrategy bs = getBufferStrategy();
		
		if(bs == null){
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.black);
		//System.out.println("render");
		g2.fillRect(0, 0, 1280, 720);
		
		//Draw here
		
		g2.translate(camera.getX(), camera.getY());
		
		bg[levelNum].render(g2);
		levels[levelNum].render(g2);
		blockHandler.render(g2);
		handler.render(g2);
		
		g2.translate(-camera.getX(), -camera.getY());
		
		//End drawing
		
		g.dispose();
		g2.dispose();
		bs.show();
	}
	
	private void loadImageLevel(BufferedImage image) {
		int w = image.getWidth();
		int h = image.getHeight();

		Random aesthetic = new Random();
		int block = 0;

		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				int pixel = image.getRGB(x, y);

				block = aesthetic.nextInt(3);

				short r = (short) ((pixel >> 16) & 0xff);
				short g = (short) ((pixel >> 8) & 0xff);
				short b = (short) ((pixel) & 0xff);
				

				if (r == 255 && g == 255 && b == 255) {
					blockHandler.add(new Block(x*32, y*32, "Resources/sprites/obj/brick0.png", this, StandardID.Obstacle, true));
				}
				
				if(r == 0 && g == 255 && b == 0){
					blockHandler.add(new Block(x * 32, y * 32, "Resources/sprites/obj/bush0.png", this, StandardID.Entity, false));
				}
				
				if(r == 255 && g == 106 && b == 0){
					blockHandler.add(new ItemBlock(x * 32, y * 32, StandardID.Weapon, this));
				}
			}
		}

	}
	
	private void loadLevelData(){
		for(int i = 0; i<levels.length; i++){
			bg[i] = new Background("Resources/bg/blueskies.png");
			levels[i] = new Level("Resources/leveldata/level0.png", bg[i].getFileLocation(),7000,704);
		}
	}
	
	public static void main(String[] args) {
		new Game(1280,720, "Super Mario Brothers Remake");
	}
}
