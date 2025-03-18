package com.zebra.treadreadersampleintentapi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zebra.treadreadersampleintentapi.callbacks.ITreadReaderCallBack;
import com.zebra.treadreadersampleintentapi.models.TreadReaderVersions;


public class FragmentVersions extends Fragment {

    ActivityHome activityHome;

    TextView tvScanCount,tvSerialNumber,tvHwVersion,tvFwVersion,tvAlgorithmVersion,tvHwAccessoryVersion,tvServiceVersions,tvSdkVersions;

    public FragmentVersions() {
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
        View view = inflater.inflate(R.layout.fragment_versions, container, false);
        tvScanCount = view.findViewById(R.id.tvScanCount);
        tvSerialNumber = view.findViewById(R.id.tvSerialNumber);
        tvHwVersion = view.findViewById(R.id.tvHwVersion);
        tvFwVersion = view.findViewById(R.id.tvFwVersion);
        tvAlgorithmVersion = view.findViewById(R.id.tvAlgorithmVersion);
        tvHwAccessoryVersion = view.findViewById(R.id.tvHwAccessoryVersion);
        tvServiceVersions = view.findViewById(R.id.tvServiceVersions);
        tvSdkVersions = view.findViewById(R.id.tvSdkVersions);
        activityHome = (ActivityHome) getActivity();

        tvSerialNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityHome.showImageCount++;
            }
        });

        return view;
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

            }

            @Override
            public void onVersionReceived(String jsonData) {
                setDataForUi(jsonData);
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onException(String exception,String action) {

            }
        });

    }


    private void setDataForUi(String jsonData){
        Log.e("Data",jsonData);
        Gson gson = new Gson();
        com.zebra.treadreadersampleintentapi.models.TreadReaderVersions treadReaderVersions  = gson.fromJson(jsonData, TreadReaderVersions.class);
        tvScanCount.setText("0");
        tvSerialNumber.setText("---");
        tvHwVersion.setText(treadReaderVersions.getHardwareVersion());
        tvFwVersion.setText(treadReaderVersions.getFirmwareVersion());
        tvAlgorithmVersion.setText(treadReaderVersions.getAlgorithmVersion());
        tvHwAccessoryVersion.setText(treadReaderVersions.getHardwareaccessoryVersion());
        tvServiceVersions.setText(treadReaderVersions.getServiceVersion());
        tvSdkVersions.setText(treadReaderVersions.getSdkVersion());
        tvScanCount.setText(treadReaderVersions.getCount());
        tvSerialNumber.setText(treadReaderVersions.getSerialNumber());
    }
}