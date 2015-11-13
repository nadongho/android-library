package com.dhna.library.utils.stopwatch;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 시작 시간과 종료 시간을 기록하고, 두 시간의 간격을 측정해준다.
 */
public class Stopwatch {


    @SerializedName("summary")
    private Summary mSummary = new Summary.Builder().build();

    @SerializedName("records")
    private List<Record> mRecords = new ArrayList<>();

    private transient String mName;
    private transient Record mNowRecord;
    private transient boolean mIsDirty = false;
    private transient int mMaxCount = Integer.MAX_VALUE;

    public Stopwatch(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public Summary getSummary() {
        compute();
        return mSummary;
    }

    public void compute() {
        if (mIsDirty) {
            mIsDirty = false;

            long total = 0;
            int count = mRecords.size();
            for (Record record : mRecords) {
                total += record.duration();
            }

            mSummary = new Summary.Builder()
                    .setTotal(total)
                    .setCount(count)
                    .setAverage(total > 0 ? total / count : 0)
                    .build();
        }
    }

    public Iterator<Record> getIteratorOfRecords() {
        return mRecords.iterator();
    }

    public void setMaxCount(int max) {
        mMaxCount = max;
    }

    public void apply(Stopwatch stopwatch) {
        mIsDirty = true;

        mRecords.clear();
        mRecords.addAll(stopwatch.mRecords.subList(0, mMaxCount));
    }

    public synchronized void start() {
        if (isMax()) {
            return;
        }

        mNowRecord = new Record();
        mNowRecord.setStartTimeMillis(System.currentTimeMillis());
    }

    public synchronized void end() {
        if (mNowRecord == null) {
            return;
        }

        mIsDirty = true;

        mNowRecord.setEndTimeMillis(System.currentTimeMillis());
        mRecords.add(mNowRecord);
        mNowRecord = null;
    }

    private boolean isMax() {
        if (mRecords.size() >= mMaxCount) {
            return true;
        }
        return false;
    }

    public void clear() {
        mRecords.clear();
    }
}
