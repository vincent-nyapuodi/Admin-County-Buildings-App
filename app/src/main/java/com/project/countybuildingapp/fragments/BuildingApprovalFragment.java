package com.project.countybuildingapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.countybuildingapp.R;
import com.project.countybuildingapp.utils.BottomNavLocker;
import com.project.countybuildingapp.utils.ToolBarLocker;

public class BuildingApprovalFragment extends Fragment {

    private View view;
    private TextView tvBuildingArchitect, tvBuildingSupervisor, tvBuildingContractor;
    private TextView tvKraCertificateNo, tvNemaCertificateNo, tvSanitationCertificateNo, tvFireSafetyCertificateNo, tvInspectionCertificateNo;
    private TextView tvKraComment, tvNemaComment, tvSanitationComment, tvFireSafetyComment, tvInspectionComment;
    private ImageView imgKraDownload, imgNemaDownload, imgSanitationDownload, imgFireSafetyDownload, imgInspectionDownload;
    private Button btnKra, btnNema, btnSanitation, btnFireSafety, btnInspection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_building_approval, container, false);

        // set
        ((ToolBarLocker)getActivity()).ToolBarLocked(false);
        ((BottomNavLocker)getActivity()).BottomNavLocked(true);

        // find view by id
        tvBuildingArchitect = view.findViewById(R.id.tv_buildingarchitect);
        tvBuildingSupervisor = view.findViewById(R.id.tv_buildingsupervisor);
        tvBuildingContractor = view.findViewById(R.id.tv_buildingcontractor);

        tvKraCertificateNo = view.findViewById(R.id.tv_kracertificateno);
        tvNemaCertificateNo = view.findViewById(R.id.tv_nemacertificateno);
        tvSanitationCertificateNo = view.findViewById(R.id.tv_sanitationcertificateno);
        tvFireSafetyCertificateNo = view.findViewById(R.id.tv_firesafetycertificateno);
        tvInspectionCertificateNo = view.findViewById(R.id.tv_inspectioncertificateno);

        tvKraComment = view.findViewById(R.id.tv_kra_comment);
        tvNemaComment = view.findViewById(R.id.tv_nema_comment);
        tvSanitationComment = view.findViewById(R.id.tv_sanitation_comment);
        tvFireSafetyComment = view.findViewById(R.id.tv_firesafety_comment);
        tvInspectionComment = view.findViewById(R.id.tv_inspection_comment);

        imgKraDownload = view.findViewById(R.id.img_download_kra);
        imgNemaDownload = view.findViewById(R.id.img_download_nema);
        imgSanitationDownload = view.findViewById(R.id.img_download_sanitation);
        imgFireSafetyDownload = view.findViewById(R.id.img_download_firesafety);
        imgInspectionDownload = view.findViewById(R.id.img_download_inspection);

        btnKra = view.findViewById(R.id.btn_kra_approve);
        btnNema = view.findViewById(R.id.btn_nema_approve);
        btnSanitation = view.findViewById(R.id.btn_sanitation_approve);
        btnFireSafety = view.findViewById(R.id.btn_firesafety_approve);
        btnInspection = view.findViewById(R.id.btn_inspection_approve);

        // set / load data


        // listeners
        btnKra.setOnClickListener(kraListener);
        btnNema.setOnClickListener(nemaListener);
        btnSanitation.setOnClickListener(sanitationListener);
        btnFireSafety.setOnClickListener(fireSafetyListener);
        btnInspection.setOnClickListener(inspectionListener);

        return view;
    }

    private View.OnClickListener kraListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    private View.OnClickListener nemaListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    private View.OnClickListener sanitationListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };


    private View.OnClickListener fireSafetyListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };


    private View.OnClickListener inspectionListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };
}