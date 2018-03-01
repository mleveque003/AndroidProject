package com.example.maxen.projetandroid;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by maxen on 09/02/2018.
 */

public class Image implements Filtres {
    private Bitmap bitmap;
    private Bitmap originalBitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getOriginalBitmap() {
        return originalBitmap;
    }

    public void setOriginalBitmap(Bitmap originalBitmap) {
        this.originalBitmap = originalBitmap;
    }

    Image(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.originalBitmap = bitmap;
    }

    @Override
    public Bitmap toGray() {
        int max = 0;
        int min = 255;

        Bitmap bm = bitmap.copy(bitmap.getConfig(), true);
        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        Log.i("TAG", Integer.toString(pixels[0]) + " " + bm.getPixel(0,0));
        int[] histogram =new int[256];

        bm.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        for (int i = 0; i < pixels.length; i++) {
            int pixel = pixels[i];
            // retrieve color of all channels
            int R = Color.red(pixel);
            int G = Color.green(pixel);
            int B = Color.blue(pixel);
            // take conversion up to one single value
            R = G = B = (int) (0.299 * R + 0.587 * G + 0.114 * B);

            if (R < min)
                min = R;
            if (R > max)
                max = R;
            pixels[i] = Color.rgb(R, G, B);
            int j = pixels[i];

        }
        int LUT[]= new int[256];


        for (int i = 0; i<256; i++){
            LUT[i]=(255*(i-min))/(max-min);
        }
        int pixelsnew[] = pixels;
        for(int i = 0; i < pixels.length; i++) {
            pixelsnew[i] = Color.rgb(LUT[Color.red(pixels[i])],
                    LUT[Color.red(pixels[i])],
                    LUT[Color.red(pixels[i])]);
        }

        bm.setPixels(pixelsnew, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        bitmap = bm;
        return bm;
    }

    @Override
    public Bitmap updateLuminosity(int max, int progress) {
        if(max != 0) {
            Bitmap bm = bitmap.copy(bitmap.getConfig(), true);

            int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];

            bm.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

            for (int i = 0; i < pixels.length; i++) {
                int R = Color.red(pixels[i]);
                int G = Color.green(pixels[i]);
                int B = Color.blue(pixels[i]);

                pixels[i] = Color.rgb(R * progress / max,
                        G * progress / max,
                        B * progress / max);
            }

            bm.setPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
            bitmap = bm;
            return bm;
        }
        return null;
    }

    @Override
    public Bitmap egalisationHistogramme() {
        Bitmap bm = bitmap.copy(bitmap.getConfig(), true);
        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        int histogram[] = new int[256];
        bm.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        for(int i = 0; i<pixels.length; i++){
            if(Color.red(pixels[i]) != Color.green(pixels[i]) || Color.red(pixels[i]) != Color.blue(pixels[i])){
                return null;
            }
            int pixel = Color.red(pixels[i]);
            histogram[pixel]++;
        }

        int histoCumul[] = new int[256];
        histoCumul[0] = histogram[0];
        for (int i=1; i<256; i++) {
            histoCumul[i] = histoCumul[i-1] + histogram[i]; // calcul du cumul
        }
        for(int i = 0; i<pixels.length; i++){
            pixels[i] = Color.rgb((histoCumul[Color.red(pixels[i])])/pixels.length,
                    (histoCumul[Color.red(pixels[i])])/pixels.length,
                    (histoCumul[Color.red(pixels[i])])/pixels.length);
            Log.i("TAG", Integer.toString((histoCumul[Color.red(pixels[i])])/pixels.length));
        }
        bm.setPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        bitmap = bm;
        return bm;
    }

}