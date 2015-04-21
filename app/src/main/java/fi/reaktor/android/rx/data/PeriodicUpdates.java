package fi.reaktor.android.rx.data;

import android.util.Log;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.RssItem;
import nl.matshofman.saxrssreader.RssReader;

public class PeriodicUpdates {
    private static final String TAG = PeriodicUpdates.class.getSimpleName();

    private Timer fetchTimer;
    private TimerTask fetchTimerTask;
    private Feeds feeds;
    private FeedUpdatesListener feedUpdatesListener;

    public PeriodicUpdates(Feeds feeds, FeedUpdatesListener feedUpdatesListener) {
        this.feeds = feeds;
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

                        /*
                        "http://feeds.reuters.com/reuters/MostRead",
                        "http://feeds.reuters.com/Reuters/worldNews",
                        "http://feeds.reuters.com/reuters/technologyNews",
                        "http://feeds.reuters.com/reuters/scienceNews",
                        "http://feeds.reuters.com/reuters/sportsNews",
                        "http://feeds.reuters.com/Reuters/PoliticsNews",
                        "http://feeds.reuters.com/reuters/businessNews",
                        "http://feeds.reuters.com/news/wealth",
                        "http://feeds.reuters.com/reuters/peopleNews",
                        "http://feeds.reuters.com/reuters/entertainment",
                        "http://feeds.reuters.com/reuters/environment",
                        "http://feeds.reuters.com/reuters/lifestyle"
                        */
                });

                // add or update new feeds
                for (String url : feedUrls) {
                    Log.d(TAG, "Fetching " + url);
                    Feed feed = fetchRssFeed(url);
                    if (feed != null) {
                        if (feed.getGuid() == null) {
                            feed.setGuid(url);
                        }
                        Log.d(TAG, "Adding parsed feed with Guid " + feed.getGuid());
                        feeds.add(feed);
                    } else {
                        Log.w(TAG, "Couldn't parse Feed from " + url);
                    }
                }

                // remove old feeds
                List<Feed> feedsToPurge = new ArrayList<>();
                for (Feed feed : feeds.getFeeds()) {
                    if (feedUrls.contains(feed.getGuid())) {
                        feedsToPurge.add(feed);
                    }
                }
                for (Feed feed : feedsToPurge) {
                    feeds.remove(feed);
                }

                feedUpdatesListener.feedsUpdated();
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

    private Feed fetchRssFeed(String url) {
        try {
            RssFeed rssFeed = RssReader.read(new URL(url));
            Feed feed = convertRssFeedIntoFeed(rssFeed);
            feed.setGuid(url);
            return feed;
        } catch (Exception e) {
            Log.e(TAG, "Exception fetching feed from " + url, e);
            return null;
        }
    }

    private Feed convertRssFeedIntoFeed(RssFeed rssFeed) {
        Feed feed = new Feed();
        feed.setTitle(rssFeed.getTitle());
        feed.setPublished(new Date(rssFeed.getPublished()));
        ArrayList<RssItem> rssItems = rssFeed.getRssItems();
        for (RssItem rssItem : rssItems) {
            feed.add(convertRssItemIntoArticle(rssItem));
        }
        return feed;
    }

    private Article convertRssItemIntoArticle(RssItem item) {
        Article article = new Article();
        article.setGuid(item.getGuid());
        article.setTitle(item.getTitle());
        article.setContent(item.getContent());
        article.setPublished(item.getPubDate());
        return article;
    }
}
