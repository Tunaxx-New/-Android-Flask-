package com.nik.someproject.Fragment.Home.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nik.someproject.DataManipulation.MyObservable;
import com.nik.someproject.DataManipulation.ObservObject;
import com.nik.someproject.Fragment.Home.Implementation.GetContent;
import com.nik.someproject.Fragment.Home.Services.IGetContent;

import java.util.Map;
import java.util.Observer;

public class HomeViewModel {
    MyObservable subject;
    IGetContent getContent;
    MutableLiveData<Boolean> isError = new MutableLiveData<>();

    public HomeViewModel(Observer observer) {
        subject = new MyObservable();
        subject.addObserver(observer);
        getContent = new GetContent();
    }

    public LiveData<Boolean> getError() { return isError; }

    public void getSliderContent() {
        Map<String, String> response = getContent.getSliderImages();
        if (response.get("Status").equals("-1"))
            isError.postValue(true);

        if (response.containsKey("image_names")) {
            String[] names = response.get("image_names").toString().split(",");
            ObservObject o = new ObservObject();
            o.type = "slider";
            o.data = names;
            subject.notifyObservers(o);
        }
    }

    public void getGridContent() {
        Map<String, String> response = getContent.getStocksImages();
        if (response.get("Status").equals("-1"))
            isError.postValue(true);

        if (response.containsKey("image_names")) {
            String[] names = response.get("image_names").toString().split(",");
            ObservObject o = new ObservObject();
            o.type = "grid";
            o.data = names;
            subject.notifyObservers(o);
        }
    }
}
