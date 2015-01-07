package com.thing342.layoutbasedscouting;

/**
 * An identifier for which position a team occupies during a match.
 * <p/>
 * <b>Values</b>
 * <table>
 * <tr>
 * <td>#</td></td><td>Value</td><td>Meaning</td>
 * </tr>
 * <tr bgcolor=red>
 * <td>1</td></td><td>RED_1</td><td>The Red Alliance member whose station is closest to the stands.</td>
 * </tr>
 * <tr bgcolor=red>
 * <td>2</td><td>RED_2</td><td>The Red Alliance member whose station is in the middle.</td>
 * </tr>
 * <tr bgcolor=red>
 * <td>3</td><td>RED_3</td><td>The Red Alliance member whose station is furthest from the stands.</td>
 * </tr>
 * <tr bgcolor=blue>
 * <td>4</td><td>BLUE_1</td><td>The Blue Alliance member whose station is closest to the stands.</td>
 * </tr>
 * <tr bgcolor=blue>
 * <td>5</td><td>BLUE_2</td><td>The Blue Alliance member whose station is in the middle.</td>
 * </tr>
 * <tr bgcolor=blue>
 * <td>6</td><td>BLUE_3</td><td>The Blue Alliance member whose station is furthest from the stands.</td>
 * </tr>
 * <tr>
 * <td>~</td><td>OTHER</td><td>For testing purposes.</td>
 * </tr>
 * </table>
 */
public enum DeviceId
{
    RED_1("red1", "Red 1", 1, 0xffD42828), RED_2("red2", "Red 2", 2, 0xFFD42828), RED_3("red3", "Red 3", 3, 0xFFD42828),
    BLUE_1("blue1", "Blue 1", 4, 0xff456799), BLUE_2("blue2", "Blue 2", 5, 0xff456799), BLUE_3("blue3", "Blue 3", 6, 0xff456799),
    OTHER("test", "Other", 0, 0xff2CAB2C);

    public String filename;
    public String name;
    public int value;
    public int hexColor;

    private DeviceId(String filename, String name, int value, int hexColor)
    {
        this.filename = filename;
        this.name = name;
        this.value = value;
        this.hexColor = hexColor;
    }

    /**
     * Parses a <code>DeviceId</code> from an integer value. (See table)
     *
     * @param value an integer value
     * @return A DeviceID parsed from the integer input.
     */
    public static DeviceId getFromValue(int value)
    {
        DeviceId[] deviceIds = DeviceId.values();
        if (value <= deviceIds.length && value > 0)
            return DeviceId.values()[value - 1];
        else return DeviceId.OTHER;
    }

    /**
     * Parses a <code>DeviceId</code> from a String. (See Table)
     *
     * @param string
     * @return
     */
    public static DeviceId getFromValue(String string)
    {
        if (string.contains("1")) return RED_1;
        if (string.contains("2")) return RED_2;
        if (string.contains("3")) return RED_3;
        if (string.contains("4")) return BLUE_1;
        if (string.contains("5")) return BLUE_2;
        if (string.contains("6")) return BLUE_3;
        return OTHER;
    }

    public String toString()
    {
        return name;
    }
}
