package fi.reaktor.android.rx.data;

import android.util.Log;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.RssItem;
import nl.matshofman.saxrssreader.RssReader;

public class PeriodicUpdates {
    private static final String TAG = PeriodicUpdates.class.getSimpleName();

    private Timer fetchTimer;
    private TimerTask fetchTimerTask;

    public PeriodicUpdates() {
        fetchTimerTask = new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "Fetch timer at " + new Date());
                String[] feedUrls = new String[] {
                        "http://feeds.reuters.com/reuters/MostRead",
                        "http://feeds.reuters.com/reuters/technologyNews",
                        "http://feeds.reuters.com/reuters/sportsNews",
                        "http://feeds.reuters.com/reuters/entertainment",
                        "http://feeds.reuters.com/reuters/businessNews",
                        "http://feeds.reuters.com/Reuters/PoliticsNews",
                        "http://feeds.reuters.com/reuters/lifestyle"
                };
                for (String url : feedUrls) {
                    Log.d(TAG, "Fetching " + url);
                    Feed feed = fetchRssFeed(url);
                }
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
        } catch (Exception e) {}
        try {
            fetchTimer.cancel();
        } catch (Exception e) {}
    }

    private Feed fetchRssFeed(String url)
    {
        try
        {
            RssFeed rssFeed = RssReader.read(new URL(url));
            return convertRssFeedIntoFeed(rssFeed);
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            return null;
        }
    }

    private Feed convertRssFeedIntoFeed(RssFeed rssFeed) {
        Feed feed = new Feed();
        feed.setGuid(rssFeed.getLink());
        feed.setTitle(rssFeed.getTitle());
        feed.setPublished(new Date(rssFeed.getPublished()));
        ArrayList<RssItem> rssItems = rssFeed.getRssItems();
        for(RssItem rssItem : rssItems) {
            feed.addArticle(convertRssItemIntoArticle(rssItem));
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
