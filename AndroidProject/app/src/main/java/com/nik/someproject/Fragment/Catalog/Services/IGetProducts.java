package com.nik.someproject.Fragment.Catalog.Services;

import java.util.Map;

public interface IGetProducts {
    Map<String, String> get(int category_id);
}
