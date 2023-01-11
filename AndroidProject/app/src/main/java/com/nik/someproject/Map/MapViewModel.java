package com.nik.someproject.Map;

import com.nik.someproject.Constants;
import com.nik.someproject.DataManipulation.MyObservable;
import com.nik.someproject.HttpManager.Implementation.HttpRequest;

import java.util.Map;
import java.util.Observer;

public class MapViewModel {
    MyObservable subject;
    public MapViewModel(Observer observer) {
        subject = new MyObservable();
        subject.addObserver(observer);
    }

    public void getShopPositions() {
        HttpRequest request = new HttpRequest();
        Map<String, String> response = request.send(Constants.host + Constants.get_shop_positions, "GET");

        if (response.containsKey("status") && response.get("status").equals("true")) {
            String[] mArrayString = response.get("magnitudes").split(",");
            String[] lArrayString = response.get("longitudes").split(",");
            double[][] data = new double[2][mArrayString.length];

            for (int i = 0; i < mArrayString.length; i++) {
                data[0][i] = Double.parseDouble(mArrayString[i]);
                data[1][i] = Double.parseDouble(lArrayString[i]);
            }

            subject.notifyObservers(data);
        }
    }
}
