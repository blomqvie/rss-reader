package fi.reaktor.android.rssreader;

import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import fi.reaktor.android.rssreader.activities.ArticleActivity;
import fi.reaktor.android.rssreader.app.ApplicationConstants;
import fi.reaktor.android.rssreader.app.GlobalObservables;
import fi.reaktor.android.rssreader.app.RssReaderApplication;
import fi.reaktor.android.rssreader.data.Article;
import fi.reaktor.android.rssreader.data.Feeds;
import fi.reaktor.android.rssreader.testutils.RssReaderActivityTestCase;
import rx.Observable;

import static fi.reaktor.android.rssreader.ViewAssertions.assertIsVisible;

public class ArticleActivityTest extends RssReaderActivityTestCase<ArticleActivity> {

    private static final String TAG = ArticleActivityTest.class.getSimpleName();
    private static final AtomicInteger counter = new AtomicInteger(1);

    private Article article;

    public ArticleActivityTest() {
        super(ArticleActivity.class);
    }

    @Override
    protected void beforeApplicationInit() {
        super.beforeApplicationInit();
        article = new Article(new Date(), "Android testing. Surprisingly difficult task. Sad panda.", "Poems for the programmer", "article-" + counter.getAndIncrement());
        final Feeds fakeFeeds = new Feeds() {
            @Override
            public Article findArticle(String guid) {
                Log.d(TAG, "Faking findArticle(\"" + guid + "\")");
                return article.guid.equals(guid) ? article : null;
            }
        };
        GlobalObservables.substitute(new GlobalObservables() {
            @Override
            public Observable<Feeds> getFeeds() {
                return Observable.just(fakeFeeds);
            }
        });
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Log.d(TAG, "setUp with article " + article.guid);
        Intent intent = new Intent(getInstrumentation().getTargetContext(), ArticleActivity.class);
        intent.putExtra(ApplicationConstants.ARTICLE_GUID, article.guid);
        setActivityIntent(intent);
    }

    public void testThatArticleTitleIsDisplayed() throws InterruptedException {
        Log.d(TAG, "testThatArticleTitleIsDisplayed running with Article " + article.guid);
        TextView titleView = (TextView) getActivity().findViewById(R.id.article_title);
        assertEquals(article.title, titleView.getText());
        assertIsVisible(titleView);

        Thread.sleep(10000);
    }

    public void testThatArticleDateIsDisplayed() {
        Log.d(TAG, "testThatArticleDateIsDisplayed running with Article " + article.guid);
        TextView dateView = (TextView) getActivity().findViewById(R.id.article_published);
        assertEquals(new SimpleDateFormat("dd.MM.yyyy HH:mm Z").format(article.published), dateView.getText());
    }

    public void testThatArticleContentIsDisplayed() {
        Log.d(TAG, "testThatArticleContentIsDisplayed running with Article " + article.guid);
        TextView contentView = (TextView) getActivity().findViewById(R.id.article_text);
        assertEquals(article.content, contentView.getText());
    }
}
