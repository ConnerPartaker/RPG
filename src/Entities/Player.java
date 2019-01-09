package Entities;

import java.awt.image.BufferedImage;

public class Player extends Entity {
	
	public int roomid    = 0x00;
			
	public boolean left  = false,
			 	   right = false,
				   space = false;
				   
	public int spacecount = 0;
	
	public boolean hook = true;
	
	private boolean grounded = false;
	
	public Player(int id) {
		
		super(id);
		
		health = 100;
		
		hitx = 1;  		 hity = 2;
		textwidth  = 16; textheight = 32;
	}
	
	public BufferedImage text() {
		
		int cache, x, y;
		
		cache = (cache = -(int)System.currentTimeMillis()/100 % 4) == 3 ? 1 : cache; //Spits out 0,1,2,1,0,1... changing every 100 ms
		
		x = grounded ? velx == 0 ? 0 : 1 : vely > 0 ? 2 : 3;
		
		y = velx >= 0 ? 0 : 1;
		if (hook) y+=2;
		
		return text.getSubimage(16*(3*x + cache), 32*y, 16, 32);
	}
	
	public void update() {
		
		velx = right^left ? right ? 5 : -5 : 0;
		
		if (space) {
			
			space = false;
			
			if (spacecount++ < 10) vely = 12;
		}
	}
	
	public void addEffect(Entity e) {
		// TODO Auto-generated method stub
		
	}
	
	public void grounded() {
		
		grounded = true;
		spacecount = 0;
	}
}