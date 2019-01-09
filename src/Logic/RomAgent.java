package Logic;

import Entities.Entity;

public class RomAgent {
	
	public static double t = GameAgent.gameTimer;
	private byte[][] rom;
	
	public RomAgent(byte[][] rom) {
		this.rom = rom;
		
	}
	
	/**
	 * Looks at the entity and where it wants to move, and decides where it can move based on current wall positions.
	 * Involks the grounded function of an entity
	 * @param entity to be moved
	 */
	public void move(Entity i) {
		
		//Apply gravity. If the object is detected grounded, this will be nullified automatically
		i.vely += t*i.grav;
		
		//Calculate what of the next frame of movement is possible, affect positions and velocities accordingly.
		double fx = i.posx + t*(i.velx + i.extVelx) + (i.velx + i.extVelx <= 0 ? -1:1)*i.hitx/2;
		double fy = i.posy + t*(i.vely + i.extVely) + (i.vely + i.extVely <= 0 ? -1:1)*i.hity/2;
		
		if (romSweepY(fx, i.posy, i.hity/2)) {
			
			i.posx = i.velx + i.extVelx > 0 ? (int)fx - i.hitx/2 : (int)fx + 1 + i.hitx/2;
			i.velx = 0; i.extVelx = 0;
			
		} else {
			
			i.posx += t*(i.velx + i.extVelx);
			i.extVelx -= Math.abs(i.extVelx) < 1 ? i.extVelx : 20*Math.signum(i.extVelx)*t;
		}
		
		if (romSweepX(i.posx, fy, i.hitx/2)) {
			
			i.posy = i.vely + i.extVely > 0 ? (int)fy - i.hity/2 : (int)fy + 1 + i.hity/2;
			i.vely = 0; i.extVely = 0;
			
		} else {
			
			i.posy += t*(i.vely + i.extVely);
			i.extVely -= Math.abs(i.extVely) < 1 ? i.extVely : 20*Math.signum(i.extVely)*t;
		}
		
		
		//Check entities for their grounded state, if grounded do the grounded function.
		if(romSweepX(i.posx, i.posy - i.hity/2 - .01, i.hitx/2) && i.vely + i.extVely <= 0)		
			i.grounded();
	}
	
	
	
	/**
	 * Checks whether a given space is occupied by a wall in the room rom.
	 * NOTE: Point must be in a wall - not on a wall - to return true.
	 * i.e. the points must be more than .01 game squares inside the wall.
	 * @param x coord of location
	 * @param y coord of location
	 * @param range of test
	 * @return whether either space is occupied by a wall
	 */
	private boolean romSweepX(double x, double y, double delta) {
		for (int i = 0; i < 2*delta/.9; i++)
			if (cutsRom(x-delta + i*.5, y))
				return true;
		
		if (cutsRom(x+delta, y))
			return true;
		
		return false;
	}
	
	private boolean romSweepY(double x, double y, double delta) {
		for (int i = 0; i < 2*delta/.5; i++)
			if (cutsRom(x, y-delta + i*.5))
				return true;
		
		if (cutsRom(x, y+delta))
			return true;
		
		return false;
	}
	
	private boolean cutsRom(double x, double y) {
		return rom[rom.length - 1 - (int)y][(int)(x)] == 1 && Math.max(Math.abs(x-(int)x-.5), Math.abs(y-(int)y-.5)) < .5 - .01;
	}
}