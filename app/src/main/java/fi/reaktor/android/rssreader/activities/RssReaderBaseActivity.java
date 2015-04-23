package fi.reaktor.android.rssreader.activities;

import android.app.Activity;

import fi.reaktor.android.rssreader.app.GlobalObservables;
import fi.reaktor.android.rssreader.app.RssReaderApplication;
import fi.reaktor.android.rssreader.data.Feeds;
import rx.Observable;

public abstract class RssReaderBaseActivity extends Activity {

    @Override
    protected void onPause() {
        ((RssReaderApplication)getApplication()).persistDataIfModified();
        super.onPause();
    }

    protected Observable<Feeds> getFeedsObservable() {
        return GlobalObservables.getInstance().getFeeds();
    }
}
