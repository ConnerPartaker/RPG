package GLoader;

import java.util.HashMap;
import java.util.Map;

import Entities.*;

public class MemDict {
	
	private static final Map<String, Integer> ids  = genIDS();		//Dictionary returning id of named object (For ease of changing things)
	
	private static Map<String, Integer> genIDS() {
		
		Map<String, Integer> r = new HashMap<String, Integer>();
		
		r.put("Player", 0x0000);
		r.put("Bee", 0x0C00);
		r.put("Hook", 0x0001);
		r.put("Screen_Grab", 0x0050);
		r.put("Text", 0x0051);
		r.put("Room_Change", 0x0052);
		r.put("Sun", 0x08A0);
		r.put("Moon", 0x08A1);
		r.put("Day_Clouds", 0x08A2);
		r.put("Night_Clouds", 0x08A3);

		r.put("Overworld", 0x0000);
		r.put("Room1", 0x0001);
		
		return r;
	}
	
	public static Entity getEntity(int id) {
		switch(id) {
		
		case 0x0C00: return new Bee(id);
		//case 0x0001: return new Hook(id);
		//case 0x0050: return new Screen_Grab(id);
		//case 0x0051: return new Text(id);
		//case 0x0052: return new Room_Change(id);
		//case 0x08A0: return new Sun(id);
		//case 0x08A1: return new Moon(id);
		//case 0x08A2: return new Day_Clouds(id);
		//case 0x08A3: return new Night_Clouds(id);
			
		default: return null;
		
		}
	}
	public static String getRoom(int id) {
		switch(id) {
		
		case 0x0000: return "Overworld";
		default: return null;
		}
	}
	public static int get(String name) {
		return ids.get(name);
	}
}