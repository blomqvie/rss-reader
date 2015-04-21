package fi.reaktor.android.rx;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import fi.reaktor.android.rx.app.RssReaderApplication;
import fi.reaktor.android.rx.data.Article;

public class ArticleActivity extends RssReaderBaseActivity {
    private static final String TAG = ArticleActivity.class.getSimpleName();
    public static final String EXTRA_ARTICLE_GUID = "EXTRA_ARTICLE_GUID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        String guid = getIntent().getStringExtra(EXTRA_ARTICLE_GUID);

        Article article = ((RssReaderApplication)getApplication()).getFeeds().findArticle(guid);

        ((TextView)findViewById(R.id.article_title)).setText(article.getTitle());
        ((TextView)findViewById(R.id.article_published)).setText(formatDate(article.getPublished()));
        ((TextView)findViewById(R.id.article_text)).setText(article.getContent());
        Log.d(TAG, "Displaying article: " + article.getContent());
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
