package org.team2363.bluealliance;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class CachingBlueAllianceAPIClient extends BlueAllianceAPIClient
{
    private static final DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
    private String cachingDir;

    public CachingBlueAllianceAPIClient(String appId, String cachingDir)
    {
        super(appId);
        this.cachingDir = cachingDir;
        if (!cachingDir.endsWith("/")) this.cachingDir += "/";
    }

    @Override
    public String getHTML(String url)
    {
        try {
            String requestCode = getRequestCode(url);
            String cache = readCache(requestCode);
            String localLastModified = dateFormat.format(0);

            if (cache != null) {
                JSONObject cacheJSON = new JSONObject(cache);
                localLastModified = cacheJSON.get("last_modified").toString();
                JSONArray arr = cacheJSON.optJSONArray("cache_array");
                if (arr != null) cache = arr.toString();
            }

            if (cache != null) Log.d("jarray", cache);

            URL oracle;
            HttpURLConnection conn;
            String line;
            BufferedReader rd;
            StringBuilder result = new StringBuilder();

            try {
                oracle = new URL(url);
                conn = (HttpURLConnection) oracle.openConnection();
                conn.setRequestMethod("GET");
                conn.addRequestProperty("X-TBA-App-Id", getAppId());
                conn.addRequestProperty("If-Modified-Since ", localLastModified);

                int responseCode = conn.getResponseCode();

                if (cache != null && responseCode == HttpURLConnection.HTTP_NOT_MODIFIED) {
                    Log.d("Caching", "Server returned 304. Using cache...");
                    return cache;
                } else if (responseCode == HttpURLConnection.HTTP_OK) {
                    rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = rd.readLine()) != null) {
                        result.append(line).append("\n");
                    }
                    rd.close();
                    writeCache(result.toString(), requestCode);
                    return result.toString();
                } else {
                    Log.d("ResponseCode", "HTTP " + responseCode);
                    Log.d("URL", url);
                }

            } catch (MalformedURLException e) {
                Log.e("error", "error", e);
            } catch (IOException e) {
                Log.e("error", "error", e);
            }

            if (cache != null) return cache;
            else return null;
        } catch (JSONException e) {
            StringBuilder newlined = new StringBuilder();
            for (int i = 0; i < e.getMessage().length(); i++) {
                if (i % 80 == 0) newlined.append("\n");
                newlined.append(e.getMessage().charAt(i));
            }
            Log.e("error", newlined.toString(), e);
            return null;
        }
    }

    private String getRequestCode(String url)
    {
        String code = url.replace("http://www.thebluealliance.com/api/v2/", "");
        code = code.replace("/", "_");
        return code;
    }

    private void writeCache(final String text, final String requestCode)
    {
        try {
            if (text.startsWith("{")) {
                JSONObject cache = new JSONObject(text);
                cache.put("last_modified", dateFormat.format(System.currentTimeMillis()));

                BufferedWriter out = null;
                try {
                    out = new BufferedWriter(new FileWriter(cachingDir + requestCode + ".json"));
                    out.write(cache.toString());
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (text.startsWith("[")) {
                JSONArray cache = new JSONArray(text);
                JSONObject cacheObject = new JSONObject();
                cacheObject.put("last_modified", dateFormat.format(System.currentTimeMillis()));
                cacheObject.put("cache_array", cache);

                try {
                    BufferedWriter out = new BufferedWriter(new FileWriter(cachingDir + requestCode + ".json"));
                    out.write(cacheObject.toString());
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (JSONException e) {
            Log.e("error", "error", e);
        }
    }

    private String readCache(String requestCode)
    {
        try {
            BufferedReader r = new BufferedReader(new FileReader(cachingDir + requestCode + ".json"));
            String line;
            StringBuilder text = new StringBuilder();
            while ((line = r.readLine()) != null) text.append(line).append("\n");
            r.close();
            return text.toString();
        } catch (FileNotFoundException e) {
            Log.e("error", "error", e);
            return null;
        } catch (IOException e) {
            Log.e("error", "error", e);
            return null;
        }
    }
}
