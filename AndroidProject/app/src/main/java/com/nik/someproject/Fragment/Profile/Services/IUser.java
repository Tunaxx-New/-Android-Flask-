package com.nik.someproject.Fragment.Profile.Services;

import com.nik.someproject.DataManipulation.User;

import java.util.Map;

public interface IUser {
    Map<String, String> get(String hash);
    Map<String, String> set(String hash, User user);
}
