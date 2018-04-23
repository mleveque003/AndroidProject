package com.example.maxen.projetandroid;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by maxen on 09/02/2018.
 * This class extends the filters interface and all the code is implemented here.
 * The class contains every image calcultaion.
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

    Image(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.originalBitmap = bitmap;
    }

    /**
     * Returns the grayscale of the actual bitmap on screen and replace the bitmap with this new image.
     * The function will also stretch the grayscale which causes the image to have the lowest grayscale moved t 0 and the highest moved to 255.
     * This permits to have a nicer greyscale image.
     *
     * @return the grayscale image
     */
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


    /**
     * Returns a new bitmap with adapted luminosity created from the progress bar.
     * The method will basically make a percentage of the progress/max and multiply every pixel value in the image.
     *
     * @param max       max value of the luminosity seekbar
     * @param progress  value of the pointer in the seekbar
     * @return          the luminosity-modified image.
     */
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


    /**
     * Returns a grayscale image with an equalized histogram from a grayscale image.
     * In order to equalize the histogram, we need to calculate the histogram and cumulated histogram of the image.
     * Then, for each pixel, we have to calculate his value equalized and put it in a new bitmap which will be returned.
     * @return the equalized image
     */
    @Override
    public Bitmap histogramEqualization() {
        Bitmap bm = bitmap.copy(bitmap.getConfig(), true);
        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        bm.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        int histogram[] = histogramCalculation(pixels);
        int histoCumul[] = histoCumulCalculation(histogram);

        int[] newPixels = new int[pixels.length];
        for(int i =0; i<pixels.length; i++){
            int red =(histoCumul[Color.red(pixels[i])]*256)/pixels.length;
            int pixel = Color.rgb(red,red,red);
            if(red>255 ||red<0)
                Log.d("TAG", Integer.toString(red));
            newPixels[i] = pixel;
        }
        bm.setPixels(newPixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        return bm;
    }

    /**
     * Calculates the histogram from a list of pixels.
     * @param pixels    the list of pixels of the image
     * @return          the image's histogram
     */
    private int[] histogramCalculation(int[] pixels){
        int[] histogram = new int[256];
        for(int i = 0; i < pixels.length; ++i){
            int k = Color.red(pixels[i]);
            histogram[k]++;
        }
        return histogram;
    }

    /**
     * Calculates the cumulated histogram from the histogram.
     * @param histogram the image's histogram
     * @return          the image's cumulated histogram
     */
    private int[] histoCumulCalculation(int[] histogram){
        int histoCumul[] = new int[256];

        for(int i = 1; i<256;++i){
            histoCumul[i]=histogram[i]+histoCumul[i-1];
            Log.e("TAG", "hist : " + Integer.toString(histogram[i]) + " cum : " + Integer.toString(histoCumul[i]));
        }

        return histoCumul;
    }

    /**
     * Calculates the image with a sepia filter from any given image.
     * It is close to a grayscale filter but with the depth adding more green and red color, to get the "yellow" we
     * get with a sephia filter.
     * 
     * @return the sephia filtered image
     */
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

        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        bmpSephia.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        for(int i=0; i < pixels.length; i++) {
                c = pixels[i];

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
                pixels[i] = Color.rgb(r, g, b);
        }
        bmpSephia.setPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        bitmap = bmpSephia;
        return bmpSephia;
    }

    /**
     * Returns a bitmap, which, with one selected color, will isolate the pixels where the color is more present than the 2 others.
     * @param c The color to isolate (1 = red, 2 = green, 3 = blue)
     * @return  The image with 1 color isolated.
     */
    public Bitmap isolateRGB(int c){ //c : 1 = red, 2 = green, 3 = blue
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

    /**
     * Applies a convolution filter on the actual image.
     * Uses the given mask to apply the convolution using the methos we saw during class
     * @param mask  The mask to apply the convolution filter with, must be squared and any non-digit number size.
     * @return      The image with the mask applied.
     */
    @Override
    public Bitmap applyConvolution(int[][] mask){
        int size = mask.length;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int[] pixels = new int[width *height];

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
        Log.d("TAG divide", Integer.toString(divide));



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
        bitmap = bmC;
        return bmC;
    }


    /**
     * Returns an image with a drawn-with-a-pencil effect. To do so, we :
     * 1 - apply a gaussian convolution to reove noise
     * 2 - turn the image to grayscale
     * 3 - apply a laplacian convolution
     * 4 - remove negative value
     *
     * @return  the image with the applied effect.
     */
    @Override
    public Bitmap pencilEffect() {
        Bitmap bm = bitmap.copy(bitmap.getConfig(),true);

        //Method followed :  AUTOMATIC GENERATION OF PENCIL-SKETCH LIKE DRAWINGS
        //                   FROM PERSONAL PHOTOS.
        // By Jin Zhou and Baoxin Li.
        //https://pdfs.semanticscholar.org/0268/bc91c9e374062633af7481b4f2c04cca1db5.pdf
        //Due to a lack of time and multiple problems, we had to modify the method to get something that looks like a pen effect but which isn't really respecting the method.


        int[][] gaussMask = {{1,4,7,4,1},{4,16,26,16,4},{7,26,41,26,7},{4,16,26,16,4},{1,4,7,4,1}};
        //Gaussian filter mask.
        bm = applyConvolution(gaussMask);
        //Using this mask helps removing the noise of an image.

        bm = toGray();

        int[][] laplaceMask = {{0,-1,0},{-1,4,-1},{0,-1,0}};
        bm = applyConvolution(laplaceMask);

        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        for(int i = 0; i < pixels.length; ++i) {

            int pix = pixels[i];

            int R = Color.red(pix);

            if(R <= 0){
                pix = Color.rgb(255,255,255);
            }
            pixels[i] = pix;
        }
        bm.setPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        return bm;
    }

}