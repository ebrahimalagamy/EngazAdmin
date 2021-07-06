package com.example.engazadmin.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.engazadmin.Auth.AdminModel;
import com.example.engazadmin.Companies.CompanyAdapter;
import com.example.engazadmin.Companies.CompanyModel;
import com.example.engazadmin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView companiesRecyclerView;
    private CompanyAdapter companiesAdapter;
    private ProgressBar mProgressCircle;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = firebaseDatabase.getReference();
    private List<CompanyModel> companiesModelList;
    private String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private AdminModel adminModel;
    private View view;
    private String companyID = "";
    HomeFragment.BottomSheetListner sheetListner;


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_home, container, false);

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null)
            companyID = bundle.getString("companyId");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        companiesRecyclerView = view.findViewById(R.id.recycler_view);
        companiesRecyclerView.setHasFixedSize(true);

        companiesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mProgressCircle = view.findViewById(R.id.progress_circle);
        companiesModelList = new ArrayList<>();

        myRef.child("Admins").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot sp : snapshot.getChildren()) {
                    if (sp.getKey().equals(uid)) {
                        adminModel = sp.getValue(AdminModel.class);
                        System.out.println(sp);
                        sheetListner.onButtomClicked(adminModel
                                ,uid
                                );
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        myRef.child("All Companies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> companies = adminModel.getCompanies();
                System.out.println(dataSnapshot.getValue());
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    System.out.println(postSnapshot.getKey());
                    if (companies.contains(postSnapshot.getKey())) {

                        CompanyModel upload = postSnapshot.getValue(CompanyModel.class);
                        upload.setId(postSnapshot.getKey());
                        companiesModelList.add(upload);
                        System.out.println(upload);
                    }
                }
                companiesAdapter = new CompanyAdapter(getActivity(), companiesModelList);
                companiesRecyclerView.setAdapter(companiesAdapter);
                mProgressCircle.setVisibility(View.INVISIBLE);

//                fabAddCompany = view.findViewById(R.id.fab_add_company);
//                fabAddCompany.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(MainActivity.this, AddCompaniesActivity.class);
//                        intent.putExtra("admin", adminModel);
//                        startActivity(intent);
//                    }
//                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });


    }
    public interface BottomSheetListner
    {
        void onButtomClicked( AdminModel adminModelِِ, String uid);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            sheetListner = (BottomSheetListner) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement bottom sheet");
        }

    }
}
