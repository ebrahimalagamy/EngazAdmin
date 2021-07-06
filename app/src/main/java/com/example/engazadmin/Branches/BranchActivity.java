package com.example.engazadmin.Branches;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.engazadmin.Companies.CompanyModel;
import com.example.engazadmin.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BranchActivity extends Activity {
    private RecyclerView recyclerViewBranch;
    private BranchAdapter adapterBranch;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = firebaseDatabase.getReference();
    private List<BranchModel> branchModelList;
    private CompanyModel companiesModel;
    private String branchId = "";
    private String companyId = "";
    private FloatingActionButton floatingActionButtonBranch;
    //    private ProgressBar progressCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branches);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
//            branchId = bundle.getString("branchId");
//            System.out.println("SDfsdf   "+ branchId);
            companyId = bundle.getString("companyId");


        recyclerViewBranch = findViewById(R.id.recycler_view_branch);
        recyclerViewBranch.setHasFixedSize(true);
        recyclerViewBranch.setLayoutManager(new LinearLayoutManager(this));
//        progressCircle = findViewById(R.id.progress_bar_branch);
        branchModelList = new ArrayList<>();

        myRef.child("All Companies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot sp : snapshot.getChildren()) {

                    if (sp.getKey().equals(companyId))
                        companiesModel = sp.getValue(CompanyModel.class);
                    System.out.println(sp.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        myRef.child("Branches").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> branches = companiesModel.getBranches();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    if (branches.contains(postSnapshot.getKey())) {

                        BranchModel upload = postSnapshot.getValue(BranchModel.class);
                        upload.setId(postSnapshot.getKey());
                        branchModelList.add(upload);

                    }
                }
                adapterBranch = new BranchAdapter(BranchActivity.this, branchModelList, branchId,companyId);
                recyclerViewBranch.setAdapter(adapterBranch);

//                progressCircle.setVisibility(View.INVISIBLE);

                floatingActionButtonBranch = findViewById(R.id.fab_add_branch);
                floatingActionButtonBranch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(BranchActivity.this, BranchAddActivity.class);
                        intent.putExtra("companies", companiesModel);
//                        if (!branchId.equals(""))
//                            intent.putExtra("branchId", branchId);
                            intent.putExtra("companyId",companyId);

                        startActivity(intent);
                        finish();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BranchActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                progressCircle.setVisibility(View.INVISIBLE);

            }
        });

    }
}