package fi.reaktor.android.rx.data;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class ArticleTest {

    @Test
    public void contentIsTrimmedFromBeginningAndEnd() {
        Article article = new Article(new Date(), " Much trimming needed around this    \t  ", "", "");
        assertEquals("Much trimming needed around this", article.content);
    }

    @Test
    public void tabsAreConvertedToRegularSpaces() {
        Article article = new Article(new Date(), "Tabs\tbecome  \t  regular \tspaces", "", "");
        assertEquals("Tabs become regular spaces", article.content);
    }

    @Test
    public void contentWithoutEscapingsIsLeftAsIs() {
        Article article = new Article(new Date(), "No escaped HTML entities here", "", "");
        assertEquals("No escaped HTML entities here", article.content);
    }

    @Test
    public void htmlEntitiesAreDecoded() {
        Article article = new Article(new Date(), "Entities like &auml; are decoded", "", "");
        assertEquals("Entities like ä are decoded", article.content);
    }

    @Test
    public void nonBreakingSpaceBecomesRegularSpace() {
        Article article = new Article(new Date(), "Non&nbsp;breaking&nbsp;space", "", "");
        assertEquals("Non breaking space", article.content);
    }
}