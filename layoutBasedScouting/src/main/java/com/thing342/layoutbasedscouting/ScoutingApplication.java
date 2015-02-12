package com.thing342.layoutbasedscouting;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.thing342.layoutbasedscouting.fields.Checkbox;
import com.thing342.layoutbasedscouting.fields.Counter;
import com.thing342.layoutbasedscouting.fields.Divider;
import com.thing342.layoutbasedscouting.fields.Notes;
import com.thing342.layoutbasedscouting.fields.RatingStars;
import com.thing342.layoutbasedscouting.fields.Slider;
import com.thing342.layoutbasedscouting.recyclerush.ToteStacker;
import com.thing342.layoutbasedscouting.util.IterableHashMap;
import com.thing342.layoutbasedscouting.util.JSONReader;
import com.thing342.layoutbasedscouting.util.MimeTypes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.team2363.bluealliance.Event;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Class that stores global-level objects.
 */
public class ScoutingApplication extends Application
{

    public static final String PREFS = "com.thing342.layoutbasedscouting_preferences";
    public static final String XMLPATH_PREF = "xmlPath";
    public static final String DEVICEID_PREF = "deviceid";
    public static final String MATCHESFIST_PREF = "matchesFirst";
    private static final HashMap<String, Class<? extends Field>> fieldDictionary =
            new HashMap<String, Class<? extends Field>>();
    private static ScoutingApplication instance;
    public final IterableHashMap<Integer, FRCTeam> teamsList = new IterableHashMap<Integer, FRCTeam>();
    public final IterableHashMap<Integer, MatchGroup> groups = new IterableHashMap<Integer, MatchGroup>();
    public final IterableHashMap<String, FieldGroup> groupedData = new IterableHashMap<String, FieldGroup>();
    public final ArrayList<String> fieldIds = new ArrayList<String>();
    private final ArrayList<Field> data = new ArrayList<Field>();
    private View scoreLayout;
    private Event[] events;

    ///////////////////--CONSTRUCTORS--/////////////////////////////

