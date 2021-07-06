package com.example.engazadmin.ServicesInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.engazadmin.R;
import com.example.engazadmin.Services.ServicesModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;


public class AddInfoActivity extends Activity {
    private EditText editTextAddTitle1, editTextAddDescription1,
            editTextAddDescription2,editTextAddDescription3,editTextAddDescription4,
            editTextAddDescription5,editTextAddTitle2, editTextAddDescription6, editTextAddDescription7;
    private Button buttonUploadInformation, buttonBack;
    private StorageTask mUploadTask;
    private ServicesModel servicesModel;
    private String cpmID = "";
    private StorageReference storageReferenceServices;
    private DatabaseReference databaseReferenceServices;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_information);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Intent intent = getIntent();
        servicesModel = (ServicesModel) intent.getExtras().getSerializable("services");

        if (intent != null)
            cpmID = intent.getStringExtra("id");

        editTextAddTitle1 = findViewById(R.id.edit_text_add_title1);
        editTextAddDescription1 = findViewById(R.id.edit_text_add_description1);
        editTextAddDescription2 = findViewById(R.id.edit_text_add_description2);
        editTextAddDescription3 = findViewById(R.id.edit_text_add_description3);
        editTextAddDescription4 = findViewById(R.id.edit_text_add_description4);
        editTextAddDescription5 = findViewById(R.id.edit_text_add_description5);
        editTextAddTitle2 = findViewById(R.id.edit_text_add_title2);
        editTextAddDescription6 = findViewById(R.id.edit_text_add_description6);
        editTextAddDescription7 = findViewById(R.id.edit_text_add_description7);
        buttonUploadInformation = findViewById(R.id.button_upload_information);
        buttonBack = findViewById(R.id.button_show_upload_information);
        storageReferenceServices = FirebaseStorage.getInstance().getReference("Information");
        databaseReferenceServices = FirebaseDatabase.getInstance().getReference();

        buttonUploadInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // don't upload image many times when click many times
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(AddInfoActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }

            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityBranch();
            }
        });
    }
    private void uploadFile() {
        InfoModel informationModel = new InfoModel(editTextAddTitle1.getText().toString().trim(), editTextAddDescription1.getText().toString().trim(),
                editTextAddDescription2.getText().toString().trim(), editTextAddDescription3.getText().toString().trim(),
                editTextAddDescription4.getText().toString().trim(), editTextAddDescription5.getText().toString().trim(),
                editTextAddTitle2.getText().toString().trim(), editTextAddDescription6.getText().toString().trim(),editTextAddDescription7.getText().toString().trim()
        );

        String uploadId = databaseReferenceServices.child("Information").push().getKey();
        informationModel.setId(uploadId);
        servicesModel.addInformation(uploadId);

        if (!cpmID.equals(""))
            databaseReferenceServices.child("Services").child(cpmID).setValue(servicesModel);
        databaseReferenceServices.child("Information").child(uploadId).setValue(informationModel);

    }

    private void openActivityBranch() {
        Intent intent = new Intent(this, InfoActivity.class);
        if (!cpmID.equals(""))
            intent.putExtra("id", cpmID);
        startActivity(intent);
        finish();
    }
}
