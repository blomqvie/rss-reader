package fi.reaktor.android.rx.app;

import android.app.Application;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.googlecode.totallylazy.Sequence;

import java.util.List;

import fi.reaktor.android.rx.data.Feed;
import fi.reaktor.android.rx.data.FeedUpdatesListener;
import fi.reaktor.android.rx.data.Feeds;
import fi.reaktor.android.rx.data.PeriodicUpdates;
import fi.reaktor.android.rx.data.UserData;

public class RssReaderApplication extends Application implements FeedUpdatesListener {

    private PeriodicUpdates periodicUpdates;
    private Feeds feeds;
    private UserData userData;

    @Override
    public void onCreate() {
        Log.d("APPLICATION", "onCreate()");
        super.onCreate();
        feeds = new Feeds();
        userData = UserData.load(this);
        periodicUpdates = new PeriodicUpdates(this);
        periodicUpdates.start();
    }

    @Override
    public void onTerminate() {
        Log.d("APPLICATION", "onTerminate()");
        periodicUpdates.stop();
        super.onTerminate();
    }

    @Override
    public void feedsUpdated(List<Feed> feeds) {
        this.feeds = new Feeds(feeds);
        // TODO 1: Implement this with RxJava Subject.
        // Create a field of type Subject in to RssReaderApplication and use it to publish new values.
        // Provide an API to subscribe to the Subject by exposing it as Observable.
        Intent intent = new Intent("feeds-updated");
        intent.putExtra("updated", true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public Feeds getFeeds() {
        return feeds;
    }

    public void persistDataIfModified() {
        userData.persist(this);
    }
}
