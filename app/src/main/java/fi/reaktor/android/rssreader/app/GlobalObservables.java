package fi.reaktor.android.rssreader.app;

import java.util.List;

import fi.reaktor.android.rssreader.data.Feed;
import fi.reaktor.android.rssreader.data.FeedUpdatesListener;
import fi.reaktor.android.rssreader.data.Feeds;
import rx.Observable;
import rx.subjects.BehaviorSubject;

public class GlobalObservables implements FeedUpdatesListener {
    private static GlobalObservables realThing = new GlobalObservables();
    private static GlobalObservables substitute = null;

    public static GlobalObservables getInstance() {
        if (substitute != null) {
            return substitute;
        } else {
            return realThing;
        }
    }

    public static void substitute(GlobalObservables fake) {
        substitute = fake;
    }

    public GlobalObservables() {
        feeds = BehaviorSubject.create(new Feeds());
    }

    protected BehaviorSubject<Feeds> feeds;

    public Observable<Feeds> getFeeds() {
        return feeds.asObservable();
    }

    @Override
    public void feedsUpdated(List<Feed> feeds) {
        this.feeds.onNext(new Feeds(feeds));
    }
}
