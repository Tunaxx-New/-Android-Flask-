package com.nik.someproject.HttpManager.Implementation;

import android.app.Activity;
import android.util.Log;

import com.nik.someproject.Constants;
import com.nik.someproject.DataManipulation.Message;
import com.nik.someproject.DataManipulation.MyObservable;
import com.nik.someproject.DataManipulation.ObservObject;
import com.nik.someproject.HttpManager.Services.ISocket;

import java.net.URISyntaxException;
import java.util.Observable;
import java.util.Observer;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketManager implements ISocket {
    Socket socket;
    String uhash;
    Activity activity;
    MyObservable subject;

    public SocketManager(Activity activity) {
        this.activity = activity;
        subject = new MyObservable();

        Thread reconnect = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (socket != null) {
                        socket.disconnect();
                        socket.connect();
                    }
                    try {
                        Thread.sleep(50000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        reconnect.start();
    }

    public void addObserver(Observer observer) {
        subject.addObserver(observer);
    }

    @Override
    public void connect() {
        try {
            String uri = Constants.socket_protocol + "://" + Constants.socket_ip + ":" + Constants.socket_port;
            socket = IO.socket(uri);
            socket.connect();
            socket.on("connect", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    if (uhash != null)
                        socket.emit("join", uhash);
                }
            });
            socket.on("message", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Message message = new Message();
                    message.message = args[0].toString();
                    message.user_id = (int)args[1];
                    message.date = args[2].toString();

                    ObservObject o = new ObservObject();
                    o.type = "socket";
                    o.data = message;
                    join();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            subject.notifyObservers(o);
                        }
                    });
                }
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void join() {
        if (uhash != null && socket != null && socket.connected())
            socket.emit("join", uhash);
    }

    @Override
    public void leave() {
        if (uhash != null && socket != null && socket.connected())
            socket.emit("leave", uhash);
    }

    @Override
    public void message(String message, int to_id) {
        if (uhash != null && socket != null && socket.connected())
            socket.emit("message", uhash, message, to_id);
    }

    public void setUhash(String uhash) {
        this.uhash = uhash;
    }

    @Override
    protected void finalize() throws Throwable {
        leave();
        socket.disconnect();
        super.finalize();
    }
}
