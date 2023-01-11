package com.nik.someproject.Fragment.Chat.View;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.nik.someproject.DataManipulation.Message;
import com.nik.someproject.DataManipulation.ObservObject;
import com.nik.someproject.DataManipulation.SavedData;
import com.nik.someproject.Fragment.Chat.ViewModel.ChatViewModel;
import com.nik.someproject.Fragment.MyView;
import com.nik.someproject.R;

import java.util.Observable;

public class ChatView extends MyView implements java.util.Observer {
    LinearLayout container;
    Button send;
    Button back;
    EditText messageText;

    ChatViewModel chatViewModel;

    int tech_id;

    public ChatView(Activity activity, SavedData savedData, int tech_id) {
        super(R.layout.chat, activity, savedData);
        chatViewModel = new ChatViewModel(activity, savedData, tech_id, 1, 10, this);
        chatViewModel.addMessageObserver(this);
        this.tech_id = tech_id;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        container = view.findViewById(R.id.chat_container);
        send = view.findViewById(R.id.chat_message_send);
        back = view.findViewById(R.id.chat_back);
        messageText = view.findViewById(R.id.chat_message_get);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult("Back", null);
                remove();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageText.getText().toString();
                if (message.equals(""))
                    return;
                chatViewModel.sendServer(message);
                messageText.setText("");
            }
        });

        chatViewModel.getError().observe(this.getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean b) {
                if (!b)
                    return;
                Bundle errorBundle = new Bundle();
                errorBundle.putString("title", "Server error");
                errorBundle.putString("description", "Server is not available in current time");
                setResult("No server", errorBundle);
                remove();
            }
        });

        chatViewModel.joinServer();
        chatViewModel.get(savedData.get(R.string.HASH_KEY));
    }

    public void addMessage(Message message, int position) {
        View messageView;
        if (message.user_id != tech_id)
            messageView = getLayoutInflater().inflate(R.layout.user_message, null);
        else
            messageView = getLayoutInflater().inflate(R.layout.another_message, null);

        ((TextView)messageView.findViewById(R.id.message_data)).setText(message.message);
        ((TextView)messageView.findViewById(R.id.message_date)).setText(message.date);

        container.addView(messageView, position);
    }

    public void addMessages(Message[] messages) {
        for (Message message : messages)
            addMessage(message, 0);
    }

    @Override
    public void update(Observable observable, Object o) {
        ObservObject obj = (ObservObject)o;
        switch (obj.type) {
            case "socket":
                addMessage((Message)obj.data, -1);
                break;

            case "messages":
                addMessages((Message[])obj.data);
                break;
        }
    }
}
