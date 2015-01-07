package com.thing342.layoutbasedscouting.fields;

import com.thing342.layoutbasedscouting.Rating;

/**
 * A group of primitive and enum wrapper classes with public default constructors, instantiable by
 * <code>Class.newInstance()</code> This will (hopefully) be used to allow fields to
 * become more extensible by eliminating the need for hardcoded instantiation of new data entries in
 * data sets.
 * Created by wes on 1/7/15.
 */
class InstantiableTypes
{
    /**
     * A wrapper for <code>Integer</code> with a default constructor.
     */
    static class InstantiableInteger
    {
        int value = 0;
    }

    /**
     * A wrapper for <code>Boolean</code> with a default constructor.
     */
    static class InstantiableBoolean
    {
        boolean value = false;
    }

    /**
     * A wrapper for <code>Rating</code> with a default constructor.
     */
    static class InstantiableRating
    {
        Rating value = Rating.NA;
    }
}
