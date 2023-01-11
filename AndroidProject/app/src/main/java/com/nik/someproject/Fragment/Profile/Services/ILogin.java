package com.nik.someproject.Fragment.Profile.Services;

import java.util.Map;

public interface ILogin {
    Map<String, String> login(String phone, String password);
}
