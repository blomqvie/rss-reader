package fi.reaktor.android.rssreader;

import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Sequences;

import java.util.Date;

import fi.reaktor.android.rssreader.app.ApplicationConstants;
import fi.reaktor.android.rssreader.app.RssReaderApplication;
import fi.reaktor.android.rssreader.data.Feed;
import fi.reaktor.android.rssreader.data.Feeds;
import rx.Subscription;
import rx.android.app.AppObservable;
import rx.android.content.ContentObservable;

public class FeedActivity extends RssReaderBaseActivity {

    private Subscription broadcasts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(getIntent().getCharSequenceExtra(ApplicationConstants.FEED_TITLE));
    }

    private String getFeedId() {
        return getIntent().getStringExtra("feed-guid");
    }

    private Option<Feed> findFeed(String feedGuid) {
        Feeds feeds = ((RssReaderApplication) getApplication()).getFeeds();
        return feeds.getFeedSeq().find(f -> f.guid.equals(feedGuid));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // XXX: this is silly
        String feedGuid = getFeedId();
        Option<Feed> feed = findFeed(feedGuid);
        if (feed.isEmpty()) {
            finish();
        }
        ListView articleList = (ListView) findViewById(R.id.article_list);
        articleList.setAdapter(new ArticlesAdapter(feed.get(), this));
        broadcasts = AppObservable
                .bindActivity(this, ContentObservable.fromLocalBroadcast(this, new IntentFilter("feeds-updated"))
                .map(i -> findFeed(getFeedId())))
                .subscribe(feedOpt -> {
                    Feed f = feedOpt.getOrElse(new Feed("guid", "Placeholder feed", new Date(), Sequences.empty()));
                    articleList.setAdapter(new ArticlesAdapter(f, FeedActivity.this));
                });
    }

    @Override
    protected void onPause() {
        broadcasts.unsubscribe();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feed, menu);
        MenuItem favoriteItem = menu.findItem(R.id.action_favorite);
        // if(feed.isFavorite()) {
        //     favoriteItem.setIcon(android.R.drawable.ic_delete);
        // }
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
            // feed.setFavorite(!feed.isFavorite());
            // item.setIcon(feed.isFavorite() ? android.R.drawable.ic_delete : android.R.drawable.ic_input_add);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
