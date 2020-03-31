package com.gulshansutey.newsapplication.networking;

public interface RestCallbacks<T>  {

    void onSuccess(T t,int code);
    void onFailure(Throwable throwable,int code);

}
