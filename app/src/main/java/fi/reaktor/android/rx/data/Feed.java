package fi.reaktor.android.rx.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Feed {

    private String guid;
    private String title;
    private Date published;
    private List<Article> articles = new ArrayList<>();

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }

    public void add(Article article) {
        boolean found = false;
        for (Article a : articles) {
            if (a.getGuid().equals(article.getGuid())) {
                found = true;
                Log.d(Feed.class.getSimpleName(), "Updating an existing article: " + article.getGuid());
                a.update(article);
                break;
            }
        }
        if (!found) {
            Log.d(Feed.class.getSimpleName(), "Adding a new article: " + article.getGuid());
            articles.add(article);
        }
    }

    public void update(Feed feed) {
        this.title = feed.title;
        this.published = feed.published;
        this.articles = feed.articles;
    }
}
