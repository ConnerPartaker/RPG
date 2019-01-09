package Entities;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import GLoader.Fetcher;

public abstract class Entity {
	
	public int id, layer;		//ID of the entity, and layer it resides on (relative to main, - for closer, + for further
	
	public double posx, posy,	//Position coords in game squares per second
				  velx, vely,	//Velocity vector in game squares per second
				  hitx, hity,	//Hit box size
				  grav,			//Gravitational influence
				  
				  health, 		//Entity's health
				  
				  extVelx, extVely; //External velocital influences in game squares per second
	
	
	public ArrayList<Entity> 
		oldTouching = new ArrayList<Entity>(), //Array holding all the entities this entity was touching at the last moment
		newTouching = new ArrayList<Entity>(); //Array holding all the entities this entity is currently touching
	
	public int[][] reactList;	//A set list of entity ids this entity has a reaction to. The row containing the id determines the effect. 
								//Effect of row x can vary between entities, but generally row 0 is damage, and row 1 is bounce.
	
	
	public BufferedImage text;	//Entity textures
	public int textheight,		//Height of one texture
			   textwidth; 		//Width of one texture
	
	
	
	public Entity(int id) {
		
		this.id = id;
		
		layer = 0;
		
		grav = -15;
		
		velx = 0; vely = 0;
		hitx = 1; hity = 1;
		
		textheight = 16;
		textwidth  = 16;
		
		String ent = this.getClass().getSimpleName();
		text = Fetcher.loadEntImg(ent);  //Image of player
		reactList = Fetcher.loadEntRom(ent); //List of reactory entities
	}
	
	
	/**
	 * Set the entity's location to the given game square coordinates
	 * @param x location
	 * @param y location
	 */
	public void setPos(int x, int y) {
		posx = x + hitx/2;
		posy = y + hity/2;
	}
	
	
	/**
	 * React to all the entities that have just been put into the touching array.
	 * Calls the addEffect function on any new entities in the hitbox region
	 */
	public void react() {
		
		for (Entity e : new ArrayList<Entity>(oldTouching))
			if (!newTouching.contains(e))
				oldTouching.remove(e);
		
		for (Entity e : new ArrayList<Entity>(newTouching))
			if (oldTouching.contains(e)) {newTouching.remove(e);}
			else {addEffect(e);}
		
		oldTouching.addAll(newTouching);
		newTouching.clear();
	}
	
	
	public abstract void update();				//Function holding all movements specific to this entity.
	public abstract void addEffect(Entity e);	//The entity handed has just touched the entity. This adds whatever affects this entity has for this type of entity
	public abstract void grounded();			//Some entities have special effects depending on whether they are grounded
	public abstract BufferedImage text();		//Return the correct texture of the entity for the current moment
}