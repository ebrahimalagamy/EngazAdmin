package com.example.engazadmin.Services;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.engazadmin.Branches.BranchModel;
import com.example.engazadmin.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddServicesActivity extends Activity {

    private static final int PICK_IMAGE_REQUEST = 3;
    private ImageView imageViewServices;
    private EditText editTextDescriptionServices;
    //    private EditText editTextMore;
    private Button buttonChooseImageServices;
    private Button buttonUploadDataServices;
    private Button buttonShowDataServices;
    //    private ProgressBar progressBarServices;
    private Uri imageUriServices;
    private StorageTask mUploadTask;
    private BranchModel branchModel;
    private String branchId = "";
    private String companyId = "";

    private StorageReference storageReferenceServices;
    private DatabaseReference databaseReferenceServices, historyRef;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_services);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Intent intent = getIntent();

        branchModel = (BranchModel) intent.getExtras().getSerializable("branches");

        if (intent != null)
            branchId = intent.getStringExtra("branchId");
            companyId = intent.getStringExtra("companyId");

        buttonChooseImageServices = findViewById(R.id.button_choose_image_services);
        buttonUploadDataServices = findViewById(R.id.button_upload_services);
        buttonShowDataServices = findViewById(R.id.button_show_upload_services);
        editTextDescriptionServices = findViewById(R.id.edit_text_description_services);
//        editTextMore = findViewById(R.id.edit_text_more_services);
//        progressBarServices = findViewById(R.id.progress_bar_services);
        imageViewServices = findViewById(R.id.image_view_services);
        storageReferenceServices = FirebaseStorage.getInstance().getReference("Services");
        historyRef = FirebaseDatabase.getInstance().getReference("History");
        databaseReferenceServices = FirebaseDatabase.getInstance().getReference();

        buttonChooseImageServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();

            }
        });

        buttonUploadDataServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // don't upload image many times when click many times
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(AddServicesActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }

            }
        });
        buttonShowDataServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityServices();
            }
        });

    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUriServices = data.getData();
            Picasso.with(this).load(imageUriServices).into(imageViewServices);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (imageUriServices != null) {
            StorageReference fileReference = storageReferenceServices.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUriServices));
            mUploadTask = fileReference.putFile(imageUriServices)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
//                                    progressBarServices.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(AddServicesActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful()) ;
                            Uri downloadUrl = urlTask.getResult();

                            ServicesModel servicesModel = new ServicesModel(editTextDescriptionServices.getText().toString().trim(),
                                    downloadUrl.toString());

                            String uploadId = databaseReferenceServices.child("Services").push().getKey();
                            servicesModel.setId(uploadId);
                            branchModel.addServices(uploadId);

                            if (!branchId.equals(""))
                                databaseReferenceServices.child("Branches").child(branchId).setValue(branchModel);
                            databaseReferenceServices.child("Services").child(uploadId).setValue(servicesModel);
                            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            Date date = new Date();
                            String date1 = dateFormat.format(date);
                            historyRef.child(companyId).child(branchId).child(uploadId).child(date1).setValue(0);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddServicesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener((com.google.firebase.storage.OnProgressListener<? super UploadTask.TaskSnapshot>) taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                        progressBarServices.setProgress((int) progress);
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }

    private void openActivityServices() {
        Intent intent = new Intent(this, ServicesActivity.class);
        if (!branchId.equals(""))
            intent.putExtra("branchId", branchId);  //id
        startActivity(intent);
        finish();
    }
}
