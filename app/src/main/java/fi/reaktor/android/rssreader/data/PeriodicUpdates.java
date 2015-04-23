package fi.reaktor.android.rssreader.data;

import android.util.Log;

import com.googlecode.totallylazy.Sequence;

import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.RssReader;
import rx.Observable;
import rx.schedulers.Schedulers;

import static com.googlecode.totallylazy.Sequences.sequence;

public class PeriodicUpdates {

    private static final String TAG = PeriodicUpdates.class.getSimpleName();

    private static List<String> feedUrls = Arrays.asList("http://feeds.arstechnica.com/arstechnica/gadgets",
            "http://feeds.arstechnica.com/arstechnica/cars",
            "http://feeds.arstechnica.com/arstechnica/multiverse",
            "http://feeds.arstechnica.com/arstechnica/science",
            "http://feeds.arstechnica.com/arstechnica/gaming",
            "http://feeds.arstechnica.com/arstechnica/apple",
            "http://feeds.arstechnica.com/arstechnica/tech-policy",
            "http://feeds.arstechnica.com/arstechnica/security",
            "http://feeds.arstechnica.com/arstechnica/business",
            "http://feeds.arstechnica.com/arstechnica/technology-lab",
            "http://feeds.arstechnica.com/arstechnica/staff-blogs",
            "http://feeds.arstechnica.com/arstechnica/features");


    // TODO: this class should a static method (e.g. updates()) which returns an Observable emitting Feeds objects.

    // 1. Use interval to create an observable which emits items
    // 2. It should use IO scheduler
    // 3. Transform Long emitted by the observable into an observable which emits Feeds
    // 4. Log errors
    // 5. Make sure that we retry rss fetching if an error occurs

    public static Observable<Feeds> updates() {
        return Observable.timer(0, 30, TimeUnit.SECONDS, Schedulers.io())
                .map(l -> sequence(feedUrls))
                .map(urls -> urls.map(PeriodicUpdates::fetchRssFeed))
                .map(seq -> new Feeds(seq.toList()))
                .doOnError(e -> Log.e(TAG, "Error while fetching RSS", e))
                .retry();
    }

    private static Feed fetchRssFeed(String url) {
        try {
            RssFeed rssFeed = RssReader.read(new URL(url));
            Feed feed = convertRssFeedIntoFeed(rssFeed, url);
            return feed;
        } catch (Exception e) {
            Log.e(TAG, "Exception fetching feed from " + url, e);
            return null;
        }
    }

    private static Feed convertRssFeedIntoFeed(RssFeed rssFeed, String url) {
        Sequence<Article> articles = sequence(rssFeed.getRssItems()).map(item -> new Article(item.getPubDate(), item.getContent(), item.getTitle(), item.getGuid()));
        Feed feed = new Feed(url, rssFeed.getTitle(), new Date(rssFeed.getPublished()), articles);
        return feed;
    }
}
