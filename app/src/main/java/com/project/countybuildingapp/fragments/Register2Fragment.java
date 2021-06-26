package com.project.countybuildingapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.countybuildingapp.R;
import com.project.countybuildingapp.models.County;
import com.project.countybuildingapp.utils.BottomNavLocker;
import com.project.countybuildingapp.utils.ToolBarLocker;
import com.project.countybuildingapp.utils.ValidationsClass;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;


public class Register2Fragment extends Fragment {

    private View view;

    private ShowHidePasswordEditText txtPassword;
    private CheckBox chkAgree;
    private Button btnRegister;
    private ProgressBar progressBar;

    private String name, email, countyname;
    private int phone;

    private ValidationsClass validate;

    private Register2FragmentArgs args;

    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register2, container, false);

        // set
        ((ToolBarLocker) getActivity()).ToolBarLocked(false);
        ((BottomNavLocker) getActivity()).BottomNavLocked(true);

        validate = new ValidationsClass();

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("table_county_registration");

        args = Register2FragmentArgs.fromBundle(getArguments());
        name = args.getName();
        email = args.getEmail();
        countyname = args.getCountyGovernment();
        phone = args.getPhone();

        // find view by id
        txtPassword = view.findViewById(R.id.txt_register_password);
        chkAgree = view.findViewById(R.id.chk_register_terms);
        btnRegister = view.findViewById(R.id.btn_register2);
        progressBar = view.findViewById(R.id.progressbar_register);

        // set / load data


        // listeners
        btnRegister.setOnClickListener(registerListener);

        return view;
    }

    private View.OnClickListener registerListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (chkAgree.isChecked()) {
                if (create(txtPassword)) {
                    progressBar.setVisibility(View.VISIBLE);

                    String password = txtPassword.getText().toString().trim();

                    County county = new County(name, email, countyname, phone);
                    reference.push().setValue(county).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                register(email, password);
                            } else {
                                Toast.makeText(getContext(), "Error uploading", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });

                }
            } else {
                Toast.makeText(getContext(), "Agree to the terms to complete registration", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private boolean create(ShowHidePasswordEditText txtPass) {

        boolean valid = false;

        String password = txtPass.getText().toString().trim();

        validate.setPassword(password);

        switch (validate.validatePassword()) {
            case 0:
                txtPass.setError("Text field is empty");
                txtPass.setFocusable(true);
                valid = false;
                break;
            case 1:
                txtPass.setError("Password should be 6 or more characters");
                txtPass.setFocusable(true);
                Toast.makeText(getContext(), "Feedback = " + validate.validatePassword(), Toast.LENGTH_LONG).show();
                valid = false;
                break;
            case 2:
                txtPass.setError("At least one uppercase letter, one number");
                txtPass.setFocusable(true);
                valid = false;
                break;
            case 3:
                valid = true;
                txtPass.setError(null);
                break;

        }

        return valid;
    }


    private void register(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            progressBar.setVisibility(View.GONE);

                            Toast.makeText(getContext(), "Sucessful Registration", Toast.LENGTH_SHORT).show();

                            Navigation.findNavController(view).navigate(R.id.navigateToHomeFromRegister);
                        } else {
                            progressBar.setVisibility(View.GONE);
                            if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                                Toast.makeText(getContext(), "Weak Password", Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser account) {

        if (account != null) {
            Toast.makeText(getContext(), "U Signed In successfully", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getContext(), "U Didnt signed in", Toast.LENGTH_LONG).show();
        }

    }
}