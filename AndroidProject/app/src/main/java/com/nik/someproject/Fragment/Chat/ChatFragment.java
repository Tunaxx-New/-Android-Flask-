package com.nik.someproject.Fragment.Chat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.nik.someproject.DataManipulation.SavedData;
import com.nik.someproject.Fragment.Chat.View.ChatSelectView;
import com.nik.someproject.Fragment.Chat.View.ChatView;
import com.nik.someproject.Fragment.MyFragment;
import com.nik.someproject.Fragment.Profile.View.LoginView;
import com.nik.someproject.R;

public class ChatFragment extends MyFragment {

    int tech_id = -1;
    boolean isError = false;

    public ChatFragment(Activity activity, SavedData savedData) {
        super(activity, savedData);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") ViewGroup root = (ViewGroup) inflater.inflate(R.layout.chat_fragment, container);

        enter();

        return root;
    }

    @Override
    public void enter() {
        if (savedData.contains(R.string.HASH_KEY)) {
            if (tech_id == -1) {
                createChatSelectView(isError || savedData.get(R.string.RELOGIN_CHAT_KEY).equals("relogin"));
            } else {
                createChatView(isError || savedData.get(R.string.RELOGIN_CHAT_KEY).equals("relogin"));
            }
            isError = false;
            savedData.save(R.string.RELOGIN_CHAT_KEY, "logged");
        }
        else {
            createLocalError("Login error", "Please login into your account!", R.id.fragment_chat_container);
            isError = true;
        }
    }

    @Override
    protected void onErrorExit() {
        enter();
    }

    private void createChatSelectView(boolean forceInitialization) {
        if (!isAdded())
            return;
        if (!forceInitialization)
            if (getFragmentManager() == null || getChildFragmentManager().getFragments().size() > 0)
                return;

        ChatSelectView chatSelectView = new ChatSelectView(activity, savedData);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_chat_container, chatSelectView, "chat_select").commit();

        getChildFragmentManager().setFragmentResultListener("Selected", chatSelectView, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                tech_id = result.getInt("tech_id");
                createChatView(true);
            }
        });

        //
        // The resulting No server, when profile view destroys
        // No server - when server not responding on get user profile
        //
        getChildFragmentManager().setFragmentResultListener("No server", chatSelectView, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                createError(result.getString("title"), result.getString("description"));
                isError = true;
            }
        });
    }

    private void createChatView(boolean forceInitialization) {
        if (!isAdded())
            return;
        if (!forceInitialization)
            if (getFragmentManager() == null || getChildFragmentManager().getFragments().size() > 0)
                return;

        ChatView chatView = new ChatView(activity, savedData, tech_id);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_chat_container, chatView, "chat").commit();

        getChildFragmentManager().setFragmentResultListener("Back", chatView, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                tech_id = -1;
                createChatSelectView(true);
            }
        });

        //
        // The resulting No server, when profile view destroys
        // No server - when server not responding on get user profile
        //
        getChildFragmentManager().setFragmentResultListener("No server", chatView, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                createError(result.getString("title"), result.getString("description"));
                isError = true;
            }
        });
    }
}
