package com.muilat.android.eshop;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.muilat.android.eshop.data.CartContract;
import com.muilat.android.eshop.data.CartContract.CartEntry;
import com.muilat.android.eshop.data.Items;

public class DetailsActivity extends AppCompatActivity {
    public static final String EXTRA_ITEM = "extra-item";

    Items mItem;

    ImageView imageView ;
    TextView name, description, price;
    RatingBar ratingBar;
    EditText quantity_editText;

    int quantity = 0;

    MaterialFavoriteButton favoriteButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TextView title = findViewById(R.id.toolbar_title);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        favoriteButton= new MaterialFavoriteButton.Builder(this)
                .favorite(true)
                .color(MaterialFavoriteButton.STYLE_WHITE)
                .type(MaterialFavoriteButton.STYLE_HEART)
                .rotationDuration(400)
                .create();
        toolbar.addView(favoriteButton);

        favoriteButton.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
            @Override
            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                Toast.makeText(DetailsActivity.this, "fav change", Toast.LENGTH_SHORT).show();
            }
        });
        imageView = findViewById(R.id.item_image_view);
        name = findViewById(R.id.name_text_view);
        price = findViewById(R.id.price_text_view);
        description = findViewById(R.id.description_text_view);
        ratingBar = findViewById(R.id.item_ratingBar);
        quantity_editText = findViewById(R.id.quantity_editText);

        quantity = Integer.parseInt(quantity_editText.getText().toString());

        if(getIntent() != null){
            mItem = getIntent().getParcelableExtra(EXTRA_ITEM);
            title.setText(mItem.getName()+"");
            name.setText(mItem.getName()+"");
            price.setText(mItem.getPrice()+"");
            ratingBar.setProgress(mItem.getRating());
            String poster = "http://boombox.ng/images/fragrance/" + mItem.getImageUrl();


//            Glide.with(this)
//                    .load(poster)
//                    .into(imageView)
        }



    }

    public void decrement(View view) {
        if(quantity > 1)
            quantity = quantity-1;
        quantity_editText.setText(quantity+"");
    }

    public void increment(View view) {
        quantity = quantity+1;
        quantity_editText.setText(quantity+"");
    }

    public void addToCart(View view) {

        ContentValues contentValue  = new ContentValues();
        contentValue.put(CartEntry.COLUMN_PRICE, mItem.getPrice()*quantity);
        contentValue.put(CartEntry.COLUMN_ITEM_ID, mItem.getId());
        contentValue.put(CartEntry.COLUMN_NAME, mItem.getName());
        contentValue.put(CartEntry.COLUMN_IMAGE_URL, mItem.getImageUrl());
        contentValue.put(CartEntry.COLUMN_QUANTITY, quantity);

        Uri id = getContentResolver().insert(CartContract.CONTENT_URI, contentValue);
        if(id !=null){
            Toast.makeText(DetailsActivity.this, mItem.getName()+" added to cart", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.ic_search:
//                Intent intent = new Intent(this, CartActivity.class);
//                startActivity(intent);
                return true;
            //TODO
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
