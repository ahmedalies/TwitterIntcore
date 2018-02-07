package com.free.ahmed.twitterintcore;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ahmed on 2/7/2018.
 */

public interface Api {
    @GET("/1.1/followers/list.json")
    Call<ResponseBody> list(@Query("user_id") long id, @Query("skip_status") int skip_status, @Query("count") int count);
    Call<ResponseBody> nextCursor(@Query("user_id") long id, @Query("skip_status") int skip_status,
                                  @Query("count") int count, @Query("cursor") String nextCursor);
}
