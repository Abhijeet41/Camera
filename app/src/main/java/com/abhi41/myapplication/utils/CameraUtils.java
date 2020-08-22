package com.abhi41.myapplication.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.abhi41.myapplication.MainActivity;
import com.karumi.dexter.BuildConfig;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class CameraUtils {

    public static void refreshGallery (Context context,String filePath){

        MediaScannerConnection.scanFile(context,
                new String[]{filePath}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {

                    }
                });
    }
    public static boolean checkpermission(Context context)
    {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Downsizing the bitmap to avoid OutOfMemory exceptions
     */

    public static Bitmap optimizedBitmap(int sampleSize,String filePath)
    {
        // bitmap factory
        BitmapFactory.Options options = new BitmapFactory.Options();
        // downsizing image as it throws OutOfMemory Exception for larger
        // images
        options.inSampleSize = sampleSize;
        return BitmapFactory.decodeFile(filePath,options);
    }

    /**
     * Checks whether device has camera or not. This method not necessary if
     * android:required="true" is used in manifest file
     */
    public static boolean isDeviceSupportCamera (Context context)
    {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
        {
            // this device has a camera
            return true;
        }else {
            // no camera on this device
            return false;
        }
    }

    /**
     * Open device app settings to allow user to enable permissions
     */

    public static void openSettings(Context context)
    {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", BuildConfig.APPLICATION_ID,null));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static Uri getOutputMediaFileUri(Context context, File file)
    {

        return FileProvider.getUriForFile(context,
                "com.abhi41.myapplication.provider", file);
    }

    /**
     * Creates and returns the image or video file before opening the camera
     */

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static File getOutputMediaFile(int type)
    {
        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                MainActivity.GALLERY_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e(MainActivity.GALLERY_DIRECTORY_NAME, "Oops! Failed create "
                        + MainActivity.GALLERY_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Preparing media file naming convention
        // adds timestamp

        String timeStamp = new SimpleDateFormat("ddmmyyyy_HHmmss",
                Locale.getDefault()).format(new Date());

        File mediaFile;
        if (type == MainActivity.MEDIA_TYPE_IMAGE)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    +"IMG_"+timeStamp+"." + MainActivity.IMAGE_EXTENSION);
        }else if (type == MainActivity.MEDIA_TYPE_VIDEO)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    +"VID_"+timeStamp+"."+MainActivity.VIDEO_EXTENSION);
        }else {
            return null;
        }
        return mediaFile;
    }

}
