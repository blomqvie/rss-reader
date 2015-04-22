package fi.reaktor.android.rx.data;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Feeds {

    private final List<Feed> feeds;

    public Feeds() {
        this.feeds = new ArrayList<>();
    }

    public Feeds(List<Feed> feeds) {
        this.feeds = feeds;
    }

    public Sequence<Feed> getFeedSeq() {
        return sequence(feeds);
    }

    public List<Feed> getFeeds() {
        return feeds;
    }

    public Article findArticle(String guid) {
        return getFeedSeq().flatMap(f -> f.articles).find(a -> a.guid.equals(guid)).getOrNull();
    }
}
