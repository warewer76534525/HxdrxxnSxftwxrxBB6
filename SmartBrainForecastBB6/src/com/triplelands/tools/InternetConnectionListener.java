package com.triplelands.tools;

import java.io.InputStream;

public interface InternetConnectionListener {

    public void onReceiveResponseEvent(InputStream is);

    public void onErrorOccurEvent(Exception e);

    public void onCancelEvent();

    public void onStartEvent(long length, String type);

    public void onNotFound();
}
