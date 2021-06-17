package com.project.countybuildingapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.project.countybuildingapp.R;
import com.project.countybuildingapp.utils.BottomNavLocker;
import com.project.countybuildingapp.utils.ToolBarLocker;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;


public class Register2Fragment extends Fragment {

    private View view;

    private ShowHidePasswordEditText txtPassword;
    private CheckBox chkAgree;
    private Button btnRegister;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register2, container, false);

        // set
        ((ToolBarLocker)getActivity()).ToolBarLocked(false);
        ((BottomNavLocker)getActivity()).BottomNavLocked(true);


        // find view by id
        txtPassword = view.findViewById(R.id.txt_register_password);
        chkAgree = view.findViewById(R.id.chk_register_terms);
        btnRegister = view.findViewById(R.id.btn_register2);

        // set / load data


        // listeners
        chkAgree.setOnCheckedChangeListener(chkListener);
        btnRegister.setOnClickListener(registerListener);

        return view;
    }

    private CompoundButton.OnCheckedChangeListener chkListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                btnRegister.setEnabled(true);
            } else {
                btnRegister.setEnabled(false);
            }
        }
    };

    private View.OnClickListener registerListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Navigation.findNavController(view).navigate(R.id.navigateToHomeFromRegister);
        }
    };
}