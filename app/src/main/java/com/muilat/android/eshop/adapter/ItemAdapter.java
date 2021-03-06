package com.muilat.android.eshop.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.muilat.android.eshop.DetailsActivity;
import com.muilat.android.eshop.R;
import com.muilat.android.eshop.data.Items;

public class ItemAdapter  extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private Cursor mCursor;
    private final String TAG = ItemAdapter.class.getSimpleName();

    Context mContext;

    String user_lang_pref;



    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        final  ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_item, parent, false));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = viewHolder.getAdapterPosition();
                Items item = getItem(position);


                Intent detailsIntent = new Intent(mContext, DetailsActivity.class);
                detailsIntent.putExtra(DetailsActivity.EXTRA_ITEM, item);
                mContext.startActivity(detailsIntent);
            }
        });

//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
//        user_lang_pref = sharedPreferences.getString(mContext.getString(R.string.pref_language_key),
//                mContext.getResources().getString(R.string.pref_lang_hau_yor_value));


        return viewHolder;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.N)
    public void onBindViewHolder(ItemAdapter.ViewHolder holder, int position) {

        mCursor.moveToPosition(position); // get to the right location in the cursor

        Items item = new Items(mCursor);



        holder.name.setText(item.getName()+"");
        holder.price.setText(item.getPrice()+"");
        holder.ratingBar.setProgress(item.getRating());
        String poster = "http://boombox.ng/images/fragrance/" + item.getImageUrl();

        Glide.with(mContext)
                .load(poster)
//                .placeholder(R.drawable.load)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    /**
     * Return a {@link Items} represented by this item in the adapter.
     * Method is used to run machine tests.
     *
     * @param position Cursor item position
     * @return A new {@link Items}
     */
    public Items getItem(int position) {
        if (mCursor.moveToPosition(position)) {
            return new Items(mCursor);
        }
        return null;
    }

    /**
     * @param data update cursor
     */
    public void swapCursor(Cursor data) {
        Log.e(TAG, "Swapping cursor");
        mCursor = data;
        notifyDataSetChanged();
    }

    /**
     * An Recycler item view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, price;
        ImageView imageView;
        RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_text_view);
            ratingBar = itemView.findViewById(R.id.item_ratingBar);
            price = itemView.findViewById(R.id.price_text_view);
            imageView = itemView.findViewById(R.id.item_image_view);
        }
    }
}