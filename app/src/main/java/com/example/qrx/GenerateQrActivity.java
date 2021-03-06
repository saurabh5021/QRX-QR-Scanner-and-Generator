package com.example.qrx;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;

import android.net.Uri;
import android.os.Bundle;

import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;


public class GenerateQrActivity extends AppCompatActivity {

    private TextView qrShowerT;
    private ImageView qrShowerI;
    private EditText editText;
    private Button generateQR2;
    private Button share;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr);

        qrShowerT = findViewById(R.id.QR_shower_text);
        qrShowerI = findViewById(R.id.QR_shower);
        editText = findViewById(R.id.EditText);
        generateQR2 = findViewById(R.id.GenerateQR2);
        share = findViewById(R.id.share);


        generateQR2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = editText.getText().toString().trim();

                WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = windowManager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int dimen = width<height ? width:height;
                    dimen = dimen* 3/4;

                if(data.isEmpty()){
                    Toast.makeText(GenerateQrActivity.this, "You Have Not Entered Any Data", Toast.LENGTH_SHORT).show();
                }
                else{
                    MultiFormatWriter writer = new MultiFormatWriter();
                    try {
                        BitMatrix matrix = writer.encode(data,BarcodeFormat.QR_CODE,dimen, dimen);
                        BarcodeEncoder encoder = new BarcodeEncoder();
                        Bitmap bitmap = encoder.createBitmap(matrix);
                        qrShowerT.setVisibility(View.GONE);
                        qrShowerI.setImageBitmap(bitmap);
                    }
                    catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(qrShowerI.getDrawable() == null){
                    Toast.makeText(GenerateQrActivity.this, "Please Create QR First", Toast.LENGTH_SHORT).show();
                }
                else {
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) qrShowerI.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    shareImageandText(bitmap);
                }
            }
        });

    }

    private void shareImageandText(Bitmap bitmap) {
        Uri uri = getmageToShare(bitmap);
        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_TEXT, editText.getText().toString() + "QR Code");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
        intent.setType("image/png");
        startActivity(Intent.createChooser(intent, "Share Via"));
    }

    private Uri getmageToShare(Bitmap bitmap) {
        File imagefolder = new File(getCacheDir(), "images");
        Uri uri = null;
        try {
            imagefolder.mkdirs();
            File file = new File(imagefolder, "shared_image.png");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
            outputStream.flush();
            outputStream.close();
            uri = FileProvider.getUriForFile(this, "com.anni.shareimage.fileprovider", file);
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return uri;
    }
}