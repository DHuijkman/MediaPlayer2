package com.desktop.duco.mediaplayer2.util;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;

import com.desktop.duco.mediaplayer2.R;

import org.hamcrest.core.AllOf;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;

public class Reset {
    public static void reset() {
        while(true) {
            try {
                onView(allOf(withId(R.id.recycler_view), isDisplayed()))
                        .check(matches(isDisplayed()));
                onView(allOf(withId(R.id.recycler_view),isDisplayed()))
                        .perform(RecyclerViewActions.scrollToPosition(0));
                return;
            } catch (Error | Exception e) {
                onView(allOf(withId(R.id.pager), isDisplayed()))
                        .perform(swipeRight());
            }
        }
    }
}
