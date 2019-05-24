package com.muilat.android.eshop;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.muilat.android.eshop.adapter.CartAdapter;
import com.muilat.android.eshop.data.CartContract;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.text.NumberFormat;

import static com.muilat.android.eshop.data.CartContract.CONTENT_URI;

public class CartActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = "CartActivity";
    private static final int CART_LOADER = 111;

    CartAdapter mCartAdapter;
    Cursor mCursor;

    Double totalPrice;

    private static PayPalConfiguration config = new PayPalConfiguration()

            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)

            .clientId("AejPzHTA5RM1P6pWbQFqJIoPswfJto150Xbsj_vmUyS1xEHETOYtokUzhZN-9adwFMu57qjvqKyueM7r");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        RecyclerView recycler =  findViewById(R.id.cart_recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);

        mCartAdapter = new CartAdapter();
        recycler.setAdapter(mCartAdapter);
        recycler.setHasFixedSize(true);


        getSupportLoaderManager().initLoader(CART_LOADER, null, this);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete

                // COMPLETED (1) Construct the URI for the item to delete
                //[Hint] Use getTag (from the adapter code) to get the id of the swiped item
                // Retrieve the id of the task to delete
                int id = (int) viewHolder.itemView.getTag();

                // Build appropriate uri with String row id appended
                String stringId = Integer.toString(id);
                Uri uri = CartContract.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();

                //Delete a single row of data using a ContentResolver
                getContentResolver().delete(uri, null, null);
                getSupportLoaderManager().restartLoader(CART_LOADER, null, CartActivity.this);

                calculateTotal(mCursor);


            }
        }).attachToRecyclerView(recycler);



        //Paypal Intent
        Intent intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startService(intent);

    }

    public double calculateTotal(Cursor cursor){
        totalPrice = 0.00;
        for (int i = 0; i<cursor.getCount(); i++)
        {
            int price = cursor.getColumnIndex(CartContract.CartEntry.COLUMN_PRICE);
//            int quantity = cursor.getColumnIndex(CartContract.CartEntry.COLUMN_QUANTITY);

            cursor.moveToPosition(i);
            Double itemPrice = cursor.getDouble(price);
//            int itemQuantity = cursor.getInt(quantity);
            totalPrice += itemPrice ;

        }

        TextView totalCost =  findViewById(R.id.total_textView);
        String convertPrice = NumberFormat.getCurrencyInstance().format(totalPrice);
        totalCost.setText(convertPrice);
        return totalPrice;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the cart data
            Cursor mCartData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mCartData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mCartData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {

                try {
                    mCursor = getContentResolver().query(CONTENT_URI,null,null,null,null);

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage()+" Failed to asynchronously load Categorys.");
                    e.printStackTrace();
                    return null;
                }

                return mCursor;
            }

            public void deliverResult(Cursor data) {
                mCartData = data;
                super.deliverResult(data);
            }
        };
    }



    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursor = null;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursor = cursor;
        mCartAdapter.swapCursor(cursor);
        calculateTotal(cursor);
    }

    @Override
    public void onResume(){
        super.onResume();
        getSupportLoaderManager().restartLoader(CART_LOADER, null, this);
    }

    public void arrowBack(View v){
        finish();
    }

    public void paymentClick(View pressed) {

        // PAYMENT_INTENT_SALE will cause the payment to complete immediately.
        // Change PAYMENT_INTENT_SALE to
        //   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
        //   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
        //     later via calls from your server.

        PayPalPayment payment = new PayPalPayment(new BigDecimal(totalPrice), "USD", "Being payment for items ordered" ,
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));

                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }


}
