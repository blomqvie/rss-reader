package fi.reaktor.android.rx.data;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class FeedsTest {

    private final Article articleA2 = article("A2");
    private final Article articleB1 = article("B1");
    private final Feed feed1 = new Feed("F1", "Feed 1", new Date(), Arrays.asList(article("A1"), articleA2));
    private final Feed feed2 = new Feed("F2", "Feed 2", new Date(), Arrays.asList(articleB1, article("B2")));
    private Feeds feeds;

    @Before
    public void setUp() {
        feeds = new Feeds(Arrays.asList(feed1, feed2));
    }

    @Test
    public void findsArticlesByTheirGUID() {
        assertEquals(feeds.findArticle("B1"), articleB1);
        assertEquals(feeds.findArticle("A2"), articleA2);
    }

    /**
     * Shorthand for creating an article when we're only interested in its identity rather than content.
     */
    private Article article(String guid) {
        return new Article(new Date(), "Content of " + guid, "Title of " + guid, guid);
    }
}