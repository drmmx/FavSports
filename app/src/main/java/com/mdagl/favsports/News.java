package com.mdagl.favsports;

public class News {

    private String titleNews, textNews, imageNews, urlNews;

    public News(String titleNews, String textNews, String imageNews, String urlNews) {
        this.titleNews = titleNews;
        this.textNews = textNews;
        this.imageNews = imageNews;
        this.urlNews = urlNews;
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
}
