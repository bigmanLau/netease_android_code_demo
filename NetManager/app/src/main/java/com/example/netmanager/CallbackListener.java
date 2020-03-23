package com.example.netmanager;

import java.io.InputStream;

public interface CallbackListener {
    void onSuccess(InputStream inputStream);
    void onFailure();
}
