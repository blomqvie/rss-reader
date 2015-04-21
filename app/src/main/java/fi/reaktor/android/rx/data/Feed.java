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

    public void addArticle(Article article) {
        if (contains(article)) {
            Log.d(Feed.class.getSimpleName(), "Updating an existing article: " + article.getGuid());
            getArticleByGuid(article.getGuid()).update(article);
        } else {
            Log.d(Feed.class.getSimpleName(), "Adding a new article: " + article.getGuid());
            articles.add(article);
        }
    }

    private boolean contains(Article article) {
        return getArticleByGuid(article.getGuid()) != null;
    }

    public Article getArticleByGuid(String guid) {
        for (Article a : articles) {
            if (a.getGuid().equals(guid)) {
                return a;
            }
        }
        return null;
    }
}
