package com.example.buchanan_ross_s2144101;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class pullParseXml {
    static final String itemTag = "item";
    static final String titleTag = "title";
    static final String descriptionTag = "description";
    static final String linkTag = "link";
    static final String pubDate = "pubDate";

    public static List<currentIncidents> parseData(String parseDataInput) {

        List<currentIncidents> currentIncidentsSite;
        currentIncidentsSite = new ArrayList<currentIncidents>();

        currentIncidents curIncidentsSite = null;
        String curText = "";

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser pullParse = factory.newPullParser();
            pullParse.setInput(new StringReader(parseDataInput));
            int eventType = pullParse.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                // get current tag
                String tagName = pullParse.getName();

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagName.equalsIgnoreCase(itemTag)) {
                            curIncidentsSite = new currentIncidents();
                            System.out.println("in item tag");
                        }
                        break;

                    case XmlPullParser.TEXT:
                        curText = pullParse.getText();
                        System.out.println("in text case");
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagName.equalsIgnoreCase(itemTag)) {
                            currentIncidentsSite.add(curIncidentsSite);
                            System.out.println("in item tag - end tag item");
                        }
                        else if(tagName.equalsIgnoreCase(titleTag)) {
                            curIncidentsSite.setTitle(curText);
                            System.out.println("in item tag - end tag title");
                        }
                        else if(tagName.equalsIgnoreCase(descriptionTag)) {
                            curIncidentsSite.setDescription(curText);
                            System.out.println("in item tag - end tag description");
                        }
                        else if(tagName.equalsIgnoreCase(linkTag)) {
                            curIncidentsSite.setLink(curText);
                            System.out.println("in item tag - end tag link");
                        }
                        else if(tagName.equalsIgnoreCase(pubDate)) {
                            curIncidentsSite.setPubDate(curText);
                            System.out.println("in item tag - end tag pubdate");
                        }
                        break;
                }
                eventType = pullParse.next();
            }
        } catch (XmlPullParserException ex) {
            Log.e("My Tag", "Parsing Error" + ex.toString());
        } catch (IOException exIO) {
            Log.e("My Tag", "IO error during parsing");
        }
        Log.e("My Tag", "End Document");
        return currentIncidentsSite;
    }
}
