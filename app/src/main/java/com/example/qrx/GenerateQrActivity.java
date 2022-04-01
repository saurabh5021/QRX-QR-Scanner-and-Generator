package com.example.qrx;


import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;

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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;



public class GenerateQrActivity extends AppCompatActivity {

    private TextView qrShowerT;
    private ImageView qrShowerI;
    private EditText editText;
    private Button generateQR2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr);

        qrShowerT = findViewById(R.id.QR_shower_text);
        qrShowerI = findViewById(R.id.QR_shower);
        editText = findViewById(R.id.EditText);
        generateQR2 = findViewById(R.id.GenerateQR2);


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

    }
}