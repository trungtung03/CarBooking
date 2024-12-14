package com.example.carbooking.utils;

import android.app.Application;

import com.example.carbooking.models.UserObject;
import com.example.carbooking.network.NetworkCall;
import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CustomApplication extends Application {

    private Gson gson;
    private GsonBuilder builder;
    private CustomSharedPreference shared;
    private NetworkCall networkCall;
    public static String type;
    public static String uid;

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);

        builder = new GsonBuilder();
        gson = builder.create();
        shared = new CustomSharedPreference(getApplicationContext());
        networkCall = new NetworkCall(getApplicationContext());
    }

    public CustomSharedPreference getShared() {
        return shared;
    }

    public Gson getGsonObject() {
        return gson;
    }

    public NetworkCall getNetworkCall() {
        return networkCall;
    }

    /**
     * Get current logged in user
     * @return
     */
    public UserObject getLoginUser() {
        Gson mGson = getGsonObject();
        String storedUser = getShared().getUserData();
        return mGson.fromJson(storedUser, UserObject.class);
    }
}
