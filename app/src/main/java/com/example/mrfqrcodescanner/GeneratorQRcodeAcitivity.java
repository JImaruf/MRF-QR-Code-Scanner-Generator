package com.example.mrfqrcodescanner;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.ContentValues.TAG;
import static android.os.Build.VERSION.SDK_INT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class GeneratorQRcodeAcitivity extends AppCompatActivity {
    private TextView grCodeTv;
    private Button gnrtBtn,saveimgBTN;
    TextInputEditText dataEdt;
    private QRGEncoder qrgEncoder;
    Bitmap bitmap;
    ImageView qrimg;
    String data;
    OutputStream outputStream;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator_qrcode_acitivity);


        grCodeTv = findViewById(R.id.tvgenerator);
        gnrtBtn =findViewById(R.id.GnQRrbtn);
        dataEdt = findViewById(R.id.textinputet);
        qrimg =findViewById(R.id.imgQRcode);
        saveimgBTN=findViewById(R.id.saveQRcode);

        gnrtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateQR();

            }
        });
        saveimgBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPermission()){
                    showPermissionDialog();
                }
                
                else
                {
                    saveImage();
                }
            }
        });
    }

    private void saveImage() {

        String folder_main = "Saved QR Code";

        File dir = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!dir.exists()) {
            dir.mkdir();
        }
        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        File file = new File(dir,System.currentTimeMillis()+".jpg");
        try {
            outputStream = new FileOutputStream(file);
        }
        catch (FileNotFoundException e)
        {
                     e.printStackTrace();
        }

        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        Toast.makeText(this, "successful", Toast.LENGTH_SHORT).show();

        try {
            outputStream.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        try {
            outputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


    }


    public void generateQR()
    {

       data = dataEdt.getText().toString();
        if(data.isEmpty())
        {
            Toast.makeText(GeneratorQRcodeAcitivity.this, "Please Enter some data for Generate QR code", Toast.LENGTH_SHORT).show();
        }
        else
        {
            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display =manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width =point.x;
            int height=point.y;
            int dimen =width<height ? width:height;
            dimen = dimen * 3/4;
            qrgEncoder = new QRGEncoder(dataEdt.getText().toString(),null, QRGContents.Type.TEXT,dimen);

            try {
                // Getting QR-Code as Bitmap
                bitmap = qrgEncoder.getBitmap();
                // Setting Bitmap to ImageView
                grCodeTv.setVisibility(View.GONE);
                saveimgBTN.setVisibility(View.VISIBLE);
                qrimg.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void showPermissionDialog() {
        if (SDK_INT >= Build.VERSION_CODES.R) {

            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", new Object[]{getApplicationContext().getPackageName()})));
                startActivityForResult(intent, 2000);
            } catch (Exception e) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 2000);

            }

        } else
            ActivityCompat.requestPermissions(GeneratorQRcodeAcitivity.this,
                    new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 333);
    }

    private boolean checkPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int write = ContextCompat.checkSelfPermission(getApplicationContext(),
                    WRITE_EXTERNAL_STORAGE);
            int read = ContextCompat.checkSelfPermission(getApplicationContext(),
                    READ_EXTERNAL_STORAGE);

            return write == PackageManager.PERMISSION_GRANTED &&
                    read == PackageManager.PERMISSION_GRANTED;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 333) {
            if (grantResults.length > 0) {
                boolean write = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean read = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (read && write){
                    //write farther code

                }else {

                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2000) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {

                } else {

                }
            }
        }
    }

}