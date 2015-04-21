package fi.reaktor.android.rx.data;

import java.util.ArrayList;
import java.util.List;

public class Feeds {

    private List<Feed> feeds = new ArrayList<>();

    public List<Feed> getFeeds() {
        return feeds;
    }

    public void add(Feed feed) {
        boolean found = false;
        for (Feed f : feeds) {
            if(f.getGuid().equals(feed.getGuid())) {
                found = true;
                f.update(feed);
                break;
            }
        }
        if(!found) {
            feeds.add(feed);
        }
    }
}
