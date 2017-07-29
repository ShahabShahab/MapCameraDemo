package com.example.user.mapapplication;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.mapapplication.Model.Clinic;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class ClinicMapPreviewFragment extends Fragment implements View.OnClickListener {

    Clinic clinic;
    TextView clinicName, clinicAddress;
    //ClinicClickListener listener;

    public ClinicMapPreviewFragment(Clinic clinic, MapsPagerAdapter listener) {
        // Required empty public constructor
        setClinic(clinic);
        //this.listener = listener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_clinic_map_preview, container, false);
        view.setOnClickListener(this);
        clinicName = (TextView) view.findViewById(R.id.txtClinicName);
        clinicAddress = (TextView) view.findViewById(R.id.txtClinicAddress);
        clinicAddress.setText(clinic.getAddress());
        clinicName.setText(clinic.getName());
        return view;
    }

    public Clinic getClinic() {
        return clinic;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

    @Override
    public void onClick(View v) {
        //listener.getClinic(this.clinic);
    }
}
