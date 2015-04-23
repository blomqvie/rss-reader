package fi.reaktor.android.rssreader.observables;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public final class OperatorGrowing<T, U> implements Observable.Operator<T, T> {
    final Func1<? super T, ? extends U> keySelector;

    public OperatorGrowing(Func1<? super T, ? extends U> keySelector) {
        this.keySelector = keySelector;
    }

    @Override
    public Subscriber<? super T> call(final Subscriber<? super T> child) {
        return new Subscriber<T>(child) {
            U previousKey;
            boolean hasPrevious;
            @Override
            public void onNext(T t) {
                U currentKey = previousKey;
                U key = keySelector.call(t);
                previousKey = key;

                if (hasPrevious) {
                    if (!(currentKey == key || (key != null && key.equals(currentKey)))) {
                        child.onNext(t);
                    } else {
                        request(1);
                    }
                } else {
                    hasPrevious = true;
                    child.onNext(t);
                }
            }

            @Override
            public void onError(Throwable e) {
                child.onError(e);
            }

            @Override
            public void onCompleted() {
                child.onCompleted();
            }

        };
    }
}

