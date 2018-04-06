package com.example.maxen.projetandroid;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
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
        bm.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        int histogram[] = CalculHistogram(pixels);
        int histoCumul[] = CalculHistoCumul(histogram);

        int[] newPixels = new int[pixels.length];
        for(int i =0; i<pixels.length; i++){
            int red =(histoCumul[Color.red(pixels[i])]*255)/pixels.length;
            int pixel = Color.rgb(red,red,red);
            if(red>255 ||red<0)
                Log.d("TAG", Integer.toString(red));
            newPixels[i] = pixel;
        }
        bm.setPixels(newPixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        return bm;
    }

    private int[] CalculHistogram(int[] pixels){
        int[] histogram = new int[256];
        for(int i = 0; i < pixels.length; ++i){
            int k = Color.red(pixels[i]);
            histogram[k]++;
        }
        return histogram;
    }

    private int[] CalculHistoCumul(int[] histogram){
        int histoCumul[] = new int[256];

        for(int i = 1; i<256;++i){
            histoCumul[i]=histogram[i]+histoCumul[i-1];
            Log.e("TAG", "hist : " + Integer.toString(histogram[i]) + " cum : " + Integer.toString(histoCumul[i]));
        }

        return histoCumul;
    }

    @Override
    public Bitmap sepia() {
        int width, height, r,g, b, c, gry;
        height = bitmap.getHeight();
        width = bitmap.getWidth();
        int depth = 20;

        Bitmap bmpSephia = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmpSephia);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setScale(.3f, .3f, .3f, 1.0f);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        for(int x=0; x < width; x++) {
            for(int y=0; y < height; y++) {
                c = bitmap.getPixel(x, y);

                r = Color.red(c);
                g = Color.green(c);
                b = Color.blue(c);

                gry = (r + g + b) / 3;
                r = g = b = gry;

                r = r + (depth * 2);
                g = g + depth;

                if(r > 255) {
                    r = 255;
                }
                if(g > 255) {
                    g = 255;
                }
                bmpSephia.setPixel(x, y, Color.rgb(r, g, b));
            }
        }
        bitmap = bmpSephia;
        return bmpSephia;
    }

    public Bitmap majorerRGB(int c){ //c : 1 = red, 2 = green, 3 = blue
        Bitmap bm = bitmap.copy(bitmap.getConfig(),true);


        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        bm.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        for(int i = 0; i < pixels.length; ++i) {
            int pixel = pixels[i];

            switch (c) {
                case 1:

                    int R = Color.red(pixel);
                    int G = Color.green(pixel);
                    int B = Color.blue(pixel);

                    if (R <= G || R <= B) {
                        R = G = B = (int) (0.299 * Color.red(pixel) + 0.587 * G + 0.114 * B);
                        // set new pixel color to output bitmap
                    }
                    // take conversion up to one single value
                    pixels[i] = Color.rgb(R,G,B);
                    break;
                case 2:
                    // retrieve color of all channels
                    int R2 = Color.red(pixel);
                    int G2 = Color.green(pixel);
                    int B2 = Color.blue(pixel);
                    // take conversion up to one single value

                    if (G2 <= R2 || G2 <= B2) {
                        G2 = R2 = B2 = (int) (0.299 * R2 + 0.587 * G2 + 0.114 * B2);
                        // set new pixel color to output bitmap

                    }
                    pixels[i] = Color.rgb(R2,G2,B2);
                    break;
                case 3:
                    // retrieve color of all channels
                    int R3 = Color.red(pixel);
                    int G3 = Color.green(pixel);
                    int B3 = Color.blue(pixel);
                    if (B3 <= R3 || B3 <= G3)
                        B3 = R3 = G3 = (int) (0.299 * R3 + 0.587 * G3 + 0.114 * B3);

                    pixels[i] = Color.rgb(R3,G3,B3);
                    break;
                default:
                    return null;
            }
            bm.setPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        }
        bitmap = bm;
        return bm;
    }

    @Override
    public Bitmap applyConvolution(int[][] mask){
        int size = mask.length;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int[] pixels = new int[width *height];
        int[] pixels2 = new int[width *height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        int pixels2d[][] = new int[width][height];


        for(int i=0; i<width;i++)  //A bit slower execution, but reading the code gets easier.
            for(int j=0;j<height;j++)
                pixels2d[i][j] = pixels[(j*width) + i];

        int divide = 0;
        for(int k = 0; k < size; k++){
            for(int l = 0; l<size; l++){
                divide += mask[k][l];
            }
        }
        Log.d("TAG", Integer.toString(divide));



        Bitmap bmC = bitmap.copy(bitmap.getConfig(),true);
        for(int i = size/2; i< width - size/2; i++ ){
            for(int j = size/2; j < height - size/2; j++){


                int modifyPixelR = 0;
                int modifyPixelG = 0;
                int modifyPixelB = 0;
                for(int k = 0; k < size; k++){
                    for(int l = 0; l<size; l++){
                        modifyPixelR+= mask[k][l]*Color.red(pixels2d[i+k-(size/2)][j+ l-(size/2)]);
                        modifyPixelG+= mask[k][l]*Color.green(pixels2d[i+k-(size/2)][j+ l-(size/2)]);
                        modifyPixelB+= mask[k][l]*Color.blue(pixels2d[i+k-(size/2)][j+ l-(size/2)]);

                    }
                }
                if(divide != 0) {
                    modifyPixelR /= divide;
                    modifyPixelG /= divide;
                    modifyPixelB /= divide;
                }
                int color = Color.rgb(modifyPixelR, modifyPixelG, modifyPixelB);

                pixels[(j*width) + i]= color;


            }
        }
        bmC.setPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        return bmC;
    }

}