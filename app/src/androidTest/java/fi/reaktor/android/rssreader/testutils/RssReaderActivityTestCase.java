package fi.reaktor.android.rssreader.testutils;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import fi.reaktor.android.rssreader.app.RssReaderApplication;

public abstract class RssReaderActivityTestCase<T extends Activity> extends ActivityInstrumentationTestCase2<T> {

    public RssReaderActivityTestCase(Class<T> activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        beforeApplicationInit();
        super.setUp();
        RssReaderApplication app = (RssReaderApplication) getInstrumentation().getTargetContext().getApplicationContext();
        afterApplicationInit(app);
    }

    protected void beforeApplicationInit() {}
    protected void afterApplicationInit(RssReaderApplication application) {}
}
