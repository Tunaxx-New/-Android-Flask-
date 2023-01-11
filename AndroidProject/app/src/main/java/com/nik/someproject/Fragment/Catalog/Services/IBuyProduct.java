package com.nik.someproject.Fragment.Catalog.Services;

import java.util.Map;

public interface IBuyProduct {
    Map<String, String> buy(int id, int count, String hash);
}
