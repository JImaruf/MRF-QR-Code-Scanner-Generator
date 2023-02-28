package com.example.mrfqrcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class ScanQRcodeActivity extends AppCompatActivity {
     CodeScanner mCodeScanner;
    CodeScannerView scannerView;
    TextView resulttv;
    Bitmap bitmap;
    private static final int REQ_CODE=200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);
        resulttv = findViewById(R.id.resulttv);



             if(checkPermission())
             {
                 Toast.makeText(this, "Permission Already Granted!", Toast.LENGTH_SHORT).show();
                 scannerFunction();
             }

             else
             {
                 ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA,Manifest.permission.VIBRATE},REQ_CODE);
             }


    }


    public void scannerFunction()
    {
        scannerView = findViewById(R.id.qrcodeScanner);
        mCodeScanner = new CodeScanner(ScanQRcodeActivity.this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ScanQRcodeActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                        resulttv.setText(result.getText());
                    }
                });
            }
        });
       mCodeScanner.startPreview();
//        scannerView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mCodeScanner.startPreview();
//            }
//        });
    }

    private boolean checkPermission() {
       int camResult = ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA);
       int vibResult = ActivityCompat.checkSelfPermission(this,Manifest.permission.VIBRATE);

       return  camResult == PackageManager.PERMISSION_GRANTED && vibResult==PackageManager.PERMISSION_GRANTED;

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQ_CODE)
        {
            if(grantResults.length>0)
            {
                int cameraRes = grantResults[0];
                int vibResult = grantResults[1];

                boolean checkCam = cameraRes==PackageManager.PERMISSION_GRANTED;
                boolean checkVib = vibResult == PackageManager.PERMISSION_GRANTED;
                if(checkCam&&checkVib)
                {
                    Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                    scannerFunction();
                }
                else {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}