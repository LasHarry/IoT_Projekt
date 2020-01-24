package com.imapro.iotprototype;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RestApi {

    @GET("nr")
    Call<ResponseBody> getUsers();
}
