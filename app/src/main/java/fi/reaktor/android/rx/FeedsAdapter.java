package fi.reaktor.android.rx;

import android.content.Context;
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
            view = new TextView(context);
        }
        TextView tw = (TextView) view;
        tw.setText(getItem(i).getTitle());
        return tw;
    }
}
