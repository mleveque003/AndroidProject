package com.example.maxen.projetandroid;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ImageViewP imageView;
    private Image image;
    private Button toGray,egalHist, colorBtn;
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

        viewList = new ArrayList<Object>();

        viewList.add(toGray);
        viewList.add(luminosityTv);
        viewList.add(luminosityBar);
        viewList.add(egalHist);
        viewList.add(colorBtn);
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
        Toast.makeText(getApplicationContext(), "This is my Toast message!",
                Toast.LENGTH_LONG).show(); //Delete when operationnal
        toGray.setVisibility(View.VISIBLE);

        colorBtn.setVisibility(View.INVISIBLE);
        luminosityBar.setVisibility(View.INVISIBLE);
        luminosityTv.setVisibility(View.INVISIBLE);
        egalHist.setVisibility(View.INVISIBLE);
    }

    public void clickLuminosity(MenuItem item) {
        luminosityBar.setVisibility(View.VISIBLE);
        luminosityTv.setVisibility(View.VISIBLE);

        colorBtn.setVisibility(View.INVISIBLE);
        toGray.setVisibility(View.INVISIBLE);
        egalHist.setVisibility(View.INVISIBLE);
        egalHist.setVisibility(View.INVISIBLE);
    }

    public void clickConstrast(MenuItem item) {
        //TODO

        egalHist.setVisibility(View.INVISIBLE);
        colorBtn.setVisibility(View.INVISIBLE);
        toGray.setVisibility(View.INVISIBLE);
        luminosityBar.setVisibility(View.INVISIBLE);
        luminosityTv.setVisibility(View.INVISIBLE);
    }

    public void clickEgalisation(MenuItem item) {
        egalHist.setVisibility(View.VISIBLE);

        colorBtn.setVisibility(View.INVISIBLE);
        toGray.setVisibility(View.INVISIBLE);
        luminosityBar.setVisibility(View.INVISIBLE);
        luminosityTv.setVisibility(View.INVISIBLE);
    }

    public void clickCouleur(MenuItem item) {
        colorBtn.setVisibility(View.VISIBLE);

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

   
}
