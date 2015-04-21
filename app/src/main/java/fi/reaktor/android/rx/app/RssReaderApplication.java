package fi.reaktor.android.rx.app;

import android.app.Application;
import android.util.Log;

import fi.reaktor.android.rx.data.PeriodicUpdates;

public class RssReaderApplication extends Application {
    private PeriodicUpdates periodicUpdates;

    @Override
    public void onCreate() {
        Log.d("APPLICATION", "onCreate()");
        super.onCreate();
        periodicUpdates = new PeriodicUpdates();
        periodicUpdates.start();
    }

    @Override
    public void onTerminate() {
        Log.d("APPLICATION", "onTerminate()");
        periodicUpdates.stop();
        super.onTerminate();
    }
}
