package com.project.countybuildingapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.countybuildingapp.R;
import com.project.countybuildingapp.adapters.BuildingAdapter;
import com.project.countybuildingapp.models.Building;
import com.project.countybuildingapp.models.County;
import com.project.countybuildingapp.utils.BottomNavLocker;
import com.project.countybuildingapp.utils.ToolBarLocker;

public class HomeFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;

    private String email, countyname;

    private FirebaseUser user;
    private DatabaseReference reference, countyreference;
    private BuildingAdapter adapter;
    private FirebaseRecyclerOptions<Building> options;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        // set
        ((ToolBarLocker)getActivity()).ToolBarLocked(false);
        ((BottomNavLocker)getActivity()).BottomNavLocked(false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("table_building");
        countyreference = FirebaseDatabase.getInstance().getReference().child("table_county_registration");

        // find view by id
        recyclerView = view.findViewById(R.id.recyclerview_buildings);

        // set / load data
        loadData();

        // listeners

        return view;
    }

    private String getEmail(){
        if (user != null) {
            email = user.getEmail();
        }
        return email;
    }

    private void loadData () {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        countyreference.orderByChild("email").equalTo(getEmail()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot != null) {
                    County county = snapshot.getValue(County.class);

                    countyname = county.getCountyname();
                }

                options = new FirebaseRecyclerOptions.Builder<Building>()
                        .setQuery(reference.orderByChild("buildingcounty").equalTo(countyname), Building.class)
                        .build();

                adapter = new BuildingAdapter(options, getContext());
                recyclerView.setAdapter(adapter);
                adapter.startListening();
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

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}