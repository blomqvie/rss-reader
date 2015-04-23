package fi.reaktor.android.rssreader.data;

import android.util.Log;

import com.googlecode.totallylazy.Sequence;

import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.RssReader;

import static com.googlecode.totallylazy.Sequences.sequence;

public class PeriodicUpdates {

    private static final String TAG = PeriodicUpdates.class.getSimpleName();


    // TODO: this class should a static method (e.g. updates()) which returns an Observable emitting Feeds objects.

    // 1. Use interval to create an observable which emits items
    // 2. It should use IO scheduler
    // 3. Transform Long emitted by the observable into an observable which emits Feeds
    // 4. Log errors
    // 5. Make sure that we retry rss fetching if an error occurs


    private Timer fetchTimer;
    private TimerTask fetchTimerTask;

    private FeedUpdatesListener feedUpdatesListener;

    public PeriodicUpdates(FeedUpdatesListener feedUpdatesListener) {
        this.feedUpdatesListener = feedUpdatesListener;
        fetchTimerTask = new TimerTask() {
            @Override
            public void run() {
                List<String> feedUrls = Arrays.asList(new String[] {
                        "http://feeds.arstechnica.com/arstechnica/gadgets",
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
                        "http://feeds.arstechnica.com/arstechnica/features"
                });

                Sequence<Feed> feeds = sequence(feedUrls).map(url -> fetchRssFeed(url));
                feedUpdatesListener.feedsUpdated(feeds.toList());
            }
        };
    }

    public void start() {
        fetchTimer = new Timer("fetchtimer");
        fetchTimer.scheduleAtFixedRate(fetchTimerTask, 0, 5 * 60 * 1000);
    }

    public void stop() {
        try {
            fetchTimerTask.cancel();
        } catch (Exception e) {
        }
        try {
            fetchTimer.cancel();
        } catch (Exception e) {
        }
    }

    // TODO: use stuff below
    
    private Feed fetchRssFeed(String url) {
        try {
            RssFeed rssFeed = RssReader.read(new URL(url));
            Feed feed = convertRssFeedIntoFeed(rssFeed, url);
            return feed;
        } catch (Exception e) {
            Log.e(TAG, "Exception fetching feed from " + url, e);
            return null;
        }
    }

    private Feed convertRssFeedIntoFeed(RssFeed rssFeed, String url) {
        Sequence<Article> articles = sequence(rssFeed.getRssItems()).map(item -> new Article(item.getPubDate(), item.getContent(), item.getTitle(), item.getGuid()));
        Feed feed = new Feed(url, rssFeed.getTitle(), new Date(rssFeed.getPublished()), articles);
        return feed;
    }
}
