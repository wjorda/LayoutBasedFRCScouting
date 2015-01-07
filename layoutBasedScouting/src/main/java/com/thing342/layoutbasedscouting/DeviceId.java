package com.thing342.layoutbasedscouting;

public enum DeviceId {
	RED_1("red1", "Red 1", 1, 0xffD42828), RED_2("red2", "Red 2", 2, 0xFFD42828), RED_3("red3", "Red 3", 3, 0xFFD42828), 
	BLUE_1("blue1", "Blue 1", 4, 0xff456799), BLUE_2("blue2", "Blue 2", 5, 0xff456799), BLUE_3("blue3", "Blue 3", 6, 0xff456799), 
	OTHER("test", "Other", 0, 0xff2CAB2C);
	
	public String filename;
	public String name;
	public int value;
	public int hexColor;
	
	private DeviceId(String filename, String name, int value, int hexColor) {
		this.filename = filename;
		this.name = name;
		this.value = value;
		this.hexColor = hexColor;
	}
	
	public static DeviceId getFromValue(int value) {

		switch (value) {
			case 1: return RED_1;
			case 2: return RED_2;
			case 3: return RED_3;
			case 4: return BLUE_1;
			case 5: return BLUE_2;
			case 6: return BLUE_3;
			default: return OTHER;
		}
	}
	
	public String toString() { return name; }

	public static DeviceId getFromValue(String string) {
		if (string.contains("1")) return RED_1;
		if (string.contains("2")) return RED_2;
		if (string.contains("3")) return RED_3;
		if (string.contains("4")) return BLUE_1;
		if (string.contains("5")) return BLUE_2;
		if (string.contains("6")) return BLUE_3;
		return OTHER;
	}
}
