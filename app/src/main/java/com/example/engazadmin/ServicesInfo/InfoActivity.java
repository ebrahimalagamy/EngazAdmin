package com.example.engazadmin.ServicesInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.engazadmin.R;
import com.example.engazadmin.Services.ServicesModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InfoActivity extends Activity {

    private RecyclerView recyclerViewInformation;
    private InfoAdapter adapterInformation;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = firebaseDatabase.getReference();
    private List<InfoModel> informationModels;
    private ServicesModel servicesModel;
    private FloatingActionButton fabInformation;
    private String companyID = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_information);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            companyID = bundle.getString("id");

        recyclerViewInformation = findViewById(R.id.recycler_view_information);
        recyclerViewInformation.setHasFixedSize(true);
        recyclerViewInformation.setLayoutManager(new LinearLayoutManager(this));
//        progressCircle = findViewById(R.id.progress_bar_branch);
        informationModels = new ArrayList<>();

        myRef.child("Services").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot sp : snapshot.getChildren()) {

                    if (sp.getKey().equals(companyID))
                        servicesModel = sp.getValue(ServicesModel.class);
                    System.out.println(sp.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        myRef.child("Information").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<String> information = servicesModel.getInformation();
                System.out.println(snapshot.getValue());

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    if (information.contains(postSnapshot.getKey())) {

                        InfoModel upload = postSnapshot.getValue(InfoModel.class);
                        upload.setId(postSnapshot.getKey());
                        informationModels.add(upload);
                    }
                }
                adapterInformation = new InfoAdapter(InfoActivity.this, informationModels, companyID);
                recyclerViewInformation.setAdapter(adapterInformation);

//                progressCircle.setVisibility(View.INVISIBLE);



                fabInformation = findViewById(R.id.fab_add_information);
                fabInformation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(InfoActivity.this, AddInfoActivity.class);
                        intent.putExtra("services", servicesModel);
                        if (!companyID.equals(""))
                            intent.putExtra("id", companyID);

                        startActivity(intent);
                        finish();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(InfoActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                progressCircle.setVisibility(View.INVISIBLE);

            }
        });


    }
}
