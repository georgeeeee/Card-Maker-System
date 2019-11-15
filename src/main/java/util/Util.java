package util;

import java.util.UUID;

public class Util {
	public final static String[] pageNames = {"front", "innerLeft", "innerRight"};
	
	public static String generateUniqueId() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
