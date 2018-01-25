package com.zood.mediaplayer.localmedia;

/**
 * Created by Mehdi Haghgoo on 1/17/18.
 */

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

/**
 * Class for managing (external) storage, where ICE media will be stored.
 */
public class LocalMediaModel{
    private Context mContext;
    private static final String TAG = LocalMediaModel.class.getSimpleName();

    public LocalMediaModel(Context ctx) {
        super();
        mContext = ctx;
    }


    public Cursor onQueryVideo() {
        String [] proj = { MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.TITLE
        };

        ContentResolver cr = mContext.getContentResolver();
        Cursor vidCursor = MediaStore.Video.query(cr, MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                proj);
        Log.i(TAG, "onQueryVideo: cursor count = " + vidCursor.getCount());
        return vidCursor;
    }

    public Cursor onQueryMusic() {
         String [] projection = { MediaStore.Audio.Media._ID,
         MediaStore.Audio.Media.DATA,
         MediaStore.Audio.Media.DISPLAY_NAME,
         MediaStore.Audio.Media.SIZE,
                 MediaStore.Audio.Media.ARTIST,
                 MediaStore.Audio.Media.TITLE
         };

         ContentResolver cr = mContext.getContentResolver();
         return cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null,
                 null, MediaStore.Audio.Media._ID);
    }
}
