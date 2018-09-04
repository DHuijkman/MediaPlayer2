package com.desktop.duco.mediaplayer2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.listeners.ClickEventHook;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

public class SimpleItem extends AbstractItem<SimpleItem, SimpleItem.ViewHolder> {

    public interface SimpleItemClickDelegate {
        void onSimpleItemClick(final SimpleItem simpleItem, View v);
        void onOptionClick(final SimpleItem simpleItem, View v);
    }
    public Boolean shouldAnimate = false;
    public String songTitle;


    public SimpleItem(String songTitle) {
        this.songTitle = songTitle;
    }


    //The unique ID for this type of item
    @Override
    public int getType() {
        return R.id.song_adapter_id;
    }

    //The layout to be used for this type of item
    @Override
    public int getLayoutRes() {
        return R.layout.musiclist_layout;
    }

    @Override
    public ViewHolder getViewHolder(@NonNull View v) {
        return new ViewHolder(v);
    }

    public static class ClickDelegate<T extends SimpleItemClickDelegate> extends ClickEventHook<SimpleItem> {
        private WeakReference<T> context;

        public ClickDelegate(WeakReference<T> context) {
            this.context = context;
        }

        @Nullable
        @Override
        public View onBind(RecyclerView.ViewHolder viewHolder) {
            if(viewHolder instanceof ViewHolder) {
                ViewHolder vh = (ViewHolder) viewHolder;
                return vh.title;
            }
            return null;
        }

        @Override
        public void onClick(View v, int position, FastAdapter<SimpleItem> fastAdapter, SimpleItem item) {
            if(context.get() == null)
                return;

            context.get().onSimpleItemClick(item,v);
        }
    }

    public static class OptionClickDelegate<T extends SimpleItemClickDelegate> extends ClickEventHook<SimpleItem> {
        private WeakReference<T> context;

        public OptionClickDelegate(WeakReference<T> context) {
            this.context = context;
        }

        @Nullable
        @Override
        public View onBind(RecyclerView.ViewHolder viewHolder) {
            if(viewHolder instanceof ViewHolder) {
                ViewHolder vh = (ViewHolder) viewHolder;
                return vh.ivOptions;
            }
            return null;
        }

        @Override
        public void onClick(View v, int position, FastAdapter<SimpleItem> fastAdapter, SimpleItem item) {
            if(context.get() == null)
                return;

            context.get().onOptionClick(item,v);
        }
    }


    /**
     * our ViewHolder
     */
    public static class ViewHolder extends FastAdapter.ViewHolder<SimpleItem> {
        protected ImageView ivPlayingCurrently;
        protected ImageView ivOptions;
        protected TextView title;


        public ViewHolder(View view) {
            super(view);
            this.ivPlayingCurrently = view.findViewById(R.id.iv_playing_currently);
            this.ivOptions = view.findViewById(R.id.iv_options);
            this.title = view.findViewById(R.id.title);

        }

        @Override
        public void bindView(SimpleItem item, List<Object> payloads) {
            title.setText(item.songTitle);
            title.setSelected(item.shouldAnimate);
        }

        @Override
        public void unbindView(SimpleItem item) {
            title.setSelected(false);
        }
    }
}
