package com.example.buchanan_ross_s2144101;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.accessibilityservice.AccessibilityService;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private TextView rawDataDisplay;
    private Button frButton1, frButton2, frButton3;
    private Fragment fr1, fr2, fr3;
    private String result = "";
    private ListView currentIncidentsListView, roadworks, planRoadworks;

    // Traffic Scotland Planned Roadworks XML link
    private String pRoadWorksUrl = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String curIncidentsUrl = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String roadworksUrl = "https://trafficscotland.org/rss/feeds/roadworks.aspx";


    private ArrayAdapter<currentIncidents> arrayAdapterCurrentIncidents;
    private ArrayList currentIncidentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MyTag", "in onCreate");
        // Set up the raw links to the graphical components
        rawDataDisplay = (TextView) findViewById(R.id.rawDataDisplay);
        frButton1 = (Button) findViewById(R.id.frButton1);
        frButton2 = (Button) findViewById(R.id.frButton2);
        frButton3 = (Button) findViewById(R.id.frButton3);

        frButton1.setOnClickListener(this);
        frButton2.setOnClickListener(this);
        frButton3.setOnClickListener(this);

        // fr1 = new FragmentOne();

        currentIncidentsListView = (ListView) findViewById(R.id.curIncidents);
        roadworks = (ListView) findViewById(R.id.roadworks);
        planRoadworks = (ListView) findViewById(R.id.planRoadWorks);

        currentIncidentsList = new ArrayList();
        arrayAdapterCurrentIncidents = new ArrayAdapter<currentIncidents>(MainActivity.this, android.R.layout.simple_list_item_1, currentIncidentsList);

        Log.e("MyTag", "after startButton");
        // More Code goes here

    }

    public void startProgress() {
        // Run network access on a separate thread;
        if (frButton1.isPressed())
            new Thread(new Task(curIncidentsUrl)).start();

        // if(frButton2.isPressed())
        //   new Thread(new Task(roadworksUrl)).start();

        //if(frButton3.isPressed())
        //  new Thread(new Task(pRoadWorksUrl)).start();

    } //

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
            String inputLine = "";

            Log.e("MyTag", "in run");

            try {
                Log.e("MyTag", "in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                Log.e("MyTag", "after ready");
                //
                // Now read the data. Make sure that there are no specific hedrs
                // in the data file that you need to ignore.
                // The useful data that you need is in each of the item entries
                //
                while ((inputLine = in.readLine()) != null) {
                    result = result + inputLine;
                    Log.e("MyTag", inputLine);

                }
                in.close();
            } catch (IOException ae) {
                Log.e("MyTag", "ioexception in run");
            }

            //
            // Now that you have the xml data you can parse it
            //

            pullParseXml.parseData(result);
            // Now update the TextView to display raw XML data
            // Probably not the best way to update TextView
            // but we are just getting started !

            MainActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");
                    System.out.println("here!");
                    currentIncidentsListView.setAdapter(arrayAdapterCurrentIncidents);
                }
            });
        }
    }
}