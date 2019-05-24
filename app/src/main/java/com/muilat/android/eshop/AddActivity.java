package com.muilat.android.eshop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
//import com.google.android.gms.tasks.Continuation;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
import com.muilat.android.eshop.data.Items;

public class AddActivity extends AppCompatActivity {

    private static final int RC_IMAGE_PICKER = 11;
    EditText nameEditText, descriptionEditText, priceEditText;
    RatingBar ratingBar;
    ImageView imagePicker;

    Uri imageUrl;

//    private FirebaseStorage mFirebaseStorage;
//    private StorageReference mStorageReference;
//    private FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();
//    StorageReference photoRef;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        nameEditText =findViewById(R.id.name_editText);
        descriptionEditText =findViewById(R.id.description_editText);
        priceEditText =findViewById(R.id.price_editText);
        ratingBar =findViewById(R.id.ratingBar);
        imagePicker = findViewById(R.id.imagePicker);

        Button saveButton = findViewById(R.id.saveButton);

        imagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_IMAGE_PICKER);

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

//                if (imageUrl != null){
//                    mFirebaseStorage = FirebaseStorage.getInstance();
//                    mStorageReference = mFirebaseStorage.getReference().child("item_images");
//
//                    photoRef = mStorageReference.child(imageUrl.getLastPathSegment());
//                    //upload file to firebase storage
//                    photoRef.putFile(imageUrl).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                        @Override
//                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                            if (!task.isSuccessful()) {
//                                Toast.makeText(AddActivity.this, "Something went wrong. Please try again!", Toast.LENGTH_LONG).show();
//
//                                throw task.getException();
//                            }
//
//                            //continue with the task to get the download url
//                            return photoRef.getDownloadUrl();
//                        }
//                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Uri> task) {
//                            if (task.isSuccessful()) {
//                                Uri image_url = task.getResult();
//
//                                String name = nameEditText.getText().toString();
//                                String description = descriptionEditText.getText().toString();
//                                double price = Double.parseDouble(priceEditText.getText().toString());
//                                int rating = ratingBar.getProgress();
//                                final Items item = new Items(name, description, image_url.toString(), price, rating);
////                                mCategory.setCatImageUrl(imageUrl.toString());
//
//                                firestoreDb.collection("items").add(item).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<DocumentReference> task) {
//                                        Toast.makeText(AddActivity.this,  item.getName()+" added to item", Toast.LENGTH_LONG).show();
//
//                                    }
//                                });
//
//
//
//                                finish();
//                            } else {
//                                Toast.makeText(AddActivity.this, "Something went wrong. Please try again!", Toast.LENGTH_LONG).show();
//
//                            }
//                        }
//                    });
//
//                }

            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_IMAGE_PICKER){
            if(resultCode == RESULT_OK){
                imageUrl = data.getData();

                Glide.with(AddActivity.this)
                        .load(imageUrl.toString())
                        .into(imagePicker);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
