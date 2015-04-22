package fi.reaktor.android.rx.data;

import com.googlecode.totallylazy.Sequence;

import java.util.List;

public interface FeedUpdatesListener {

    void feedsUpdated(List<Feed> feeds);
}
