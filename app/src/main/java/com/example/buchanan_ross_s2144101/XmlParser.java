package com.example.buchanan_ross_s2144101;

/*
   Name: Ross Buchanan
   Student Number: S2144101
 */

import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class XmlParser {
    static final String titleTag = "title";
    static final String descriptionTag = "description";
    static final String linkTag = "link";
    static final String pointTag = "point";
    static final String pubDateTag = "pubDate";

    private List<CurrentIncidents> incidents = new ArrayList<CurrentIncidents>();
    private CurrentIncidents incident;
    private String text;

    private List<Roadworks> roadworks = new ArrayList<Roadworks>();
    private Roadworks roadwork;

    private List<PlannedRoadWorks> pRoadworks = new ArrayList<PlannedRoadWorks>();
    private PlannedRoadWorks plannedRoadWork;

    public List<CurrentIncidents> parseDataIncidents(String parseDataInput) {
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

                       case XmlPullParser.TEXT:
                            text = pullParse.getText();
                       break;

                    case XmlPullParser.START_TAG:
                        if (tagName.equalsIgnoreCase(titleTag)) {
                            incident = new CurrentIncidents();
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagName.equalsIgnoreCase(titleTag)) {
                            incident.setTitle(text);
                        } else if (tagName.equalsIgnoreCase(titleTag)) {
                            incident.setTitle(text);
                            Log.e("My Tag", "title is " + incident.getTitle());
                        } else if (tagName.equalsIgnoreCase(descriptionTag)) {
                            incident.setDescription(text);
                            Log.e("My Tag", "description is " + incident.getDescription());
                        } else if (tagName.equalsIgnoreCase(linkTag)) {
                            incident.setLink(text);
                            Log.e("My Tag", "link is " + incident.getLink());
                        } else if (tagName.equalsIgnoreCase(pointTag)) {
                            incident.setPoint(text);
                            Log.e("My Tag", "Point is " + incident.getPoint());
                        } else if (tagName.equalsIgnoreCase(pubDateTag)) {
                            incident.setPubDate(text);
                            Log.e("My Tag", "pubDate is " + incident.getPubDate());
                        }

                        if(incident.getPubDate() == null)
                            incidents.remove(incident);
                        else
                            incidents.add(incident);

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
        return incidents;
    }

    public List<Roadworks> parseDataRoadWorks(String parseDataInput) {
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

                    case XmlPullParser.TEXT:
                        text = pullParse.getText();
                        break;

                    case XmlPullParser.START_TAG:
                        if (tagName.equalsIgnoreCase(titleTag)) {
                            roadwork = new Roadworks();
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagName.equalsIgnoreCase(titleTag)) {
                            roadwork.setTitle(text);
                        } else if (tagName.equalsIgnoreCase(titleTag)) {
                            roadwork.setTitle(text);
                            Log.e("My Tag", "title is " + roadwork.getTitle());
                        } else if (tagName.equalsIgnoreCase(descriptionTag)) {
                            String[] split = text.split("<br />");
                            StringBuilder stringBuilder = new StringBuilder();

                            for(String str : split) {
                                stringBuilder.append(str).append(" ");

                                if (str.contains("Start Date")) {
                                    try {
                                        String startDate = str.split("Start Date: ")[1];
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy - HH:mm");
                                        Date start = dateFormat.parse(startDate);
                                        roadwork.setStartDate(start);
                                        Log.e("Start", "Date: " + start);
                                    } catch (Exception ex) {
                                        Log.e("Error: ", ex.getMessage());
                                    }
                                }
                                else if (str.contains("End Date")) {
                                    try
                                    {
                                        String endDate = str.split("End Date: ")[1];
                                        SimpleDateFormat dateFormat1 = new SimpleDateFormat("E, dd MMM yyyy - HH:mm");
                                        Date end = dateFormat1.parse(endDate);
                                        roadwork.setEndDate(end);
                                        Log.e("End", "Date: " + roadwork.getEndDate());
                                    } catch (Exception ex) {
                                        Log.e("Error: ", ex.getMessage());
                                    }
                                    long numOfDays = roadwork.getEndDate().getTime() - roadwork.getStartDate().getTime();
                                    float daysBetween = (numOfDays / (1000 * 60 * 60 * 24));

                                    if(daysBetween <= 3) {
                                        roadwork.setTimeFrame("Road works wont be here for long");

                                    }
                                    else if (daysBetween > 3 && daysBetween <= 10)
                                        roadwork.setTimeFrame("Road works will be here for quite a while");
                                    else
                                        roadwork.setTimeFrame("Road works will be here for a while");

                                    Log.e("Days: ", "Days amount: " + daysBetween);
                                }
                            }
                            roadwork.setDescription(stringBuilder.toString());
                            Log.e("My Tag", "description is " + roadwork.getDescription());
                        } else if (tagName.equalsIgnoreCase(linkTag)) {
                            roadwork.setLink(text);
                            Log.e("My Tag", "link is " + roadwork.getLink());
                        } else if (tagName.equalsIgnoreCase(pointTag)) {
                            roadwork.setPoint(text);
                            Log.e("My Tag", "Point is " + roadwork.getPoint());
                        } else if (tagName.equalsIgnoreCase(pubDateTag)) {
                            roadwork.setPubDate(text);
                            Log.e("My Tag", "pubDate is " + roadwork.getPubDate());
                        }

                        if(roadwork.getPubDate() == null)
                            roadworks.remove(roadwork);
                        else
                            roadworks.add(roadwork);

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
        return roadworks;
    }

    public List<PlannedRoadWorks> parseDataPlannedRoadWorks(String parseDataInput) {
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

                    case XmlPullParser.TEXT:
                        text = pullParse.getText();
                        break;

                    case XmlPullParser.START_TAG:
                        if (tagName.equalsIgnoreCase(titleTag)) {
                            plannedRoadWork = new PlannedRoadWorks();
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagName.equalsIgnoreCase(titleTag)) {
                            plannedRoadWork.setTitle(text);
                        } else if (tagName.equalsIgnoreCase(titleTag)) {
                            plannedRoadWork.setTitle(text);
                            Log.e("My Tag", "title is " + plannedRoadWork.getTitle());
                        } else if (tagName.equalsIgnoreCase(descriptionTag)) {
                            String[] split = text.split("<br />");
                            StringBuilder stringBuilder = new StringBuilder();

                            for(String str : split) {
                                stringBuilder.append(str).append(" ");

                                if (str.contains("Start Date")) {
                                    try {
                                        String startDate = str.split("Start Date: ")[1];
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy - HH:mm");
                                        Date start = dateFormat.parse(startDate);
                                        plannedRoadWork.setStartDate(start);
                                        Log.e("Start", "Date: " + start);
                                    } catch (Exception ex) {
                                        Log.e("Error: ", ex.getMessage());
                                    }
                                } else if (str.contains("End Date")) {
                                    try {
                                        String endDate = str.split("End Date: ")[1];
                                        SimpleDateFormat dateFormat1 = new SimpleDateFormat("E, dd MMM yyyy - HH:mm");
                                        Date end = dateFormat1.parse(endDate);
                                        plannedRoadWork.setEndDate(end);
                                        Log.e("End", "Date: " + plannedRoadWork.getEndDate());
                                    } catch (Exception ex) {
                                        Log.e("Error: ", ex.getMessage());
                                    }
                                    long numOfDays = plannedRoadWork.getEndDate().getTime() - plannedRoadWork.getStartDate().getTime();
                                    float daysBetween = (numOfDays / (1000 * 60 * 60 * 24));

                                    if (daysBetween <= 3)
                                        plannedRoadWork.setTimeFrame("Road works wont be here for long");
                                    else if (daysBetween > 3 && daysBetween <= 10)
                                        plannedRoadWork.setTimeFrame("Road works will be here for quite a while");
                                    else
                                        plannedRoadWork.setTimeFrame("Road works will be here for a while");
                                }
                            }
                            plannedRoadWork.setDescription(stringBuilder.toString());
                            Log.e("My Tag", "description is " + plannedRoadWork.getDescription());
                        } else if (tagName.equalsIgnoreCase(linkTag)) {
                            plannedRoadWork.setLink(text);
                            Log.e("My Tag", "link is " + plannedRoadWork.getLink());
                        } else if (tagName.equalsIgnoreCase(pointTag)) {
                            plannedRoadWork.setPoint(text);
                            Log.e("My Tag", "Point is " + plannedRoadWork.getPoint());
                        } else if (tagName.equalsIgnoreCase(pubDateTag)) {
                            plannedRoadWork.setPubDate(text);
                            Log.e("My Tag", "pubDate is " + plannedRoadWork.getPubDate());
                        }

                        if(plannedRoadWork.getPubDate() == null)
                            pRoadworks.remove(plannedRoadWork);
                        else
                            pRoadworks.add(plannedRoadWork);
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
        return pRoadworks;
    }
}
