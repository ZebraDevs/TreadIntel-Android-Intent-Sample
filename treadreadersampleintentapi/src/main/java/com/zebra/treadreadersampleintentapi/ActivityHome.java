package com.zebra.treadreadersampleintentapi;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.zebra.treadreadersampleintentapi.callbacks.ITreadReaderCallBack;
import com.zebra.treadreadersampleintentapi.util.IntentAPIConstantUtils;

import java.util.Objects;

public class ActivityHome extends AppCompatActivity {

    ITreadReaderCallBack iTreadReaderCallBack;

    public int showImageCount = 0;

    String fwConnectionStateHolder = "";
    String fwStateHolder = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.navigation_demo:
                        selectedFragment = new FreagmentDemo();
                        getParams("reader_timeout");
                        break;
                    case R.id.navigation_test:
                        selectedFragment = new FragmentTest();
                        getVersion();
                        break;
                    case R.id.navigation_versions:
                        selectedFragment = new FragmentVersions();
                        getVersion();
                        break;
                }
                if (selectedFragment != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment, selectedFragment);
                    transaction.commit();
                }

                return true;
            }
        });

        // Set the default fragment
        if (savedInstanceState == null) {
            navView.setSelectedItemId(R.id.navigation_demo);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initiateFramework();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseFramework();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void registerListener(ITreadReaderCallBack callBack){
        iTreadReaderCallBack = callBack;
    }

    private void initiateFramework(){
        Intent intent = new Intent();
        intent.setPackage(IntentAPIConstantUtils.TTDR_SDK_PACKAGE);
        intent.setAction(IntentAPIConstantUtils.TTDR_INTENT_ACTION_INITIATE_READER);

        PendingIntent openFrameworkPendingIntent = createPendingResult(IntentAPIConstantUtils.TTDR_REQUEST_CODE, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
        intent.putExtra(IntentAPIConstantUtils.TTDR_CALLBACK_RESPONSE, openFrameworkPendingIntent);

        startForegroundService(intent);
    }

    public void startMeasure(){
        Intent intent = new Intent();
        intent.setAction(IntentAPIConstantUtils.TTDR_INTENT_ACTION_START);
        PendingIntent currentStatePendingIntent = createPendingResult(IntentAPIConstantUtils.TTDR_REQUEST_CODE, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
        intent.putExtra(IntentAPIConstantUtils.TTDR_CALLBACK_RESPONSE, currentStatePendingIntent);
        sendBroadcast(intent);
    }

    public void getVersion (){
        Log.e("TTDR","getVersion()");
        Intent intent = new Intent();
        intent.setAction(IntentAPIConstantUtils.TTDR_INTENT_ACTION_VERSION);
        PendingIntent currentStatePendingIntent = createPendingResult(IntentAPIConstantUtils.TTDR_REQUEST_CODE, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
        intent.putExtra(IntentAPIConstantUtils.TTDR_CALLBACK_RESPONSE, currentStatePendingIntent);
        sendBroadcast(intent);
    }

    public void checkStatus(){
        Intent intent = new Intent();
        intent.setAction(IntentAPIConstantUtils.TTDR_INTENT_ACTION_CURRENT_STATE);
        PendingIntent currentStatePendingIntent = createPendingResult(IntentAPIConstantUtils.TTDR_REQUEST_CODE, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
        intent.putExtra(IntentAPIConstantUtils.TTDR_CALLBACK_RESPONSE, currentStatePendingIntent);
        sendBroadcast(intent);
    }

    private void releaseFramework() {
        Intent intent = new Intent(IntentAPIConstantUtils.TTDR_INTENT_ACTION_RELEASE);
        PendingIntent closeFrameworkPendingIntent = createPendingResult(IntentAPIConstantUtils.TTDR_REQUEST_CODE, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
        intent.putExtra(IntentAPIConstantUtils.TTDR_CALLBACK_RESPONSE, closeFrameworkPendingIntent);
        sendBroadcast(intent);
    }


    public void setParams(String param, String val){
        Intent intent = new Intent(IntentAPIConstantUtils.TTDR_INTENT_ACTION_SET_PARAMS);
        PendingIntent closeFrameworkPendingIntent = createPendingResult(IntentAPIConstantUtils.TTDR_REQUEST_CODE, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
        intent.putExtra(IntentAPIConstantUtils.TTDR_CALLBACK_RESPONSE, closeFrameworkPendingIntent);
        intent.putExtra(IntentAPIConstantUtils.TTDR_FW_PARAM, param);
        intent.putExtra(IntentAPIConstantUtils.TTDR_FW_PARAM_VALUE, val);
        sendBroadcast(intent);
    }

    public void getParams(String param){
        Intent intent = new Intent(IntentAPIConstantUtils.TTDR_INTENT_ACTION_GET_PARAMS);
        PendingIntent closeFrameworkPendingIntent = createPendingResult(IntentAPIConstantUtils.TTDR_REQUEST_CODE, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
        intent.putExtra(IntentAPIConstantUtils.TTDR_CALLBACK_RESPONSE, closeFrameworkPendingIntent);
        intent.putExtra(IntentAPIConstantUtils.TTDR_FW_PARAM, param);
        sendBroadcast(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentAPIConstantUtils.TTDR_REQUEST_CODE){
            String mACTION = data.getStringExtra(IntentAPIConstantUtils.TTDR_ACTION);
            if (Objects.equals(mACTION, IntentAPIConstantUtils.TTDR_INTENT_ACTION_INITIATE_READER)){
                int mACTION_STATUS = data.getIntExtra(IntentAPIConstantUtils.TTDR_ACTION_STATUS,0);
                iTreadReaderCallBack.onInitiateReader(mACTION_STATUS);
            }else if (Objects.equals(mACTION,IntentAPIConstantUtils.TTDR_INTENT_ACTION_ON_STATE_CHANGE)) {
                String mFW_STATE = data.getStringExtra(IntentAPIConstantUtils.TTDR_FW_STATE);
                String mFW_CONNECTION_STATE = data.getStringExtra(IntentAPIConstantUtils.TTDR_FW_CONNECTION_STATE);
                fwStateHolder = mFW_STATE;
                fwConnectionStateHolder = mFW_CONNECTION_STATE;
                Log.e("FW_STAT",fwStateHolder);
                Log.e("FW_CONNECTION_STATE",fwConnectionStateHolder);
                iTreadReaderCallBack.onStateChange(mFW_STATE,mFW_CONNECTION_STATE);
            }if (Objects.equals(mACTION, IntentAPIConstantUtils.TTDR_INTENT_ACTION_START)) {
                int mACTION_STATUS = data.getIntExtra(IntentAPIConstantUtils.TTDR_ACTION_STATUS,0);
                if (mACTION_STATUS == IntentAPIConstantUtils.TTDR_ACTION_SUCCESS){
                    String jsonData = data.getStringExtra(IntentAPIConstantUtils.TTDR_ACTION_DATA);
                    iTreadReaderCallBack.onDataReceived(jsonData);
                }else if (mACTION_STATUS == IntentAPIConstantUtils.TTDR_ACTION_FAILED){
                    iTreadReaderCallBack.onError(getString(R.string.scan_failed));
                }
            }if (Objects.equals(mACTION, IntentAPIConstantUtils.TTDR_INTENT_ACTION_VERSION)) {
                Log.e("TTDR","TTDR_INTENT_ACTION_VERSION");
                int mACTION_STATUS = data.getIntExtra(IntentAPIConstantUtils.TTDR_ACTION_STATUS,0);
                if (mACTION_STATUS == IntentAPIConstantUtils.TTDR_ACTION_SUCCESS){
                    String jsonData = data.getStringExtra(IntentAPIConstantUtils.TTDR_ACTION_DATA);
                    iTreadReaderCallBack.onVersionReceived(jsonData);
                }else if (mACTION_STATUS == IntentAPIConstantUtils.TTDR_ACTION_FAILED){
                    iTreadReaderCallBack.onError(getString(R.string.get_verion_failed));
                }
            }else if (Objects.equals(mACTION,IntentAPIConstantUtils.TTDR_INTENT_ACTION_CURRENT_STATE)) {
                String mFW_STATE = data.getStringExtra(IntentAPIConstantUtils.TTDR_FW_STATE);
                String mFW_CONNECTION_STATE = data.getStringExtra(IntentAPIConstantUtils.TTDR_FW_CONNECTION_STATE);
                iTreadReaderCallBack.onStateChange(mFW_STATE,mFW_CONNECTION_STATE);
            }else if (Objects.equals(mACTION,IntentAPIConstantUtils.TTDR_INTENT_ACTION_ERROR)) {
                int errorCode = data.getIntExtra(IntentAPIConstantUtils.TTDR_INTENT_ERROR_CODE,0);
                String errorMessage = data.getStringExtra(IntentAPIConstantUtils.TTDR_INTENT_ERROR_MESSAGE);
                iTreadReaderCallBack.onError(errorMessage);
            }else if (Objects.equals(mACTION,IntentAPIConstantUtils.TTDR_INTENT_ACTION_EXCEPTION)) {
                String exception = data.getStringExtra(IntentAPIConstantUtils.TTDR_INTENT_EXCEPTION_DESCRIPTION);
                String exceptionOccuredAction = data.getStringExtra(IntentAPIConstantUtils.TTDR_INTENT_EXCEPTION_OCCURRED_ACTION);
                iTreadReaderCallBack.onException(exception,exceptionOccuredAction);
            }else if (Objects.equals(mACTION,IntentAPIConstantUtils.TTDR_INTENT_ACTION_GET_PARAMS)) {
                String paramJson = data.getStringExtra(IntentAPIConstantUtils.TTDR_ACTION_DATA);
                iTreadReaderCallBack.onParamReceive(paramJson);
            }
        }
    }

    public void showError(String error,View.OnClickListener onClickListener){
        Snackbar snackbarWithAction = Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG);
        snackbarWithAction.setAction(getString(R.string.close),onClickListener);
        snackbarWithAction.show();
    }



    public void showDeviceConnectionErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.ttdr_connection_error_title));
        builder.setMessage(getString(R.string.ttdr_connection_error));

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}