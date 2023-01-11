package com.nik.someproject.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.nik.someproject.R;

public class ErrorView extends Fragment {
    TextView textTitle;
    TextView textDescription;
    Button exit;

    String title;
    String description;

    public ErrorView(String title, String description) {
        super(R.layout.error);
        this.title = title;
        this.description = description;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        textTitle = view.findViewById(R.id.errorTitle);
        textDescription = view.findViewById(R.id.errorDescription);
        exit = view.findViewById(R.id.errorExit);

        textTitle.setText(title);
        textDescription.setText(description);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().setFragmentResult("exit", savedInstanceState);
                remove();
            }
        });
    }

    private void remove() {
        getParentFragmentManager().beginTransaction().remove(this).commit();
    }
}
