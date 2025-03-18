package com.zebra.treadreadersampleintentapi.callbacks;

public interface ITreadReaderCallBack {

    void onInitiateReader(int result);
    void onStateChange(String mFW_STATE,String mFW_CONNECTION_STATE);
    void onDataReceived(String jsonData);
    void onParamReceive(String jsonData);
    void onVersionReceived(String jsonData);
    void onError(String error);
    void onException(String exception,String action);
}
