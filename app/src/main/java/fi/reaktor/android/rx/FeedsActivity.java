package fi.reaktor.android.rx;

import android.app.Activity;
import android.os.Bundle;

public class FeedsActivity extends Activity {

    private static final String TAG = FeedsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds);
    }
}
