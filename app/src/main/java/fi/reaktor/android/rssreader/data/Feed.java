package fi.reaktor.android.rssreader.data;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

public class Feed implements Serializable {

    public final String guid;
    public final String title;
    public final Date published;
    public final Sequence<Article> articles;

    public Feed(String guid, String title, Date published, Sequence<Article> articles) {
        this.guid = guid;
        this.title = title;
        this.published = published;
        this.articles = articles;
    }

    Feed(String guid, String title, Date published, Collection<Article> articles) {
        this(guid, title, published, Sequences.sequence(articles));
    }

    public Sequence<Article> pastWeeksArticles() {
        return articles.filter(a -> {
            Date oneWeekAgo = DateTime.now().minusDays(7).toDate();
            return a.published.after(oneWeekAgo);
        }).reverse();
    }
}
