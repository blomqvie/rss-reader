package fi.reaktor.android.rx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ListView;

import fi.reaktor.android.rx.app.RssReaderApplication;
import fi.reaktor.android.rx.data.Feeds;

public class FeedsActivity extends RssReaderBaseActivity {

    private static final String TAG = FeedsActivity.class.getSimpleName();

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ListView feedsList = (ListView) findViewById(R.id.feeds_list);
            ((BaseAdapter) feedsList.getAdapter()).notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds);

        ListView feedsList = (ListView) findViewById(R.id.feeds_list);
        Feeds feeds = ((RssReaderApplication) getApplication()).getFeeds();
        feedsList.setAdapter(new FeedsAdapter(feeds, this));

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("feeds-updated"));
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
}
