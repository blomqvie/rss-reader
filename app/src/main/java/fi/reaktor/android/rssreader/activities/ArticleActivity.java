package fi.reaktor.android.rssreader.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import fi.reaktor.android.rssreader.R;
import fi.reaktor.android.rssreader.app.ApplicationConstants;
import fi.reaktor.android.rssreader.app.GlobalObservables;
import fi.reaktor.android.rssreader.app.RssReaderApplication;
import fi.reaktor.android.rssreader.data.Article;
import rx.android.app.AppObservable;
import fi.reaktor.android.rssreader.data.Feeds;
import rx.Observable;

public class ArticleActivity extends RssReaderBaseActivity {
    private static final String TAG = ArticleActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        String guid = getIntent().getStringExtra(ApplicationConstants.ARTICLE_GUID);

        // TODO assumes behaviour subject, use replay or similar
        AppObservable.bindActivity(this, getFeedsObservable().take(1)).subscribe(feeds -> {
            Article article = feeds.findArticle(guid);
            if (article == null) return;
            getActionBar().setTitle(getIntent().getCharSequenceExtra(ApplicationConstants.FEED_TITLE));

            ((TextView) findViewById(R.id.article_title)).setText(article.title);
            ((TextView) findViewById(R.id.article_published)).setText(formatDate(article.published));
            ((TextView) findViewById(R.id.article_text)).setText(article.content);
            Log.d(TAG, "Displaying article: " + article.content);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "ArticleActivity.onDestroy()");
    }

    private String formatDate(Date date) {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm Z").format(date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_article, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }
}
