package com.example.buchanan_ross_s2144101;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Color;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/*
   Name: Ross Buchanan
   Student Number: S2144101
 */

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private Button curIncidentbtn, roadworksbtn, plannedRoadWorksbtn;

    private String incidentRes = "", roadworkRes = "";
    private CurrentIncidents currentIncident;

    // Traffic Scotland Planned Roadworks XML link
    private String pRoadWorksUrl = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String curIncidentsUrl = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String roadworksUrl = "https://trafficscotland.org/rss/feeds/roadworks.aspx";

    private ArrayAdapter<CurrentIncidents> arrayAdapterCurrentIncidents;
    private ListView currentIncidentsList;
    private List<CurrentIncidents> incidents = null;

    private ArrayAdapter<Roadworks> arrayAdapterRoadworks;
    private ListView roadworkslist;
    private List<Roadworks> roadworks = null;

    private ArrayAdapter<PlannedRoadWorks> arrayAdapterPlannedRoadworks;
    private ListView plannedRoadworksList;
    private List<PlannedRoadWorks> plannedRoadWorks = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MyTag", "in onCreate");
        // Set up the raw links to the graphical components

        curIncidentbtn = (Button) findViewById(R.id.curIncidentbtn);
        curIncidentbtn.setOnClickListener(this);
        currentIncidentsList = (ListView) findViewById(R.id.currentIncidentsList);
        currentIncidentsList.setVisibility(View.GONE);

        roadworksbtn = (Button)findViewById(R.id.roadworksbtn);
        roadworksbtn.setOnClickListener(this);
        roadworkslist = (ListView)findViewById(R.id.roadworkslist);
        roadworkslist.setVisibility(View.GONE);

        plannedRoadWorksbtn = (Button)findViewById(R.id.plannedRoadWorksbtn);
        plannedRoadWorksbtn.setOnClickListener(this);
        plannedRoadworksList = (ListView)findViewById(R.id.plannedRoadWorksList);
        plannedRoadworksList.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapterCurrentIncidents.getFilter().filter(newText);
                arrayAdapterRoadworks.getFilter().filter(newText);
                arrayAdapterPlannedRoadworks.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    public void startProgress() {
        // this is a comment
        // this is another comment
        if (curIncidentbtn.isPressed()) {
            incidentRes = "";
            new Thread(new Task(curIncidentsUrl)).start();
            currentIncidentsList.setVisibility(View.VISIBLE);
            roadworkslist.setVisibility(View.GONE);
            plannedRoadworksList.setVisibility(View.GONE);
        }
        if(roadworksbtn.isPressed()) {
            incidentRes = "";
            new Thread(new Task(roadworksUrl)).start();
            roadworkslist.setVisibility(View.VISIBLE);
            currentIncidentsList.setVisibility(View.GONE);
            plannedRoadworksList.setVisibility(View.GONE);
        }
        if(plannedRoadWorksbtn.isPressed()) {
            incidentRes = "";
            new Thread(new Task(pRoadWorksUrl)).start();
            plannedRoadworksList.setVisibility(View.VISIBLE);
            currentIncidentsList.setVisibility(View.GONE);
            roadworkslist.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        Log.e("MyTag", "in onClick");
        startProgress();
        Log.e("MyTag", "after startProgress");
    }

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable {
        private String url;

        public Task(String aurl) {
            url = aurl;
        }

        @Override
        public void run() {

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine1 = "", inputLine2 = "";

            Log.e("MyTag", "in run");

            try {
                Log.e("MyTag", "in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                Log.e("MyTag", "after ready");

                while ((inputLine1 = in.readLine()) != null) {
                    incidentRes += inputLine1;
                    Log.e("MyTag", inputLine1);
                }
                in.close();

                XmlParser parser = new XmlParser();
                incidents = parser.parseDataIncidents(incidentRes);
                arrayAdapterCurrentIncidents = new ArrayAdapter<CurrentIncidents>(MainActivity.this, android.R.layout.simple_list_item_1, incidents);

                roadworks = parser.parseDataRoadWorks(incidentRes);
                arrayAdapterRoadworks = new ArrayAdapter<Roadworks>(MainActivity.this, android.R.layout.simple_list_item_1, roadworks);

                plannedRoadWorks = parser.parseDataPlannedRoadWorks(incidentRes);
                arrayAdapterPlannedRoadworks = new ArrayAdapter<PlannedRoadWorks>(MainActivity.this, android.R.layout.simple_list_item_1, plannedRoadWorks);
            }
            catch (IOException ae) {
                Log.e("MyTag", "ioexception in run");
            }
            MainActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");
                    if(!arrayAdapterCurrentIncidents.equals(null))
                        currentIncidentsList.setAdapter(arrayAdapterCurrentIncidents);
                    if(!arrayAdapterRoadworks.equals(null))
                        roadworkslist.setAdapter(arrayAdapterRoadworks);
                    if(!arrayAdapterPlannedRoadworks.equals(null))
                        plannedRoadworksList.setAdapter(arrayAdapterPlannedRoadworks);
                }
            });
        }
    }
}

