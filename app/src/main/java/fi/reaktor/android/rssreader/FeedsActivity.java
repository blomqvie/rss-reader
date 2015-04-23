package fi.reaktor.android.rssreader;

import android.app.ActionBar;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import fi.reaktor.android.rssreader.app.RssReaderApplication;
import fi.reaktor.android.rssreader.data.Feeds;
import rx.Subscription;
import rx.android.app.AppObservable;
import rx.android.content.ContentObservable;

public class FeedsActivity extends RssReaderBaseActivity implements ActionBar.OnNavigationListener {

    private static final String TAG = FeedsActivity.class.getSimpleName();
    private static final String[] spinnerOptions = new String[]{"All", "Unread", "Favorites"};

    private Subscription broadcasts;

    private void setAdapter() {
        ListView feedsList = (ListView) findViewById(R.id.feeds_list);
        RssReaderApplication app = (RssReaderApplication) getApplication();
        Feeds feeds = app.getFeeds();
        feedsList.setAdapter(new FeedsAdapter(feeds, this));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds);
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getActionBar().setListNavigationCallbacks(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerOptions), this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAdapter();
        broadcasts = AppObservable
                .bindActivity(this, ContentObservable.fromLocalBroadcast(this, new IntentFilter("feeds-updated")))
                .subscribe(i -> {
                    setAdapter();
                    Log.d(TAG, "FeedsActivity received new data for feeds list");
                });
    }

    @Override
    protected void onPause() {
        broadcasts.unsubscribe();
        super.onPause();
    }

    @Override
    public boolean onNavigationItemSelected(int i, long l) {
        Log.d(TAG, "TODO: Feeds should be filtered: " + spinnerOptions[i]);
        return true;
    }
}
