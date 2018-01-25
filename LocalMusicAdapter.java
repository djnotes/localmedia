package com.zood.mediaplayer.localmedia;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zood.mediaplayer.R;


/**
 * Created by Mehdi Haghgoo on 1/21/18.
 */

public class LocalMusicAdapter extends RecyclerView.Adapter<LocalMusicAdapter.ViewHolder> {
    private Context mContext;
    private Cursor mDataset;
    private ItemClickListener mClickListener;
    private static final String TAG = LocalMusicAdapter.class.getSimpleName();

    public LocalMusicAdapter(Context context, Cursor cursor) {
        super();
        mContext = context;
        mDataset = cursor;

        //Move to first
        mDataset.moveToFirst();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mediaTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            mediaTitle = itemView.findViewById(R.id.mediaTitle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());

        }
    }


    @Override
    public LocalMusicAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup itemView = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.local_music_item, null, false);;
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LocalMusicAdapter.ViewHolder holder, int position) {
        mDataset.moveToPosition(position);
        int col = mDataset.getColumnIndex(MediaStore.Audio.Media.TITLE);
        if(col == -1) col = mDataset.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
        //Get the URI to send to media player
        int col2 = mDataset.getColumnIndex(MediaStore.Audio.Media.DATA);
        Log.i(TAG, "onBindViewHolder: DATA: " + mDataset.getString(col2) );
        String titleText = mDataset.getString(col);
        holder.mediaTitle.setText(titleText);
    }

    @Override
    public int getItemCount() {
        return mDataset.getCount();
    }

    void setClickListener(ItemClickListener itemClickListener) {
        mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
