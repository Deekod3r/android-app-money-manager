package com.project.hucemoney.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.project.hucemoney.entities.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkUtils {
    private static final String BASE_URL = "http://api.example.com";
    private static final OkHttpClient client = new OkHttpClient();

    public static void request(String method, String url, RequestBody body, Callback callback, boolean isAbsolute) {
        Request.Builder builder = new Request.Builder()
                .url(isAbsolute ? url : getAbsoluteUrl(url));
        switch (method) {
            case "GET":
                builder.get();
                break;
            case "POST":
                builder.post(body);
                break;
            case "PUT":
                builder.put(body);
                break;
            case "DELETE":
                builder.delete(body);
                break;
        }
        Request request = builder.build();
        client.newCall(request).enqueue(callback);
    }

    public static void request(String method, String url, Map<String, String> params, Callback callback, boolean isAbsolute) {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        RequestBody body = builder.build();
        request(method, url, body, callback, isAbsolute);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public void test() {
        // Tạo một đối tượng RequestBody từ một chuỗi JSON
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        //String json = "{\"username\":\"your_username\",\"password\":\"your_password\"}";
        Gson gson = new Gson();
        User myObject = new User();
        String json = gson.toJson(myObject);
        RequestBody body = RequestBody.create(json, JSON);

        // Gửi yêu cầu và xử lý phản hồi
        NetworkUtils.request("POST", "https://example.com/api/login", body, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // Xử lý phản hồi trả về
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // Xử lý lỗi
            }

        }, true);


        // Tạo một Map chứa các tham số yêu cầu
        Map<String, String> params = new HashMap<>();
        params.put("username", "your_username");
        params.put("password", "your_password");

        // Gửi yêu cầu và xử lý phản hồi
        NetworkUtils.request("POST", "https://example.com/api/login", params, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // Xử lý lỗi
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // Xử lý phản hồi thành công
            }
        }, true);

    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (networkCapabilities != null) {
                return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
            }
        }
        return false;
    }
}



