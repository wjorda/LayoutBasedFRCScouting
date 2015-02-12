package com.thing342.layoutbasedscouting.fields;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.thing342.layoutbasedscouting.Field;
import com.thing342.layoutbasedscouting.R;
import com.thing342.layoutbasedscouting.ScoutingApplication;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Element;

/**
 * A <code>Field</code> for writing un-formatted text.
 */
public class Notes extends Field<String>
{

    private final int resId = R.layout.note;
    private String hint;
    private EditText textBox;

    static {
        ScoutingApplication.addField("notes", Notes.class);
    }

    private String id = "";


    public Notes()
    {
        super("");
    }

    /**
     * @param hint Text that will be displayed if the textbox is empty.
     */
    public Notes(String hint)
    {
        super("");
        this.hint = hint;
    }

    @Override
    public View getView(Context context, String initValue)
    {
        View v = LayoutInflater.from(context).inflate(resId, null);
        textBox = ((EditText) v.findViewById(R.id.value));
        textBox.setHint(hint);
        textBox.setText(initValue);
        return v;
    }

    @Override
    public void setUp(Element e)
    {
        hint = e.getAttribute("hint");
        id = e.getAttribute("id");
    }

    @Override
    public String getValue()
    {
        String text = textBox.getText().toString();
        text = text.replace(",", "_");
        text = text.replace(";", "_");
        text = text.replace("\"", "_");
        text = text.replace("\n", "_");
        return text;
    }

    @Override
    public String parse(String value)
    {
        return value;
    }

    @Override
    public String parse(JSONObject value)
    {
        return value.optString("notes", "");
    }

    @Override
    protected JSONObject getJSON(boolean flag) throws JSONException
    {
        JSONObject object = new JSONObject();
        object.put("notes", getValue());
        return object;
    }

    @Override
    public String getId()
    {
        return id;
    }

    /**
     * @return The text that is displayed if the textbox is left empty.
     */
    public String getName()
    {
        return hint;
    }
}
