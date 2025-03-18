package com.zebra.treadreadersampleintentapi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.zebra.treadreadersampleintentapi.callbacks.ITreadReaderCallBack;

public class FragMore extends Fragment {

    ActivityHome activityHome;

    private GestureDetectorCompat gestureDetector;

    SeekBar scanSpeedSeekBar;

    int scanSpeed = 0;

    public FragMore() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.frag_more, container, false);
        activityHome = (ActivityHome) getActivity();
        scanSpeedSeekBar = v.findViewById(R.id.scanSpeedSeekBar);

        scanSpeedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                switch (i){
                    case 0:
                        activityHome.setParams("reader_timeout", "0002");
                        break;
                    case 1:
                        activityHome.setParams("reader_timeout", "0004");
                        break;
                    case 2:
                        activityHome.setParams("reader_timeout", "0008");
                        break;
                    case 3:
                        activityHome.setParams("reader_timeout", "0010");
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        gestureDetector = new GestureDetectorCompat(activityHome, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(@NonNull MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(@NonNull MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(@NonNull MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onScroll(@NonNull MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
                return false;
            }

            @Override
            public void onLongPress(@NonNull MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(@NonNull MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
                return false;
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        activityHome.registerListener(new ITreadReaderCallBack() {
            @Override
            public void onInitiateReader(int result) {

            }

            @Override
            public void onStateChange(String mFW_STATE, String mFW_CONNECTION_STATE) {

            }

            @Override
            public void onDataReceived(String jsonData) {

            }

            @Override
            public void onParamReceive(String jsonData) {
                Log.e("PARAM",jsonData);
                if (jsonData.contains("0002")){
                    scanSpeedSeekBar.setProgress(0);
                }else if (jsonData.contains("0004")){
                    scanSpeedSeekBar.setProgress(1);
                }else if (jsonData.contains("0008")){
                    scanSpeedSeekBar.setProgress(2);
                }else if (jsonData.contains("0010")){
                    scanSpeedSeekBar.setProgress(3);
                }
            }

            @Override
            public void onVersionReceived(String jsonData) {

            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onException(String exception,String action) {

            }
        });

    }
}
