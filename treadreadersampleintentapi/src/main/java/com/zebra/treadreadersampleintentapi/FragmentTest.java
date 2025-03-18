package com.zebra.treadreadersampleintentapi;

import static com.zebra.treadreadersampleintentapi.util.Constants.NEUTRON_DEPTH_HIGH;
import static com.zebra.treadreadersampleintentapi.util.Constants.NEUTRON_DEPTH_LOW;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.zebra.treadreadersampleintentapi.callbacks.ITreadReaderCallBack;
import com.zebra.treadreadersampleintentapi.models.ScanData;
import com.zebra.treadreadersampleintentapi.adapter.ScanDataAdapter;
import com.zebra.treadreadersampleintentapi.models.TreadData;
import com.zebra.treadreadersampleintentapi.models.TreadDataList;
import com.zebra.treadreadersampleintentapi.models.TreadReaderVersions;
import com.zebra.treadreadersampleintentapi.util.Constants;
import com.zebra.treadreadersampleintentapi.util.Utils;

import java.util.ArrayList;
import java.util.Objects;

public class FragmentTest extends Fragment{

    private static final String TAG = FragmentTest.class.getName();
    ActivityHome activityHome;
    ArrayList<ScanData> scanData = new ArrayList<>();
    Button btnMeasure;
    private BottomSheetBehavior<View> mBottomSheetBehavior = null;
    EditText etTester,etTire;
    ImageView btnTesterClear,btnTireClear,btnBackDrop;
    TextView tvInnerPlunger,tvCenterPlunger,tvOuterPlunger;
    TextView tvInnerDepth,tvCenterDepth,tvOuterDepth,tvTireId;
    ImageView indicatorInner1,indicatorInner2,indicatorInner3,indicatorCenter1,indicatorCenter2,indicatorCenter3,indicatorOuter1,indicatorOuter2,indicatorOuter3;
    SeekBar seekBarInner,seekBarCenter,seekBarOuter;
    RelativeLayout lytTestData,lytScanData;
    TreadReaderVersions treadReaderVersions;
    RecyclerView rvScanData;
    ScanDataAdapter adapter;
    Gson gson = new Gson();

    private boolean saveImageParamSet = false;
    boolean mIsDataAcquiring = false;
    public FragmentTest() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setUpViews(View view){
        btnMeasure = view.findViewById(R.id.btnMeasure);
        etTester = view.findViewById(R.id.etTester);
        etTire = view.findViewById(R.id.etTire);
        btnTesterClear = view.findViewById(R.id.btnTesterClear);
        btnTireClear = view.findViewById(R.id.btnTireClear);
        seekBarInner = view.findViewById(R.id.seekBarInner);
        seekBarCenter = view.findViewById(R.id.seekBarCenter);
        seekBarOuter = view.findViewById(R.id.seekBarOuter);
        tvInnerPlunger = view.findViewById(R.id.tvInnerPlunger);
        tvCenterPlunger = view.findViewById(R.id.tvCenterPlunger);
        tvOuterPlunger = view.findViewById(R.id.tvOuterPlunger);
        btnBackDrop = view.findViewById(R.id.btnBackDrop);
        lytTestData = view.findViewById(R.id.lytTestData);
        lytScanData = view.findViewById(R.id.lytScanData);
        rvScanData = view.findViewById(R.id.rvScanData);

        tvTireId = view.findViewById(R.id.tvTireId);
        tvInnerDepth = view.findViewById(R.id.tvInnerDepth);
        tvCenterDepth = view.findViewById(R.id.tvCenterDepth);
        tvOuterDepth = view.findViewById(R.id.tvOuterDepth);
        indicatorInner1 = view.findViewById(R.id.indicatorInner1);
        indicatorInner2 = view.findViewById(R.id.indicatorInner2);
        indicatorInner3 = view.findViewById(R.id.indicatorInner3);
        indicatorCenter1 = view.findViewById(R.id.indicatorCenter1);
        indicatorCenter2 = view.findViewById(R.id.indicatorCenter2);
        indicatorCenter3 = view.findViewById(R.id.indicatorCenter3);
        indicatorOuter1 = view.findViewById(R.id.indicatorOuter1);
        indicatorOuter2 = view.findViewById(R.id.indicatorOuter2);
        indicatorOuter3 = view.findViewById(R.id.indicatorOuter3);
    }

