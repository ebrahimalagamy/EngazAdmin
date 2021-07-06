package com.example.engazadmin.Services;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.engazadmin.Branches.BranchModel;
import com.example.engazadmin.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ServicesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewServices;
    private ServicesAdapter adapterServices;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = firebaseDatabase.getReference();
    private List<ServicesModel> servicesModels;
    private BranchModel branchModel;
    private FloatingActionButton FABServices;
    private String branchId = "";
    private String companyId = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            branchId = bundle.getString("branchId");
            companyId = bundle.getString("companyId");

        System.out.println("companyyyy id" + companyId);
        System.out.println("btancccch id" + branchId);


        recyclerViewServices = findViewById(R.id.recycler_view_services);
        recyclerViewServices.setHasFixedSize(true);
        recyclerViewServices.setLayoutManager(new GridLayoutManager(this, 2));
//        progressCircle = findViewById(R.id.progress_bar_branch);
        servicesModels = new ArrayList<>();

        myRef.child("Branches").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot sp : snapshot.getChildren()) {

                    if (sp.getKey().equals(branchId))
                        branchModel = sp.getValue(BranchModel.class);
                    System.out.println(sp.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myRef.child("Services").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> services = branchModel.getServices();
                System.out.println(snapshot.getValue());

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    if (services.contains(postSnapshot.getKey())) {

                        ServicesModel upload = postSnapshot.getValue(ServicesModel.class);
                        upload.setId(postSnapshot.getKey());
                        servicesModels.add(upload);
                    }
                }
                adapterServices = new ServicesAdapter(ServicesActivity.this, servicesModels, branchId,companyId);
                recyclerViewServices.setAdapter(adapterServices);

//                progressCircle.setVisibility(View.INVISIBLE);

                FABServices = findViewById(R.id.fab_add_services);
                FABServices.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        openDialog();
                        Intent intent = new Intent(ServicesActivity.this, AddServicesActivity.class);
                        intent.putExtra("branches", branchModel);
                        if (!branchId.equals(""))
                            intent.putExtra("branchId", branchId);
                            intent.putExtra("companyId", companyId);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ServicesActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                progressCircle.setVisibility(View.INVISIBLE);

            }
        });
    }

//    private void openDialog() {
//        Dialog dialog = new Dialog();
//        dialog.show(getSupportFragmentManager(),"dialog");
//
//    }

}

