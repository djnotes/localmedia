package com.zood.mediaplayer.localmedia;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Mehdi Haghgoo on 1/17/18.
 * Here goes my MVP code for managing
 */

public interface LocalMediaContract {
    /**
     * View class to deal with activity and UI
     */
    interface View {

        void onVideoQueryResults(Cursor cursor);

        void playMedia(Uri parse);

        void onMusicQueryResults(Cursor musicCursor);
    }

    /**
     * Manages model and view interactions, provides the logic for our local media management system
     */
    interface Presenter{
        void attachView(View view);
        void attachContext(Context ctx);

        void onQueryVideo();

        void onItemClick(Cursor cursor, int position);

        void onQueryMusic();
    }
}
