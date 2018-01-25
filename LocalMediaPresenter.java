package com.zood.mediaplayer.localmedia;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

/**
 * Created by Mehdi Haghgoo on 1/17/18.
 */

public class LocalMediaPresenter implements LocalMediaContract.Presenter {
    private static final String TAG = LocalMediaPresenter.class.getSimpleName();
    private LocalMediaContract.View mView;
    private LocalMediaModel mModel;
    private Context mContext;
    private Cursor mCursor;

    public LocalMediaPresenter() {
        super();
    }

    @Override
    public void attachView(LocalMediaContract.View view) {
        mView = view;
    }

    @Override
    public void attachContext(Context ctx) {
        mContext = ctx;
        mModel = new LocalMediaModel(mContext);
    }

    @Override
    public void onQueryVideo() {
        mCursor = mModel.onQueryVideo();
        mView.onVideoQueryResults(mCursor);
    }

    @Override
    public void onItemClick(Cursor cursor, int position) {
        cursor.moveToPosition(position);
        int colIndex = cursor.getColumnIndex(MediaStore.Video.Media.DATA); //NOTE: This will return music data too
        String data = cursor.getString(colIndex);
        mView.playMedia(Uri.parse(data));
    }

    @Override
    public void onQueryMusic() {
        mCursor = mModel.onQueryMusic();
        Log.i(TAG, "onQueryMusic: cursor size: " + mCursor.getCount());
        mView.onMusicQueryResults(mCursor);
    }


}
