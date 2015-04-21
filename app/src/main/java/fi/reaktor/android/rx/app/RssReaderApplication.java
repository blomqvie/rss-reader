package fi.reaktor.android.rx.app;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fi.reaktor.android.rx.data.Feed;
import fi.reaktor.android.rx.data.Feeds;
import fi.reaktor.android.rx.data.PeriodicUpdates;

public class RssReaderApplication extends Application {
    private PeriodicUpdates periodicUpdates;

    private Feeds feeds = new Feeds();

    @Override
    public void onCreate() {
        Log.d("APPLICATION", "onCreate()");
        super.onCreate();
        periodicUpdates = new PeriodicUpdates(feeds);
        periodicUpdates.start();
    }

    @Override
    public void onTerminate() {
        Log.d("APPLICATION", "onTerminate()");
        periodicUpdates.stop();
        super.onTerminate();
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
