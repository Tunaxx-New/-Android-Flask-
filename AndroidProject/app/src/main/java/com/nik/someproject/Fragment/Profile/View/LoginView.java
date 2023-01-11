package com.nik.someproject.Fragment.Profile.View;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.nik.someproject.DataManipulation.SavedData;
import com.nik.someproject.Fragment.MyView;
import com.nik.someproject.Fragment.Profile.ViewModel.LoginViewModel;
import com.nik.someproject.R;

public class LoginView extends MyView {
    TextView phone;
    TextView password;
    TextView error;
    Button submit;

    LoginViewModel viewModel;

    public LoginView(Activity activity, SavedData savedData) {
        super(R.layout.login, activity, savedData);
        viewModel = new LoginViewModel();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        phone = view.findViewById(R.id.log_phone);
        password = view.findViewById(R.id.log_password);
        error = view.findViewById(R.id.error);
        submit = view.findViewById(R.id.log_submit);

        viewModel.getLoginResult().observe(this.getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String hash) {
                if (hash != null && !hash.equals("")) {
                    setResult("logged", savedInstanceState);
                    savedData.save(R.string.HASH_KEY, hash);
                    remove();
                }
            }
        });

        viewModel.getPhone().observe(this.getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                phone.setText(s);
            }
        });

        viewModel.getError().observe(this.getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                error.setText(s);
                if (s.contains("Status -1")) {
                    setResult("No server", savedInstanceState);
                    remove();
                }
            }
        });

        viewModel.getPassword().observe(this.getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                password.setText(s);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.login(phone.getText().toString(), password.getText().toString());
            }
        });
    }
}
