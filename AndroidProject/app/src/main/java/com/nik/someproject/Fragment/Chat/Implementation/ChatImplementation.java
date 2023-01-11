package com.nik.someproject.Fragment.Chat.Implementation;

import com.nik.someproject.Constants;
import com.nik.someproject.Fragment.Chat.Services.IGetMessage;
import com.nik.someproject.Fragment.Chat.Services.ISendMessage;
import com.nik.someproject.HttpManager.Implementation.HttpManager;
import com.nik.someproject.HttpManager.Services.IRequest;

import java.util.HashMap;
import java.util.Map;

public class ChatImplementation implements IGetMessage, ISendMessage {
    IRequest request;
    HashMap<String, String> prop;
    HashMap<String, String> param;

    public ChatImplementation() {
        request = new HttpManager();
        prop = new HashMap<String, String>();
        param = new HashMap<String, String>();
        prop.put("Content-Type", "application/x-www-form-urlencoded");
        prop.put("charset", "utf-8");
        prop.put("Accept", "*/*");
        prop.put("Accept-Encoding", "gzip, deflate, br");
        prop.put("Connection", "keep-alive");
    }

    @Override
    public Map<String, String> get(String hash, int to, int start, int end) {
        param.put("hash", hash);
        param.put("to_id", Integer.toString(to));
        param.put("start", Integer.toString(start));
        param.put("end", Integer.toString(end));
        return request.request(Constants.host + Constants.get_messages_route, param, prop, "POST");
    }

    @Override
    public Map<String, String> send(String data, String hash, int to_id) {
        return null;
    }
}
