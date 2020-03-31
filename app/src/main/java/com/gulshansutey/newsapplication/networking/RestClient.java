package com.gulshansutey.newsapplication.networking;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.URLUtil;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/** RestClient */
public final class RestClient <T>{


    private final String TAG = RestClient.class.getName();
    private int requestCode;
    private String url = null;
    private String method = METHOD.GET.name;


    /**
     * set url
     */
    public RestClient setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * set method type
     */
    public RestClient setMethod(METHOD method) {
        this.method = method.name;
        return this;
    }

    /**
     * set a response code associated with the api call
     */
    public RestClient setRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    /**
     * Executes the RestClient in a background thread and delivers the callbacks
     *
     * @param callback response callbacks,
     * @param type     Object type
     */
    public void execute(final Class<T> type,final Callback<T> callback) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    final String s = makeRequest();
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.v(TAG,"Response - "+s);

                            callback.onSuccess(new Gson().fromJson(s, type), requestCode);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailure(new Throwable(e.getLocalizedMessage()), requestCode);
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    /**
     * Makes api call Creates a {@code URL} object from the {@code String} url
     *
     * @return {@link String server_response}
     */
    private String makeRequest() throws Exception {
        //Check is url is valid
        if (url == null || url.isEmpty() || !URLUtil.isValidUrl(url)) {
            throw new NullPointerException("URL is empty or invalid :" + url);
        }

        URL url = new URL(this.url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        // setting the  Request Method Type
        httpURLConnection.setRequestMethod(this.method);
        // adding the headers for request
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        try {
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String server_response = readStream(httpURLConnection.getInputStream());
                Log.v(TAG, server_response);
                return server_response;
            } else {
                Log.v(TAG, "Response Code - "+responseCode);
                return null;
            }
        } finally {
            httpURLConnection.disconnect();
        }
    }

    /**
     * @param in Byte InputStream of client response
     * @return a response as string after appending it line by line from BufferedReader
     */
    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder response = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return response.toString();
    }

    public enum METHOD {
        POST("POST"),
        GET("GET");

        private final String name;

        METHOD(String s) {
            name = s;
        }

    }


    /**
     * Callbacks for the client response
     */
    public interface Callback<T> {
        void onSuccess(T t, int code);

        void onFailure(Throwable throwable, int code);
    }

}



