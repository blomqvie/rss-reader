package fi.reaktor.android.rssreader.app;

import android.app.Application;
import android.util.Log;

import java.util.List;

import fi.reaktor.android.rssreader.data.Feed;
import fi.reaktor.android.rssreader.data.FeedUpdatesListener;
import fi.reaktor.android.rssreader.data.Feeds;
import fi.reaktor.android.rssreader.data.PeriodicUpdates;
import fi.reaktor.android.rssreader.data.UserData;
import rx.Observable;
import rx.Subscription;
import rx.observables.ConnectableObservable;
import rx.subjects.BehaviorSubject;

public class RssReaderApplication extends Application {

    private UserData userData;
    private ConnectableObservable<Feeds> feeds;
    private Subscription hotSubscription;

    @Override
    public void onCreate() {
        Log.d("APPLICATION", "onCreate()");
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
