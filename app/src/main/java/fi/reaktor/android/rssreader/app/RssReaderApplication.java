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
import rx.subjects.BehaviorSubject;

public class RssReaderApplication extends Application implements FeedUpdatesListener {

    private PeriodicUpdates periodicUpdates;
    private BehaviorSubject<Feeds> feeds;
    private UserData userData;

    @Override
    public void onCreate() {
        Log.d("APPLICATION", "onCreate()");
        super.onCreate();
        feeds = BehaviorSubject.create(new Feeds());
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
        this.feeds.onNext(new Feeds(feeds));
    }

    public Observable<Feeds> getFeeds() {
        return feeds.asObservable();
    }

    public void persistDataIfModified() {
        userData.persist(this);
    }
}
