package fi.reaktor.android.rssreader;

import android.view.View;
import android.widget.TextView;

import junit.framework.Assert;

public class ViewAssertions {
    public static void assertIsVisible(TextView titleView) {
        Assert.assertEquals(titleView.getVisibility(), View.VISIBLE);
    }

    public static void assertIsInvisible(TextView titleView) {
        Assert.assertEquals(titleView.getVisibility(), View.INVISIBLE);
    }

    public static void assertIsGone(TextView titleView) {
        Assert.assertEquals(titleView.getVisibility(), View.GONE);
    }
}
