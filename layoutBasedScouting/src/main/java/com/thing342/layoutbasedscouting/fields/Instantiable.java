package com.thing342.layoutbasedscouting.fields;

import com.thing342.layoutbasedscouting.Rating;

/**
 * An interface for creating wrapper classes for types that do not have a default have a default
 * constructor. Implementing types should create a 'default' instance of the enclosed type and then
 * return it in their implementation of <code>create()</code>.
 * @param <T> Type the implementing class is wrapping.
 */
public interface Instantiable<T>
{
    /**
     * This
     * @return A 'default' version of the enclosed type.
     */
    public T getEntry();

    /**
     * A wrapper for <code>Integer</code> with a default constructor.
     */
    public static class InstantiableInteger implements Instantiable<Integer>
    {
        public int value = 0;

        @Override
        public Integer getEntry()
        {
            return value;
        }
    }

    /**
     * A wrapper for <code>Boolean</code> with a default constructor.
     */
    public static class InstantiableBoolean implements Instantiable<Boolean>
    {
        public boolean value = false;

        @Override
        public Boolean getEntry()
        {
            return value;
        }
    }

    /**
     * A wrapper for <code>Rating</code> with a default constructor.
     */
    public static class InstantiableRating implements Instantiable<Rating>
    {
        public Rating value = Rating.NA;

        @Override
        public Rating getEntry()
        {
            return value;
        }
    }

}
