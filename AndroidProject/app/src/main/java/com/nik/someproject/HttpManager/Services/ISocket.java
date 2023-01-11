package com.nik.someproject.HttpManager.Services;

import java.util.Observer;

public interface ISocket {
    void connect();
    void join();
    void leave();
    void message(String message, int to_id);
    void setUhash(String uhash);
    void addObserver(Observer observer);
}
