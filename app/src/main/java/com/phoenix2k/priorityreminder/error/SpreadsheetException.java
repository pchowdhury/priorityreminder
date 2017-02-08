package com.phoenix2k.priorityreminder.error;

/**
 * Created by Pushpan on 08/02/17.
 */

public class SpreadsheetException extends Exception {
    private String mMessage;

    public SpreadsheetException(String message) {
        this.mMessage = message;
    }

    @Override
    public String getMessage() {
        return mMessage;
    }
}
