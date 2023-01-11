package com.nik.someproject.Fragment.Chat.ViewModel;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nik.someproject.DataManipulation.Message;
import com.nik.someproject.DataManipulation.MyObservable;
import com.nik.someproject.DataManipulation.ObservObject;
import com.nik.someproject.DataManipulation.SavedData;
import com.nik.someproject.Fragment.Chat.Implementation.ChatForegroundService;
import com.nik.someproject.Fragment.Chat.Implementation.ChatImplementation;
import com.nik.someproject.Fragment.Chat.Services.IGetMessage;
import com.nik.someproject.Fragment.Profile.Implementation.LoginImplementation;
import com.nik.someproject.Fragment.Profile.Services.ILogin;
import com.nik.someproject.HttpManager.Implementation.SocketManager;
import com.nik.someproject.HttpManager.Services.ISocket;
import com.nik.someproject.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class ChatViewModel extends ViewModel {
    Activity mainActivity;
    SavedData savedData;
    MyObservable subject;

    int tech_id;
    int start = 1;
    int end = 10;

    MutableLiveData<Boolean> isError = new MutableLiveData<>();

    IGetMessage getMessage;
    ISocket socket;

    public ChatViewModel(Activity mainActivity, SavedData savedData, int tech_id, int start, int end, Observer observer) {
        getMessage = new ChatImplementation();
        this.mainActivity = mainActivity;
        this.savedData = savedData;

        this.tech_id = tech_id;
        this.start = start;
        this.end = end;

        subject = new MyObservable();
        subject.addObserver(observer);

        socket = new SocketManager(mainActivity);
        socket.connect();

        isError.postValue(false);
    }

    public void addMessageObserver(Observer observer) {
        socket.addObserver(observer);
    }

    public void joinServer() {
        socket.setUhash(savedData.get(R.string.HASH_KEY));
        socket.join();
    }

    public void leaveServer() {
        socket.setUhash(savedData.get(R.string.HASH_KEY));
        socket.leave();
    }

    public void sendServer(String message) {
        socket.setUhash(savedData.get(R.string.HASH_KEY));
        socket.message(message, tech_id);
    }

    public LiveData<Boolean> getError() { return isError; }

    public void get(String hash) {
        Map<String, String> response = getMessage.get(hash, tech_id, start, end);

        if (response.get("Status").equals("-1")) {
            isError.postValue(true);
            return;
        }

        int count = end - start + 1;
        start += count;
        end += count;

        if (response.get("froms") != null) {
            String[] from_strings = response.get("froms").toString().split(",");
            String[] msgs = response.get("messages").toString().split(",");
            String[] dates = response.get("dates").toString().split(",");
            Log.i("LOL", from_strings.length + " ");

            Message[] messages = new Message[from_strings.length];
            for (int i = 0; i < messages.length; i++) {
                if (from_strings[i] == "")
                    return;
                messages[i] = new Message();
                messages[i].user_id = Integer.parseInt(from_strings[i]);
                messages[i].message = msgs[i];
                messages[i].date = dates[i];
            }

            ObservObject o = new ObservObject();
            o.type = "messages";
            o.data = messages;
            subject.notifyObservers(o);
        }
    }
}
