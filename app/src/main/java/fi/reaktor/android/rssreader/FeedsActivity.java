package fi.reaktor.android.rssreader;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import rx.Subscription;
import rx.android.app.AppObservable;

public class FeedsActivity extends RssReaderBaseActivity implements ActionBar.OnNavigationListener {

    private static final String TAG = FeedsActivity.class.getSimpleName();
    private static final String[] spinnerOptions = new String[]{"All", "Unread", "Favorites"};

    private Subscription feeds;

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
        feeds = AppObservable
                .bindActivity(this, getFeedsObservable())
                .subscribe(feeds -> {
                    Log.d(TAG, "FeedsActivity received new data for feeds list");
                    ListView feedsList = (ListView) findViewById(R.id.feeds_list);
                    feedsList.setAdapter(new FeedsAdapter(feeds, this));
                });
    }

    @Override
    protected void onPause() {
        feeds.unsubscribe();
        super.onPause();
    }

    @Override
    public boolean onNavigationItemSelected(int i, long l) {
        Log.d(TAG, "TODO: Feeds should be filtered: " + spinnerOptions[i]);
        return true;
    }
}
