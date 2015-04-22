package fi.reaktor.android.rx;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import fi.reaktor.android.rx.app.RssReaderApplication;
import fi.reaktor.android.rx.data.Feeds;

public class FeedsActivity extends RssReaderBaseActivity implements ActionBar.OnNavigationListener {

    private static final String TAG = FeedsActivity.class.getSimpleName();
    private static final String[] spinnerOptions = new String[]{"All", "Unread", "Favorites"};

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setAdapter();
            Log.d(TAG, "FeedsActivity received new data for feeds list");
        }
    };

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

        setAdapter();

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("feeds-updated"));

        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getActionBar().setListNavigationCallbacks(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerOptions), this);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(int i, long l) {
        Log.d(TAG, "TODO: Feeds should be filtered: " + spinnerOptions[i]);
        return true;
    }
}
