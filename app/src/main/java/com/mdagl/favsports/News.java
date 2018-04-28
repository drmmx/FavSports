package com.mdagl.favsports;

public class News {

    private String titleNews, textNews, imageNews, urlNews, textHowSport;

    public News(String titleNews, String textNews, String imageNews, String urlNews, String textHowSport) {
        this.titleNews = titleNews;
        this.textNews = textNews;
        this.imageNews = imageNews;
        this.urlNews = urlNews;
        this.textHowSport = textHowSport;
    }

    public String getTitleNews() {
        return titleNews;
    }

    public String getTextNews() {
        return textNews;
    }

    public String getImageNews() {
        return imageNews;
    }

    public String getUrlNews() {
        return urlNews;
    }

    public String getTextHowSport() {
        return textHowSport;
    }
}
