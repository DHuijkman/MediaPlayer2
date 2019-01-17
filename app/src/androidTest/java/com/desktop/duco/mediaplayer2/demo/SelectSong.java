package com.desktop.duco.mediaplayer2.demo;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;

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
import static org.hamcrest.Matchers.allOf;

public class SelectSong  extends AbstractTest {



    private int pause = 1000;
    public void start(){
        customSleep(pause);
        onView(allOf(ViewMatchers.withId(R.id.recycler_view), isDisplayed()))
                .perform(RecyclerViewActions
                        .actionOnHolderItem(ScrollObjects.scrollSongs("Initial D - Deja Vu"),click()));
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
