package fi.reaktor.android.rx;

import android.app.Activity;

import fi.reaktor.android.rx.app.RssReaderApplication;

public abstract class RssReaderBaseActivity extends Activity {

    @Override
    protected void onPause() {
        ((RssReaderApplication)getApplicationContext()).persistDataIfModified();
        super.onPause();
    }
}
