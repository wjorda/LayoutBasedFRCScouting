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
    public abstract T getEntry();

    public abstract void setValue(Object value);

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

        @Override
        public void setValue(Object i)
        {
            value = (int) i;
        }

        @Override
        public String toString()
        {
            return Integer.toString(value);
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

        @Override
        public void setValue(Object b)
        {
            if (b instanceof Integer)
                value = ((Integer) b == 1);
            else
                value = (Boolean) b;
        }

        @Override
        public String toString()
        {
            return Integer.toString(value ? 1 : 0);
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

        @Override
        public void setValue(Object r)
        {
            value = (Rating) r;
        }

        @Override
        public String toString()
        {
            return value.toString();
        }
    }

}
