package com.example.maxen.projetandroid;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class LaunchActivity extends AppCompatActivity {

    private static final String FILE_NAME = "temp.jpg";

    private static final int REQUEST_IMAGE_PICTURE = 1;
    private static final int SELECT_FILE = 0;
    private Button bCharger;
    private Button bPrendrePhoto;
    private ImageView img;
    private Image image;
    private Button egalHist, colorBtn,
            luminosityBtn, sepiaBtn, redBtn, greenBtn, blueBtn, penBtn;
    private TextView luminosityTv;
    private SeekBar luminosityBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        luminosityTv = (TextView) findViewById(R.id.luminosityTv);
        luminosityBar = (SeekBar) findViewById(R.id.seekBarLuminosity);
        egalHist = (Button) findViewById(R.id.egalhisto);
        colorBtn = (Button) findViewById(R.id.colorBtn);
        luminosityBtn = (Button) findViewById(R.id.luminosityBtn);
        sepiaBtn = (Button) findViewById(R.id.sepia);
        redBtn = (Button) findViewById(R.id.majoRouge);
        greenBtn = (Button) findViewById(R.id.majoVert);
        blueBtn = (Button) findViewById(R.id.majoBleu);
        penBtn = (Button) findViewById(R.id.btnPen);


        bCharger = (Button) findViewById(R.id.buttCharger);
        bPrendrePhoto = (Button) findViewById(R.id.buttPrendrePhoto);
        img = (ImageView) findViewById(R.id.imageView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_launcher_drawer, menu);
        return true;
    }


    public void buCharger(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    public void buPrendrePhoto(View view) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_PICTURE);


        }

     // dispatchTakePictureIntent();
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            //...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_PICTURE);
            }
        }
    }

    /*
    startCamera() -> Start the camera to be able to take a photo
     */
    public void startCamera() {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, 1);
    }


    /*
    getCameraFile() -> Returns the photo taken with the camera.
     */
    public File getCameraFile() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){

            if(requestCode == REQUEST_IMAGE_PICTURE){

                Bundle extras = data.getExtras();
                Bitmap bitmap = (Bitmap) extras.get("data");

                img.setImageBitmap(bitmap);
                image = new Image(bitmap);

            }

            if(requestCode == SELECT_FILE){

                final Uri imageUri = data.getData();
                final InputStream imageStream;
                try {
                    imageStream = getContentResolver().openInputStream(imageUri);

                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                img.setImageBitmap(selectedImage);
                image = new Image(selectedImage);
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void clickLuminosity(MenuItem item) {
        luminosityBar.setVisibility(View.VISIBLE);
        luminosityTv.setVisibility(View.VISIBLE);
        luminosityBtn.setVisibility(View.VISIBLE);


        colorBtn.setVisibility(View.INVISIBLE);
        egalHist.setVisibility(View.INVISIBLE);
        egalHist.setVisibility(View.INVISIBLE);
        sepiaBtn.setVisibility(View.INVISIBLE);
        redBtn.setVisibility(View.INVISIBLE);
        greenBtn.setVisibility(View.INVISIBLE);
        blueBtn.setVisibility(View.INVISIBLE);
    }



    public void clickEgalisation(MenuItem item) {
        egalHist.setVisibility(View.VISIBLE);

        luminosityBtn.setVisibility(View.INVISIBLE);
        colorBtn.setVisibility(View.INVISIBLE);
        luminosityBar.setVisibility(View.INVISIBLE);
        luminosityTv.setVisibility(View.INVISIBLE);
        sepiaBtn.setVisibility(View.INVISIBLE);
        redBtn.setVisibility(View.INVISIBLE);
        greenBtn.setVisibility(View.INVISIBLE);
        blueBtn.setVisibility(View.INVISIBLE);
    }

    public void clickCouleur(MenuItem item) {
        colorBtn.setVisibility(View.VISIBLE);
        sepiaBtn.setVisibility(View.VISIBLE);
        redBtn.setVisibility(View.VISIBLE);
        greenBtn.setVisibility(View.VISIBLE);
        blueBtn.setVisibility(View.VISIBLE);

        luminosityBtn.setVisibility(View.INVISIBLE);
        egalHist.setVisibility(View.INVISIBLE);
        luminosityBar.setVisibility(View.INVISIBLE);
        luminosityTv.setVisibility(View.INVISIBLE);
    }

    public void luminosityClick(View view) {
        img.setImageBitmap(image.updateLuminosity(luminosityBar.getMax(), luminosityBar.getProgress()));
    }

    public void egalHisto(View view) {
        Bitmap bm = image.egalisationHistogramme();
        if(bm != null)
            img.setImageBitmap(bm);
        else{
            Toast.makeText(getApplicationContext(), "Erreur durant l'égalisation d'histogramme (essayez de passer l'image en niveau de gris).",
                    Toast.LENGTH_LONG).show();
        }

    }

    public void clickToGrayBtn(View view) {
        img.setImageBitmap(image.toGray());
    }

    public void clickReset(MenuItem item) {
        img.setImageBitmap(image.getOriginalBitmap());
        image.setBitmap(image.getOriginalBitmap());
    }

    public void sepia(View view) {
        img.setImageBitmap(image.sepia());
    }

    public void majobleu(View view) {
        img.setImageBitmap(image.majorerRGB(3));
    }

    public void majovert(View view) {
        img.setImageBitmap(image.majorerRGB(2));
    }

    public void majorouge(View view) {
        img.setImageBitmap(image.majorerRGB(1));
    }
}

