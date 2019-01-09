package Entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Frontend.GamePanel;
import GLoader.*;

public class Room {
	
	private static final double SRAT = GamePanel.SRAT;	//Screen Ratio
	
	public  BufferedImage       room;			//Final image of the room at the given moment
	
	private final BiIndexedList map;			//Base unchanging image of the room without entities
	public  final byte[][]      rom;			//Map of room items such as floors, air, and entity starting positions
	public  final double		width, height, conv, maxw, maxh;
	public  final int           pixelw, pixelh;
	
	public  ArrayList<Entity> ents = 
			new ArrayList<Entity>();			//List of entities contained within the room. ents[0] is the player
	
	public Room(Player p) {
		
		//Load the room's rom, including all entities (positions unset)
		RomAndEntList ml;
		
		rom = (ml = Fetcher.loadRoomRom(MemDict.getRoom(p.roomid))).rom;
		for (int id : ml.ents) ents.add(id == 0 ? p : MemDict.getEntity(id));
		
		//Load the images of the room
		map = Fetcher.loadRoomImg(MemDict.getRoom(p.roomid));
		
		//Set room dimensions and mathematical constants
		width  = rom[0].length;
		height = rom   .length;
		
		maxw = width  - 40;
		maxh = height - 40*SRAT;
		
		conv = map.bi[map.main].getWidth()*1.0/width;
		
		pixelw = (int)(40*conv);
		pixelh = (int)(40*conv*SRAT);
		
		//Set room dimensions
		room = new BufferedImage(pixelw, pixelh, BufferedImage.TYPE_INT_ARGB);
		
		//Set the positions of all entities in the room
		byte pos;
		for (int y = rom.length-1; y >= 0 ; y--)
			for (int x = 0; x < rom[0].length; x++)
				if ((pos = rom[rom.length - y - 1][x]) < 0)
					ents.get(pos+128).setPos(x, y+5);
	}
	
	/**
	 * Draws entities to room, then crops the screen to focus on the first entity in ents
	 * Stored to be used by GamePanel
	 */
	public void update() {
		
		Graphics g = room.getGraphics();
		
		//Game Square position of top right corner of the screen on the rom
		double sx = Math.min(Math.max(         ents.get(0).posx - 20     , 0), maxw),
			   sy = Math.min(Math.max(height - ents.get(0).posy - 20*SRAT, 0), maxh);
		
		int layer = -map.main;
		
		for (BufferedImage bi : map.bi)
			g.drawImage(drawLayer(bi, layer++, sx, sy), 0, 0, null);
	}
	
	/**
	 * Return a entity filled copy of the selected layer, cropped to show the correct region of the screen
	 * @param base image of layer
	 * @param layer number relative to main
	 * @param game square location of the top left corner of the screen, x coord
	 * @param game square location of the top left corner of the screen, y coord
	 * @return drawn layer of screen to be placed on room
	 */
	private BufferedImage drawLayer(BufferedImage base, int layer, double sx, double sy) {
		
		double lConv = base.getWidth()*1.0/width;						//Like conv, converts between game squares and this layer's pixel location
		int screenx = (int)((base.getWidth () - pixelw)/(maxw) * sx);	//These two huge things take the percent the screen has traversed
		int screeny = (int)((base.getHeight() - pixelh)/(maxh) * sy);	//main, and applies this percentage to the current layer.
		
		//Make a clone of the image
		BufferedImage bi = new BufferedImage(pixelw, pixelh, BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.getGraphics();
		
		g.drawImage(base.getSubimage(screenx, screeny, pixelw, pixelh), 0, 0, null);
		
		//Draw all entities that belong to this layer. Uncomment the block text to view POS, VEL, and HP displayed in real time next to the entity
		for (Entity i : ents) 
			if (i.layer == layer) {
				
				g.drawImage(i.text(), (int) (         i.posx *lConv) - i.textwidth /2 - screenx, 
						              (int)((height - i.posy)*lConv) - i.textheight/2 - screeny, null);
		
				///*
				g.drawString(" POS: (" + (int)i.posx + ", " + (int)i.posy + ")", (int)(i.posx*lConv) + i.textwidth/2 - screenx, (int)((height - i.posy)*lConv) - i.textheight/2		 - screeny);
				g.drawString(" VEL: (" + (int)i.velx + ", " + (int)i.vely + ")", (int)(i.posx*lConv) + i.textwidth/2 - screenx, (int)((height - i.posy)*lConv) - i.textheight/2 + 15 - screeny);
				g.drawString("  HP: (" + (int)i.health                    + ")", (int)(i.posx*lConv) + i.textwidth/2 - screenx, (int)((height - i.posy)*lConv) - i.textheight/2 + 30 - screeny);
				g.drawRect((int)((i.posx - i.hitx/2)*lConv) - screenx, (int)((height - i.posy - i.hity/2)*lConv) - screeny , (int)(i.hitx*lConv), (int)(i.hity*lConv));
				//*/
			}
		
		return bi;
	}
	
	/*
	 * //Make a clone of the image
		BufferedImage bi = new BufferedImage(base.getWidth(), base.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.getGraphics();
		
		//Like conv, converts between game squares and this layer's pixel location
		double lConv = bi.getWidth()*1.0/width;
		
		//Draw the base image
		g.drawImage(base, 0, 0, null);	//TODO this line is currently the most time consuming line in the game
		
		//Draw all entities that belong to this layer. Uncomment the block text to view POS, VEL, and HP displayed in real time next to the entity
		for (Entity i : ents) 
			if (i.layer == layer) {
				
				g.drawImage(i.text(), (int)(i.posx*lConv) - i.textwidth/2, (int)((height - i.posy)*lConv) - i.textheight/2, null);
		
				/*
				g.drawString(" POS: (" + (int)i.posx + ", " + (int)i.posy + ")", (int)(i.posx*lConv) + i.textwidth/2, (int)((height - i.posy)*lConv) - i.textheight/2);
				g.drawString(" VEL: (" + (int)i.velx + ", " + (int)i.vely + ")", (int)(i.posx*lConv) + i.textwidth/2, (int)((height - i.posy)*lConv) - i.textheight/2 + 15);
				g.drawString("  HP: (" + (int)i.health                    + ")", (int)(i.posx*lConv) + i.textwidth/2, (int)((height - i.posy)*lConv) - i.textheight/2 + 30);
				g.drawRect((int)((i.posx - i.hitx/2)*lConv), (int)((height - i.posy - i.hity/2)*lConv), (int)(i.hitx*lConv), (int)(i.hity*lConv));
				//
			}
		
		return bi.getSubimage((int)((bi.getWidth () - pixelw)/(maxw) * sx), 	//These two huge things take the percent the screen has traversed
							  (int)((bi.getHeight() - pixelh)/(maxh) * sy),   	//main, and applies this percentage to the current layer.
							  pixelw, pixelh);	
		*/
}