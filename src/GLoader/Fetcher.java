package GLoader;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import javax.imageio.ImageIO;

public class Fetcher {
	
	private static final String IMGDIR   = System.getProperty("user.dir") + "/tmp/textures/";	//Entity and room textures
	private static final String ROMDIR   = System.getProperty("user.dir") + "/tmp/rom/";		//Room roms
	
	/**
	 * Loads all room images from memory the order to be drawn. The index given is the index of the main png
	 * Images are ordered u1, u2, ..., un, main, o1, o2, ..., om
	 * 
	 * @param room id
	 * @return The array of room images
	 */
	public static BiIndexedList loadRoomImg(String id) {
		
		ArrayList<File> f;
		
		BufferedImage[] bi;
		int index;
		
		try {
			//Read in the room image directory, a series of images, and sort them using FComp
			(f = new ArrayList<File>(Arrays.asList(new File(IMGDIR + "Rooms/" + id).listFiles()))).sort(new FComp());
			
			//Initialize the variables to be returned in a BiIndexedList
			bi = new BufferedImage[f.size()];	//Ordered array of room images
			index = 0;							//Index of main
			
			//Iterate over images, and if at index i we come across main.png, store i in index
			for(int i = 0; i < f.size(); i++) {
				bi[i] = ImageIO.read(f.get(i));
				if (f.get(i).toString().charAt((int) (f.get(i).toString().length() - 8)) == 'm')
					index = i;
			}
			
			//And send off the list
			return new BiIndexedList(bi, index);
		
		} catch (Exception e) {return null;}
	}
	
	
	public static BufferedImage loadEntImg(String id) {
		try {
			
			return ImageIO.read(new File(IMGDIR + "Entities/" + id + ".png"));
		
		} catch (Exception e) {return null;}
	}
	
	
	/**
	 * ROMS go [map height, map width, ... map ..., entity1, ...]
	 * In the map, codes 128 to 255 are entity identifiers, 128 reserved for either the player or a screen grab with 129 being the player
	 * Entities are ordered by their identifiers in the map, represented as 2 bytes being their entity id
	 * 
	 * @param room id
	 * @return Full map of room in game squares coded as follows
	 *      0 - Air
	 *      1 - Normal Ground
	 *      
	 *  and the entity list ordered in order of their identifier on the map
	 *  These two are returned as a MapAndEntList object with immutable public objects map and ent.
	 */
	public static RomAndEntList loadRoomRom(String id) {
		
		BufferedReader br = null;
		ArrayList<Integer> bArray = new ArrayList<Integer>();
		
		String input;
		int numLines;
		
		byte[][] map;
		int[] ents;
		
	    try {
	    	
	    	br = new BufferedReader(new FileReader(ROMDIR + "Rooms/" + id + ".csv"));
			
			//Read map into bArray
			numLines = 0;
			while(!(input = br.readLine()).contains("x") && numLines++ != -1)
				for (String i : input.split(",")) {
						bArray.add(Integer.parseInt(i));}
	    	
			map = new byte[numLines][bArray.size()/numLines];
			
			for (int i = 0; i < numLines; i++)
				for (int j = 0; j < map[0].length; j++)
					map[i][j] = (byte)((int)bArray.get(i * map[0].length + j));
			
			bArray.clear();
			
			for (String i : br.readLine().split(","))
				if (!i.equals(""))	
					bArray.add(MemDict.get(i));
	    	
			ents = bArray.stream().mapToInt(Integer::intValue).toArray();
			
			return new RomAndEntList(map, ents);
	    	
	    } catch (IOException e) {
	    	return new RomAndEntList(new byte[0][0], new int[0]);
	    } finally {
	    	try {br.close();} 
	    	catch (IOException e) {}
	    }
	}
	
	/**
	 * Loads the rom effect list of an entity
	 * 
	 * @param ent id
	 * @return int[][] with rows being custom effect rows, and these rows containing the ids of entities effected
	 */
	public static int[][] loadEntRom(String id) {
		
		BufferedReader br = null;
		ArrayList<ArrayList<Integer>> bArray = new ArrayList<ArrayList<Integer>>();
		
		String input;
		int max;
		
		int[][] ids;
		
	    try {
	    	
	    	br = new BufferedReader(new FileReader(ROMDIR + "Entities/" + id + ".csv"));
			
			max = 0;
			while((input = br.readLine()) != null) {
				ArrayList<Integer> a = new ArrayList<Integer>();
			
				for (String i : input.split(","))
					if (!i.equals(""))
						a.add(MemDict.get(i));
				
				max = Math.max(max, a.size());
				
				bArray.add(a);
			}
			
			ids = new int[bArray.size()][max];
			
			for (int i = 0; i < ids.length; i++)
				for (int j = 0; j < max; j++)
					if (j < bArray.get(i).size())
						ids[i][j] = bArray.get(i).get(j);
	    	
			return ids;
			
	    } catch (IOException e) {
	    	return new int[0][0];
	    } finally {
	    	try {br.close();} 
	    	catch (IOException e) {}
	    }
	}
}

class FComp implements Comparator<File> {

	public int compare(File arg1, File arg2) {
		String s1 = arg1.toString();
		String s2 = arg2.toString();
		
		s1 = s1.substring(s1.length() - 6, s1.length() - 4);
		s2 = s2.substring(s2.length() - 6, s2.length() - 4);
		
		if (s1.charAt(0) == s2.charAt(0)) {
			return s1.charAt(1) < s2.charAt(1) ? -1 : 1;
		} else {
			return s1.charAt(0) == 'u' || s2.charAt(0) == 'o' ? -1 : 1; 
		}
		
	}
}