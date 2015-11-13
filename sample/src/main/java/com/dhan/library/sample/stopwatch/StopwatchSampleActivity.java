package com.dhan.library.sample.stopwatch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.dhan.library.sample.R;
import com.dhna.library.utils.stopwatch.Record;
import com.dhna.library.utils.stopwatch.Stopwatch;
import com.dhna.library.utils.stopwatch.StopwatchManager;
import com.dhna.library.utils.stopwatch.Summary;

import java.io.File;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StopwatchSampleActivity extends AppCompatActivity {

    private static final String STOPWATCH_NAME = "stopwatch_sample";

    @Bind(R.id.start_end_buton)
    Button mStartEndButton;

    @Bind(R.id.load_button)
    Button mLoadButton;

    @Bind(R.id.save_button)
    Button mSaveButton;

    @Bind(R.id.history_textview)
    TextView mHistoryTextView;

    @BindString(R.string.start)
    String mStartLabel;

    @BindString(R.string.end)
    String mEndLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sample);
        ButterKnife.bind(this);

        File directory = new File(getExternalCacheDir().getParentFile(), "stopwatch");
        StopwatchManager.getInstance().setDirectory(directory);
        StopwatchManager.getInstance().getStopwatch(STOPWATCH_NAME).setMaxCount(10);
    }

    @OnClick(R.id.start_end_buton)
    void onStartEndButtonClicked() {
        if (mStartEndButton.isSelected()) {
            mStartEndButton.setSelected(false);
            mStartEndButton.setText(mStartLabel);
            StopwatchManager.getInstance().getStopwatch(STOPWATCH_NAME).end();

            printAll();
        } else {
            mStartEndButton.setSelected(true);
            mStartEndButton.setText(mEndLabel);
            StopwatchManager.getInstance().getStopwatch(STOPWATCH_NAME).start();
        }
    }

    @OnClick(R.id.load_button)
    void onLoadButtonClicked() {
        StopwatchManager.getInstance().loadStopwatches();
        printAll();
    }

    @OnClick(R.id.save_button)
    void onSaveButtonClicked() {
        StopwatchManager.getInstance().saveStopwatches();
        printAll();
    }

    private void printAll() {
        StringBuilder builder = new StringBuilder();

        Stopwatch stopwatch = StopwatchManager.getInstance().getStopwatch(STOPWATCH_NAME);
        Summary summary = stopwatch.getSummary();

        builder.append("total: " + summary.getTotal() + "\n");
        builder.append("count: " + summary.getCount() + "\n");
        builder.append("average: " + summary.getAverage() + "\n");

        Iterator<Record> iterator = stopwatch.getIteratorOfRecords();
        while (iterator.hasNext()) {
            builder.append(toString(iterator.next()));
            builder.append("\n");
        }

        mHistoryTextView.setText(builder.toString());
    }

    private String toString(Record record) {
        return "[" + record.getStartTimeMillis() + ", " + record.getEndTimeMillis() + "] --> " + record.duration();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StopwatchManager.getInstance().release();
    }
}