    private void setupListeners(){
        seekBarInner.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvInnerPlunger.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarCenter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvCenterPlunger.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarOuter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvOuterPlunger.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnTesterClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etTester.setText("");
            }
        });

        btnTireClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etTire.setText("");
            }
        });

        btnMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etTester.getText().toString().isEmpty()){
                    etTester.setError("Required Field");
                }else if(etTire.getText().toString().isEmpty()){
                    etTire.setError("Required Field");
                }else{
                    mIsDataAcquiring = true;
                    disableMeasureButton();
                    activityHome.startMeasure();
                }
            }
        });

        btnBackDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    private void setUpTestDataView(){
        adapter = new ScanDataAdapter(scanData,activityHome);
        rvScanData.setLayoutManager(new LinearLayoutManager(activityHome));
        rvScanData.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        setUpViews(view);
        activityHome = (ActivityHome) getActivity();
        configureBackdrop();
        setupListeners();
        setUpTestDataView();
        Utils.setStoragePermissions(activityHome);
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
                if (Objects.equals(mFW_CONNECTION_STATE, "Disconnected")){
                    treadReaderVersions = null;
                    if (mIsDataAcquiring) {
                        mIsDataAcquiring = false;
                        activityHome.showDeviceConnectionErrorDialog();
                    }
                    disableMeasureButton();
                }else {
                    enableMeasureButton();
                }
            }

            @Override
            public void onDataReceived(String jsonData) {
                mIsDataAcquiring = false;
                enableMeasureButton();
                ScanData data = new ScanData();
                data.setTireId(etTire.getText().toString());
                data.setTester(etTester.getText().toString());
                data.setJsonData(jsonData);
                scanData.add(data);

                adapter.notifyDataSetChanged();

                String time = Utils.getTimeStamp();
                try {
                    saveImg(treadReaderVersions.getSerialNumber(),time,false);
                    checkForReSwipe(jsonData,time,true);
                }catch (Exception e){
                    Log.w(TAG,e.getMessage());
                }


                lytScanData.setVisibility(View.VISIBLE);
                setDataForUi(jsonData,
                        tvInnerDepth,tvCenterDepth,tvOuterDepth,
                        indicatorInner1,indicatorInner2,indicatorInner3,
                        indicatorCenter1,indicatorCenter2,indicatorCenter3,
                        indicatorOuter1,indicatorOuter2,indicatorOuter3);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Hide the layout again
                        lytScanData.setVisibility(View.GONE);
                    }
                }, 2000); // 2000 milliseconds = 2 seconds

            }

            @Override
            public void onParamReceive(String jsonData) {

            }

            @Override
            public void onVersionReceived(String jsonData) {
                Log.e("onVersionReceived",jsonData);
                Gson gson = new Gson();
                treadReaderVersions  = gson.fromJson(jsonData, TreadReaderVersions.class);

                if (Objects.equals(activityHome.fwConnectionStateHolder, "Disconnected")){
                    disableMeasureButton();
                }else {
                    enableMeasureButton();
                }
            }

            @Override
            public void onError(String error) {
                mIsDataAcquiring = false;
                enableMeasureButton();
                String time = Utils.getTimeStamp();
                logData(error,time,false,"Error");
                etTire.setText("");
                activityHome.showError(error, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }

            @Override
            public void onException(String exception,String action) {
                etTire.setText("");
            }
        });
    }

    boolean isReSwipeDialogShowForOnce = false;

    private void checkForReSwipe(String jsonData,String time,boolean isScanSuccess){
        TreadDataList treadDataList = gson.fromJson(jsonData, TreadDataList.class);
        boolean isReSwipe = false;
        for (int i = 0; i < treadDataList.getTreadList().size(); i++) {
            if (treadDataList.getTreadList().get(i).getStatus().equals("Reswipe")) {
                isReSwipe = true;
                if (isReSwipeDialogShowForOnce){
                    logData(jsonData,time,true,"Re-Swipe");
                    etTire.setText("");
                }else{
                    showReSwipeDialog(jsonData,time,treadDataList.getTreadList().get(i).getPosition());
                }
                break;
            }
        }
        if (!isReSwipe){
            logData(jsonData,time,true,"Valid");
            etTire.setText("");
        }

    }

    private void showReSwipeDialog(final String jsonData, final String time, String position) {
        isReSwipeDialogShowForOnce = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(activityHome);
        builder.setTitle("Re-swipe Dialog");
        builder.setMessage("Re-swipe on position: " + position);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                activityHome.setParams(Constants.NEUTRON_RESWIPE, "true");
                dialog.dismiss();
                logData(jsonData,time,true,"Invalid");

            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                activityHome.setParams(Constants.NEUTRON_RESWIPE, "false");
                dialog.dismiss();
                logData(jsonData,time,true,"Valid");
                etTire.setText("");
            }

        });


        AlertDialog alert = builder.create();
        alert.show();
    }

    private void enableMeasureButton(){
        if (!mIsDataAcquiring){
            if (treadReaderVersions == null){
                activityHome.getVersion();
            }else{
                try {
                    btnMeasure.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.scan_enable));
                    btnMeasure.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                    btnMeasure.setEnabled(true);

                    if (!saveImageParamSet){
                        activityHome.setParams(Constants.NEUTRON_SAVE_IMG,"1");
                        saveImageParamSet = true;
                    }
                }catch (Exception e){
                    Log.d(TAG,"enableMeasureButton");
                }
            }
        }
    }

    private void disableMeasureButton(){
        try {
            btnMeasure.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.scan_disable));
            btnMeasure.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
            btnMeasure.setEnabled(false);
            saveImageParamSet = false;
        }catch (Exception e){
            Log.d(TAG,"disableMeasureButton");
        }
    }

    private void configureBackdrop() {

        BottomSheetBehavior bsb = BottomSheetBehavior.from(lytTestData);

        // Set the initial state of the BottomSheetBehavior to HIDDEN
        bsb.setState(BottomSheetBehavior.STATE_HIDDEN);

        // Set the reference into class attribute (will be used latter)
        mBottomSheetBehavior = bsb;
    }

    private void logData(String data,String time,boolean isScanSuccess, String swipeStatus){
        ArrayList<String> arr = new ArrayList<>();

        arr.add(String.valueOf(Utils.getDataNum()));
        arr.add(etTester.getText().toString());
        arr.add(etTire.getText().toString());
        arr.add(isScanSuccess ? "SUCCESS": "ERROR - "+data);
//                Plunger Data
        arr.add(tvInnerPlunger.getText().toString());
        arr.add(tvCenterPlunger.getText().toString());
        arr.add(tvOuterPlunger.getText().toString());

        if (isScanSuccess){
            TreadDataList treadDataList  = gson.fromJson(data, TreadDataList.class);
//                Depth (mm)(Inner/Center/Outer)
            for (int i = 0; i < treadDataList.getTreadList().size(); i++) {
                arr.add(String.valueOf(treadDataList.getTreadList().get(i).getMetricTreadDepth()));
            }

//                Depth (32nds)(Inner/Center/Outer)
            for (int i = 0; i < treadDataList.getTreadList().size(); i++) {
                arr.add(Utils.mmTo32nds(treadDataList.getTreadList().get(i).getMetricTreadDepth()));
            }

//                Confidence Level (Inner/Center/Outer)
            for (int i = 0; i < treadDataList.getTreadList().size(); i++) {
                arr.add(String.valueOf(treadDataList.getTreadList().get(i).getConfidenceLevel()));
            }

//                Re-Swipe (Inner/Center/Outer)
            for (int i = 0; i < treadDataList.getTreadList().size(); i++) {
                arr.add(treadDataList.getTreadList().get(i).getStatus());
            }
        }else{
//            Depth (mm) Depth (32nds) Confidence Level Re-Swipe (Inner/Center/Outer) = 0
            for (int i = 0; i < 12; i++) {
                arr.add("0");
            }
        }
//                Date
        arr.add(time);

//                Versions and data
        if (treadReaderVersions != null) {
            arr.add(treadReaderVersions.getSerialNumber());
            arr.add(treadReaderVersions.getHardwareVersion());
            arr.add(treadReaderVersions.getFirmwareVersion());
            arr.add(treadReaderVersions.getServiceVersion());
        }

        arr.add(swipeStatus);
        try{
            Utils.logData(arr.toArray(new String[arr.size()]));
        }catch (Exception e) {

        }
    }

    private void saveImg(String serialNum, String time, boolean isError)
    {
        String time_new = time.replace(":", "-");
        time_new = time_new.replace(" ", "_");
        String dest = Constants.NEUTRON_EXTERNAL_STORAGE_BASE_PATH + "/" + serialNum + "_" + etTester.getText() + "_" + etTire.getText() + "/" + String.valueOf(Utils.getDataNum()) + "_i_" + serialNum + "_" + etTester.getText() + "_" + etTire.getText() + "_" + time_new + Constants.NEUTRON_TREAD_READER_FILE_EXT;
        String filePath = Constants.NEUTRON_EXTERNAL_STORAGE_BASE_PATH + "/" + Constants.NEUTRON_TREAD_READER_INPUT_IMAGE_FILE;
        Utils.copyFileOrDirectory(filePath, dest);

        if(!isError)
        {
            dest = Constants.NEUTRON_EXTERNAL_STORAGE_BASE_PATH + "/" + serialNum + "_" + etTester.getText() + "_" + etTire.getText() + "/" + String.valueOf(Utils.getDataNum()) + "_o_" + serialNum + "_" + etTester.getText() + "_" + etTire.getText() + "_" + time_new + Constants.NEUTRON_TREAD_READER_FILE_EXT_JPEG;
            filePath = Constants.NEUTRON_EXTERNAL_STORAGE_BASE_PATH + "/" + Constants.NEUTRON_TREAD_READER_OUTPUT_IMAGE_FILE;
            Utils.copyFileOrDirectory(filePath, dest);
        }
    }

    private void setDataForUi(String jsonData,
                              TextView tvInnerDepth,TextView tvCenterDepth,TextView tvOuterDepth,
                              ImageView indicatorInner1,ImageView indicatorInner2,ImageView indicatorInner3,
                              ImageView indicatorCenter1,ImageView indicatorCenter2,ImageView indicatorCenter3,
                              ImageView indicatorOuter1,ImageView indicatorOuter2,ImageView indicatorOuter3){

        tvTireId.setText(etTire.getText());

        Gson gson = new Gson();
        TreadDataList treadDataList  = gson.fromJson(jsonData, TreadDataList.class);
        for (int i = 0; i < treadDataList.getTreadList().size(); i++) {
            String position = treadDataList.getTreadList().get(i).getPosition();
            TreadData treadData = treadDataList.getTreadList().get(i);
            switch (position) {
                case "Inner":
                    tvInnerDepth.setText(getFormattedData(Float.parseFloat(Utils.mmTo32nds(treadData.getMetricTreadDepth()))));
                    showIndicators(indicatorInner1,indicatorInner2,indicatorInner3,treadData.getMetricTreadDepth());
                    break;
                case "Center":
                    tvCenterDepth.setText(getFormattedData(Float.parseFloat(Utils.mmTo32nds(treadData.getMetricTreadDepth()))));
                    showIndicators(indicatorCenter1,indicatorCenter2,indicatorCenter3,treadData.getMetricTreadDepth());
                    break;
                case "Outer":
                    tvOuterDepth.setText(getFormattedData(Float.parseFloat(Utils.mmTo32nds(treadData.getMetricTreadDepth()))));
                    showIndicators(indicatorOuter1,indicatorOuter2,indicatorOuter3,treadData.getMetricTreadDepth());
                    break;
            }
        }
    }

    private String getFormattedData(float value){
        return String.format("%.1f", value);
    }

    private void showIndicators(ImageView indicator1, ImageView indicator2, ImageView indicator3, float treadDepth){
        if(treadDepth < NEUTRON_DEPTH_LOW) {
            indicator1.setVisibility(View.INVISIBLE);
            indicator2.setVisibility(View.INVISIBLE);
            indicator3.setVisibility(View.VISIBLE);
            indicator3.setBackgroundColor(activityHome.getColor(R.color.low));
        } else if((treadDepth >= NEUTRON_DEPTH_LOW) && (treadDepth < NEUTRON_DEPTH_HIGH)) {
            indicator1.setVisibility(View.INVISIBLE);
            indicator2.setVisibility(View.VISIBLE);
            indicator3.setVisibility(View.VISIBLE);
            indicator2.setBackgroundColor(activityHome.getColor(R.color.mid2));
            indicator3.setBackgroundColor(activityHome.getColor(R.color.mid));
        } else if(treadDepth >= NEUTRON_DEPTH_HIGH) {
            indicator1.setVisibility(View.VISIBLE);
            indicator2.setVisibility(View.VISIBLE);
            indicator3.setVisibility(View.VISIBLE);
            indicator1.setBackgroundColor(activityHome.getColor(R.color.high3));
            indicator2.setBackgroundColor(activityHome.getColor(R.color.high2));
            indicator3.setBackgroundColor(activityHome.getColor(R.color.high));
        }
    }

}