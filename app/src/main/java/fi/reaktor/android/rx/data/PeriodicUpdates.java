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
                String[] feedUrls = new String[] { "http://feeds.reuters.com/reuters/MostRead" };
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
            URL feedUrl = new URL(url);
            RssFeed rssFeed = RssReader.read(feedUrl);
            Feed feed = new Feed();
            feed.setGuid(url);
            feed.setTitle(rssFeed.getTitle());
            feed.setPublished(new Date(rssFeed.getPublished()));
            ArrayList<RssItem> rssItems = rssFeed.getRssItems();
            for(RssItem rssItem : rssItems) {
                feed.addArticle(convertRssItemIntoArticle(rssItem));
            }
            return feed;
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            return null;
        }
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
