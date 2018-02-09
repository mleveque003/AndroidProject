package com.example.admin.traitementdimage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TD4 extends AppCompatActivity{

    private ImageView imageView;
    private Button bSave;
    private FloatingActionButton bCamera;
    private FloatingActionButton bGallery;
    static final int REQUEST_IMAGE_PICTURE = 1, SELECT_FILE = 0;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_td4);

        imageView = (ImageView) findViewById(R.id.imageID);
        bCamera = (FloatingActionButton) findViewById(R.id.buttCamera);
        bSave = (Button) findViewById(R.id.buttSave);
        bGallery = (FloatingActionButton) findViewById(R.id.buttGallery);



    }

    public static Bitmap viewToBitmap(View view, int width, int height){
        Bitmap bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public void buCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, REQUEST_IMAGE_PICTURE);
        }
    }

    public void buGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    public void buSave(View view) {

        dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("Save Image");
        dialog.setMessage("You sure to save your image ?");
        dialog.setButton("yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startSave();
            }
        });

        dialog.setButton2("no", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){

            if(requestCode == REQUEST_IMAGE_PICTURE){

                Bundle extras = data.getExtras();
                Bitmap bitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(bitmap);
            }

            if(requestCode == SELECT_FILE){

                Uri selectImageUri = data.getData();
                imageView.setImageURI(selectImageUri);
            }
        }
    }

    public void startSave(){
       FileOutputStream fileOutputStream = null;
       File file = getDisc();
       if (!file.exists() && !file.mkdirs()){
           Toast.makeText(this, "Can't create directory to save image", Toast.LENGTH_SHORT).show();
           return;
       }

       SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
       String date = simpleDateFormat.format(new Date());
       String name = "Img"+date+".jpg";
       String file_name = file.getAbsolutePath()+"/"+name;
       File new_file = new File(file_name);
       try {
           fileOutputStream = new FileOutputStream(new_file);
           Bitmap bitmap = viewToBitmap(imageView,imageView.getWidth(),imageView.getHeight());
           bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
           Toast.makeText(this,"Save image success",Toast.LENGTH_SHORT).show();
           fileOutputStream.flush();
           fileOutputStream.close();
       }catch (FileNotFoundException e){
           e.printStackTrace();
       }catch (IOException e){
           e.printStackTrace();
       }
       refreshGallery(new_file);
   }

    public void refreshGallery(File file){
        Intent intent = new Intent (Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }

    private File getDisc(){
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file, "Image demo");

    }


}


