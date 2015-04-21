package fi.reaktor.android.rx.data;

import android.text.Html;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.io.Serializable;
import java.util.Date;

public class Article implements Serializable {
    private Date published;
    private String content;
    private String title;
    private String guid;
    private boolean read;

    public void setPublished(Date published) {
        this.published = published;
    }

    public Date getPublished() {
        return published;
    }

    public void setContent(String content) {
        String unescapedHtml = content.replaceAll("&nbsp;", " ");
        String withoutHtmlTags = Jsoup.clean(Html.fromHtml(unescapedHtml).toString(), Whitelist.none());
        String withoutSpecialChars = withoutHtmlTags.replaceAll("[^ \ta-zA-Z0-9_\\.,\\-\\\\/\\\\:;\\*\\+!?\"'#€%&\\(\\)\\[\\]]", "");
        this.content = withoutSpecialChars.trim();
    }

    public String getContent() {
        return content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getGuid() {
        return guid;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public void update(Article article) {
        if (!guid.equals(article.getGuid())) {
            Log.w(Article.class.getSimpleName(), "Updating an article based on an article with different identity!");
        }
        setPublished(article.getPublished());
        setTitle(article.getTitle());
        setContent(article.getContent());
    }
}
