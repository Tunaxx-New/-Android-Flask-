package com.nik.someproject.Fragment.Profile.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nik.someproject.DataManipulation.MyObservable;
import com.nik.someproject.DataManipulation.SavedData;
import com.nik.someproject.DataManipulation.TechSupportUser;
import com.nik.someproject.DataManipulation.User;
import com.nik.someproject.Fragment.Profile.Implementation.UserImplementation;
import com.nik.someproject.Fragment.Profile.Services.IUser;
import com.nik.someproject.R;

import java.util.Map;
import java.util.Observer;

public class ProfileViewModel extends ViewModel {
    IUser userImplementation;
    SavedData savedData;
    MutableLiveData<String> logged = new MutableLiveData<>();
    String error;

    MutableLiveData<String> name = new MutableLiveData<>();
    MutableLiveData<String> surname = new MutableLiveData<>();
    MutableLiveData<String> phone = new MutableLiveData<>();
    MutableLiveData<String> email = new MutableLiveData<>();
    MutableLiveData<Boolean> phone_activated = new MutableLiveData<>();
    MutableLiveData<Boolean> email_activated = new MutableLiveData<>();
    MutableLiveData<String> created = new MutableLiveData<>();

    public ProfileViewModel(SavedData savedData) {
        logged.postValue("");
        name.postValue("");
        surname.postValue("");
        phone.postValue("");
        email.postValue("");
        phone_activated.postValue(false);
        email_activated.postValue(false);
        created.postValue("");

        userImplementation = new UserImplementation();
        this.savedData = savedData;
    }

    public String getError() {
        return error;
    }

    public LiveData<String> getLogged() { return logged; }
    public LiveData<String> getName() { return name; }
    public LiveData<String> getSurname() { return surname; }
    public LiveData<String> getPhone() { return phone; }
    public LiveData<String> getEmail() { return email; }
    public LiveData<Boolean> getIsPhone() { return phone_activated; }
    public LiveData<Boolean> getIsEmail() { return email_activated; }
    public LiveData<String> getCreated() { return created; }

    public void change(String hash, User user) {
        Map<String, String> response = userImplementation.set(hash, user);
    }

    public void get(String hash) {
        Map<String, String> response = userImplementation.get(hash);

        String loggedValue = response.get("Status") + response.get("status");
        if (loggedValue.equals("200false"))
            error = response.get("reason");

        logged.postValue(response.get("Status") + response.get("status"));

        if (response.containsKey("hash"))
        savedData.save(R.string.HASH_KEY, response.get("hash"));

        if (response.containsKey("phone")) {
            name.postValue(response.get("name"));
            surname.postValue(response.get("surname"));
            phone.postValue(response.get("phone"));
            email.postValue(response.get("email"));
            phone_activated.postValue(Boolean.parseBoolean(response.get("phone_activated")));
            email_activated.postValue(Boolean.parseBoolean(response.get("email_activated")));
            created.postValue(response.get("registered"));
        }
    }
}
