package com.thing342.layoutbasedscouting.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by wes on 1/8/15.
 */
public class IterableHashMap<K, V> extends HashMap<K, V> implements Iterable<HashMap.Entry<K, V>>
{
    @Override
    public Iterator<Entry<K, V>> iterator()
    {
        return entrySet().iterator();
    }

    public ArrayList<V> getValues()
    {
        ArrayList<V> values = new ArrayList<V>();

        for (Entry<K, V> entry : this) {
            values.add(entry.getValue());
        }

        return values;
    }

    public ArrayList<K> getKeys()
    {
        ArrayList<K> keys = new ArrayList<K>();

        for (Entry<K, V> entry : this) {
            keys.add(entry.getKey());
        }

        return keys;
    }
}
