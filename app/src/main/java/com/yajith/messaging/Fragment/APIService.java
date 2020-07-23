package com.yajith.messaging.Fragment;

import com.yajith.messaging.Notification.MyResponse;
import com.yajith.messaging.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                "Content-Type:application/json",
                "Authorization:key=AAAAuM4bNnk:APA91bHF49orkgDaMnBdFzdM5PW3R_PStr-tLt0Rn3e5Vy31P5wDNJhPZBuwYPApWi7nmlBWZhmjoPguF_ksrWddRlx6t7CuSbkThiQ-vh8dBoS6wSUbxleuuIOw3InD2HOOHCUz2uKk",

            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
