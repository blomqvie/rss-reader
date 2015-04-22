package fi.reaktor.android.rx.data;

import android.text.Html;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.io.Serializable;
import java.util.Date;

public class Article implements Serializable {
    public final Date published;
    public final String content;
    public final String title;
    public final String guid;

    public Article(Date published, String content, String title, String guid) {
        this.published = published;
        this.content = escapeContent(content);
        this.title = title;
        this.guid = guid;
    }

    private String escapeContent(String content) {
        String unescapedHtml = content.replaceAll("&nbsp;", " ");
        String withoutHtmlTags = Jsoup.clean(Html.fromHtml(unescapedHtml).toString(), Whitelist.none());
        String withoutSpecialChars = withoutHtmlTags.replaceAll("[^ \ta-zA-Z0-9_\\.,\\-\\\\/\\\\:;\\*\\+!?\"'#€%&\\(\\)\\[\\]]", "");
        return withoutSpecialChars.trim();
    }
}
