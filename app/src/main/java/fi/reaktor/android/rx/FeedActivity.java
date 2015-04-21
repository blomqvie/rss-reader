package fi.reaktor.android.rx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.ListView;

import fi.reaktor.android.rx.app.RssReaderApplication;
import fi.reaktor.android.rx.data.Feed;
import fi.reaktor.android.rx.data.Feeds;

public class FeedActivity extends RssReaderBaseActivity {

    BroadcastReceiver feedUpdatesReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ListView articleList = (ListView) findViewById(R.id.article_list);
            ((BaseAdapter) articleList.getAdapter()).notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        String feedGuid = getIntent().getStringExtra("feed-guid");
        Feeds feeds = ((RssReaderApplication) getApplication()).getFeeds();
        Feed feed = null;
        for (Feed f : feeds.getFeeds()) {
            if (f.getGuid().equals(feedGuid)) {
                feed = f;
                break;
            }
        }
        if(feed == null) {
            finish();
        } else {
            ListView articleList = (ListView) findViewById(R.id.article_list);
            articleList.setAdapter(new ArticlesAdapter(feed, this));
            LocalBroadcastManager.getInstance(this).registerReceiver(feedUpdatesReceiver, new IntentFilter("feeds-updated"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) {
            // TODO
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
