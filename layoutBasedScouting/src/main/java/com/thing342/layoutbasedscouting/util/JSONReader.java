package com.thing342.layoutbasedscouting.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by wes on 2/7/15.
 */
public class JSONReader
{
    private final File file;

    public JSONReader(File file)
    {
        this.file = file;
    }

    public JSONObject readJson() throws IOException, JSONException
    {
        return new JSONObject(readFile());
    }

    public JSONArray readArray() throws IOException, JSONException
    {
        return new JSONArray(readFile());
    }

    private String readFile() throws IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder text = new StringBuilder();
        String ln;
        while ((ln = reader.readLine()) != null) {
            text.append(ln);
        }

        return text.toString();
    }

}
