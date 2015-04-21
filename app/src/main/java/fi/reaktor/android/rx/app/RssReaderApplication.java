package fi.reaktor.android.rx.app;

import android.app.Application;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import fi.reaktor.android.rx.data.FeedUpdatesListener;
import fi.reaktor.android.rx.data.Feeds;
import fi.reaktor.android.rx.data.PeriodicUpdates;

public class RssReaderApplication extends Application implements FeedUpdatesListener {

    private PeriodicUpdates periodicUpdates;

    private Feeds feeds = new Feeds();

    @Override
    public void onCreate() {
        Log.d("APPLICATION", "onCreate()");
        super.onCreate();
        periodicUpdates = new PeriodicUpdates(feeds, this);
        periodicUpdates.start();
    }

    @Override
    public void onTerminate() {
        Log.d("APPLICATION", "onTerminate()");
        periodicUpdates.stop();
        super.onTerminate();
    }

    @Override
    public void feedsUpdated() {
        Intent intent = new Intent("feeds-updated");
        intent.putExtra("updated", true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public PeriodicUpdates getPeriodicUpdates() {
        return periodicUpdates;
    }

    public void setPeriodicUpdates(PeriodicUpdates periodicUpdates) {
        this.periodicUpdates = periodicUpdates;
    }

    public Feeds getFeeds() {
        return feeds;
    }

    public void setFeeds(Feeds feeds) {
        this.feeds = feeds;
    }
}
