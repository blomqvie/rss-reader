package fi.reaktor.android.rssreader.data;

import java.util.List;

public interface FeedUpdatesListener {

    void feedsUpdated(List<Feed> feeds);
}
