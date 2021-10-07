package com.credit.backup.api;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {
    @GET("VGOL")
    Call<List<Post>> getPosts();
}
