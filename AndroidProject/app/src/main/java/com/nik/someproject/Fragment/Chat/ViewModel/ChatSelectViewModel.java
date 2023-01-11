package com.nik.someproject.Fragment.Chat.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nik.someproject.DataManipulation.Message;
import com.nik.someproject.DataManipulation.MyObservable;
import com.nik.someproject.DataManipulation.ObservObject;
import com.nik.someproject.DataManipulation.TechSupportUser;
import com.nik.someproject.Fragment.Chat.Implementation.GetTechsupports;
import com.nik.someproject.Fragment.Chat.Services.IGetTechsupports;

import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class ChatSelectViewModel {
    ArrayList<Integer> tech_ids;
    MyObservable subject;
    IGetTechsupports getTechsupports;

    MutableLiveData<Boolean> isError = new MutableLiveData<>();

    public ChatSelectViewModel(Observer observer) {
        tech_ids = new ArrayList<Integer>();
        getTechsupports = new GetTechsupports();
        subject = new MyObservable();
        subject.addObserver(observer);
        isError.postValue(false);
    }

    public LiveData<Boolean> getError() { return isError; }

    public void get(String uhash) {
        Map<String, String> response = getTechsupports.get(uhash);

        if (response.get("Status").equals("-1")) {
            isError.postValue(true);
            return;
        }

        if (response.get("full_names") != null) {
            String[] full_names = response.get("full_names").toString().split(",");
            String[] ids = response.get("ids").toString().split(",");

            TechSupportUser[] users = new TechSupportUser[ids.length];
            for (int i = 0; i < users.length; i++) {
                users[i] = new TechSupportUser();
                users[i].id = Integer.parseInt(ids[i]);
                users[i].fullName = full_names[i];
            }

            subject.notifyObservers(users);
        }
    }
}
