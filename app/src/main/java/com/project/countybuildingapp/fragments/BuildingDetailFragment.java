package com.project.countybuildingapp.fragments;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.project.countybuildingapp.R;
import com.project.countybuildingapp.utils.BottomNavLocker;
import com.project.countybuildingapp.utils.ToolBarLocker;

public class BuildingDetailFragment extends Fragment {

    private View view;

    private ImageView imgKraVerification, imgNemaVerification, imgFireSafetyVerification, imgSanitationVerification, imgInspectionVerification;
    private TextView tvProfileBuildingName, tvProfileBuildingLocation, tvProfileSeeMore, tvCertificationSeeMore;
    private ImageSlider imgBuildingSlider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_building_detail, container, false);

        // set
        ((ToolBarLocker)getActivity()).ToolBarLocked(false);
        ((BottomNavLocker)getActivity()).BottomNavLocked(true);

        // find view by id
        imgBuildingSlider = view.findViewById(R.id.img_building_slider);
        imgKraVerification = view.findViewById(R.id.img_certifications_kraverification);
        imgNemaVerification = view.findViewById(R.id.img_certifications_nemaverification);
        imgFireSafetyVerification = view.findViewById(R.id.img_certifications_firesafetyverification);
        imgSanitationVerification = view.findViewById(R.id.img_certifications_sanitationverification);
        imgInspectionVerification = view.findViewById(R.id.img_certifications_inspectionverification);

        tvProfileBuildingName = view.findViewById(R.id.tv_profile_buildingname);
        tvProfileBuildingLocation = view.findViewById(R.id.tv_profile_buildinglocation);
        tvProfileSeeMore = view.findViewById(R.id.tv_profile_seemore);
        tvCertificationSeeMore = view.findViewById(R.id.tv_certifications_seemore);


        // set / load data


        // listeners
        tvProfileSeeMore.setOnClickListener(profileListener);
        tvCertificationSeeMore.setOnClickListener(certificationListener);

        return view;
    }


    private View.OnClickListener profileListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };


    private View.OnClickListener certificationListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };
}