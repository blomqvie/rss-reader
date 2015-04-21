package fi.reaktor.android.rx;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import fi.reaktor.android.rx.data.Article;
import fi.reaktor.android.rx.data.Feed;

public class ArticlesAdapter extends BaseAdapter {

    private Feed feed;
    private Context context;

    public ArticlesAdapter(Feed feed, Context context) {
        this.feed = feed;
        this.context = context;
    }

    @Override
    public int getCount() {
        return feed.getArticles().size();
    }

    @Override
    public Article getItem(int i) {
        return feed.getArticles().get(i);
    }

    @Override
    public long getItemId(int i) {
        return feed.getArticles().get(i).getGuid().hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listitem_article, null);
            TextView title = (TextView) view.findViewById(R.id.article_title);
            TextView teaser = (TextView) view.findViewById(R.id.article_teaser);
            view.setTag(new Holder(title, teaser));
        }

        Article article = getItem(i);

        Holder h = (Holder) view.getTag();
        h.title.setText(article.getTitle());
        String content = article.getContent();
        String teaserText = content != null ? (content.length() > 200 ? content.substring(0, 200) : content) : "No content available";
        h.teaser.setText(teaserText);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ArticleActivity.class);
                intent.putExtra("article-guid", article.getGuid());
                context.startActivity(intent);
            }
        });
        return view;
    }

    private static class Holder {
        public final TextView title;
        public final TextView teaser;

        public Holder(TextView title, TextView teaser) {
            this.title = title;
            this.teaser = teaser;
        }
    }
}
