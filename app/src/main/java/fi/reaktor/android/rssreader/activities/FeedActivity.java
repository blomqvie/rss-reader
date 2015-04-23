package fi.reaktor.android.rssreader.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.googlecode.totallylazy.Sequences;

import java.util.Date;

import fi.reaktor.android.rssreader.R;
import fi.reaktor.android.rssreader.app.ApplicationConstants;
import fi.reaktor.android.rssreader.data.Feed;
import rx.Subscription;
import rx.android.app.AppObservable;

public class FeedActivity extends RssReaderBaseActivity {

    private Subscription feeds;
    private String feedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(getIntent().getCharSequenceExtra(ApplicationConstants.FEED_TITLE));
        feedId = getIntent().getStringExtra("feed-guid");
    }

    @Override
    protected void onResume() {
        super.onResume();
        feeds = AppObservable
                .bindActivity(this, getFeedsObservable())
                .map(feeds -> feeds.getFeedSeq().find(f -> f.guid.equals(feedId)))
                .subscribe(feedOpt -> {
                    Feed f = feedOpt.getOrElse(new Feed("guid", "Placeholder feed", new Date(), Sequences.empty()));
                    ListView articleList = (ListView) findViewById(R.id.article_list);
                    articleList.setAdapter(new ArticlesAdapter(f, FeedActivity.this));
                });
    }

    @Override
    protected void onPause() {
        feeds.unsubscribe();
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
