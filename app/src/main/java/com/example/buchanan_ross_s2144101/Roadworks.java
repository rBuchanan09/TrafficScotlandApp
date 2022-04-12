package com.example.buchanan_ross_s2144101;

/*
   Name: Ross Buchanan
   Student Number: S2144101
 */

import java.util.Date;

public class Roadworks {
    private String title, description, link, point, pubDate, timeFrame;
    private Date startDate, endDate;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public String getPoint() {
        return point;
    }

    public String getPubDate() {
        return pubDate;
    }

    public Date getStartDate() { return  startDate; }

    public Date getEndDate() { return endDate; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public void setTimeFrame(String timeFrame) { this.timeFrame = timeFrame; }

    @Override
    public String toString() {
        return  "Title: " + title + "\nDescription: \n" + description + "\nLink: \n" + link + "\nPoint: " + point + "\nPublish Date: " + pubDate + "\n\n" + timeFrame;
    }

}
