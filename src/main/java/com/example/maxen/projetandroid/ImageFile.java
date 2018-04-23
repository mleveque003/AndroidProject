package com.example.maxen.projetandroid;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Admin on 23/04/2018.
 */

public class ImageFile {

    private static String mCurrentPhotoPath;

    public static String getmCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }


    /** Create an image file name */
    private static String getImageFileName(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return  "JPEG_" + timeStamp + "_";
    }

    public static File createImageFile() throws IOException {

        String imageFileName = getImageFileName();
        File storageDir = Environment.getExternalStoragePublicDirectory("MyFolder");
        if (!storageDir.exists())
            storageDir.mkdirs();

        String filename = imageFileName + ".jpg";

        return new File(storageDir,filename);

    }

    public static File createTempImageFile(Activity activity) throws IOException {
        String imageFileName = getImageFileName();

        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public static void startSave(Bitmap myBitmap, Activity activity){

        File f = null;
        try{
            f = createImageFile();
            FileOutputStream fos = new FileOutputStream(f);
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        }catch(IOException e){
            Toast.makeText(activity.getApplicationContext(),"Impossible to create the picture", Toast.LENGTH_SHORT).show();
        }finally {
            if (f != null){
                // Add the new image to the user image gallery
                MediaScannerConnection.scanFile(activity,
                        new String[]{f.toString()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> uri=" + uri);
                            }
                        });
                Toast toast = Toast.makeText(activity,"Image saved !",Toast.LENGTH_SHORT);
                toast.show();
            }

        }

    }

}
