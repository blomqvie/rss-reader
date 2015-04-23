package fi.reaktor.android.rssreader;

import android.app.Activity;

import fi.reaktor.android.rssreader.app.RssReaderApplication;

public abstract class RssReaderBaseActivity extends Activity {

    @Override
    protected void onPause() {
        ((RssReaderApplication)getApplicationContext()).persistDataIfModified();
        super.onPause();
    }
}
