package com.project.countybuildingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.countybuildingapp.R;
import com.project.countybuildingapp.fragments.HomeFragmentDirections;
import com.project.countybuildingapp.models.Building;

import java.util.Arrays;

public class BuildingAdapter extends FirebaseRecyclerAdapter<Building, BuildingAdapter.MyViewHolder> {

    private Context context;
    private DatabaseReference reference;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public BuildingAdapter(@NonNull FirebaseRecyclerOptions<Building> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Building model) {
        RelativeLayout lytBuilding = holder.lytBuilding;
        TextView tvBuildingName = holder.tvListBuildingName;
        TextView tvBuildingStatus = holder.tvListStatus;
        TextView tvBuildingResidence = holder.tvListResidenceType;
        TextView tvBuildingLocation = holder.tvListLocation;
        ImageView imgStatus = holder.imgStatus;

        tvBuildingName.setText(model.getBuildingname());

        String [] array = model.getBuildingtype().split("_");
        String residencetype = array[0].toString();

        String type = "";

        switch (model.getBuildingtype()) {
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

        boolean status = model.getCheck_status();

        tvBuildingName.setText(model.getBuildingname());
        tvBuildingResidence.setText(residencetype + " residence | " + type);
        tvBuildingStatus.setVisibility(status ? View.GONE : View.VISIBLE);
        imgStatus.setVisibility(status ? View.VISIBLE : View.GONE);
        tvBuildingLocation.setText(model.getBuildingtown() + ", " + model.getBuildingcounty());

        lytBuilding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference = FirebaseDatabase.getInstance().getReference().child("table_building");

                String refKey = getRef(position).getKey();

                reference.orderByKey().equalTo(refKey).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot != null) {
                            reference.child(refKey).child("check_status").setValue(true);

                            Navigation.findNavController(view).navigate(HomeFragmentDirections.navigateToBuildingDetail(refKey));
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
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_buildings, parent, false);

        BuildingAdapter.MyViewHolder myViewHolder = new BuildingAdapter.MyViewHolder(view);

        return myViewHolder;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout lytBuilding;
        TextView tvListBuildingName, tvListStatus, tvListResidenceType, tvListLocation;
        ImageView imgStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            lytBuilding = itemView.findViewById(R.id.lyt_building);
            tvListBuildingName = itemView.findViewById(R.id.tv_list_buildingname);
            tvListStatus = itemView.findViewById(R.id.tv_list_status);
            tvListResidenceType = itemView.findViewById(R.id.tv_list_residence);
            tvListLocation = itemView.findViewById(R.id.tv_list_location);
            imgStatus = itemView.findViewById(R.id.img_list_status);
        }
    }
}
