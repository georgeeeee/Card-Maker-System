package util;

import java.util.ArrayList;
import java.util.UUID;

public class Util {
	public final static ArrayList<String> pageNames = new ArrayList<String>() {{
	    add("front");
	    add("innerLeft");
	    add("innerRight");
	}};
	
	public static String generateUniqueId() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
