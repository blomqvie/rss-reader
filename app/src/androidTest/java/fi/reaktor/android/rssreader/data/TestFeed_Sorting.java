package fi.reaktor.android.rssreader.data;

import android.test.AndroidTestCase;

import org.joda.time.DateTime;

import java.util.Date;

import static com.googlecode.totallylazy.Sequences.sequence;

public class TestFeed_Sorting extends AndroidTestCase {

    private Article article1;
    private Article article3;
    private Article article2;
    private Article article4;
    private Feed feed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        article1 = new Article(date("2015-04-12"), blah(), blah(), "A1");
        article2 = new Article(date("2015-04-15"), blah(), blah(), "A2");
        article3 = new Article(date("2015-04-21"), blah(), blah(), "A3");
        article4 = new Article(date("2015-04-23"), blah(), blah(), "A4");
        feed = new Feed("Feed-X", blah(), new Date(), sequence(article1, article2, article3, article4));
    }


    public void testFilterPastWeeksArticles() {
        assertEquals(sequence(article4, article3), feed.pastWeeksArticles());
    }


    private static Date date(String stringRepresentation) {
        return DateTime.parse(stringRepresentation).toDate();
    }

    private static String blah() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < 3 + Math.random() * 10; i++) {
            s.append(i == 0 ? "Blah" : "blah");
        }
        return s.toString();
    }
}
