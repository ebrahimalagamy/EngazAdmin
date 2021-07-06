package com.example.engazadmin.Fragments.Booking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.engazadmin.Fragments.ServiceRequestModel;
import com.example.engazadmin.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class AcceptRequestFrag extends Fragment {

    private View acceptView;
    private RecyclerView recyclerAccept;
    private DatabaseReference pendingAcceptedRef, userRef, serviceRef;
    private FirebaseAuth auth;
    private String currentUserID, ServiceNameSuckeer;

    public AcceptRequestFrag() {
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        acceptView = inflater.inflate(R.layout.fragment_aceept_request, container, false);
        recyclerAccept = acceptView.findViewById(R.id.recycler_pending_accept);
        recyclerAccept.setLayoutManager(new LinearLayoutManager(getContext()));
        auth = FirebaseAuth.getInstance();
        currentUserID = auth.getCurrentUser().getUid();
        pendingAcceptedRef = FirebaseDatabase.getInstance().getReference().child("PendingAccepted").child(currentUserID);
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        serviceRef = FirebaseDatabase.getInstance().getReference().child("Services");

        return acceptView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<ServiceRequestModel>()
                .setQuery(pendingAcceptedRef, ServiceRequestModel.class)
                .build();
        FirebaseRecyclerAdapter<ServiceRequestModel, pendingAcceptedViewHolder> adapter =
                new FirebaseRecyclerAdapter<ServiceRequestModel, pendingAcceptedViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull @NotNull pendingAcceptedViewHolder holder, int i, @NonNull @NotNull ServiceRequestModel requestModel) {

                        String requestsIDs = getRef(i).getKey();
                        DatabaseReference serviceId = getRef(i).child("serviceId").getRef();
                        DatabaseReference getType = getRef(i).child("status").getRef();
                        getType.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                if (snapshot.exists()) {
                                    String type = snapshot.getValue().toString();
                                    if (type.equals("ACCEPTED")) {

                                        pendingAcceptedRef.child(requestsIDs).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                String time_pending = snapshot.child("time").getValue().toString();
                                                String window_number = snapshot.child("windowNO").getValue().toString();

                                                holder.timePending.setText(time_pending);
                                                holder.windowNumber.setText(window_number);

                                            }

                                            @Override
                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                            }
                                        });


                                        serviceId.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    String service_id = snapshot.getValue().toString();
                                                    serviceRef.child(service_id).child("servicesDescription").addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                                                            ServiceNameSuckeer = snapshot.getValue().toString();
                                                            holder.servicesName.setText(ServiceNameSuckeer);
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

                                            }
                                        });
                                        userRef.child(requestsIDs).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                                String requestUserName = snapshot.child("fullName").getValue().toString();
                                                String requestUserSSN = snapshot.child("ssn").getValue().toString();
                                                String requestUserPhoneNumber = snapshot.child("phone").getValue().toString();

                                                holder.userName.setText(requestUserName);
                                                holder.userSSN.setText(requestUserSSN);
                                                holder.userPhoneNumber.setText(requestUserPhoneNumber);

                                            }

                                            @Override
                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                            }
                                        });

                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });


                    }

                    @NonNull
                    @NotNull
                    @Override
                    public pendingAcceptedViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.accept_item, parent, false);
                        pendingAcceptedViewHolder viewHolder = new pendingAcceptedViewHolder(view);
                        return viewHolder;
                    }
                };
        recyclerAccept.setAdapter(adapter);
        adapter.startListening();
    }

    public static class pendingAcceptedViewHolder extends RecyclerView.ViewHolder {
        private TextView userName, userSSN, userPhoneNumber, servicesName, timePending, windowNumber;


        public pendingAcceptedViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name);
            userSSN = itemView.findViewById(R.id.user_ssn);
            userPhoneNumber = itemView.findViewById(R.id.user_phone_number);
            servicesName = itemView.findViewById(R.id.service_name);
            timePending = itemView.findViewById(R.id.time_pending);
            windowNumber = itemView.findViewById(R.id.window_number);

        }
    }
}