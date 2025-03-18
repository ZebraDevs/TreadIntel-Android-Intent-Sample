package com.zebra.treadreadersampleintentapi;

import static android.view.View.GONE;

import static com.zebra.treadreadersampleintentapi.util.Constants.NEUTRON_DEPTH_HIGH;
import static com.zebra.treadreadersampleintentapi.util.Constants.NEUTRON_DEPTH_LOW;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zebra.treadreadersampleintentapi.callbacks.ITreadReaderCallBack;
import com.zebra.treadreadersampleintentapi.models.TreadData;
import com.zebra.treadreadersampleintentapi.models.TreadDataList;
import com.zebra.treadreadersampleintentapi.util.Constants;
import com.zebra.treadreadersampleintentapi.util.Utils;

import java.io.File;
import java.util.Objects;

public class FreagmentDemo extends Fragment implements SeekBar.OnSeekBarChangeListener {

    Button btnMeasure,btnClose;
    LinearLayout lytImage;
    TextView tvInnerDepth,tvCenterDepth,tvOuterDepth,tvConfidenceInner,tvConfidenceCenter,tvConfidenceOuter,tvInnerPlunger,tvCenterPlunger,tvOuterPlunger;
    SeekBar seekBarInner,seekBarCenter,seekBarOuter,scanSpeedSeekBar;

    ImageView indicatorInner1,indicatorInner2,indicatorInner3,indicatorCenter1,indicatorCenter2,indicatorCenter3,indicatorOuter1,indicatorOuter2,indicatorOuter3,ivImage,btnImage;
    boolean isRegisterListener = false;
    boolean mIsDataAcquiring = false;

    public FreagmentDemo() {
        // Required empty public constructor
    }

