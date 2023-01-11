package com.nik.someproject.DataManipulation;

import java.util.Observable;

public class MyObservable extends Observable {
    @Override
    public boolean hasChanged() {
        return true;
    }
}
