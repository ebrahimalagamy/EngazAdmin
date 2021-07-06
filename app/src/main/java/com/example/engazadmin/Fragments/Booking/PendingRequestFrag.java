package com.example.engazadmin.Fragments.Booking;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.engazadmin.Fragments.ServiceRequestModel;
import com.example.engazadmin.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PendingRequestFrag extends Fragment {

    private View requestFragmentView;
    private RecyclerView myRequestsList;
    private DatabaseReference pendingAppointmentRef, usersRef, acceptedRef, serviceRef, historyRef;
    private FirebaseAuth firebaseAuth;
    private String currentUserId, ServiceName;
    private int hour, minute;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    Date date = new Date();
    String date1 = dateFormat.format(date);
    String tima="";

    public PendingRequestFrag() {
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        requestFragmentView = inflater.inflate(R.layout.fragment_pending_request, container, false);

        pendingAppointmentRef = FirebaseDatabase.getInstance().getReference().child("PendingAppointments");
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserId = firebaseAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        acceptedRef = FirebaseDatabase.getInstance().getReference().child("PendingAccepted");
        serviceRef = FirebaseDatabase.getInstance().getReference().child("Services");
        historyRef = FirebaseDatabase.getInstance().getReference().child("History");
        myRequestsList = requestFragmentView.findViewById(R.id.recycler_request_user);
        myRequestsList.setLayoutManager(new LinearLayoutManager(getContext()));

        return requestFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull @org.jetbrains.annotations.NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseRecyclerOptions<ServiceRequestModel> options =
                new FirebaseRecyclerOptions.Builder<ServiceRequestModel>()
                        .setQuery(pendingAppointmentRef.child(currentUserId), ServiceRequestModel.class)
                        .build();
        FirebaseRecyclerAdapter<ServiceRequestModel, RequestsViewHolder> adapter =
                new FirebaseRecyclerAdapter<ServiceRequestModel, RequestsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull @NotNull RequestsViewHolder holder, int i, @NonNull @NotNull ServiceRequestModel requestModel) {
                        holder.itemView.findViewById(R.id.text_pending).setVisibility(View.VISIBLE);
                        String userId = getRef(i).getKey();
                        DatabaseReference serviceId = getRef(i).child("serviceId").getRef();
                        DatabaseReference getType = getRef(i).child("status").getRef();

                        getType.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    String type = snapshot.getValue().toString();
                                    if (type.equals("PENDING")) {

                                        serviceId.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    String service_id = snapshot.getValue().toString();

                                                    serviceRef.child(service_id).child("servicesDescription").addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                                                            ServiceName = snapshot.getValue().toString();
                                                            holder.servicesName.setText(ServiceName);
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

                                        usersRef.child(userId).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                final String requestUserName = snapshot.child("fullName").getValue().toString();
                                                final String requestUserSSN = snapshot.child("ssn").getValue().toString();
                                                final String requestUserPhoneNumber = snapshot.child("phone").getValue().toString();

                                                holder.userName.setText(requestUserName);
                                                holder.userSSN.setText(requestUserSSN);
                                                holder.userPhoneNumber.setText(requestUserPhoneNumber);
                                                holder.cardView.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                        LayoutInflater inflater = getActivity().getLayoutInflater();
                                                        View view1 = inflater.inflate(R.layout.accept_dialog, null);
                                                        EditText editTextNOWindow = view1.findViewById(R.id.edit_text_window_number);
                                                        String windowNO = editTextNOWindow.getText().toString();

                                                        TextView textViewShowTime = view1.findViewById(R.id.text_view_show_time);
                                                        Button buttonAddTime = view1.findViewById(R.id.button_add_time);
                                                        buttonAddTime.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                                                                    @Override
                                                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                                        hour = hourOfDay;
                                                                        PendingRequestFrag.this.minute = minute;
                                                                        Calendar calendar = Calendar.getInstance();
                                                                        calendar.set(0, 0, 0, hour, PendingRequestFrag.this.minute);
                                                                        textViewShowTime.setText(DateFormat.format("hh:mm aa", calendar));
                                                                        System.out.println("textViewShowTime" + textViewShowTime.getText());
                                                                        tima=textViewShowTime.getText().toString();

                                                                    }
                                                                }, 12, 0, false
                                                                );
                                                                timePickerDialog.updateTime(hour, minute);
                                                                timePickerDialog.show();

                                                            }
                                                        });
                                                        builder.setView(view1)
                                                                .setTitle("User request")
                                                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        pendingAppointmentRef.child(currentUserId).child(userId)
                                                                                .removeValue()
                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<Void> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            pendingAppointmentRef.child(userId).child(currentUserId)
                                                                                                    .removeValue()
                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<Void> task) {
                                                                                                            if (task.isSuccessful()) {
                                                                                                                Toast.makeText(getContext(), "Request Canceled", Toast.LENGTH_SHORT).show();
                                                                                                            }
                                                                                                        }
                                                                                                    });
                                                                                        }
                                                                                    }
                                                                                });
                                                                    }
                                                                })
                                                                .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        if (!tima.equals(""))
                                                                        {  acceptedRef.child(currentUserId).child(userId).child("time").setValue(tima);

                                                                            acceptedRef.child(currentUserId).child(userId).child("windowNO").setValue(editTextNOWindow.getText().toString());
                                                                        acceptedRef.child(currentUserId).child(userId).child("status")
                                                                                .setValue("ACCEPTED").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {

                                                                                    pendingAppointmentRef.child(currentUserId).child(userId).addValueEventListener(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {

                                                                                            String serviceId = snapshot.child("serviceId").getValue().toString();
                                                                                            String appointmentId = snapshot.child("appointmentId").getValue().toString();
                                                                                            String userId = snapshot.child("userId").getValue().toString();
                                                                                            String companyID = snapshot.child("companyID").getValue().toString();
                                                                                            String branchID = snapshot.child("branchID").getValue().toString();

                                                                                            historyRef.child(companyID).child(branchID).child(serviceId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                                                                                                    if (snapshot.exists()) {
                                                                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                                            if (dataSnapshot.getKey() == date1) {
                                                                                                                int value = dataSnapshot.getValue(Integer.class);
                                                                                                                value += 1;
                                                                                                                historyRef.child(companyID).child(branchID).child(serviceId).child(date1).setValue(value);
                                                                                                            } else {
                                                                                                                historyRef.child(companyID).child(branchID).child(serviceId).child(date1).setValue(1);

                                                                                                            }

                                                                                                        }
                                                                                                    } else {
                                                                                                        historyRef.child(companyID).child(branchID).child(serviceId).child(date1).setValue(1);

                                                                                                    }

                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

                                                                                                }
                                                                                            });

                                                                                            acceptedRef.child(currentUserId).child(userId).child("serviceId").setValue(serviceId);
                                                                                            acceptedRef.child(currentUserId).child(userId).child("appointmentId").setValue(appointmentId);
                                                                                            acceptedRef.child(currentUserId).child(userId).child("userId").setValue(userId);
                                                                                            acceptedRef.child(currentUserId).child(userId).child("companyID").setValue(companyID);
                                                                                            acceptedRef.child(currentUserId).child(userId).child("branchID").setValue(branchID);

;


                                                                                        }

                                                                                        @Override
                                                                                        public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

                                                                                        }
                                                                                    });

//                                                                                acceptedRef.child(currentUserId).child(userId).child("serviceId").setValue();

                                                                                pendingAppointmentRef.child(currentUserId).child(userId)
                                                                                        .removeValue()
                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<Void> task) {
                                                                                                Toast.makeText(getContext(), "Request Accepted", Toast.LENGTH_SHORT).show();


                                                                                            }
                                                                                        });
                                                                                }
                                                                            }
                                                                        });

                                                                    }else
                                                                        {
                                                                            Toast.makeText(getActivity(), "time not entered", Toast.LENGTH_SHORT).show();

                                                                        }
                                                                    }
                                                                });





                                                        builder.show();
                                                    }

                                                });
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
                    public RequestsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item, parent, false);
                        RequestsViewHolder holder = new RequestsViewHolder(view);
                        return holder;
                    }
                };
        myRequestsList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class RequestsViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userSSN, userPhoneNumber, servicesName;
        CardView cardView;

        public RequestsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name);
            userSSN = itemView.findViewById(R.id.user_ssn);
            userPhoneNumber = itemView.findViewById(R.id.user_phone_number);
            servicesName = itemView.findViewById(R.id.service_name);
            cardView = itemView.findViewById(R.id.card_view_branch);

        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("resumed");
    }
}