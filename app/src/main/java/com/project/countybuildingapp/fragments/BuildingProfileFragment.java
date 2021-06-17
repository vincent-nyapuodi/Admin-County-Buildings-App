package com.project.countybuildingapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.countybuildingapp.R;
import com.project.countybuildingapp.utils.BottomNavLocker;
import com.project.countybuildingapp.utils.ToolBarLocker;


public class BuildingProfileFragment extends Fragment {

    private View view;
    private TextView tvOwnerName, tvOwnerPhone, tvBuildingName, tvBuildingResidence, tvBuildingLocation, tvBuildingDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_building_profile, container, false);

        // set
        ((ToolBarLocker)getActivity()).ToolBarLocked(false);
        ((BottomNavLocker)getActivity()).BottomNavLocked(true);

        // find view by id
        tvOwnerName = view.findViewById(R.id.tv_ownername);
        tvOwnerPhone = view.findViewById(R.id.tv_ownerphone);
        tvBuildingName = view.findViewById(R.id.tv_buildingname);
        tvBuildingResidence = view.findViewById(R.id.tv_buildingresidence);
        tvBuildingLocation = view.findViewById(R.id.tv_buildinglocation);
        tvBuildingDescription = view.findViewById(R.id.tv_buildingdescription);

        // set / load data


        // listeners


        return view;
    }
}