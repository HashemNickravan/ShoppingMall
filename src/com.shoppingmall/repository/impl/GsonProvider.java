package com.shoppingmall.repository.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonProvider {

    private static final Gson gson =
            new GsonBuilder().setPrettyPrinting().create();

    private GsonProvider() {
    }

    public static Gson get() {
        return gson;
    }
}
