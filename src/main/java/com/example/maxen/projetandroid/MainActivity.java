package com.example.maxen.projetandroid;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int SELECT_FILE = 0;
    private Image image;
    private Button egalHist, colorBtn;
    private Button redBtn, greenBtn, blueBtn, sypiaBtn, majBtn, penBtn;
    private TextView luminosityTv;
    private SeekBar luminosityBar;
    private ImageView imageView;



    // These matrices will be used to scale points of the image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // The 3 states (events) which the user is trying to perform
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // these PointF objects are used to record the point(s) the user is touching
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;



/********************************** MUST READ *****************************************************
*
*
*
* ALL OF THESE FUNCTIONS ARE USED MOSTLY FOR THE USER INTERFACE, THERE ISN'T ANY CALCULATION HERE.
*
*
*
************************************************************************************************** */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        luminosityTv = (TextView) findViewById(R.id.luminosityTv);
        luminosityBar = (SeekBar) findViewById(R.id.seekBarLuminosity);
        egalHist = (Button) findViewById(R.id.egalhisto);
        colorBtn = (Button) findViewById(R.id.colorBtn);
        redBtn = (Button) findViewById(R.id.buttRed);
        blueBtn = (Button) findViewById(R.id.buttBlue);
        greenBtn = (Button) findViewById(R.id.buttGreen);
        sypiaBtn = (Button) findViewById(R.id.buttSypia);
        majBtn = (Button) findViewById(R.id.buttMAJ);
        imageView = (ImageView) findViewById(R.id.imageView);
        penBtn = (Button) findViewById(R.id.btnPen);

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ImageView v = (ImageView) view;
                zoomInZoomOut(view, motionEvent);
                return true;

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return true;
    }

    public void clickLuminosity(MenuItem item) {
        luminosityBar.setVisibility(View.VISIBLE);
        luminosityTv.setVisibility(View.VISIBLE);
        majBtn.setVisibility(View.VISIBLE);

        colorBtn.setVisibility(View.INVISIBLE);
        egalHist.setVisibility(View.INVISIBLE);
        egalHist.setVisibility(View.INVISIBLE);
        redBtn.setVisibility(View.INVISIBLE);
        greenBtn.setVisibility(View.INVISIBLE);
        blueBtn.setVisibility(View.INVISIBLE);
        penBtn.setVisibility(View.INVISIBLE);
        sypiaBtn.setVisibility(View.INVISIBLE);
    }


    public void clickEgalisation(MenuItem item) {
        egalHist.setVisibility(View.VISIBLE);

        majBtn.setVisibility(View.INVISIBLE);
        colorBtn.setVisibility(View.INVISIBLE);
        luminosityBar.setVisibility(View.INVISIBLE);
        luminosityTv.setVisibility(View.INVISIBLE);
        redBtn.setVisibility(View.INVISIBLE);
        greenBtn.setVisibility(View.INVISIBLE);
        blueBtn.setVisibility(View.INVISIBLE);
        sypiaBtn.setVisibility(View.INVISIBLE);
        penBtn.setVisibility(View.INVISIBLE);
    }

    public void clickCouleur(MenuItem item) {
        colorBtn.setVisibility(View.VISIBLE);
        redBtn.setVisibility(View.VISIBLE);
        greenBtn.setVisibility(View.VISIBLE);
        blueBtn.setVisibility(View.VISIBLE);
        sypiaBtn.setVisibility(View.VISIBLE);

        majBtn.setVisibility(View.INVISIBLE);
        penBtn.setVisibility(View.INVISIBLE);
        egalHist.setVisibility(View.INVISIBLE);
        luminosityBar.setVisibility(View.INVISIBLE);
        luminosityTv.setVisibility(View.INVISIBLE);

    }

    public void clickPen(MenuItem item) {
        penBtn.setVisibility(View.VISIBLE);

        colorBtn.setVisibility(View.INVISIBLE);
        redBtn.setVisibility(View.INVISIBLE);
        greenBtn.setVisibility(View.INVISIBLE);
        blueBtn.setVisibility(View.INVISIBLE);
        sypiaBtn.setVisibility(View.INVISIBLE);
        majBtn.setVisibility(View.INVISIBLE);
        egalHist.setVisibility(View.INVISIBLE);
        luminosityBar.setVisibility(View.INVISIBLE);
        luminosityTv.setVisibility(View.INVISIBLE);
    }


    public void clickMoyenneur(MenuItem item) {
        int[][] mask = new int[3][3];

        for(int i = 0; i< 3;i++)
            for(int j = 0; j< 3;j++)
                mask[i][j] = 1;

        imageView.setImageBitmap(image.applyConvolution(mask));
    }

    public void clickGauss(MenuItem item) {
        int[][] mask = {{1,2,3,2,1},
                {2,6,8,6,2},
                {3,8,10,8,3},
                {2,6,8,6,2},
                {1,2,3,2,1}};

        imageView.setImageBitmap(image.applyConvolution(mask));
    }

    public void clickSobH(MenuItem item) {
        int[][] mask = {{-1,0,1},{-2,0,2},{-1,0,1}};
        imageView.setImageBitmap(image.applyConvolution(mask));
    }

    public void clickSobV(MenuItem item) {
        int[][] mask = {{1,2,1},{0,0,0},{-1,-2,-1}};
        imageView.setImageBitmap(image.applyConvolution(mask));
    }

    public void clickLaplacien(MenuItem item) {
        int[][] mask = {{0,1,0},{1,-4,1},{0,1,0}};
        imageView.setImageBitmap(image.applyConvolution(mask));
    }

    public void clickToGrayBtn(View view) {
        imageView.setImageBitmap(image.toGray());
    }

    public void egalHisto(View view) {
        Bitmap bm = image.histogramEqualization();
        if(bm != null)
            imageView.setImageBitmap(bm);
        else{
            Toast.makeText(getApplicationContext(), "Erreur durant l'égalisation d'histogramme (essayez de passer l'image en niveau de gris).",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void buGallery(View view) {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    public void buCamera(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = ImageFile.createTempImageFile(this);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:

                    // Get the dimensions of the View
                    int targetW = imageView.getWidth();
                    int targetH = imageView.getHeight();

                    // Get the dimensions of the bitmap
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    bmOptions.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(ImageFile.getmCurrentPhotoPath(), bmOptions);
                    int photoW = bmOptions.outWidth;
                    int photoH = bmOptions.outHeight;

                    // Determine how much to scale down the image
                    int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

                    // Decode the image file into a Bitmap sized to fill the View
                    bmOptions.inJustDecodeBounds = false;
                    bmOptions.inSampleSize = scaleFactor;
                    bmOptions.inPurgeable = true;

                    Bitmap bitmap = BitmapFactory.decodeFile(ImageFile.getmCurrentPhotoPath(), bmOptions);
                    imageView.setImageBitmap(bitmap);
                    image = new Image(bitmap);
                    break;
                case SELECT_FILE:

                    final Uri imageUri = data.getData();
                    final InputStream imageStream;
                    try {
                        imageStream = getContentResolver().openInputStream(imageUri);

                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        imageView.setImageBitmap(selectedImage);
                        image = new Image(selectedImage);
                    }
                    catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
            }


        }
    }

    public void buSave(View view) {

        ImageFile.startSave(image.getBitmap(),this);

    }

    public void sepia(View view) {
        imageView.setImageBitmap(image.sepia());
    }

    public void majobleu(View view) {
        imageView.setImageBitmap(image.isolateRGB(3));
    }

    public void majovert(View view) {
        imageView.setImageBitmap(image.isolateRGB(2));
    }

    public void majorouge(View view) {
        imageView.setImageBitmap(image.isolateRGB(1));
    }

    public void luminosityClick(View view) {
        imageView.setImageBitmap(image.updateLuminosity(luminosityBar.getMax(), luminosityBar.getProgress()));
    }

    public boolean zoomInZoomOut(View v, MotionEvent event) {
        ImageView view = (ImageView) v;
        view.setScaleType(ImageView.ScaleType.MATRIX);
        float scale;

        // Handle touch events here...

        switch (event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN: // first finger down only
                matrix.set(view.getImageMatrix());
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                mode = DRAG;
                break;

            case MotionEvent.ACTION_UP: // first finger lifted

            case MotionEvent.ACTION_POINTER_UP: // second finger lifted

                mode = NONE;
                break;

            case MotionEvent.ACTION_POINTER_DOWN: // first and second finger down

                oldDist = spacing(event);
                if (oldDist > 5f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if (mode == DRAG)
                {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y); // create the transformation in the matrix  of points
                }
                else if (mode == ZOOM)
                {
                    // pinch zooming
                    float newDist = spacing(event);
                    if (newDist > 5f)
                    {
                        matrix.set(savedMatrix);
                        scale = newDist / oldDist;
                        /* setting the scaling of the matrix...
                           if scale > 1 means zoom in...
                           if scale < 1 means zoom out */
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }

        view.setImageMatrix(matrix); // display the transformation on screen

        return true; // indicate event was handled
    }

    /**
     * --------------------------------------------------------------------------
     * Method: spacing Parameters:
      * MotionEvent Returns: float
      * Description: checks the spacing between the two fingers on touch
     * ----------------------------------------------------
     */

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * --------------------------------------------------------------------------
     * Method: midPoint
     * Parameters: PointF object
     * MotionEvent Returns: void
     * Description: calculates the midpoint between the two fingers
     * ------------------------------------------------------------
     */

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }


    public void clickReset(MenuItem item) {
        imageView.setImageBitmap(image.getOriginalBitmap());
        image.setBitmap(image.getOriginalBitmap());
    }


    public void clickToGray(View view) {
        imageView.setImageBitmap(image.toGray());
    }


    public void btnClickPen(View view) {
        imageView.setImageBitmap(image.pencilEffect());
    }


}
