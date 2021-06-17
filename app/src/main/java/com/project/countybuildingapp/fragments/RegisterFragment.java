package com.project.countybuildingapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.project.countybuildingapp.R;
import com.project.countybuildingapp.utils.BottomNavLocker;
import com.project.countybuildingapp.utils.ToolBarLocker;


public class RegisterFragment extends Fragment {

    private View view;

    private EditText txtName, txtEmail, txtPhone;
    private Spinner spinnerCounty;
    private Button btnRegister;
    private TextView tvRegisterToLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);

        // set
        ((ToolBarLocker)getActivity()).ToolBarLocked(false);
        ((BottomNavLocker)getActivity()).BottomNavLocked(true);

        // find view by id
        txtName = view.findViewById(R.id.txt_register_name);
        txtEmail = view.findViewById(R.id.txt_register_email);
        txtPhone = view.findViewById(R.id.txt_register_phone);
        spinnerCounty = view.findViewById(R.id.spinner_county);
        btnRegister = view.findViewById(R.id.btn_register1);
        tvRegisterToLogin = view.findViewById(R.id.tv_registerto_login);

        // set / load data


        // listeners
        btnRegister.setOnClickListener(registerListener);
        tvRegisterToLogin.setOnClickListener(registerToLoginListener);

        return view;
    }


    private View.OnClickListener registerListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Navigation.findNavController(view).navigate(R.id.navigateToRegister2);
        }
    };

    private View.OnClickListener registerToLoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Navigation.findNavController(view).navigateUp();
        }
    };
}