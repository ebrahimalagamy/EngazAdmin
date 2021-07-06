package com.example.engazadmin.Branches;

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
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.engazadmin.Companies.CompanyModel;
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

public class BranchAddActivity extends Activity {
    private static final int PICK_IMAGE_REQUEST = 2;

    private Button buttonChooseImageBranch;
    private Button buttonUploadDataBranch;
    private Button buttonShowDataBranch;
    private ImageView imageViewBranch;
    private EditText editTextNameBranch;
    private EditText editTextTimeWorkBranch;
    private EditText editTextDaysWorkBranch;
    private EditText editTextStatusBranch;
    private ProgressBar progressBarBranch;
    private Uri imageUriBranch;
    private StorageTask mUploadTask;
    private CompanyModel companiesModel;
    private String companyId = "";
    private StorageReference storageReferenceBranch;
    private DatabaseReference databaseReferenceBranch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_branch);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        Intent intent = getIntent();
        companiesModel = (CompanyModel) intent.getExtras().getSerializable("companies");

        if (intent != null)
            companyId = intent.getStringExtra("companyId");


        buttonChooseImageBranch = findViewById(R.id.button_choose_image_branch);
        buttonUploadDataBranch = findViewById(R.id.button_upload_branch);
        buttonShowDataBranch = findViewById(R.id.button_show_upload_branch);
        editTextNameBranch = findViewById(R.id.edit_text_name_branch);
        editTextTimeWorkBranch = findViewById(R.id.edit_text_time_work);
        editTextDaysWorkBranch = findViewById(R.id.edit_text_days_work);
        editTextStatusBranch = findViewById(R.id.edit_text_status);
        progressBarBranch = findViewById(R.id.progress_bar_branch);
        imageViewBranch = findViewById(R.id.image_view_branch);
        storageReferenceBranch = FirebaseStorage.getInstance().getReference("Branches");
        databaseReferenceBranch = FirebaseDatabase.getInstance().getReference();

        buttonChooseImageBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();

            }
        });
        buttonUploadDataBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // don't upload image many times when click many times
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(BranchAddActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }

            }
        });
        buttonShowDataBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityBranch();
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
            imageUriBranch = data.getData();
            Picasso.with(this).load(imageUriBranch).into(imageViewBranch);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (imageUriBranch != null) {
            StorageReference fileReference = storageReferenceBranch.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUriBranch));

            mUploadTask = fileReference.putFile(imageUriBranch)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBarBranch.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(BranchAddActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful()) ;
                            Uri downloadUrl = urlTask.getResult();

                            BranchModel branchModel = new BranchModel(editTextNameBranch.getText().toString().trim(),
                                    editTextTimeWorkBranch.getText().toString().trim(), editTextDaysWorkBranch.getText().toString().trim(),
                                    editTextStatusBranch.getText().toString().trim(),
                                    downloadUrl.toString());

                            String uploadId = databaseReferenceBranch.child("Branches").push().getKey();
                            branchModel.setId(uploadId);
                            companiesModel.addBranch(uploadId);

                            if (!companyId.equals(""))
                                databaseReferenceBranch.child("All Companies").child(companyId).setValue(companiesModel);
                            databaseReferenceBranch.child("Branches").child(uploadId).setValue(branchModel);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(BranchAddActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener((com.google.firebase.storage.OnProgressListener<? super UploadTask.TaskSnapshot>) taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressBarBranch.setProgress((int) progress);
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void openActivityBranch() {
        Intent intent = new Intent(this, BranchActivity.class);
        if (!companyId.equals(""))
            intent.putExtra("companyId", companyId);
        startActivity(intent);
        finish();
    }


}

