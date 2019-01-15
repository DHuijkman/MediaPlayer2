package com.desktop.duco.mediaplayer2.objects;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.RecyclerView;

import com.desktop.duco.mediaplayer2.objects.SongItem;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class ScrollObjects {
    public static Matcher<RecyclerView.ViewHolder> scrollSongs(final String title) {
        return new BoundedMatcher<RecyclerView.ViewHolder, SongItem.ViewHolder>(SongItem.ViewHolder.class) {

            @Override
            protected boolean matchesSafely(SongItem.ViewHolder item) {
                return item.title.getText().toString().equals(title);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("current text: " + title);
            }
        };
    }
}
