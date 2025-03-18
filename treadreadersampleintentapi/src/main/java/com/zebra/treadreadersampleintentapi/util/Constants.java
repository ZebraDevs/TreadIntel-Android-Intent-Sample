package com.zebra.treadreadersampleintentapi.util;

import android.os.Environment;

public class Constants
{
    public static final String NEUTRON_EXTERNAL_STORAGE_BASE_PATH = String.valueOf(Environment.getExternalStorageDirectory());
    public static final String NEUTRON_TREAD_READER_LOG_CSV_FILE = "TreadReaderLogFile.csv";
    public static final String NEUTRON_TREAD_READER_INPUT_IMAGE_FILE = "tt_input.png";
    public static final String NEUTRON_TREAD_READER_OUTPUT_IMAGE_FILE = "tt_output.jpeg";
    public static final String NEUTRON_TREAD_READER_FILE_EXT = ".png";
    public static final String NEUTRON_TREAD_READER_FILE_EXT_JPEG = ".jpeg";
    public static final String NEUTRON_SAVE_IMG = "save_img";
    public static final String NEUTRON_RESWIPE = "reswipe";
    public static final String NEUTRON_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String[] NEUTRON_TREAD_READER_LOG_CSV_FILE_HEADER = {"ID", "Username", "Tire ID", "Status", "Plunger (32nds)(Inner)", "Plunger (32nds)(Center)", "Plunger (32nds)(Outer)", "Depth (mm)(Inner)", "Depth (mm)(Center)", "Depth (mm)(Outer)", "Depth (32nds)(Inner)", "Depth (32nds)(Center)", "Depth (32nds)(Outer)", "Confidence Level (Inner)", "Confidence Level (Center)", "Confidence Level (Outer)", "Re-Swipe (Inner)", "Re-Swipe (Center)", "Re-Swipe (Outer)", "Date", "Serial Number", "HW Version", "FW Version", "Framework Version", "Tread Status"};
    public static final float NEUTRON_DEPTH_LOW = 3;
    public static final float NEUTRON_DEPTH_HIGH = 6;
}