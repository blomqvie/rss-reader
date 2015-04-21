package fi.reaktor.android.rx;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import fi.reaktor.android.rx.data.PeriodicUpdates;

public class FeedsActivity extends Activity {

    private static final String TAG = FeedsActivity.class.getSimpleName();

    private PeriodicUpdates periodicUpdates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds);
        periodicUpdates = new PeriodicUpdates();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
        periodicUpdates.start();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause()");
        periodicUpdates.stop();
        super.onPause();
    }
}
