package com.example.carbooking.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.example.carbooking.ui.fragment.CompleteFragment;

public class CustomFragmentPageAdapter extends FragmentPagerAdapter {

    private static final int FRAGMENT_COUNT = 1;

    public CustomFragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new CompleteFragment();
//            case 1:
//                return new CompleteFragment();
//            case 2:
//                return new CancelledFragment();
        }
        return null;
    }
    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Booking";
//            case 1:
//                return "Complete";
//            case 2:
//                return "Cancelled";
        }
        return null;
    }
}