    ActivityHome activityHome;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.freagment_demo, container, false);
        btnMeasure = view.findViewById(R.id.btnMeasure);
        tvInnerDepth = view.findViewById(R.id.tvInnerDepth);
        tvCenterDepth = view.findViewById(R.id.tvCenterDepth);
        tvOuterDepth = view.findViewById(R.id.tvOuterDepth);
        tvConfidenceInner = view.findViewById(R.id.tvConfidenceInner);
        tvConfidenceCenter = view.findViewById(R.id.tvConfidenceCenter);
        tvConfidenceOuter = view.findViewById(R.id.tvConfidenceOuter);
        tvInnerPlunger = view.findViewById(R.id.tvInnerPlunger);
        tvCenterPlunger = view.findViewById(R.id.tvCenterPlunger);
        tvOuterPlunger = view.findViewById(R.id.tvOuterPlunger);

        indicatorInner1 = view.findViewById(R.id.indicatorInner1);
        indicatorInner2 = view.findViewById(R.id.indicatorInner2);
        indicatorInner3 = view.findViewById(R.id.indicatorInner3);
        indicatorCenter1 = view.findViewById(R.id.indicatorCenter1);
        indicatorCenter2 = view.findViewById(R.id.indicatorCenter2);
        indicatorCenter3 = view.findViewById(R.id.indicatorCenter3);
        indicatorOuter1 = view.findViewById(R.id.indicatorOuter1);
        indicatorOuter2 = view.findViewById(R.id.indicatorOuter2);
        indicatorOuter3 = view.findViewById(R.id.indicatorOuter3);

        seekBarInner = view.findViewById(R.id.seekBarInner);
        seekBarCenter = view.findViewById(R.id.seekBarCenter);
        seekBarOuter = view.findViewById(R.id.seekBarOuter);
        scanSpeedSeekBar = view.findViewById(R.id.scanSpeedSeekBar);

        btnClose = view.findViewById(R.id.btnClose);
        lytImage = view.findViewById(R.id.lytImage);
        ivImage = view.findViewById(R.id.ivImage);
        btnImage = view.findViewById(R.id.btnImage);

        seekBarInner.setOnSeekBarChangeListener(this);
        seekBarCenter.setOnSeekBarChangeListener(this);
        seekBarOuter.setOnSeekBarChangeListener(this);
        scanSpeedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                switch (i){
                    case 0:
                        activityHome.setParams("reader_timeout", "0002");
                        break;
                    case 1:
                        activityHome.setParams("reader_timeout", "0008");
                        break;
                    case 2:
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
        activityHome = (ActivityHome) getActivity();

        btnMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
                mIsDataAcquiring = true;
                disableMeasureButton();
                activityHome.startMeasure();
            }
        });

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lytImage.setVisibility(View.VISIBLE);
                createImageDialog(ivImage);
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lytImage.setVisibility(GONE);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Objects.equals(activityHome.fwConnectionStateHolder, "Disconnected")){
            disableMeasureButton();
        }else {
            enableMeasureButton();
        }

        activityHome.registerListener(new ITreadReaderCallBack() {
            @Override
            public void onInitiateReader(int result) {

            }

            @Override
            public void onStateChange(String mFW_STATE, String mFW_CONNECTION_STATE) {
                if (Objects.equals(mFW_CONNECTION_STATE, "Disconnected")){
                    if (mIsDataAcquiring) {
                        mIsDataAcquiring = false;
                        activityHome.showDeviceConnectionErrorDialog();
                    }
                    disableMeasureButton();
                }else {
                    enableMeasureButton();
                    activityHome.getParams("reader_timeout");
                }
            }

            @Override
            public void onDataReceived(String jsonData) {
                mIsDataAcquiring = false;
                enableMeasureButton();
                setDataForUi(jsonData);
            }

            @Override
            public void onParamReceive(String jsonData) {
                if (jsonData.contains("0002")){
                    scanSpeedSeekBar.setProgress(0);
                }else if (jsonData.contains("0008")){
                    scanSpeedSeekBar.setProgress(1);
                }else if (jsonData.contains("0010")){
                    scanSpeedSeekBar.setProgress(2);
                }
            }

            @Override
            public void onVersionReceived(String jsonData) {

            }

            @Override
            public void onError(String error) {
                mIsDataAcquiring = false;
                enableMeasureButton();
                activityHome.showError(error, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }

            @Override
            public void onException(String exception,String action) {

            }
        });

        if (activityHome.showImageCount > 4){
            btnImage.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        isRegisterListener = false;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private boolean saveImageParamSet = false;

    private void enableMeasureButton(){
        if (!mIsDataAcquiring){
            btnMeasure.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.scan_enable));
            btnMeasure.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
            btnMeasure.setEnabled(true);
            if (!saveImageParamSet){
                activityHome.setParams(Constants.NEUTRON_SAVE_IMG,"1");
                saveImageParamSet = true;
            }
        }
    }

    private void disableMeasureButton(){
        btnMeasure.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.scan_disable));
        btnMeasure.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
        btnMeasure.setEnabled(false);
        saveImageParamSet = false;
    }

    private void setDataForUi(String jsonData){
        Gson gson = new Gson();
        TreadDataList treadDataList  = gson.fromJson(jsonData, TreadDataList.class);
        for (int i = 0; i < treadDataList.getTreadList().size(); i++) {
            String position = treadDataList.getTreadList().get(i).getPosition();
            TreadData treadData = treadDataList.getTreadList().get(i);
            switch (position) {
                case "Inner":
                    tvInnerDepth.setText(getFormattedData(Float.parseFloat(Utils.mmTo32nds(treadData.getMetricTreadDepth()))));
                    tvConfidenceInner.setText(String.valueOf(treadData.getConfidenceLevel()));
                    showIndicators(indicatorInner1,indicatorInner2,indicatorInner3,treadData.getMetricTreadDepth());
                    break;
                case "Center":
                    tvCenterDepth.setText(getFormattedData(Float.parseFloat(Utils.mmTo32nds(treadData.getMetricTreadDepth()))));
                    tvConfidenceCenter.setText(String.valueOf(treadData.getConfidenceLevel()));
                    showIndicators(indicatorCenter1,indicatorCenter2,indicatorCenter3,treadData.getMetricTreadDepth());
                    break;
                case "Outer":
                    tvOuterDepth.setText(getFormattedData(Float.parseFloat(Utils.mmTo32nds(treadData.getMetricTreadDepth()))));
                    tvConfidenceOuter.setText(String.valueOf(treadData.getConfidenceLevel()));
                    showIndicators(indicatorOuter1,indicatorOuter2,indicatorOuter3,treadData.getMetricTreadDepth());
                    break;
            }
        }
    }

    private void showIndicators(ImageView indicator1, ImageView indicator2, ImageView indicator3, float treadDepth){
        if(treadDepth < NEUTRON_DEPTH_LOW) {
            indicator1.setVisibility(View.INVISIBLE);
            indicator2.setVisibility(View.INVISIBLE);
            indicator3.setVisibility(View.VISIBLE);
            indicator3.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.low));
        } else if((treadDepth >= NEUTRON_DEPTH_LOW) && (treadDepth < NEUTRON_DEPTH_HIGH)) {
            indicator1.setVisibility(View.INVISIBLE);
            indicator2.setVisibility(View.VISIBLE);
            indicator3.setVisibility(View.VISIBLE);
            indicator2.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.mid2));
            indicator3.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.mid));
        } else if(treadDepth >= NEUTRON_DEPTH_HIGH) {
            indicator1.setVisibility(View.VISIBLE);
            indicator2.setVisibility(View.VISIBLE);
            indicator3.setVisibility(View.VISIBLE);
            indicator1.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.high3));
            indicator2.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.high2));
            indicator3.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.high));
        }
    }

    private void clearData(){

        tvInnerDepth.setText("0.0");
        tvConfidenceInner.setText("0.0");
        tvCenterDepth.setText("0.0");
        tvConfidenceCenter.setText("0.0");
        tvOuterDepth.setText("0.0");
        tvConfidenceOuter.setText("0.0");

        indicatorInner1.setVisibility(View.INVISIBLE);
        indicatorInner2.setVisibility(View.INVISIBLE);
        indicatorInner3.setVisibility(View.INVISIBLE);
        indicatorCenter1.setVisibility(View.INVISIBLE);
        indicatorCenter2.setVisibility(View.INVISIBLE);
        indicatorCenter3.setVisibility(View.INVISIBLE);
        indicatorOuter1.setVisibility(View.INVISIBLE);
        indicatorOuter2.setVisibility(View.INVISIBLE);
        indicatorOuter3.setVisibility(View.INVISIBLE);
    }

    private String getFormattedData(float value){
        return String.format("%.1f", value);
    }

    private void createImageDialog(ImageView image)
    {
        String filePath = Constants.NEUTRON_EXTERNAL_STORAGE_BASE_PATH + "/" + Constants.NEUTRON_TREAD_READER_INPUT_IMAGE_FILE;
        File file = new File(filePath);

        if(file.exists())
        {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            image.setImageBitmap(bitmap);
        }

    }
}