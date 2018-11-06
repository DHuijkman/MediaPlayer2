package com.desktop.duco.mediaplayer2;



import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import junit.framework.TestCase;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNot.not;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class MusicListSelectSongTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.WRITE_EXTERNAL_STORAGE");

    @Test
    public void clickItem(){
        onView(allOf(withId(R.id.recycler_view), isDisplayed()))
                .perform(RecyclerViewActions
                        .actionOnHolderItem(withHolder("Initial D - Deja Vu"),click()));

        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(allOf(withId(R.id.pager), isDisplayed())).perform(swipeLeft());

        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(allOf(withId(R.id.tvTitle), isDisplayed())).check(matches(withText("Initial D - Deja Vu")));


        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Matcher<RecyclerView.ViewHolder> withHolder(final String title){
        return new BoundedMatcher<RecyclerView.ViewHolder, SongItem.ViewHolder>(SongItem.ViewHolder.class) {

            @Override
            protected boolean matchesSafely(SongItem.ViewHolder item) {
                return item.title.getText().toString().equals(title);
            }

            @Override
            public void describeTo(Description description) {
            }


        };
    }

}
