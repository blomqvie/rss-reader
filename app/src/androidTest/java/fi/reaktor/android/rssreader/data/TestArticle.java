package fi.reaktor.android.rssreader.data;

import android.test.AndroidTestCase;

import java.util.Date;

public class TestArticle extends AndroidTestCase {

    public void testContentIsTrimmedFromBeginningAndEnd() {
        Article article = new Article(new Date(), " Much trimming needed around this    \t  ", "", "");
        assertEquals("Much trimming needed around this", article.content);
    }

    public void testTabsAreConvertedToRegularSpaces() {
        Article article = new Article(new Date(), "Tabs\tbecome  \t  regular \tspaces", "", "");
        assertEquals("Tabs become regular spaces", article.content);
    }

    public void testContentWithoutEscapingsIsLeftAsIs() {
        Article article = new Article(new Date(), "No escaped HTML entities here", "", "");
        assertEquals("No escaped HTML entities here", article.content);
    }

    public void testHtmlEntitiesAreDecoded() {
        Article article = new Article(new Date(), "Entities like &auml; are decoded", "", "");
        assertEquals("Entities like ä are decoded", article.content);
    }

    public void testNonBreakingSpaceBecomesRegularSpace() {
        Article article = new Article(new Date(), "Non&nbsp;breaking&nbsp;space", "", "");
        assertEquals("Non breaking space", article.content);
    }
}