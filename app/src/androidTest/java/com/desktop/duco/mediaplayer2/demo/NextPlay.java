package com.desktop.duco.mediaplayer2.demo;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;

import com.desktop.duco.mediaplayer2.AbstractTest;
import com.desktop.duco.mediaplayer2.R;
import com.desktop.duco.mediaplayer2.objects.ScrollObjects;
import com.desktop.duco.mediaplayer2.util.Reset;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.desktop.duco.mediaplayer2.util.CustomSleep.customSleep;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;

public class NextPlay extends AbstractTest {
    private int pause = 1000;
    public void start() {
        customSleep(pause);
        onView(allOf(withId(R.id.recycler_view),isDisplayed())).perform(RecyclerViewActions.actionOnHolderItem(
                ScrollObjects.scrollSongs("George Ezra - Shotgun (Jesse Bloch Bootleg) [FREE DOWNLOAD]"),click()));

        onView(allOf(withId(R.id.pager), isDisplayed()))
                .perform(swipeLeft());

        customSleep(pause);

        onView(allOf(withId(R.id.btNxt),isDisplayed())).perform(click());

        customSleep(pause);

        ViewInteraction appCompatImageView3 = onView(
                allOf(withId(R.id.btPlay),isDisplayed()));
        appCompatImageView3.perform(click());

        customSleep(pause);

        ViewInteraction appCompatImageView4 = onView(
                allOf(withId(R.id.tvTitle) ,isDisplayed()));
        appCompatImageView4.check(matches(not(withText("Spiderbait - Black Betty (Oh'Sabi! Bounce Betty Bootleg) [FREE DOWNLOAD]"))));
    }

    public void reset() {
        Reset.reset();
    }
}
