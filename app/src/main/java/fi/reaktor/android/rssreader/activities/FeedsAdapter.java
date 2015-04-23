package fi.reaktor.android.rssreader.activities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import fi.reaktor.android.rssreader.R;
import fi.reaktor.android.rssreader.app.ApplicationConstants;
import fi.reaktor.android.rssreader.data.Feed;
import fi.reaktor.android.rssreader.data.Feeds;

public class FeedsAdapter extends BaseAdapter {

    private final List<Feed> feeds;
    private Context context;

    public FeedsAdapter(Feeds feeds, Context context) {
        this.feeds = feeds.getFeeds();
        this.context = context;
    }

    @Override
    public int getCount() {
        return feeds.size();
    }

    @Override
    public Feed getItem(int i) {
        return feeds.get(i);
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).guid.hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.listitem_feed, null);
            TextView title = (TextView) view.findViewById(R.id.feed_title);
            TextView unread = (TextView) view.findViewById(R.id.feed_unread);
            view.setTag(new Holder(title, unread));
        }

        Feed feed = getItem(i);

        Holder h = (Holder) view.getTag();
        h.title.setText(feed.title);
        h.unread.setText("0");
        view.setOnClickListener(view1 -> {
            Intent i1 = new Intent(context, FeedActivity.class);
            i1.putExtra(ApplicationConstants.FEED_GUID, feed.guid);
            i1.putExtra(ApplicationConstants.FEED_TITLE, feed.title);
            context.startActivity(i1);
        });

        return view;
    }

    private static class Holder {
        public final TextView title;
        public final TextView unread;

        public Holder(TextView title, TextView unread) {
            this.title = title;
            this.unread = unread;
        }
    }

}
