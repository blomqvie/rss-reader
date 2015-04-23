package fi.reaktor.android.rssreader.observables;

import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import rx.Observable;
import rx.Subscriber;
import rx.android.AndroidSubscriptions;

public class ScrollViewObservable implements Observable.OnSubscribe<Integer> {

    private final ScrollView view;

    public static Observable<Integer> create(final ScrollView view) {
        return Observable.create(new ScrollViewObservable(view));
    }

    private ScrollViewObservable(ScrollView view) {
        this.view = view;
    }

    @Override
    public void call(Subscriber<? super Integer> o) {
        final ViewTreeObserver.OnScrollChangedListener listener = () -> {
            int visibleBottom = view.getScrollY() + view.getHeight();
            int contentBottom = view.getChildAt(0).getHeight();
            float percentage = (visibleBottom * 100.0f) / contentBottom;
            if (!Float.isNaN(percentage)) {
                o.onNext(Math.min(100, Math.round(percentage)));
            }
        };
        view.getViewTreeObserver().addOnScrollChangedListener(listener);
        o.add(AndroidSubscriptions.unsubscribeInUiThread(() -> {
            view.getViewTreeObserver().removeOnScrollChangedListener(listener);
        }));
    }
}
