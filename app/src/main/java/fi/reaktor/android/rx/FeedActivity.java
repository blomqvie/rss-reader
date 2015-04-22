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

import com.googlecode.totallylazy.Option;

import fi.reaktor.android.rx.app.RssReaderApplication;
import fi.reaktor.android.rx.data.Feed;
import fi.reaktor.android.rx.data.Feeds;

public class FeedActivity extends RssReaderBaseActivity {

    BroadcastReceiver feedUpdatesReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ListView articleList = (ListView) findViewById(R.id.article_list);
            RssReaderApplication app = (RssReaderApplication) getApplication();
            Option<Feed> feed = app.getFeeds().getFeedSeq().find(f -> f.guid.equals(getFeedId()));
            articleList.setAdapter(new ArticlesAdapter(feed.getOrNull(), FeedActivity.this));
        }
    };

    private Feed feed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(getIntent().getCharSequenceExtra("feed-title"));

        String feedGuid = getFeedId();
        findFeed(feedGuid);
        if(feed == null) {
            finish();
        } else {
            ListView articleList = (ListView) findViewById(R.id.article_list);
            articleList.setAdapter(new ArticlesAdapter(feed, this));
            LocalBroadcastManager.getInstance(this).registerReceiver(feedUpdatesReceiver, new IntentFilter("feeds-updated"));
        }
    }

    private String getFeedId() {
        return getIntent().getStringExtra("feed-guid");
    }

    private void findFeed(String feedGuid) {
        Feeds feeds = ((RssReaderApplication) getApplication()).getFeeds();
        for (Feed f : feeds.getFeeds()) {
            if (f.guid.equals(feedGuid)) {
                feed = f;
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(feedUpdatesReceiver);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feed, menu);
        MenuItem favoriteItem = menu.findItem(R.id.action_favorite);
        // TODO read user data
//        if(feed.isFavorite()) {
//            favoriteItem.setIcon(android.R.drawable.ic_delete);
//        }
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
            // TODO read user data
//            feed.setFavorite(!feed.isFavorite());
//            item.setIcon(feed.isFavorite() ? android.R.drawable.ic_delete : android.R.drawable.ic_input_add);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
