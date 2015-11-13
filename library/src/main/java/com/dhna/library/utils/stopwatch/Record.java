package com.dhna.library.utils.stopwatch;

import com.google.gson.annotations.SerializedName;

/**
 * 하나의 시작과 종료 시간의 기록이다.
 */
public class Record {

    @SerializedName("start")
    private long mStartTimeMillis;

    @SerializedName("end")
    private long mEndTimeMillis;

    @SerializedName("duration")
    private long mDuration;

    public long getStartTimeMillis() {
        return mStartTimeMillis;
    }

    public void setStartTimeMillis(long startTimeMillis) {
        mStartTimeMillis = startTimeMillis;
    }

    public long getEndTimeMillis() {
        return mEndTimeMillis;
    }

    public void setEndTimeMillis(long endTimeMillis) {
        mEndTimeMillis = endTimeMillis;
        mDuration = duration();
    }

    public int duration() {
        return Math.max((int) (mEndTimeMillis - mStartTimeMillis), 0);
    }
}
