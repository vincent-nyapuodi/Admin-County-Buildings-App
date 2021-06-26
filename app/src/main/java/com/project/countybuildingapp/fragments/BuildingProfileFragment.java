package com.project.countybuildingapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.countybuildingapp.R;
import com.project.countybuildingapp.models.Building;
import com.project.countybuildingapp.models.Owner;
import com.project.countybuildingapp.utils.BottomNavLocker;
import com.project.countybuildingapp.utils.ToolBarLocker;


public class BuildingProfileFragment extends Fragment {

    private View view;
    private TextView tvOwnerName, tvOwnerPhone, tvBuildingName, tvBuildingResidence, tvBuildingLocation, tvBuildingDescription;

    private String refKey, buildingemail, ownername, buildingname, buildingtype, buildinglocation, buildingdetails;
    private int ownerphone;

    private BuildingProfileFragmentArgs args;

    private DatabaseReference reference, ownerreference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_building_profile, container, false);

        // set
        ((ToolBarLocker)getActivity()).ToolBarLocked(false);
        ((BottomNavLocker)getActivity()).BottomNavLocked(true);

        args = BuildingProfileFragmentArgs.fromBundle(getArguments());
        refKey = args.getRefKey();
        buildingemail = args.getBuildingemail();

        reference = FirebaseDatabase.getInstance().getReference().child("table_building");
        ownerreference = FirebaseDatabase.getInstance().getReference().child("table_owner");

        // find view by id
        tvOwnerName = view.findViewById(R.id.tv_ownername);
        tvOwnerPhone = view.findViewById(R.id.tv_ownerphone);
        tvBuildingName = view.findViewById(R.id.tv_buildingname);
        tvBuildingResidence = view.findViewById(R.id.tv_buildingresidence);
        tvBuildingLocation = view.findViewById(R.id.tv_buildinglocation);
        tvBuildingDescription = view.findViewById(R.id.tv_buildingdescription);

        // set / load data
        loadData();

        // listeners

        return view;
    }

    private void loadData () {
        ownerreference.orderByChild("email").equalTo(buildingemail).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot != null) {
                    Owner owner = snapshot.getValue(Owner.class);

                    ownername = owner.getName();
                    ownerphone = owner.getPhone();
                }

                tvOwnerName.setText(ownername);
                tvOwnerPhone.setText("0" + String.valueOf(ownerphone));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.orderByKey().equalTo(refKey).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot != null) {
                    Building building = snapshot.getValue(Building.class);

                    buildingname = building.getBuildingname();
                    buildingtype = building.getBuildingtype();
                    buildinglocation = building.getBuildingcounty() + " County - " + building.getBuildingtown();
                    buildingdetails = building.getBuildingdesc();

                    String [] array = buildingtype.split("_");
                    String residencetype = array[0].toString();

                    String type = "";

                    switch (buildingtype) {
                        case "private_1":
                            type = "city";
                            break;
                        case "private_2":
                            type = "upcountry";
                            break;
                        case "private_3":
                            type = "ghetto";
                            break;
                        case "private_4":
                            type = "estate";
                            break;
                        case "commercial_1":
                            type = "hotel";
                            break;
                        case "commercial_2":
                            type = "rental";
                            break;
                        case "commercial_3":
                            type = "industrial";
                            break;
                        case "commercial_4":
                            type = "office";
                            break;
                    }

                    tvBuildingName.setText(buildingname);
                    tvBuildingLocation.setText(buildinglocation);
                    tvBuildingResidence.setText(residencetype + " residence | " + type);
                    tvBuildingDescription.setText(buildingdetails);
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}