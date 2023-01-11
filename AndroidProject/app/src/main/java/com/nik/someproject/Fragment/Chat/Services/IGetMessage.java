package com.nik.someproject.Fragment.Chat.Services;

import java.util.Map;

public interface IGetMessage {
    Map<String, String> get(String hash, int to, int start, int end);
}
