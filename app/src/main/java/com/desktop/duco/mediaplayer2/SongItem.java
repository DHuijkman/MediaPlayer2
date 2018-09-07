package com.desktop.duco.mediaplayer2;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.listeners.ClickEventHook;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SongItem extends AbstractItem<SongItem, SongItem.ViewHolder> {

    public interface SimpleItemClickDelegate {
        void onSimpleItemClick(final SongItem songItem, View v);
        void onOptionClick(final SongItem songItem, View v);
    }
    public Boolean shouldAnimate = false;
    public File songFile;
    public String songTitle;


    public SongItem(File songFile) {
        this.songFile = songFile;
        songTitle = songFile.getName().replace(".mp3","");
    }

    public File getSongFile() {
        return songFile;
    }

    public String getSongTitle() {
        return songTitle;
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

    public static class ClickDelegate<T extends SimpleItemClickDelegate> extends ClickEventHook<SongItem> {
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
        public void onClick(View v, int position, FastAdapter<SongItem> fastAdapter, SongItem item) {
            if(context.get() == null)
                return;

            context.get().onSimpleItemClick(item,v);
        }
    }

    public static class OptionClickDelegate<T extends SimpleItemClickDelegate> extends ClickEventHook<SongItem> {
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
        public void onClick(View v, int position, FastAdapter<SongItem> fastAdapter, SongItem item) {
            if(context.get() == null)
                return;

            context.get().onOptionClick(item,v);
        }
    }


    /**
     * our ViewHolder
     */
    public static class ViewHolder extends FastAdapter.ViewHolder<SongItem> {
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
        public void bindView(SongItem item, List<Object> payloads) {
            title.setText(item.songTitle);
            title.setSelected(item.shouldAnimate);
        }

        @Override
        public void unbindView(SongItem item) {
            title.setSelected(false);
        }
    }
}
