package com.example.maxen.projetandroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Image image;
    private Button toGray,egalHist, colorBtn,
            luminosityBtn;
    private TextView luminosityTv;
    private SeekBar luminosityBar;

    private ArrayList<Object> viewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toGray = (Button) findViewById(R.id.toGrayBtn);
        luminosityTv = (TextView) findViewById(R.id.luminosityTv);
        luminosityBar = (SeekBar) findViewById(R.id.seekBarLuminosity);
        egalHist = (Button) findViewById(R.id.egalhisto);
        colorBtn = (Button) findViewById(R.id.colorBtn);
        luminosityBtn = (Button) findViewById(R.id.luminosityBtn);
        imageView = (ImageView) findViewById(R.id.imageView);


        viewList = new ArrayList<Object>(); //This list is used to help in the reading of the code (it cancels redundant lines).
        //TODO Later
        viewList.add(toGray);
        viewList.add(luminosityTv);
        viewList.add(luminosityBar);
        viewList.add(egalHist);
        viewList.add(colorBtn);

        Bundle bundle = getIntent().getExtras();

        Uri imageUri = getIntent().getData();
        imageView.setImageURI(imageUri);

        Bitmap bmp = BitmapFactory.decodeByteArray(
                getIntent().getByteArrayExtra("picture"),0,getIntent().getByteArrayExtra("byteArray").length);
        if(bmp != null)
            imageView.setImageBitmap(bmp);
    }


    /*
    * CREATION OF THE TOP RIGHT MENU
    * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return true;
    }

    /*
     * END
     */

    /*
    * MENU CLICKS - USUALLY TRIGGERS THE APPARITION AND DISAPPARITION OF BUTTONS RELATED TO THE OPTION CLICKED
    * */

    public void clickToGray(MenuItem item) {
        toGray.setVisibility(View.VISIBLE);

        luminosityBtn.setVisibility(View.INVISIBLE);
        colorBtn.setVisibility(View.INVISIBLE);
        luminosityBar.setVisibility(View.INVISIBLE);
        luminosityTv.setVisibility(View.INVISIBLE);
        egalHist.setVisibility(View.INVISIBLE);
    }

    public void clickLuminosity(MenuItem item) {
        luminosityBar.setVisibility(View.VISIBLE);
        luminosityTv.setVisibility(View.VISIBLE);
        luminosityBtn.setVisibility(View.VISIBLE);


        colorBtn.setVisibility(View.INVISIBLE);
        toGray.setVisibility(View.INVISIBLE);
        egalHist.setVisibility(View.INVISIBLE);
        egalHist.setVisibility(View.INVISIBLE);
    }

    public void clickConstrast(MenuItem item) {
        //TODO

        luminosityBtn.setVisibility(View.INVISIBLE);
        egalHist.setVisibility(View.INVISIBLE);
        colorBtn.setVisibility(View.INVISIBLE);
        toGray.setVisibility(View.INVISIBLE);
        luminosityBar.setVisibility(View.INVISIBLE);
        luminosityTv.setVisibility(View.INVISIBLE);
    }

    public void clickEgalisation(MenuItem item) {
        egalHist.setVisibility(View.VISIBLE);

        luminosityBtn.setVisibility(View.INVISIBLE);
        colorBtn.setVisibility(View.INVISIBLE);
        toGray.setVisibility(View.INVISIBLE);
        luminosityBar.setVisibility(View.INVISIBLE);
        luminosityTv.setVisibility(View.INVISIBLE);
    }

    public void clickCouleur(MenuItem item) {
        colorBtn.setVisibility(View.VISIBLE);

        luminosityBtn.setVisibility(View.INVISIBLE);
        egalHist.setVisibility(View.INVISIBLE);
        toGray.setVisibility(View.INVISIBLE);
        luminosityBar.setVisibility(View.INVISIBLE);
        luminosityTv.setVisibility(View.INVISIBLE);
    }

    /*
     * BUTTON CLICKS - USUALLY TRIGGERS PICTURE MODIFICATIONS
     * */

    public void clickToGrayBtn(View view) {
        //TODO
    }


    public void egalHisto(View view) {
        //TODO
    }


    public void luminosityClick(View view) {
    }
}
