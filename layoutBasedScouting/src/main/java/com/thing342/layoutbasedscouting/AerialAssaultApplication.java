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
import com.thing342.layoutbasedscouting.fields.Field;
import com.thing342.layoutbasedscouting.fields.Notes;
import com.thing342.layoutbasedscouting.fields.RatingStars;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Class that stores global-level objects.
 */
public class AerialAssaultApplication extends Application
{

    public static final String PREFS = "com.thing342.layoutbasedscouting_preferences";
    public static final String XMLPATH_PREF = "xmlPath";
    public static final String DEVICEID_PREF = "deviceid";
    public static final String FIRSTLAUNCH_PREF = "firstLaunch";
    public static final String MATCHESFIST_PREF = "matchesFirst";

    public ArrayList<FRCTeam> teamsList = new ArrayList<FRCTeam>();
    public ArrayList<Field> data = new ArrayList<Field>();
    public ArrayList<MatchGroup> groups = new ArrayList<MatchGroup>();
    public int matches = 0;
    private View scoreLayout;

    ///////////////////--CONSTRUCTORS--/////////////////////////////

    ///////////////////--OVERRIDEN METHODS--///////////////////////

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

    }

    ///////////////////--PUBLIC METHODS--///////////////////////

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

                ////////Log.d("AerialAssault", e.getTagName());
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
                }

                Log.d("AerialAssist", e.getAttribute("id"));


                ////////Log.d("AerialAssault", e.getTagName());
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            ////////Log.e("AerialAssault", "Error", e);
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
                        teamsList.add(new FRCTeam(teamNum, "")); //If team doesn't exist, add a new one
                    teamsList.get(getTeamPos(teamNum)).createMatch(matchNum);
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
                    thisTeam.matches.add(new Match(thisTeam, csvr.getValues()));
                    //////////Log.d("AerialAssault", "Read " + csvr.getRawRecord());
                    matches++;
                }
                teamsList.add(thisTeam);
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

        for (FRCTeam f : teamsList) {
            if (f.getMatch(matchNum) != null) teams.add(f);
        }

        return teams.toArray(new FRCTeam[teams.size()]);
    }

    /**
     * Sorts the list of teams by match number.
     */
    public void sort()
    {
        Collections.sort(teamsList);
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
        teamsList.add(team1);
        FRCTeam team2 = new FRCTeam(1002, "FooBots");
        team2.createMatch(2);
        team2.createMatch(4);
        teamsList.add(team2);
        FRCTeam team3 = new FRCTeam(1003, "BazBots");
        team3.createMatch(1);
        team3.createMatch(2);
        teamsList.add(team3);
        FRCTeam team4 = new FRCTeam(1004, "NOT PAY FOR THIS TEAM");
        team4.createMatch(3);
        team4.createMatch(4);
        teamsList.add(team4);
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

    public int getTeamPos(int num) throws IllegalArgumentException
    {
        for (int i = 0; i < teamsList.size(); i++) {
            if (teamsList.get(i).number == num) return i;
        }

        throw new IllegalArgumentException("Team does not exist");
    }

    private void resetMatchGroups()
    {

        groups.clear();
        MatchGroup m;

        for (int i = 0; i < 314; i++) {
            m = new MatchGroup(i, getTeamsWithMatch(i));
            Log.d("AerialAssault", "New Match Group " + i + " Size " + Integer.toString(m.teams.length));
            if (m.teams.length > 0) groups.add(new MatchGroup(i, getTeamsWithMatch(i)));

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
        for (FRCTeam f : teamsList) if (f.number == teamNum) return f;
        return null;
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
        teamsList.add(newTeam);
        //////////Log.d("AerialAssault", "Added Team " + Integer.toString(number));
    }

    ////////////////--NESTED CLASSES--//////////////////////////////

    /**
     * An <code>AsyncTask</code> for saving match data to files in the background.
     */
    private class SaveTask extends AsyncTask<Object, Object, Object>
    {

        @Override
        protected Object doInBackground(Object... params)
        {
            matches = 0;
            for (FRCTeam team : teamsList) {
                try {

                    String FILEPATH = getFilesDir().getAbsolutePath() + File.separator + Integer.toString(team.number);// + ".csv";
                    CsvWriter csvw = new CsvWriter(FILEPATH);
                    csvw.setDelimiter(',');
                    csvw.setRecordDelimiter(';');

                    for (Match m : team.matches) {
                        csvw.writeRecord(m.getRecord(false));
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

            sort();
            return null;
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
                    for (FRCTeam t : teamsList) {
                        thisMatch = t.getMatch(i);
                        if (thisMatch != null && thisMatch.isEdited())
                            csvw.writeRecord(thisMatch.getRecord(true));
                    }
                }

                csvw.flush();
                csvw.close();

                String path[] = {exportPath + exportName};
                String mime[] = {MimeTypes.csv.contentType};

                MediaScannerConnection.scanFile(getApplicationContext(), path, mime, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
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
