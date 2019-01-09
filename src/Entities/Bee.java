package Entities;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Bee extends Entity {
	
	public double dir = Math.random();
	
	public Bee(int id) {
		
		super(id);
		
		health = 5;
		
		grav = 0;
		
		hitx = .5;  hity = .5;
	}

	
	public void update() {
		
		if (Math.random() < .3)
			dir = Math.random();
		
		velx = 6*Math.cos(2*Math.PI*dir);
		vely = 6*Math.sin(2*Math.PI*dir);
	}
	
	public void addEffect(Entity e) {
		if (Arrays.binarySearch(reactList[0], e.id) >= 0)
			e.health -= 10;
		if (Arrays.binarySearch(reactList[1], e.id) >= 0)
			e.extVelx += 10*Math.signum(e.posx - posx);
	}

	
	public void grounded() {
		// TODO Auto-generated method stub
		
	}
	
	
	public BufferedImage text() {
		return text.getSubimage((int)(System.currentTimeMillis()/8 %2)*16, 0, 16, 16);
	}
}