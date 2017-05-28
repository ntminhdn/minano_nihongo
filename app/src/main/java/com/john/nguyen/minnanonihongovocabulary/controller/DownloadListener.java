package com.john.nguyen.minnanonihongovocabulary.controller;

/**
 * Created by Administrator on 18/03/2017.
 */

public interface DownloadListener {
    public static int STATUS_NONE = 0;
    public static int STATUS_FINISHED = 1;
    public static int STATUS_ERROR = 2;
    public static int STATUS_MINIMIZE = 3;
    public static int STATUS_EXIT = 4;

    public void onStatus(int status);
}
