package com.mahad.a3;



import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TabAdapter extends FragmentStateAdapter {

    public TabAdapter(@NonNull MainActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new NewProductFragment();  // New product fragment
            case 1:
                return new ScheduledFragment();  // Scheduled fragment
            case 2:
                return new DeliveredFragment();  // Delivered fragment
            default:
                return new NewProductFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

