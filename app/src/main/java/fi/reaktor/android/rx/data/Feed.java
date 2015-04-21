package fi.reaktor.android.rx.data;

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
        articles.add(article);
    }
}
