package com.zebra.treadreadersampleintentapi.util;

import android.Manifest;
import android.content.pm.PackageManager;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.zebra.treadreadersampleintentapi.ActivityHome;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils
{
    public static String mmTo32nds(float num)
    {
        float temp = (num * 32) / 25.4f;
        return String.format("%.1f", temp);
    }

    public static String getTimeStamp()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.NEUTRON_DATE_FORMAT);
        return dateFormat.format(new Date());
    }

    public static int getDataNum()
    {
        String filePath = Constants.NEUTRON_EXTERNAL_STORAGE_BASE_PATH + "/" + Constants.NEUTRON_TREAD_READER_LOG_CSV_FILE;
        File file = new File(filePath);
        int count = 0;

        if(file.exists() && !file.isDirectory())
        {
            CSVReader reader = null;

            try
            {
                reader = new CSVReader(new FileReader(filePath));
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }

            if(reader == null)
            {
                return -1;
            }

            try
            {
                while(reader.readNext() != null)
                {
                    count++;
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return count;
        }
        else
        {
            return count + 1;
        }
    }

    public static void logData(String[] data)
    {
        String filePath = Constants.NEUTRON_EXTERNAL_STORAGE_BASE_PATH + "/" + Constants.NEUTRON_TREAD_READER_LOG_CSV_FILE;
        File file = new File(filePath);
        FileWriter fileWriter = null;
        CSVWriter CSVWriter = null;

        if(file.exists() && !file.isDirectory())
        {
            try
            {
                fileWriter = new FileWriter(filePath, true);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            CSVWriter = new CSVWriter(fileWriter);
        }
        else
        {
            try
            {
                CSVWriter = new CSVWriter(new FileWriter(filePath));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }


            CSVWriter.writeNext(Constants.NEUTRON_TREAD_READER_LOG_CSV_FILE_HEADER);
        }

        CSVWriter.writeNext(data);

        try
        {
            CSVWriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void setStoragePermissions(ActivityHome activityHome)
    {
        if(activityHome.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            activityHome.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        if(activityHome.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            activityHome.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    public static void copyFileOrDirectory(String srcDir, String destDir)
    {
        try
        {
            File srcFile = new File(srcDir);
            File destFile = new File(destDir);

            if(srcFile.isDirectory())
            {
                String files[] = srcFile.list();
                int filesLength = files.length;

                for(int i = 0; i < filesLength; i++)
                {
                    String srcPath = (new File(srcFile, files[i]).getPath());
                    String destPath = destFile.getPath();
                    copyFileOrDirectory(srcPath, destPath);
                }
            }
            else
            {
                copyFile(srcFile, destFile);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void deleteFile()
    {
        String filePath = Constants.NEUTRON_EXTERNAL_STORAGE_BASE_PATH + "/" + Constants.NEUTRON_TREAD_READER_INPUT_IMAGE_FILE;
        File file = new File(filePath);

        if(file.exists())
        {
            file.delete();
        }

        filePath = Constants.NEUTRON_EXTERNAL_STORAGE_BASE_PATH + "/" + Constants.NEUTRON_TREAD_READER_OUTPUT_IMAGE_FILE;
        file = new File(filePath);

        if(file.exists())
        {
            file.delete();
        }
    }

    private static void copyFile(File srcFile, File destFile) throws IOException
    {
        if(!destFile.exists())
        {
            destFile.getParentFile().mkdirs();
        }

        FileChannel srcFileChannel = null;
        FileChannel destFileChannel = null;

        try
        {
            srcFileChannel = new FileInputStream(srcFile).getChannel();
            destFileChannel = new FileOutputStream(destFile).getChannel();
            destFileChannel.transferFrom(srcFileChannel, 0, srcFileChannel.size());
        }
        finally
        {
            if(srcFileChannel != null)
            {
                srcFileChannel.close();
            }

            if(destFileChannel != null)
            {
                destFileChannel.close();
            }
        }
    }
}