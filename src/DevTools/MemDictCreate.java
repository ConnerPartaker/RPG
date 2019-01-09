package DevTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class MemDictCreate {
	
	private static final String path = System.getProperty("user.dir") + "/tmp/rom/";
	
	
	public static String[] getNames(String type) {
		List<String> f = Arrays.asList(new File(path + type).list()).stream().map(f1 -> f1.substring(0, f1.length() - 4)).collect(Collectors.toList());
		System.out.println(f.toString());
		return f.toArray(new String[f.size()]);
	}
	
	public static void read(String type, int dict) {
		
		BufferedReader br = null;
		
		try {
			
			br = new BufferedReader(new FileReader(path + type + " Book.csv"));
			
			br.readLine();
			br.readLine();
			String input;
			while ((input = br.readLine()) != null) {
				String[] args = input.split(",");
				for (int i = 1; i < args.length; i += 3)
					if (!args[i].equals(""))
						
						switch(dict) {
							
							case 1: System.out.println("r.put(0x0" + Integer.toHexString(i/3).toUpperCase() + (args[0].length() < 2 ? "0":"") + args[0] +  
									(type.equals("Room")?", \"Rooms/":", \"Entities/") + args[i] + "\");");	
								
									break;
									
							case 2: System.out.println("r.put(\"" + args[i] + "\", 0x0" + Integer.toHexString(i/3).toUpperCase() + (args[0].length()==1?"0":"") + args[0] + ");");				
									
									break;
									
							case 3: System.out.println("case " + "0x0" + Integer.toHexString(i/3).toUpperCase() + (args[0].length()==1?"0":"") + args[0] + ": return new " +
									args[i] + "(id);");
						}
			}
		} catch (Exception e) {
		} finally {
			try {br.close();}
			catch (Exception e) {}
		}
	}
	public static void read()  {
		read("Entity", 1);
		System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
		read("Room"  , 1);
		System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
		read("Entity", 2);
		System.out.println("");
		read("Room"  , 2);
		System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
		read("Entity", 3);
	}
	
	public static void main(String[] args) {

		read();
	}
}

