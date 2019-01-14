package com.desktop.duco.mediaplayer2.tests;

import android.content.Context;
import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;

import com.desktop.duco.mediaplayer2.AbstractTest;
import com.desktop.duco.mediaplayer2.MainActivity;
import com.desktop.duco.mediaplayer2.R;
import com.desktop.duco.mediaplayer2.util.GetTestActivity;
import com.desktop.duco.mediaplayer2.util.Reset;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.desktop.duco.mediaplayer2.util.CustomSleep.customSleep;
import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNot.not;

public class MusicListSelectSongTest extends AbstractTest {



    private int pause = 100;
    public void start(){
        onView(allOf(ViewMatchers.withId(R.id.recycler_view), isDisplayed()))
                .perform(RecyclerViewActions
                        .actionOnHolderItem(ScrollObjects.withHolder("Initial D - Deja Vu"),click()));
        customSleep(pause);
        onView(allOf(withId(R.id.pager), isDisplayed())).perform(swipeLeft());
        customSleep(pause);
        onView(allOf(withId(R.id.tvTitle), isDisplayed())).check(matches(withText("Initial D - Deja Vu")));
        customSleep(pause);
    }

    public void reset() {
        Reset.reset();
    }



}
