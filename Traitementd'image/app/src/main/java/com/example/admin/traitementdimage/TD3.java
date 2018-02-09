package com.example.admin.traitementdimage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.GeomagneticField;
import android.hardware.camera2.params.ColorSpaceTransform;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TD3 extends AppCompatActivity {

    ImageView imageView, imageView2;
    Button bBackToTD2, bGoToTD4, bContraste, bcontraste2, bNoirEtBlanc, bNoirEtBlanc2, bInitialise, bInitialize2, bMaxMin1, bMaxMin2;
    Bitmap bitmap, bitmap2;
    TextView textView;
    static int max = 0;
    static int min = 255;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_td3);

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        bBackToTD2 = (Button) findViewById(R.id.buttBackToTD2);
        bGoToTD4 = (Button) findViewById(R.id.buttGoToTD4);
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ana);
        bitmap2 = BitmapFactory.decodeResource(getResources(),R.drawable.ana2);
        textView = (TextView) findViewById(R.id.textView2);
        bNoirEtBlanc = (Button) findViewById(R.id.buttBetW);
        bNoirEtBlanc2 = (Button) findViewById(R.id.buttBetW2);
        bInitialise = (Button) findViewById(R.id.buttInitialize);
        bInitialize2 = (Button) findViewById(R.id.buttInitialize2);
        bContraste = (Button) findViewById(R.id.buttContraste1);
        bcontraste2 = (Button) findViewById(R.id.buttContraste2);
        bMaxMin1 = (Button) findViewById(R.id.buttMaxMin1);
        bMaxMin2 = (Button) findViewById(R.id.buttMaxMin2);


        bBackToTD2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(TD3.this, TD2.class);
                startActivity(myIntent);
            }
        });

        bNoirEtBlanc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap newBitmap = NoirEtBlanc(bitmap);
                imageView.setImageBitmap(newBitmap);
            }
        });

        bNoirEtBlanc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap newBitmap = NoirEtBlanc(bitmap2);
                imageView2.setImageBitmap(newBitmap);
            }
        });

        bInitialise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageBitmap(bitmap);
            }
        });

        bInitialize2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView2.setImageBitmap(bitmap2);
            }
        });

        bMaxMin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                max = maxi(bitmap,max);
                min = mini(bitmap,min);
                String taille = String.valueOf(min+ " "+ max);
                textView.setText(taille);
            }
        });

        bMaxMin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                max = maxi(bitmap2,max);
                min = mini(bitmap2,min);
                String taille = String.valueOf(min+ " "+ max);
                textView.setText(taille);
            }
        });

        bContraste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bitmap newBitmap = histograme(bitmap);
                imageView.setImageBitmap(newBitmap);
            }
        });

        bcontraste2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bitmap newBitmap = histograme(bitmap2);
                imageView2.setImageBitmap(newBitmap);
            }
        });

        bGoToTD4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TD3.this, TD4.class);
                startActivity(intent);
            }
        });
    }

    public static int maxi(Bitmap original, int max){
        int width = original.getWidth();
        int height = original.getHeight();
        int [] pixels = new int[width*height];
        int colorPixel;
        int R, G, B;

        original.getPixels(pixels,0,width,0,0,width,height);
        for (int x = 0; x < (width*height); x++){

            colorPixel = pixels[x];
            R = Color.red(colorPixel);
            G = Color.green(colorPixel);
            B = Color.blue(colorPixel);

            R = (R+B+G)/3;
            R += 20; if (R>255) R = 255;
            B = G = R;
            if (max<R) max = R;
            if (max<B) max = B;
            if (max<G) max = G;
        }
        return max;
    }

    public static int mini(Bitmap original, int min){
        int width = original.getWidth();
        int height = original.getHeight();
        int [] pixels = new int[width*height];
        int colorPixel;
        int  R, G, B;

        original.getPixels(pixels,0,width,0,0,width,height);
        for (int x = 0; x < (width*height); x++){

            colorPixel = pixels[x];
            R = Color.red(colorPixel);
            G = Color.green(colorPixel);
            B = Color.blue(colorPixel);
            R = (R+B+G)/3;
            R += 20; if (R>255) R = 255;
            B = G = R;

            if (min>R) min = R;
            if (min>B) min = B;
            if (min>G) min = G;
        }
        return min;
    }

    public static Bitmap NoirEtBlanc(Bitmap original){
        Bitmap finalImage = Bitmap.createBitmap(original.getWidth(), original.getHeight(), original.getConfig());
        int width = original.getWidth();
        int height = original.getHeight();
        int [] pixels = new int[width*height];
        int colorPixel;
        int  R, G, B;

        original.getPixels(pixels,0,width,0,0,width,height);
        for (int x = 0; x < (width*height); x++){

            colorPixel = pixels[x];
            R = Color.red(colorPixel);
            G = Color.green(colorPixel);
            B = Color.blue(colorPixel);

            R = G = B = ( R + B + G ) / 3;

            pixels[x] = Color.rgb(R,G,B);
        }



        finalImage.setPixels(pixels,0,width,0,0,width,height);

        return finalImage;
    }

    public static Bitmap contraste(Bitmap original, int max, int min){
        Bitmap finalImage = Bitmap.createBitmap(original.getWidth(), original.getHeight(), original.getConfig());
        int width = original.getWidth();
        int height = original.getHeight();
        int [] pixels = new int[width*height];
        int colorPixel;
        int  R, G, B;

        original.getPixels(pixels,0,width,0,0,width,height);
        for (int x = 0; x < (width*height); x++){

            colorPixel = pixels[x];
            R = Color.red(colorPixel);
            G = Color.green(colorPixel);
            B = Color.blue(colorPixel);

            R = G = B = ( R + B + G ) / 3;

            if (max < R) max = R;
            if (min > R) min = R;

            pixels[x] = Color.rgb(R,G,B);
        }

        for(int x = 0; x<(width*height); x++){
            colorPixel = pixels[x];
            R = Color.red(colorPixel);

           // R = (255 * (R - min)) / (max - min);

            R = ((R*(max-min))/255)+min;

            B = G = R;

            pixels[x] = Color.rgb(R, G, B);
        }


        finalImage.setPixels(pixels,0,width,0,0,width,height);

        return finalImage;
    }

    public static Bitmap contrasteCouleur(Bitmap original){
        Bitmap finalImage = Bitmap.createBitmap(original.getWidth(), original.getHeight(), original.getConfig());
        int width = original.getWidth();
        int height = original.getHeight();
        int [] pixels = new int[width*height];
        int colorPixel;
        int  R, G, B;

        original.getPixels(pixels,0,width,0,0,width,height);
        for (int x = 0; x < (width*height); x++){

            colorPixel = pixels[x];
            R = Color.red(colorPixel);
            G = Color.green(colorPixel);
            B = Color.blue(colorPixel);


            R = (255 * (R - 25)) / (230 - 25); if (R < 0) R = 0; if (R > 255) R = 255;
            G = (255 * (G - 25)) / (230 - 25); if (G < 0) G = 0; if (G > 255) G = 255;
            B = (255 * (B - 25)) / (230 - 25); if (B < 0) B = 0; if (B > 255) B = 255;

            pixels[x] = Color.rgb(R, G, B);
        }
        finalImage.setPixels(pixels,0,width,0,0,width,height);

        return finalImage;
    }

    public static Bitmap histograme(Bitmap original){
        Bitmap finalImage = Bitmap.createBitmap(original.getWidth(), original.getHeight(), original.getConfig());
        int width = original.getWidth();
        int height = original.getHeight();
        int [] pixels = new int[width*height];
        int colorPixel;
        int  R, G, B;
        int C = 0;

        original.getPixels(pixels,0,width,0,0,width,height);
        for (int x = 0; x < (width*height); x++){

            colorPixel = pixels[x];
            R = Color.red(colorPixel);
            G = Color.green(colorPixel);
            B = Color.blue(colorPixel);

            int color = ( R + B + G ) / 3;

            B = G = R = color;

            pixels[x] = Color.rgb(R,G,B);
        }

        int i = 0;

        for (int x = 0; x < width*height; x++){
            colorPixel = pixels[x];

            int couleur = Color.red(colorPixel);
            while(i<255){
                C += pixels[x];
            }

            couleur = (C*255)/(height*width);

            R = B = G = couleur;

            pixels[x] = Color.rgb(R,G,B);

        }

        finalImage.setPixels(pixels,0,width,0,0,width,height);

        return finalImage;
    }
}
