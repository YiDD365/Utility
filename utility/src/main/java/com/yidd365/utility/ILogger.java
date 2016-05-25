package com.yidd365.utility;

import android.util.Log;

/**
 * Created by orinchen on 16/5/24.
 */
public interface ILogger {
    void log(String message);

    /** A {@link ILogger} defaults output appropriate for the current platform. */
    ILogger DEFAULT = new ILogger() {
        @Override public void log(String message) {
            Log.d("Tag", message);
        }
    };
}
