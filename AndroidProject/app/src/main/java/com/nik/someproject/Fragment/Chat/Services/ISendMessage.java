package com.nik.someproject.Fragment.Chat.Services;

import java.util.Map;

public interface ISendMessage {
    Map<String, String> send(String data, String hash, int to_id);
}
