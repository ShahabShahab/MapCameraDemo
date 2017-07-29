package com.example.user.mapapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.mapapplication.Model.Clinic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 6/19/2017.
 */

public class MapsPagerAdapter extends FragmentStatePagerAdapter {

    Context context;
    List<Clinic> clinicList;
    int position;

    public MapsPagerAdapter(FragmentManager fm, Context context, List<Clinic> clinicList) {
        super(fm);
        this.context = context;
        this.clinicList = clinicList;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new ClinicMapPreviewFragment(clinicList.get(position), this);
        this.position = position;
        return fragment;
    }

    @Override
    public int getCount() {
        return clinicList.size();
    }


}
