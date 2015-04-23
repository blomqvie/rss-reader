package fi.reaktor.android.rssreader.app;

import android.app.Application;
import android.util.Log;

import fi.reaktor.android.rssreader.data.Feeds;
import fi.reaktor.android.rssreader.data.PeriodicUpdates;
import fi.reaktor.android.rssreader.data.UserData;
import rx.Observable;
import rx.Subscription;
import rx.observables.ConnectableObservable;

public class RssReaderApplication extends Application {

    public static boolean manualUpdates = false;

    private UserData userData;
    private ConnectableObservable<Feeds> feeds;
    private Subscription hotSubscription;

    @Override
    public void onCreate() {
        Log.d("APPLICATION", "onCreate() with static mutable as " + manualUpdates);
        super.onCreate();
        feeds = PeriodicUpdates.updates().replay(1);
        hotSubscription = feeds.connect();
        userData = UserData.load(this);
    }

    @Override
    public void onTerminate() {
        Log.d("APPLICATION", "onTerminate()");
        hotSubscription.unsubscribe();
        super.onTerminate();
    }

    public Observable<Feeds> getFeeds() {
        return feeds;
    }

    public void persistDataIfModified() {
        userData.persist(this);
    }
}
