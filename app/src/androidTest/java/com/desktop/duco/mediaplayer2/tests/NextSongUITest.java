package com.desktop.duco.mediaplayer2.tests;


import android.Manifest;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.desktop.duco.mediaplayer2.AbstractTest;
import com.desktop.duco.mediaplayer2.MainActivity;
import com.desktop.duco.mediaplayer2.R;
import com.desktop.duco.mediaplayer2.util.Reset;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.desktop.duco.mediaplayer2.util.CustomSleep.customSleep;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;


public class NextSongUITest extends AbstractTest {
    private int pause = 5000;
    public void start() {
        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.recycler_view),isDisplayed())).perform(RecyclerViewActions.actionOnHolderItem(
                        ScrollObjects.withHolder("Sabaton - Man Of War"),click()
        ));
        appCompatTextView.perform(click());

        onView(allOf(withId(R.id.pager), isDisplayed()))
                .perform(swipeLeft());

        customSleep(pause);
//
//        onView(allOf(withId(R.id.btNxt),isDisplayed())).perform(click());
//
//
//        ViewInteraction appCompatImageView3 = onView(
//                allOf(withId(R.id.btPlay),isDisplayed()));
//        appCompatImageView3.perform(click());
//
//        ViewInteraction appCompatImageView4 = onView(
//                allOf(withId(R.id.tvTitle) ,isDisplayed()));
//        appCompatImageView4.check(matches(not(withText("Sabaton - Man Of War"))));
    }

    public void reset() {
        Reset.reset();
    }


}
