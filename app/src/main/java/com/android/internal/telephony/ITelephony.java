package com.android.internal.telephony;

/**
 * Created by RSharma on 6/28/2017.
 */

public interface ITelephony
{
    boolean endCall();

    void answerRingingCall();

    void silenceRinger();
}
