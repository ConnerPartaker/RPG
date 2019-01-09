package Logic;

import java.util.ArrayList;

import Entities.Entity;

public class HitboxAgent {
	
	private ArrayList<Entity> ents;
	
	public HitboxAgent(ArrayList<Entity> ents) {
		this.ents = ents;
	}
	
	/**
	 * Calculates hitbox position relative to others. If intersecting, the entity is sent to the touching array in the entity.
	 * Hitboxes are centered on each entity's position vector
	 * 
	 * @param Entity to be tested
	 */
	public void calc(Entity base) {
		
		for (Entity e : ents)
			if (!e.equals(base) && e.layer == base.layer)
				if (2*Math.abs(base.posx - e.posx) < base.hitx + e.hitx 
				 && 2*Math.abs(base.posy - e.posy) < base.hity + e.hity)
					base.newTouching.add(e);
	}
}
