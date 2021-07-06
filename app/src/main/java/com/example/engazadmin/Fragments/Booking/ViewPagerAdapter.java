package com.example.engazadmin.Fragments.Booking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.engazadmin.Fragments.Booking.AcceptRequestFrag;
import com.example.engazadmin.Fragments.Booking.PendingRequestFrag;

import org.jetbrains.annotations.NotNull;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(@NonNull @NotNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                PendingRequestFrag pendingRequest =new PendingRequestFrag();
                return pendingRequest;
            case 1:
                AcceptRequestFrag aceeptRequest =new AcceptRequestFrag();
                return aceeptRequest;
            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
               return "Pending";
            case 1:
               return "Accepted";
            default:
                return null;

        }    }
}
