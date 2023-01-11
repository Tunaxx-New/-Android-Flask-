package com.nik.someproject.Fragment.Chat.View;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.nik.someproject.DataManipulation.SavedData;
import com.nik.someproject.DataManipulation.TechSupportUser;
import com.nik.someproject.Fragment.Chat.ViewModel.ChatSelectViewModel;
import com.nik.someproject.Fragment.MyView;
import com.nik.someproject.R;

import java.util.Observable;
import java.util.Observer;

public class ChatSelectView extends MyView implements Observer {
    SavedData savedData;
    LinearLayout container;

    ChatSelectViewModel chatSelectViewModel;

    public ChatSelectView(Activity activity, SavedData savedData) {
        super(R.layout.chat_select, activity, savedData);
        this.savedData = savedData;
        chatSelectViewModel = new ChatSelectViewModel(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        container = view.findViewById(R.id.chats_container);

        chatSelectViewModel.getError().observe(this.getViewLifecycleOwner(), new androidx.lifecycle.Observer<Boolean>() {
            @Override
            public void onChanged(Boolean b) {
                Log.i("LOL", b.toString());
                if (!b)
                    return;
                Bundle errorBundle = new Bundle();
                errorBundle.putString("title", "Server error");
                errorBundle.putString("description", "Server is not available in current time");
                setResult("No server", errorBundle);
                remove();
            }
        });

        chatSelectViewModel.get(savedData.get(R.string.HASH_KEY));
    }

    public void addTech(TechSupportUser user) {
        View userView;
        userView = getLayoutInflater().inflate(R.layout.tech_support_select, null);
        ((TextView)userView.findViewById(R.id.techs_fullname)).setText(user.fullName);

        userView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("tech_id", user.id);
                setResult("Selected", bundle);
                remove();
            }
        });

        container.addView(userView);
    }

    public void addTechs(TechSupportUser[] users) {
        for (int i = 0; i < users.length; i++)
            addTech(users[i]);
    }

    @Override
    public void update(Observable observable, Object o) {
        addTechs((TechSupportUser[])o);
    }
}
