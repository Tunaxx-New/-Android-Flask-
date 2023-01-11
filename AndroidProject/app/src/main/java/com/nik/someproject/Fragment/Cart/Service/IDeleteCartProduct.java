package com.nik.someproject.Fragment.Cart.Service;

import java.util.Map;

public interface IDeleteCartProduct {
    Map<String, String> delete(String hash, int id);
    Map<String, String> deleteAll(String hash);
}
