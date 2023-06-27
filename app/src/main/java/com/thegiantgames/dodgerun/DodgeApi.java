package com.thegiantgames.dodgerun;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface DodgeApi {




    @GET("tip")
    Call<JsonObject> getTip();


    @GET("scores")
    Call<JsonObject> getScores();



    @POST("characters")
    Call<JsonObject>  createPost(@Body JsonObject object);




}
