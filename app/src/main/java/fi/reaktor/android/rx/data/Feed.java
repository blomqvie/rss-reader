package fi.reaktor.android.rx.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Feed implements Serializable {

    private String guid;
    private String title;
    private Date published;
    private List<Article> articles = new ArrayList<>();
    private boolean favorite;

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

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void add(Article article) {
        if (article.getContent() == null || article.getContent().length() == 0) {
            throw new RuntimeException("WTF");
        }
        boolean found = false;
        for (Article a : articles) {
            if (a.getGuid().equals(article.getGuid())) {
                found = true;
                a.update(article);
                break;
            }
        }
        if (!found) {
            articles.add(article);
        }
    }

    public void update(Feed feed) {
        this.title = feed.title;
        this.published = feed.published;
        for (Article newArticle : feed.getArticles()) {
            add(newArticle);
        }
    }

    public Article findArticle(String guid) {
        for (Article article : articles) {
            if (article.getGuid().equals(guid)) {
                return article;
            }
        }
        return null;
    }
}
