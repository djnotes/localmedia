package com.zood.mediaplayer.localmedia;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.leochuan.CarouselLayoutManager;
import com.zood.mediaplayer.R;
import com.zood.mediaplayer.utils.BaseActivity;
import com.zood.mediaplayer.utils.LocalMediaPlayerActivity_;
import com.zood.mediaplayer.utils.PublicMethods;
import com.zood.mediaplayer.utils.Util;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;


/**
 * Created by Mehdi Haghgoo on 1/17/18.
 */

@EActivity(R.layout.activity_local_media)
public class LocalMediaActivity extends BaseActivity implements LocalMediaContract.View, LocalVideoAdapter.ItemClickListener {

    private static final String TAG = LocalMediaActivity.class.getSimpleName();
    private static final int REQUEST_CODE = 100;

    /**
     * The presenter class that implements presenter contract
     */
    private LocalMediaPresenter mPresenter;
    private Cursor mCursor;

    private static boolean hasStoragePermission = false;


    @ViewById
    RecyclerView recyclerView;

    @ViewById
    TextView title; //This is the title on top of the activity

    @Click(R.id.video)
    void videoClick() {
        mPresenter.onQueryVideo();
    }

    @Click(R.id.music)
    void musicClick() {
        mPresenter.onQueryMusic();
    }

    @AfterViews
    void init() {
        //Check media
        mPresenter = new LocalMediaPresenter();
        mPresenter.attachView(this);
        mPresenter.attachContext(mContext);

        //Check for permission
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "init: READ_EXTERNAL_STORAGE permission granted");
            hasStoragePermission = true;
//            mPresenter.onQueryVideo();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
        }

        String versionName = PublicMethods.getAppVersion(this);

        //Set title
        String titleText;
        titleText = (String) getPackageManager().getApplicationLabel(getApplicationInfo());
        titleText += " " + versionName;
        title.setText(titleText);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "onRequestPermissionsResult: Permission granted");
            hasStoragePermission = true;
//            mPresenter.onQueryVideo();
        }
        else {
            hasStoragePermission = false;
        }
    }


    /**
     * Here we receive the result cursor to display in a list
     *
     * @param cursor Cursor containing media returned from the model
     */
    @Override
    public void onVideoQueryResults(Cursor cursor) {
        Log.i(TAG, "onVideoQueryResults: cursor received with size " + cursor.getCount());
        //Define an adapter
        mCursor = cursor;
        LocalVideoAdapter myAdapter = new LocalVideoAdapter(this, cursor);
        myAdapter.setClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(myAdapter);

    }

    @Override
    public void playMedia(Uri uri) {
        //Insert a fragment and play the media
        //It seems using a player activity is more suitable than using a fragment
        Intent intent = new Intent(this, LocalMediaPlayerActivity_.class);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public void onMusicQueryResults(Cursor musicCursor) {
        if(musicCursor.getCount() == 0) {
            Log.e(TAG, "onMusicQueryResults: cursor size 0");
            return;
        }
        LocalVideoAdapter musicAdapter = new LocalVideoAdapter(this, musicCursor);
        mCursor = musicCursor;
        musicAdapter.setClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(musicAdapter);

    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i(TAG, "onItemClick: You clicked item " + position);
        //Get row at position
        mPresenter.onItemClick(mCursor, position);
    }

    @Override
    public void onBackPressed() {
        //Do not return unless network is available
        if (PublicMethods.isNetworkAvailable(this)) {
            super.onBackPressed();
        }
    }


}
