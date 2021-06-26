package com.project.countybuildingapp.fragments;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.countybuildingapp.R;
import com.project.countybuildingapp.models.Building;
import com.project.countybuildingapp.utils.BottomNavLocker;
import com.project.countybuildingapp.utils.ToolBarLocker;
import com.project.countybuildingapp.utils.ValidationsClass;

public class BuildingDetailFragment extends Fragment {

    private View view;

    private ImageView imgBuilding, imgKraVerification, imgNemaVerification, imgFireSafetyVerification, imgSanitationVerification, imgInspectionVerification;
    private TextView tvProfileBuildingName, tvProfileBuildingLocation, tvProfileSeeMore, tvCertificationSeeMore;

    private String email, refKey, buildingname, buildinglocation, buildingimage, buildingcode, buildingemail;

    private BuildingDetailFragmentArgs args;

    private FirebaseUser user;
    private DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_building_detail, container, false);

        // set
        ((ToolBarLocker)getActivity()).ToolBarLocked(false);
        ((BottomNavLocker)getActivity()).BottomNavLocked(true);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("table_building");

        args = BuildingDetailFragmentArgs.fromBundle(getArguments());
        refKey = args.getRefKey();

        // find view by id
        imgBuilding = view.findViewById(R.id.img_building);
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
        loadData();

        // listeners
        tvProfileSeeMore.setOnClickListener(profileListener);
        tvCertificationSeeMore.setOnClickListener(certificationListener);

        return view;
    }


    private View.OnClickListener profileListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            BuildingDetailFragmentDirections.NavigateToBuildingProfile action =
                    BuildingDetailFragmentDirections.navigateToBuildingProfile(refKey, buildingemail);
            Navigation.findNavController(view).navigate(action);
        }
    };


    private View.OnClickListener certificationListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            BuildingDetailFragmentDirections.NavigateToBuildingApproval action =
                    BuildingDetailFragmentDirections.navigateToBuildingApproval(buildingcode, buildingname);
            Navigation.findNavController(view).navigate(action);
        }
    };


    private void loadData () {
        reference.orderByKey().equalTo(refKey).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot != null) {
                    Building building = snapshot.getValue(Building.class);

                    buildingname = building.getBuildingname();
                    buildinglocation = building.getBuildingtown() + ", " + building.getBuildingcounty();
                    buildingimage = building.getBuildingphoto();
                    buildingcode = building.getBuildingcode();
                    buildingemail = building.getOwneremail();
                }

                tvProfileBuildingName.setText(buildingname);
                tvProfileBuildingLocation.setText(buildinglocation);
                Glide.with(getContext()).load(buildingimage).into(imgBuilding);
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