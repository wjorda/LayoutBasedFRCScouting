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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
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
    public static final String FIRSTLAUNCH_PREF = "firstLaunch";
    public static final String MATCHESFIST_PREF = "matchesFirst";
    private static HashMap<String, Class<? extends Field>> fieldDictionary =
            new HashMap<String, Class<? extends Field>>();
    public IterableHashMap<Integer, FRCTeam> teamsList = new IterableHashMap<Integer, FRCTeam>();
    public IterableHashMap<Integer, MatchGroup> groups = new IterableHashMap<Integer, MatchGroup>();
    public ArrayList<Field> data = new ArrayList<Field>();
    public int matches = 0;
    private View scoreLayout;

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
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
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

    ///////////////////--PUBLIC METHODS--///////////////////////

    /**
     * Reloads XML layout from file.
     */
    @Override
    public void onCreate()
    {
        data.clear();

        SharedPreferences prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String xmlFilePath = prefs.getString(XMLPATH_PREF, null);

        if (xmlFilePath != null) {
            File xPath = new File(xmlFilePath);
            if (xPath.canRead()) {
                ////////Log.d("AerialAssault", xmlFilePath);
                changeLayout(xPath);
            }
        }

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

        Toast.makeText(getApplicationContext(), "Added new match Q" + matches, Toast.LENGTH_SHORT);
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
            //printNodeNames(nodes);

            Element e;
            for (int i = 0; i < nodes.getLength(); i++) {

                if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) e = (Element) nodes.item(i);
                else {

                    continue;
                }

                Log.d("AerialAssault", e.getTagName() + " | " + fieldDictionary.size());

                Field f = fieldDictionary.get(e.getTagName()).newInstance();
                f.setUp(e);
                data.add(f);
                /*
                if (e.getTagName().contains("counter")) {
                    Counter c = new Counter(Integer.parseInt(e.getAttribute("initValue")), e.getAttribute("name"));
                    data.add(new Counter(
                            Integer.parseInt(e.getAttribute("initValue")),
                            e.getAttribute("name")));
                    ////////Log.d("AerialAssault", "I SEE YOU HUMAN");
                } else if (e.getTagName().contains("checkbox")) {
                    data.add(new Checkbox(e.getAttribute("name")));
                    ////////Log.d("AerialAssault", "I SEE YOU HUMANHUMAN");
                } else if (e.getTagName().contains("rating")) {
                    data.add(new RatingStars(e.getAttribute("name"), Integer.parseInt(e.getAttribute("scale"))));
                    ////////Log.d("AerialAssault", "I SEE YOU HUMANHUMANHUMAN");
                } else if (e.getTagName().contains("notes")) {
                    data.add(new Notes(e.getAttribute("hint")));
                    ////////Log.d("AerialAssault", "I SEE YOU VIEWER");
                } else if (e.getTagName().contains("divider")) {
                    data.add(new Divider(e.getAttribute("name")));
                    ////////Log.d("AerialAssault", "I SEE YOU VIEWER");
                }*/

                Log.d("AerialAssist", e.getAttribute("id"));


                ////////Log.d("AerialAssault", e.getTagName());
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("AerialAssault", "Error", e);
        }

        SharedPreferences.Editor prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit();
        prefs.putString(XMLPATH_PREF, loc.getAbsolutePath());

        ////////Log.d("AerialAssault", loc.getAbsolutePath() + " into sharedprefs");

        prefs.commit();

    }

    /**
     * Deletes all teams from the application and from external storage. <b>This cannot be undone.</b>
     */
    public void clearAll()
    {
        teamsList.clear();

        if (getFilesDir().isDirectory()) {
            String[] children = getFilesDir().list();
            for (int i = 0; i < children.length; i++) {
                new File(getFilesDir(), children[i]).delete();
            }
        }
    }

    /**
     * Creates a match schedule from a list of teams.
     *
     * @param file A <code>File</code> referencing a CSV file which will be used to create te match
     *             schedule.
     */
    public void createFromFile(File file)
    {

        int teamNum = 0;
        int matchNum = 0;

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

            saveAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        resetMatchGroups();
    }

    /**
     * Exports all collected match data to a TXT file.
     *
     * @param exportName The name (without file extensions) to write the data to.
     * @throws IOException
     */
    public void exportAll(String exportName) throws IOException
    {
        ExportTask et = new ExportTask();
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
        matches = 0;
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
                    thisTeam.matches.put(new Integer(csvr.getValues()[0]), new Match(thisTeam, csvr.getValues()));
                    //////////Log.d("AerialAssault", "Read " + csvr.getRawRecord());
                    matches++;
                }
                teamsList.put(thisTeam.number, thisTeam);
            }
        } catch (Exception e) {
            ////////Log.e("AerialAssault", e.getMessage(), e);
            Toast.makeText(getBaseContext(), e.getMessage() + " Unable to read from external storage.",
                    Toast.LENGTH_LONG).show();
            initAll();
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
    public void initAll()
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
    public boolean teamExists(int num)
    {
        return (getTeam(num) != null);
    }

    private void resetMatchGroups()
    {

        groups.clear();
        MatchGroup m;

        for (int i = 0; i < 314; i++) {
            m = new MatchGroup(i, getTeamsWithMatch(i));
            //Log.d("AerialAssault", "New Match Group " + i + " Size " + Integer.toString(m.teams.length));
            if (m.teams.length > 0) groups.put(i, new MatchGroup(i, getTeamsWithMatch(i)));

        }
    }

    /**
     * Returns the team with a given number, if it exists.
     *
     * @param teamNum The number of the team.
     * @return The <code>FRCTeam</code> using the number if it exists, <code>null</code> otherwise.
     */
    public FRCTeam getTeam(int teamNum)
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
        }

        @Override
        protected Object doInBackground(Object... params)
        {
            matches = 0;
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
                    Toast.makeText(getBaseContext(), e.getMessage() + " Unable to write to external storage.",
                            Toast.LENGTH_LONG).show();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result)
        {
            SharedPreferences prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
            DeviceId restoredId = DeviceId.getFromValue(prefs.getString(DEVICEID_PREF, "0"));
            try {
                exportAll(restoredId.filename + ".txt");
            } catch (IOException ioe) {
                Toast.makeText(getApplicationContext(), "Unable to export matches: " + ioe.getMessage(), Toast.LENGTH_LONG).show();
            }
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
                CsvWriter csvw = new CsvWriter(exportPath + exportName);
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

    /*private class LayoutImportTask extends AsyncTask<File, File, Object>
    {

        private ArrayList<Field> data;
        private boolean hasRun = false;

        @Override
        protected Object doInBackground(File... locs)
        {
            try {
                Document file = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(locs[0]);
                NodeList nodes = file.getChildNodes().item(0).getChildNodes();
                //printNodeNames(nodes);

                Element e;
                for (int i = 0; i < nodes.getLength(); i++) {

                    if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE)
                        e = (Element) nodes.item(i);
                    else {

                        continue;
                    }

                    ////////Log.d("AerialAssault", e.getTagName());
                    if (e.getTagName().contains("counter")) {
                        data.add(new Counter(
                                Integer.parseInt(e.getAttribute("initValue")),
                                e.getAttribute("name")));
                        ////////Log.d("AerialAssault", "I SEE YOU HUMAN");
                    } else if (e.getTagName().contains("checkbox")) {
                        data.add(new Checkbox(e.getAttribute("name")));
                        ////////Log.d("AerialAssault", "I SEE YOU HUMANHUMAN");
                    } else if (e.getTagName().contains("rating")) {
                        data.add(new RatingStars(e.getAttribute("name"), Integer.parseInt(e.getAttribute("scale"))));
                        ////////Log.d("AerialAssault", "I SEE YOU HUMANHUMANHUMAN");
                    } else if (e.getTagName().contains("notes")) {
                        data.add(new Notes(e.getAttribute("hint")));
                        ////////Log.d("AerialAssault", "I SEE YOU VIEWER");
                    } else if (e.getTagName().contains("divider")) {
                        data.add(new Divider(e.getAttribute("name")));
                        ////////Log.d("AerialAssault", "I SEE YOU VIEWER");
                    }

                    ////////Log.d("AerialAssault", e.getTagName());
                    hasRun = true;
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                ////////Log.e("AerialAssault", "Error", e);
            }

            return null;
        }

        protected ArrayList<Field> getData()
        {
            if (hasRun) return data;
            else return null;
        }

    }*/

}
