package com.thing342.layoutbasedscouting;

public enum Rating
{
    NA(0), POOR(1), MEDIOCRE(2), GOOD(3), GREAT(4);

    public final int value;

    private Rating(int value)
    {
        this.value = value;
    }

    public static Rating parseValue(float value)
    {

        int rValue = Math.round(value);

        switch (rValue) {
            case 0:
                return NA;
            case 1:
                return POOR;
            case 2:
                return MEDIOCRE;
            case 3:
                return GOOD;
            case 4:
                return GREAT;
            default:
                return null;
        }
    }

    public static Rating parse(String s)
    {
        if (s.startsWith("s_")) {
            if (parsable(s.substring(2))) {
                Rating returnable = parseValue(Integer.parseInt((s.substring(2))));
                if (returnable != null) return returnable;
                else return null;
            }
        }

        return null;
    }

    private static boolean parsable(String s)
    {
        boolean parsable = true;
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            parsable = false;
        }

        return parsable;
    }

    public String toString()
    {
        return "s_" + Integer.toString(value);
    }
}
