package fi.reaktor.android.rx.data;

import android.util.Log;

import java.net.URL;
import java.util.ArrayList;
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

    public PeriodicUpdates() {
        fetchTimerTask = new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "Fetch timer at " + new Date());
                List<String> entries = fetchRssFeed("http://feeds.reuters.com/reuters/MostRead");
                Log.d(TAG, "Fetched " + entries.size() + " entries");
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

    private List<String> fetchRssFeed(String url)
    {
        List<String> titles = new ArrayList<>();
        try
        {
            URL feedUrl = new URL(url);
            RssFeed feed = RssReader.read(feedUrl);
            ArrayList<RssItem> rssItems = feed.getRssItems();
            for(RssItem rssItem : rssItems) {
                Log.i("RSS Reader", rssItem.getTitle());
                titles.add(rssItem.getTitle());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            return titles;
        }
    }

}
