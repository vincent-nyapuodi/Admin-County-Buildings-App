package com.project.countybuildingapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.project.countybuildingapp.R;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;


public class LoginFragment extends Fragment {

    private View view;

    private EditText txtEmail;
    private ShowHidePasswordEditText txtPassword;
    private ProgressBar progressBar;
    private Button btnLogin;
    private TextView tvLoginToRegister;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);

        // set


        // find view by id
        txtEmail = view.findViewById(R.id.txt_login_email);
        txtPassword = view.findViewById(R.id.txt_login_password);
        progressBar = view.findViewById(R.id.progressbar_login);
        btnLogin = view.findViewById(R.id.btn_login);
        tvLoginToRegister = view.findViewById(R.id.tv_loginto_register);

        // set / load data


        // listeners
        btnLogin.setOnClickListener(loginListener);
        tvLoginToRegister.setOnClickListener(loginToRegisterListener);

        return view;
    }


    private View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    private View.OnClickListener loginToRegisterListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };
}