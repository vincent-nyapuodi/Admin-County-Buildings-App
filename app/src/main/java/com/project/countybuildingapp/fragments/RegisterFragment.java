package com.project.countybuildingapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.project.countybuildingapp.R;
import com.project.countybuildingapp.utils.BottomNavLocker;
import com.project.countybuildingapp.utils.ToolBarLocker;
import com.project.countybuildingapp.utils.ValidationsClass;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;


public class RegisterFragment extends Fragment {

    private View view;

    private EditText txtName, txtEmail, txtPhone;
    private Spinner spinnerCounty;
    private Button btnRegister;
    private TextView tvRegisterToLogin;

    private ValidationsClass validate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);

        // set
        ((ToolBarLocker) getActivity()).ToolBarLocked(false);
        ((BottomNavLocker) getActivity()).BottomNavLocked(true);

        validate = new ValidationsClass();

        // find view by id
        txtName = view.findViewById(R.id.txt_register_name);
        txtEmail = view.findViewById(R.id.txt_register_email);
        txtPhone = view.findViewById(R.id.txt_register_phone);
        spinnerCounty = view.findViewById(R.id.spinner_county);
        btnRegister = view.findViewById(R.id.btn_register1);
        tvRegisterToLogin = view.findViewById(R.id.tv_registerto_login);

        // set / load data
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.counties, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCounty.setAdapter(adapter);


        // listeners
        btnRegister.setOnClickListener(registerListener);
        tvRegisterToLogin.setOnClickListener(registerToLoginListener);

        return view;
    }


    private View.OnClickListener registerListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String name = txtName.getText().toString().trim();
            String email = txtEmail.getText().toString().trim();
            String phone = txtPhone.getText().toString().trim();
            String county = spinnerCounty.getSelectedItem().toString();

            if (name.isEmpty()) {
                txtName.setError("Text field is empty");
                txtName.setFocusable(true);
            }  else if (!create(txtEmail, txtPhone)) {

            }else if (county.equals("-- SELECT COUNTY --")) {
                txtName.setError(null);
                txtPhone.setError(null);
                txtEmail.setError(null);
                Toast.makeText(getContext(), "Text field is empty", Toast.LENGTH_SHORT).show();
            } else {
                txtName.setError(null);
                txtPhone.setError(null);
                txtEmail.setError(null);

                RegisterFragmentDirections.NavigateToRegister2 action =
                        RegisterFragmentDirections.navigateToRegister2(name, email, Integer.parseInt(phone), county);
                Navigation.findNavController(view).navigate(action);
            }
        }
    };

    private View.OnClickListener registerToLoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Navigation.findNavController(view).navigateUp();
        }
    };


    private boolean create(EditText txtMail, EditText txtNo) {

        boolean valid = false;

        String email = txtMail.getText().toString().trim();
        String phone = txtNo.getText().toString().trim();

        validate.setEmail(email);
        validate.setPhoneNo(phone);

        switch (validate.validateEmail()) {
            case 0:
                txtMail.setError("Text field is empty");
                txtMail.setFocusable(true);
                valid = false;
                break;
            case 1:
                txtMail.setError("Invalid Email");
                txtMail.setFocusable(true);
                valid = false;
                break;
            case 2:
                switch (validate.validatePhoneNo()) {
                    case 0:
                        txtMail.setError(null);
                        txtNo.setError("Text Field is empty");
                        txtNo.setFocusable(true);
                        valid = false;
                        break;
                    case 1:
                        txtMail.setError(null);
                        txtNo.setError("Length should be 10");
                        txtNo.setFocusable(true);
                        valid = false;
                        break;
                    case 2:
                        txtMail.setError(null);
                        txtNo.setError("Should contain numbers only");
                        txtNo.setFocusable(true);
                        valid = false;
                        break;
                    case 3:
                        valid = true;
                        txtMail.setError(null);
                        break;
                }
                break;
        }

        return valid;
    }
}