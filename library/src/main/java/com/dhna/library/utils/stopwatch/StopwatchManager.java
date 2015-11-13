package com.dhna.library.utils.stopwatch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Stopwatch들을 관리한다.
 */
public class StopwatchManager {

    private File mDirectory;
    private Map<String, Stopwatch> mStopwatchMap = new HashMap<>();

    private static StopwatchManager sInstance;

    public static StopwatchManager getInstance() {
        if (sInstance == null) {
            sInstance = new StopwatchManager();
        }
        return sInstance;
    }

    public Stopwatch getStopwatch(String name) {
        Stopwatch stopwatch = mStopwatchMap.get(name);
        if (stopwatch == null) {
            stopwatch = new Stopwatch(name);
            mStopwatchMap.put(name, stopwatch);
        }
        return stopwatch;
    }

    public void setDirectory(File directory) {
        mDirectory = directory;
    }

    public void release() {
        mStopwatchMap.clear();
    }

    /**
     * directory에 존재하는 파일들로부터 모든 기록을 불러온다.
     * Stopwatch의 이름은 파일명이다.
     */
    public void loadStopwatches() {
        if (mDirectory == null || !mDirectory.exists() || !mDirectory.isDirectory()) {
            return;
        }

        for (File file : mDirectory.listFiles()) {
            loadStopwatch(file);
        }
    }

    /**
     * 파일의 모든 기록을 불러온다.
     *
     * @param file 기록 파일
     */
    public boolean loadStopwatch(File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            return false;
        }

        try {
            Stopwatch stopwatchLoaded = new Gson().fromJson(new JsonReader(new FileReader(file)), Stopwatch.class);

            Stopwatch stopwatch = getStopwatch(file.getName());
            stopwatch.apply(stopwatchLoaded);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * directory에 모든 기록을 파일로 저장한다.
     * 파일의 이름은 Stopwatch의 이름을 이다.
     */
    public void saveStopwatches() {
        if (mDirectory == null || mDirectory.isFile()) {
            return;
        }

        if (!mDirectory.exists()) {
            mDirectory.mkdirs();
        }

        for (Stopwatch stopwatch : mStopwatchMap.values()) {
            saveStopwatch(stopwatch);
        }
    }

    public boolean saveStopwatch(Stopwatch stopwatch) {
        File file = new File(mDirectory, stopwatch.getName());
        try {
            stopwatch.compute();
            String json = new GsonBuilder().setPrettyPrinting().create().toJson(stopwatch);

            OutputStreamWriter writer = new OutputStreamWriter(
                    new BufferedOutputStream(new FileOutputStream(file)), "UTF-8");
            writer.write(json);
            writer.flush();
            writer.close();
            return true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
