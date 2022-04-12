package com.example.buchanan_ross_s2144101;

/*
   Name: Ross Buchanan
   Student Number: S2144101
 */

public class CurrentIncidents {
    private String title, description, link, point, pubDate;

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

    @Override
    public String toString() {
        return  "Title: " + title + "\nDescription: \n" + description + "\nLink: \n" + link + "\nPoint: " + point + "\nPublish Date: " + pubDate;
    }
}
