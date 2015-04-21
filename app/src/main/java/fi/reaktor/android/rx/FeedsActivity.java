package fi.reaktor.android.rx;

import android.app.Activity;
import android.os.Bundle;
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

public class FeedsActivity extends Activity {

    private static final String TAG = "RSSREADER";

    private Timer fetchTimer;
    private TimerTask fetchTimerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();

        fetchTimer = new Timer("fetchtimer");
        fetchTimerTask = new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "Fetch timer at " + new Date());
                List<String> entries = fetchRssFeed("http://feeds.reuters.com/reuters/MostRead");
                Log.d(TAG, "Fetched " + entries.size() + " entries");
            }
        };
        fetchTimer.scheduleAtFixedRate(fetchTimerTask, 0, 5 * 60 * 1000);
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

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause()");
        fetchTimerTask.cancel();
        fetchTimer.cancel();
        super.onPause();
    }
}
