package com.example.admin.traitementdimage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;




public class TD2 extends AppCompatActivity {

    ImageView imageView;
    Bitmap bitmap;
    TextView textView;
    Button bToGray, bToGray2, bInitialize, bColorize, bBlackLine, bGetSIze, bToRed, bGoToTD3, bToGray3;
    BitmapFactory.Options options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_td2);

        imageView = (ImageView) findViewById(R.id.imageViewID);
        textView = (TextView) findViewById(R.id.textViewID);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.zzz);
        bGetSIze = (Button) findViewById(R.id.buttGetSize);
        bBlackLine = (Button) findViewById(R.id.buttBlackLine);
        bToGray  = (Button) findViewById(R.id.buttToGray);
        bToGray2 = (Button) findViewById(R.id.buttToGray2);
        bInitialize = (Button) findViewById(R.id.buttInitialize);
        bColorize = (Button) findViewById(R.id.buttColorize);
        bToRed = (Button) findViewById(R.id.buttToRed);
        options = new BitmapFactory.Options();
        bGoToTD3 = (Button) findViewById(R.id.buttGoToTd3);
        bToGray3 = (Button) findViewById(R.id.buttToGray3);


        bGetSIze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapFactory.decodeResource(getResources(), R.drawable.zzz, options);

                int width = options.outWidth;
                int height = options.outHeight;
                String taille = String.valueOf(width + " X " + height);

                textView.setText(taille);
            }
        });

        bBlackLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  Bitmap newBitmap = blackLine(bitmap);
                  imageView.setImageBitmap(newBitmap);
            }
        });

        bInitialize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap newBitmap = bitmap;
                imageView.setImageBitmap(newBitmap);
            }
        });

        bToGray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap newBitmap = toGray(bitmap);
                imageView.setImageBitmap(newBitmap);
            }
        });

        bToGray2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap newBitmap = toGray2(bitmap);
                imageView.setImageBitmap(newBitmap);
            }
        });

        bColorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap newBitmap = colorize(bitmap);
                imageView.setImageBitmap(newBitmap);
            }
        });

        bToRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap newBitmap = toRed(bitmap);
                imageView.setImageBitmap(newBitmap);
            }
        });

        bGoToTD3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(TD2.this, TD3.class);
                startActivity(myIntent);
            }
        });

        bToGray3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap newBitmap = toGray3(bitmap);
                imageView.setImageBitmap(newBitmap);
            }
        });
    }

    public static Bitmap blackLine(Bitmap original){

        Bitmap finalImage = Bitmap.createBitmap(original.getWidth(), original.getHeight(), original.getConfig());

        int width = original.getWidth();
        int height = original.getHeight();
        int pixels[] = new int[width*height];


        original.getPixels(pixels,0,width,0,0,width,height);
        finalImage.setPixels(pixels,0,width,0,0,width,height);

        for (int x=0; x<width; x++) {
            for (int y = ((height/2)-10); y < ((height/2)+10); y++) {
                finalImage.setPixel(x, y, 0xff000000);
            }
        }
        return finalImage;
    }

    public static Bitmap toGray(Bitmap original){
        Bitmap finalImage = Bitmap.createBitmap(original.getWidth(), original.getHeight(), original.getConfig());

        int A,R,G,B;
        int colorPixel;
        int width = original.getWidth();
        int height = original.getHeight();

        for(int x=0; x<width; x++){
            for(int y=0; y<height; y++){
                colorPixel = original.getPixel(x,y);
                A = Color.alpha(colorPixel);
                R = Color.red(colorPixel);
                G = Color.green(colorPixel);
                B = Color.blue(colorPixel);

                B = (R + G + B) / 3;
                G = B;
                R = B;

                finalImage.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        return finalImage;

    }

    public static Bitmap toGray2(Bitmap original){
        Bitmap finalImage = Bitmap.createBitmap(original.getWidth(), original.getHeight(), original.getConfig());
        int width = original.getWidth();
        int height = original.getHeight();
        int [] pixels = new int[width*height];
        int colorPixel;
        int A, R, G, B;

        original.getPixels(pixels,0,width,0,0,width,height);

        for (int x = 0; x < (width*height); x++){
            colorPixel = pixels[x];
            A = Color.alpha(colorPixel);
            R = Color.red(colorPixel);
            G = Color.green(colorPixel);
            B = Color.blue(colorPixel);

            B = (R + G + B) / 3;
            G = B;
            R = B;

            pixels[x] = Color.argb(A, R, G, B);
        }

        finalImage.setPixels(pixels,0,width,0,0,width,height);

        return finalImage;
    }

    public static Bitmap colorize(Bitmap original){
        Bitmap finalImage = Bitmap.createBitmap(original.getWidth(), original.getHeight(), original.getConfig());
        int width = original.getWidth();
        int height = original.getHeight();
        int [] pixels = new int[width*height];
        int colorPixel;
        int A, R, G, B ;


        original.getPixels(pixels,0,width,0,0,width,height);
        float [] outhsl = new float[3];

        for (int x = 0; x < (width*height); x++){
            colorPixel = pixels[x];
            A = Color.alpha(colorPixel);
            R = Color.red(colorPixel);
            G = Color.green(colorPixel);
            B = Color.blue(colorPixel);

            ColorUtils.RGBToHSL(R,G,B,outhsl);
            outhsl[0] = 150;
            pixels[x] = ColorUtils.HSLToColor(outhsl);
        }

        finalImage.setPixels(pixels,0,width,0,0,width,height);
        return finalImage;

    }

    public static Bitmap toRed(Bitmap original){
        Bitmap finalImage = Bitmap.createBitmap(original.getWidth(), original.getHeight(), original.getConfig());
        int width = original.getWidth();
        int height = original.getHeight();
        int [] pixels = new int[width*height];
        int colorPixel;
        int R, G, B;

        original.getPixels(pixels,0,width,0,0,width,height);

        for (int x = 0; x < (width*height); x++){
            colorPixel = pixels[x];
            G = Color.green(colorPixel);
            B = Color.blue(colorPixel);
            R = Color.red(colorPixel);

            G = B = (R+G+B)/3;
            


            pixels[x] = Color.rgb(R,G,B);
        }

        finalImage.setPixels(pixels,0,width,0,0,width,height);

        return finalImage;
    }

    public static Bitmap toGray3(Bitmap original){
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

            //B = (R + G + B)/3;

            B += 20; if (B>255) B = 255;

            R += 20; if(R>255) R= 255;

            G += 20; if(G>255) G= 255;


            pixels[x] = Color.rgb(R, G, B);
        }

        finalImage.setPixels(pixels,0,width,0,0,width,height);

        return finalImage;
    }
}
