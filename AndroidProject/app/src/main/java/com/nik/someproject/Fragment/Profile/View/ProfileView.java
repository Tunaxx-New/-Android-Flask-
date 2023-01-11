package com.nik.someproject.Fragment.Profile.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.nik.someproject.DataManipulation.SavedData;
import com.nik.someproject.DataManipulation.User;
import com.nik.someproject.Fragment.MyView;
import com.nik.someproject.Fragment.Profile.ViewModel.ProfileViewModel;
import com.nik.someproject.Map.MapActivity;
import com.nik.someproject.R;

import java.util.Observable;


public class ProfileView extends MyView {
    Button logout;
    Button save;

    EditText name;
    EditText surname;
    EditText email;
    EditText phone;
    CheckBox isEmail;
    CheckBox isPhone;
    TextView created;

    ProfileViewModel viewModel;

    public ProfileView(Activity activity, SavedData savedData) {
        super(R.layout.profile, activity, savedData);
        viewModel = new ProfileViewModel(savedData);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        logout = view.findViewById(R.id.logout);
        save = view.findViewById(R.id.profile_save);

        name = view.findViewById(R.id.profile_name);
        surname = view.findViewById(R.id.profile_surname);
        email = view.findViewById(R.id.profile_email);
        phone = view.findViewById(R.id.profile_phone);
        isEmail = view.findViewById(R.id.profile_isemail);
        isPhone = view.findViewById(R.id.profile_isphone);
        created = view.findViewById(R.id.profile_created);

        Button map = view.findViewById(R.id.profile_map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MapActivity.class);
                startActivity(intent);
            }
        });

        viewModel.getName().observe(this.getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                name.setText(s);
            }
        });

        viewModel.getSurname().observe(this.getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                surname.setText(s);
            }
        });

        viewModel.getEmail().observe(this.getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                email.setText(s);
            }
        });

        viewModel.getPhone().observe(this.getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                phone.setText(s);
            }
        });

        viewModel.getIsEmail().observe(this.getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean b) {
                isEmail.setChecked(b);
            }
        });

        viewModel.getIsPhone().observe(this.getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean b) {
                isPhone.setChecked(b);
            }
        });

        viewModel.getCreated().observe(this.getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                created.setText(s);
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();
                user.name = name.getText().toString();
                user.surname = surname.getText().toString();
                user.phone = phone.getText().toString();
                user.email = email.getText().toString();
                viewModel.change(savedData.get(R.string.HASH_KEY), user);
                setResult("Refresh", null);
                remove();
            }
        });

        viewModel.getLogged().observe(this.getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String code) {
                switch (code) {
                    case "200true":
                        // Success
                        break;

                    case "200false":
                        Toast.makeText(getContext(), viewModel.getError(), Toast.LENGTH_SHORT);
                        setResult("No login", savedInstanceState);
                        remove();
                        break;

                    case "-1null":
                        Bundle errorBundle = new Bundle();
                        errorBundle.putString("title", "Server error");
                        errorBundle.putString("description", "Server is not available in current time");
                        setResult("No server", errorBundle);
                        remove();
                        break;
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savedData.delete(R.string.HASH_KEY);
                savedData.save(R.string.RELOGIN_CHAT_KEY, "relogin");
                setResult("No login", savedInstanceState);
                remove();
            }
        });

        viewModel.get(savedData.get(R.string.HASH_KEY));
    }
}