    public ScoutingApplication()
    {
        /*
         * Instantiate built-in fields to get them added to field dictionary.
         */
        try {
            Counter.class.newInstance();
            Checkbox.class.newInstance();
            Divider.class.newInstance();
            Notes.class.newInstance();
            RatingStars.class.newInstance();
            Slider.class.newInstance();
            ToteStacker.class.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    ///////////////////--OVERRIDEN METHODS--///////////////////////

    /**
     * Adds a new field for the layout loader to look for.
     *
     * @param key        The name of the XML tag for the loaded to look for. This must be unique.
     * @param fieldClass Class object for the instance of <code>Field</code> to be added.
     * @throws IllegalArgumentException If multiple fields share the same tag.
     */
    public static void addField(String key, Class<? extends Field> fieldClass)
    {
        if (!fieldDictionary.containsKey(key)) fieldDictionary.put(key, fieldClass);
        else
            throw new IllegalArgumentException("Multiple fields may not share the same nametag: " + key);
    }

    public static ScoutingApplication getInstance()
    {
        return instance;
    }

    ///////////////////--PUBLIC METHODS--///////////////////////

    /**
     * Reloads XML layout from file.
     */
    @Override
    public void onCreate()
    {
        instance = this;
        data.clear();

        SharedPreferences prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String xmlFilePath = prefs.getString(XMLPATH_PREF, null);
        boolean firstLaunch = prefs.getBoolean("firstlaunch", true);

        if (xmlFilePath != null) {
            File xPath = new File(xmlFilePath);
            if (xPath.canRead()) {
                ////////Log.d("AerialAssault", xmlFilePath);
                changeLayout(xPath);
            }
        }
        /*if (firstLaunch) {
            new Thread( new Runnable()
            {
                @Override
                public void run()
                {
                    events = blueAlliance.eventListRequest(Calendar.getInstance().get(Calendar.YEAR));
                }
            }).start();
        }*/

        /*fieldDictionary.put("counter", Counter.class);
        fieldDictionary.put("checkbox", Checkbox.class);
        fieldDictionary.put("divider", Divider.class);
        fieldDictionary.put("rating", RatingStars.class);
        fieldDictionary.put("notes", Notes.class);*/
    }

    /**
     * Adds an extra match to the list of matches.
     *
     * @param teamNums Array containing the numbers of the teams participating in the new match
     */
    public void addExtraMatch(int teamNums[])
    {
        int matches = groups.size() + 1;

        for (int teamNum : teamNums) {
            this.getTeam(teamNum).createMatch(matches);
        }

        Toast.makeText(getApplicationContext(), "Added new match Q" + matches, Toast.LENGTH_SHORT).show();
        resetMatchGroups();
    }

    ///////////////////--FILE I/0 METHODS--////////////////////////

    /**
     * Changes the layout used on match screens.
     *
     * @param loc A <code>File</code> referencing the XML configuration file used for the layout.
     */
    public void changeLayout(File loc)
    {

        data.clear();
        scoreLayout = new LinearLayout(this);

        try {
            Document file = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(loc);
            NodeList nodes = file.getChildNodes().item(0).getChildNodes();
            FieldGroup groupData = new FieldGroup(0);
            boolean flag = true;
            String groupName = "";
            //for(int q = 0; q < nodes.getLength(); q++) Log.d("Nodes", nodes.item(q).getLocalName());

            Node n;
            Element e;
            for (int i = 0; i < nodes.getLength(); i++) {

                n = nodes.item(i);

                if (n.getNodeType() == Node.ELEMENT_NODE) e = (Element) n;
                else continue;

                Log.d("AerialAssault", e.getTagName() + " | " + fieldDictionary.size());

                if (e.getTagName().contains("group")) {
                    if (!flag) {
                        groupedData.put(groupName, groupData);
                        groupData = new FieldGroup(i);
                    }
                    groupName = e.getAttribute("name");
                    flag = false;

                } else {
                    Field f = fieldDictionary.get(e.getTagName()).newInstance();
                    f.setUp(e);
                    groupData.add(f);
                    fieldIds.add(f.getId());
                }

                Log.d("AerialAssist", e.getAttribute("id"));
            }

            groupedData.put(groupName, groupData);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("AerialAssault", "Error", e);
        }

        SharedPreferences.Editor prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit();
        prefs.putString(XMLPATH_PREF, loc.getAbsolutePath());

        ////////Log.d("AerialAssault", loc.getAbsolutePath() + " into sharedprefs");

        prefs.apply();

    }

    /**
     * Deletes all teams from the application and from external storage. <b>This cannot be undone.</b>
     */
    public void clearAll()
    {
        teamsList.clear();

        if (getFilesDir().isDirectory()) {
            String[] children = getFilesDir().list();
            for (String aChildren : children) {
                new File(getFilesDir(), aChildren).delete();
            }
        }
    }

    /**
     * Creates a match schedule from a list of teams.
     *
     * @param file A <code>File</code> referencing a CSV file which will be used to create te match
     *             schedule.
     */
    public void createMatchSchedule(File file)
    {

        int teamNum;
        int matchNum;

        teamsList.clear();
        CsvReader csvr;

        try {
            csvr = new CsvReader(file.getPath(), ',');
            //csvr.setRecordDelimiter(";")
            while (csvr.readRecord()) {

                matchNum = Integer.parseInt(csvr.get(0));

                for (int i = 1; i < csvr.getColumnCount(); i++) {
                    teamNum = Integer.parseInt(csvr.get(i));

                    if (!teamExists(teamNum))
                        teamsList.put(teamNum, new FRCTeam(teamNum, "")); //If team doesn't exist, add a new one
                    teamsList.get(teamNum).createMatch(matchNum);
                }
            }

            saveAll(true);
        } catch (Exception e) {
            Log.e("Exception Caught", e.getMessage(), e);
        }

        resetMatchGroups();
    }

    public void createMatchSchedule(String eventKey)
    {
        teamsList.clear();


    }

    /**
     * Exports all collected match data to a TXT file.
     *
     * @param exportName The name (without file extensions) to write the data to.
     */
    public void exportAll(String exportName)
    {
        SharedPreferences prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        boolean jsonExport = prefs.getBoolean("jsonExport", true);
        ExportTask et;
        if (jsonExport) et = new JSONExportTask();
        else et = new ExportTask();

        et.execute(exportName);
    }

    /**
     * Loads all match data and layout data from storage.
     */
    public void resumeAll()
    {

        File fileList[] = getFilesDir().listFiles();
        if (fileList.length == 0) {
            //initAll();
            return;
        }

        teamsList.clear();
        FRCTeam thisTeam;
        CsvReader csvr;

        try {
            for (File f : fileList) {
                thisTeam = new FRCTeam();
                //thisTeam.number = Integer.parseInt(stripExtension(f.getName()));
                thisTeam.number = Integer.parseInt(f.getName());

                csvr = new CsvReader(f.getPath(), ',');
                csvr.setRecordDelimiter(';');
                //csvr.readHeaders();
                //csvr.setHeaders(Match.RECORD_HEAD);

                while (csvr.readRecord()) {
                    thisTeam.matches.put(Integer.valueOf(csvr.getValues()[0]), new Match(thisTeam, csvr.getValues()));
                    //////////Log.d("AerialAssault", "Read " + csvr.getRawRecord());
                }
                teamsList.put(thisTeam.number, thisTeam);
            }
        } catch (Exception e) {
            Log.e("AerialAssault", e.getMessage(), e);
            //initAll();
        }

        resetMatchGroups();

    }

    /**
     * Loads all match data and layout data from storage.
     */
    public void resumeAll(boolean json)
    {

        File fileList[] = getFilesDir().listFiles();
        if (fileList.length == 0) {
            //initAll();
            return;
        }

        teamsList.clear();
        FRCTeam thisTeam;
        JSONReader jReader;

        try {
            for (File f : fileList) {
                thisTeam = new FRCTeam();
                //thisTeam.number = Integer.parseInt(stripExtension(f.getName()));
                thisTeam.number = Integer.parseInt(f.getName());

                jReader = new JSONReader(f);
                JSONArray jsonArray = jReader.readArray();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject match = jsonArray.getJSONObject(i);
                    thisTeam.matches.put(match.optInt("match_number"), new Match(thisTeam, match));
                }

                teamsList.put(thisTeam.number, thisTeam);
            }
        } catch (Exception e) {
            Log.e("AerialAssault", e.getMessage(), e);
            //initAll();
        }

        resetMatchGroups();

    }

    /**
     * Saves all collected match data to internal memory.
     */
    public void saveAll()
    {

        SaveTask st = new SaveTask();
        st.execute();
        resetMatchGroups();
    }

    /**
     * Saves all collected match data to internal memory.
     */
    public void saveAll(boolean json)
    {

        SaveTask st = new JSONSaveTask();
        st.execute();
        resetMatchGroups();
    }

    /////////////////--ACCESS METHODS--///////////////////////////

    /**
     * Returns an array containing all <code>FRCTeam</code>s with the given match number.
     *
     * @param matchNum The match number.
     * @return An array of all <code>FRCTeam</code>s participating in the given match.
     */
    public FRCTeam[] getTeamsWithMatch(int matchNum)
    {

        ArrayList<FRCTeam> teams = new ArrayList<FRCTeam>();

        for (Map.Entry<Integer, FRCTeam> entry : teamsList) {
            FRCTeam f = entry.getValue();
            if (f.getMatch(matchNum) != null) teams.add(f);
        }

        return teams.toArray(new FRCTeam[teams.size()]);
    }

    /**
     * Create dummy matches
     *
     * @deprecated Only used for dummy data.
     */
    void initAll()
    {
        FRCTeam team1 = new FRCTeam(1001, "PotatoBots");
        team1.createMatch(1);
        team1.createMatch(3);
        teamsList.put(1001, team1);
        FRCTeam team2 = new FRCTeam(1002, "FooBots");
        team2.createMatch(2);
        team2.createMatch(4);
        teamsList.put(1002, team2);
        FRCTeam team3 = new FRCTeam(1003, "BazBots");
        team3.createMatch(1);
        team3.createMatch(2);
        teamsList.put(1003, team3);
        FRCTeam team4 = new FRCTeam(1004, "NOT PAY FOR THIS TEAM");
        team4.createMatch(3);
        team4.createMatch(4);
        teamsList.put(1004, team4);
    }

    /**
     * Checks if a certain team exists.
     *
     * @param num Numbe rof the team to search for.
     * @return true if the team exists in the database.
     */
    boolean teamExists(int num)
    {
        return (getTeam(num) != null);
    }

    private void resetMatchGroups()
    {

        groups.clear();
        MatchGroup m;

        for (int i = 0; i < 314; i++) {
            FRCTeam[] r = getTeamsWithMatch(i);
            //Log.d("Teams", Arrays.toString(r));
            m = new MatchGroup(i, r);
            //Log.d("AerialAssault", "New Match Group " + i + " Size " + Integer.toString(m.teams.length));
            if (m.teams != MatchGroup.NULL) groups.put(i, new MatchGroup(i, getTeamsWithMatch(i)));

        }
    }

    /**
     * Returns the team with a given number, if it exists.
     *
     * @param teamNum The number of the team.
     * @return The <code>FRCTeam</code> using the number if it exists, <code>null</code> otherwise.
     */
    FRCTeam getTeam(int teamNum)
    {
        return teamsList.get(teamNum);
    }

    /**
     * Adds a team to the database.
     *
     * @param number Number of new team to add.
     */
    public void addTeam(int number)
    {
        FRCTeam newTeam = new FRCTeam(number, "New Team");
        newTeam.createMatch(-1);
        teamsList.put(number, newTeam);
        //////////Log.d("AerialAssault", "Added Team " + Integer.toString(number));
    }

    ////////////////--NESTED CLASSES--//////////////////////////////

    private interface OnDownloadFinishedListener
    {
        public void onDownloadFinished();
    }

    /**
     * An <code>AsyncTask</code> for saving match data to files in the background.
     */
    private class SaveTask extends AsyncTask<Object, Object, Object>
    {

        ArrayList<FRCTeam> teamArrayList;

        /**
         * Show a progress wheel while loading time map.
         */
        @Override
        protected void onPreExecute()
        {
            teamArrayList = teamsList.getValues();
            SharedPreferences prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
            DeviceId restoredId = DeviceId.getFromValue(prefs.getString(DEVICEID_PREF, "0"));
            exportAll(restoredId.filename);
        }

        @Override
        protected Object doInBackground(Object... params)
        {
            for (FRCTeam team : teamArrayList) {
                try {
                    //FRCTeam team = entry.getValue();
                    String FILEPATH = getFilesDir().getAbsolutePath() + File.separator + Integer.toString(team.number);// + ".csv";
                    CsvWriter csvw = new CsvWriter(FILEPATH);
                    csvw.setDelimiter(',');
                    csvw.setRecordDelimiter(';');

                    for (Map.Entry<Integer, Match> e : team.matches) {
                        csvw.writeRecord(e.getValue().getRecord(false));
                        ////////Log.d("AerialAssault", "Match written to " + FILEPATH);
                    }

                    csvw.flush();
                    csvw.close();
                    //writer.flush();
                    //writer.close();

                } catch (Exception e) {
                    ////////Log.e("AerialAssault", e.getMessage(), e);
                    Log.e("Exception", e.getMessage(), e);
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result)
        {
        }

    }

    /**
     * An <code>AsyncTask</code> for saving match data to files in the background.
     */
    private class JSONSaveTask extends SaveTask
    {

        @Override
        protected Object doInBackground(Object... params)
        {
            for (FRCTeam team : teamArrayList) {
                try {
                    JSONArray json = new JSONArray();
                    //FRCTeam team = entry.getValue();
                    String FILEPATH = getFilesDir().getAbsolutePath() + File.separator + Integer.toString(team.number);// + ".csv";
                    //CsvWriter csvw = new CsvWriter(FILEPATH);
                    //csvw.setDelimiter(',');
                    //csvw.setRecordDelimiter(';');

                    for (Map.Entry<Integer, Match> e : team.matches) {
                        //csvw.writeRecord(e.getValue().getRecord(false));
                        json.put(e.getValue().export());
                        ////////Log.d("AerialAssault", "Match written to " + FILEPATH);
                    }

                    FileWriter writer = new FileWriter(FILEPATH);
                    if (!new File(FILEPATH).exists()) new File(FILEPATH).mkdirs();
                    //Log.d("JSON", json.toString(5));
                    writer.write(json.toString(5));
                    writer.flush();
                    writer.close();

                    //csvw.flush();
                    //csvw.close();
                    //writer.flush();
                    //writer.close();

                } catch (Exception e) {
                    ////////Log.e("AerialAssault", e.getMessage(), e);
                    Log.e("Exception", e.getMessage(), e);
                }


            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result)
        {
        }

    }

    /**
     * An <code>AsyncTask</code> for merging collected data into a single file.
     */
    private class ExportTask extends AsyncTask<Object, Object, Object>
    {

        @Override
        protected Object doInBackground(Object... params)
        {
            try {
                Match thisMatch;
                String exportName = (String) params[0];
                //File fileList[] = getFilesDir().listFiles();
                String exportPath = Environment.getExternalStorageDirectory() + File.separator + "Scouting" + File.separator;
                CsvWriter csvw = new CsvWriter(exportPath + exportName + ".txt");
                csvw.setDelimiter(',');
                csvw.setTextQualifier('\"');

                if (!new File(exportPath).exists()) new File(exportPath).mkdirs();

                for (int i = 0; i < 314; i++) {
                    for (Map.Entry<Integer, FRCTeam> entry : teamsList) {
                        FRCTeam t = entry.getValue();
                        thisMatch = t.getMatch(i);
                        if (thisMatch != null && thisMatch.isEdited())
                            csvw.writeRecord(thisMatch.getRecord(true));
                    }
                }

                //csvw.flush();
                csvw.close();

                String path[] = {exportPath + exportName};
                String mime[] = {MimeTypes.csv.contentType};

                MediaScannerConnection.scanFile(getApplicationContext(), path, mime, null);

                return exportPath;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object result)
        {
            String path = (String) result;
            if (result != null) {
                Toast.makeText(getApplicationContext(), "Match Data exported to " + path,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Error saving data!",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    /*private class MatchDownloadTask extends AsyncTask<String, Object, org.team2363.bluealliance.Match[]>
    {

        @Override
        protected org.team2363.bluealliance.Match[] doInBackground(String... params)
        {
            String eventKey = params[0];
            int year = Integer.parseInt(params[1]);
            return blueAlliance.eventMatchRequest(year, eventKey);
        }

        @Override
        protected void onPreExecute()
        {
        }

        @Override
        protected void onPostExecute(org.team2363.bluealliance.Match[] result)
        {
            int teamNum;
            int matchNum;

            SharedPreferences prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
            DeviceId restoredId = DeviceId.getFromValue(prefs.getString(DEVICEID_PREF, "0"));
            
            for(org.team2363.bluealliance.Match match : result)
            {
                String team = match.getTeam(org.team2363.bluealliance.Match.Position.getInstance(restoredId.name));
                teamNum = Integer.parseInt(team.replace("frc", ""));
                matchNum = match.getMatchNumber();
                if (!teamExists(teamNum))
                    teamsList.put(teamNum, new FRCTeam(teamNum, "")); //If team doesn't exist, add a new one
                teamsList.get(teamNum).createMatch(matchNum);
            }
        }

    }*/

    private class JSONExportTask extends ExportTask
    {
        @Override
        protected Object doInBackground(Object... params)
        {
            try {
                /*
                    Step 1: Build JSON Object
                 */
                JSONArray array = new JSONArray();
                for (Map.Entry<Integer, FRCTeam> entry : teamsList) {
                    FRCTeam team = entry.getValue();
                    for (Map.Entry<Integer, Match> match : team.matches) {
                        JSONObject data = match.getValue().export();
                        if (match.getValue().isEdited())
                            array.put((Object) match.getValue().export());
                    }
                }
                
                /*
                    Step 2: Write to file
                 */
                String exportName = (String) params[0];
                String exportPath = Environment.getExternalStorageDirectory() + File.separator + "Scouting" + File.separator;
                FileWriter writer = new FileWriter(exportPath + exportName + ".json");
                if (!new File(exportPath).exists()) new File(exportPath).mkdirs();
                writer.write(array.toString(5));
                writer.flush();
                writer.close();

                String path[] = {exportPath + exportName};
                String mime[] = {MimeTypes.csv.contentType};

                MediaScannerConnection.scanFile(getApplicationContext(), path, mime, null);

                return exportPath;

            } catch (IOException e) {
                return null;
            } catch (JSONException e) {
                return null;
            }
        }

    }

    public class FieldGroup extends ArrayList<Field> implements Comparable<FieldGroup>
    {
        private final int order;

        private FieldGroup(int order)
        {
            this.order = order;
        }

        @Override
        public int compareTo(FieldGroup another)
        {
            return getOrder() - another.getOrder();
        }

        public int getOrder()
        {
            return order;
        }
    }
}
