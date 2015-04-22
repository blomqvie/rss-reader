package fi.reaktor.android.rx.data;

import com.googlecode.totallylazy.Sequence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
}
