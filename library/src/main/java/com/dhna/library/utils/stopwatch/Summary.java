package com.dhna.library.utils.stopwatch;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * 시간 기록들의 모음이다.
 */
public class Summary {
    @SerializedName("total")
    private long mTotal;

    @SerializedName("count")
    private long mCount;

    @SerializedName("average")
    private long mAverage;


    public static class Builder {
        private long total;
        private long count;
        private long average;

        public Builder setTotal(long total) {
            this.total = total;
            return this;
        }

        public Builder setCount(long count) {
            this.count = count;
            return this;
        }

        public Builder setAverage(long average) {
            this.average = average;
            return this;
        }

        public Summary build() {
            return new Summary(this);
        }
    }

    public Summary(Builder builder) {
        mTotal = builder.total;
        mCount = builder.count;
        mAverage = builder.average;
    }

    public long getTotal() {
        return mTotal;
    }

    public long getCount() {
        return mCount;
    }

    public long getAverage() {
        return mAverage;
    }
}
