package fi.reaktor.android.rx;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import fi.reaktor.android.rx.data.Feed;
import fi.reaktor.android.rx.data.Feeds;

public class FeedsAdapter extends BaseAdapter {

    private final Feeds feeds;
    private Context context;

    public FeedsAdapter(Feeds feeds, Context context) {
        this.feeds = feeds;
        this.context = context;
    }

    @Override
    public int getCount() {
        return feeds.getFeeds().size();
    }

    @Override
    public Feed getItem(int i) {
        return feeds.getFeeds().get(i);
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).getGuid().hashCode();
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

        Holder h = (Holder) view.getTag();
        h.title.setText(getItem(i).getTitle());
        h.unread.setText("0");
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
