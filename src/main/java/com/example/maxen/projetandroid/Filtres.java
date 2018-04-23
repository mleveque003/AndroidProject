package com.example.maxen.projetandroid;

import android.graphics.Bitmap;

/**
 * Created by maxen on 09/02/2018.
 */

public interface Filtres {
    Bitmap toGray();
    Bitmap updateLuminosity(int max, int progress);
    Bitmap histogramEqualization();
    Bitmap sepia();
    Bitmap isolateRGB(int c);
    Bitmap applyConvolution(int[][] mask);
    Bitmap pencilEffect();
}
