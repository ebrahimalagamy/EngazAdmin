package com.example.engazadmin;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.engazadmin.Auth.AdminModel;
import com.example.engazadmin.Companies.CompanyAddDialog;
import com.example.engazadmin.Fragments.HomeFragment;
import com.example.engazadmin.Fragments.ProfileFragment;
import com.example.engazadmin.Fragments.BookingFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements HomeFragment.BottomSheetListner {

    private FloatingActionButton fabAddCompany;
    private ImageButton imageButtonReservation, imageButtonProfile;
    private int placeCompany;
    private int placeReservation;
    private int placeProfile;
    private AdminModel adminModel;
    private String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            uid = bundle.getString("id");

        Initialize();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frag_container, new HomeFragment()).addToBackStack(null).commit();

        fabAddCompany.setImageResource(R.drawable.ic_baseline_add_24);
        placeCompany = 0;
        placeReservation = 0;
        placeProfile = 0;

        imageButtonReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (placeReservation == 0) {
                    placeReservation = 1;
                    YoYo.with(Techniques.FadeOut)
                            .duration(400)
                            .onEnd(new YoYo.AnimatorCallback() {
                                @Override
                                public void call(Animator animator) {
                                    imageButtonReservation.setImageResource(R.drawable.ic_booking);
                                    YoYo.with(Techniques.BounceInUp)
                                            .duration(500).playOn(imageButtonReservation);
                                }
                            }).playOn(imageButtonReservation);
                    for (Fragment f : getSupportFragmentManager().getFragments()) {
                        getSupportFragmentManager().beginTransaction().remove(f).commit();
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frag_container, new BookingFragment(), "booking").addToBackStack(null).commit();

                }
                if (placeCompany == 0) {
                    placeCompany = 1;
                    YoYo.with(Techniques.RotateOut)
                            .duration(500)
                            .onEnd(new YoYo.AnimatorCallback() {
                                @Override
                                public void call(Animator animator) {
                                    fabAddCompany.setImageResource(R.drawable.ic_home);
                                    YoYo.with(Techniques.RotateIn)
                                            .duration(500).playOn(fabAddCompany);
                                }
                            })
                            .playOn(fabAddCompany);
                }

                if (placeProfile == 1) {
                    placeProfile = 0;
                    YoYo.with(Techniques.FadeOut)
                            .duration(400)
                            .onEnd(new YoYo.AnimatorCallback() {
                                @Override
                                public void call(Animator animator) {
                                    imageButtonProfile.setImageResource(R.drawable.ic_line_chart_black);
                                    YoYo.with(Techniques.BounceInUp)
                                            .duration(500).playOn(imageButtonProfile);
                                }
                            }).playOn(imageButtonProfile);
                }

            }
        });

        fabAddCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (placeCompany == 1) {
                    placeCompany = 0;
                    YoYo.with(Techniques.RotateOut)
                            .duration(700)
                            .onEnd(new YoYo.AnimatorCallback() {
                                @Override
                                public void call(Animator animator) {
                                    fabAddCompany.setImageResource(R.drawable.ic_baseline_add_24);
                                    YoYo.with(Techniques.RotateIn)
                                            .duration(700).playOn(fabAddCompany);
                                }
                            })
                            .playOn(fabAddCompany);
                    for (Fragment f : getSupportFragmentManager().getFragments()) {
                        getSupportFragmentManager().beginTransaction().remove(f).commit();
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frag_container, new HomeFragment(), "word").addToBackStack(null).commit();

                } else if (placeCompany == 0) {
                    openDialog();
                }

                if (placeReservation == 1) {
                    placeReservation = 0;
                    YoYo.with(Techniques.FadeOut)
                            .duration(400)
                            .onEnd(new YoYo.AnimatorCallback() {
                                @Override
                                public void call(Animator animator) {
                                    imageButtonReservation.setImageResource(R.drawable.ic_booking_black);
                                    YoYo.with(Techniques.BounceInUp)
                                            .duration(500).playOn(imageButtonReservation);
                                }
                            }).playOn(imageButtonReservation);
                }
                if (placeProfile == 1) {
                    placeProfile = 0;
                    YoYo.with(Techniques.FadeOut)
                            .duration(400)
                            .onEnd(new YoYo.AnimatorCallback() {
                                @Override
                                public void call(Animator animator) {
                                    imageButtonProfile.setImageResource(R.drawable.ic_line_chart_black);
                                    YoYo.with(Techniques.BounceInUp)
                                            .duration(500).playOn(imageButtonProfile);
                                }
                            }).playOn(imageButtonProfile);

                }

            }
        });

        imageButtonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (placeProfile == 0) {
                    placeProfile = 1;
                    YoYo.with(Techniques.FadeOut)
                            .duration(400)
                            .onEnd(new YoYo.AnimatorCallback() {
                                @Override
                                public void call(Animator animator) {
                                    imageButtonProfile.setImageResource(R.drawable.ic_line_chart_);
                                    YoYo.with(Techniques.BounceInUp)
                                            .duration(500).playOn(imageButtonProfile);
                                }
                            }).playOn(imageButtonProfile);
                    for (Fragment f : getSupportFragmentManager().getFragments()) {
                        getSupportFragmentManager().beginTransaction().remove(f).commit();
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frag_container, new ProfileFragment(), "profile").addToBackStack(null).commit();
                    //getSupportFragmentManager().beginTransaction().detach(new words_frag()).detach(new profile_frag()).commit();
                }

                if (placeCompany == 0) {
                    placeCompany = 1;
                    YoYo.with(Techniques.RotateOut)
                            .duration(500)
                            .onEnd(new YoYo.AnimatorCallback() {
                                @Override
                                public void call(Animator animator) {
                                    fabAddCompany.setImageResource(R.drawable.ic_home);
                                    YoYo.with(Techniques.RotateIn)
                                            .duration(500).playOn(fabAddCompany);
                                }
                            })
                            .playOn(fabAddCompany);

                }
                if (placeReservation == 1) {
                    placeReservation = 0;
                    YoYo.with(Techniques.FadeOut)
                            .duration(500)
                            .onEnd(new YoYo.AnimatorCallback() {
                                @Override
                                public void call(Animator animator) {
                                    imageButtonReservation.setImageResource(R.drawable.ic_booking_black);
                                    YoYo.with(Techniques.BounceIn)
                                            .duration(500).playOn(imageButtonReservation);
                                }
                            })
                            .playOn(imageButtonReservation);

                }


            }
        });

    }

    private void openDialog() {
        CompanyAddDialog fragAddCompanies = new CompanyAddDialog();
        fragAddCompanies.show(getSupportFragmentManager(), "dialog");
    }

    private void Initialize() {
        fabAddCompany = findViewById(R.id.fab_add_company);
        imageButtonProfile = findViewById(R.id.image_button_profile);
        imageButtonReservation = findViewById(R.id.image_button_reservation);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onButtomClicked(AdminModel adminModelِِ, String uid) {
        this.adminModel = adminModelِِ;
        this.uid = uid;
        getIntent().putExtra("adminModel", adminModelِِ);
        getIntent().putExtra("uid", uid);
    }

}