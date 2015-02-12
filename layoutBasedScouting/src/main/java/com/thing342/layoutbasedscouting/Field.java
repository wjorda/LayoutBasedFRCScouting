package com.thing342.layoutbasedscouting;

import android.content.Context;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Element;

/**
 * Base class for data fields.
 *
 * @param <T> Data type returned by the field. This <b>MUST</b> be instantiable via a default
 *            constructor.
 */
public abstract class Field<T>
{

    private final T t;
    private String name;

    /**
     * Constructor that must be called by subclasses. This must be called so that
     * <code>getType()</code> returns the proper classname.This classname is used to instantiate
     * objects in datasets.
     *
     * @param exampleObject An example value that would be returned by this field. Its value is not
     *                      used is any way, just the class name.
     */
    public Field(T exampleObject)
    {
        this.t = exampleObject;
    }

    /**
     * Returns a <code>Class</code> object representing the type of data collected by this field.
     *
     * @return a <code>Class</code> object representing the type of data collected by this field.
     */
    public Class getType()
    {
        return t.getClass();
    }

    /**
     * Returns the title displayed next to the field.
     *
     * @return the title displayed next to the field.
     */
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public JSONObject get()
    {
        try {
            JSONObject j = getJSON(true);
            return j;
        } catch (JSONException j) {
            Log.e("Exception", j.getMessage(), j);
            return null;
        }
    }

    /**
     * Abstract method for retrieving the <code>View</code> displayed by this field.
     *
     * @param context   <code>Context</code> used for inflating <code>View</code>s
     * @param initValue Data that should be displayed by the <code>View</code>.
     * @return View to be displayed in the scouting screen.
     */
    public abstract View getView(Context context, T initValue);

    public abstract void setUp(Element e);

    public abstract T getValue();

    public abstract T parse(String value);

    public abstract T parse(JSONObject value);

    protected abstract JSONObject getJSON(boolean flag) throws JSONException;

    public abstract String getId();
}
