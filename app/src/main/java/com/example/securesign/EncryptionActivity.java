package com.example.securesign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import static com.example.securesign.Draw.bitmap;
import static com.example.securesign.Draw.bitmapPaint;
import static com.example.securesign.Draw.colorList;
import static com.example.securesign.Draw.currentDraw;
import static com.example.securesign.Draw.mCanvas;
import static com.example.securesign.Draw.pathList;

public class EncryptionActivity extends AppCompatActivity {
    public static Path path=new Path();
    public static Paint paint=new Paint();
    private   Button brush;
    private Button eraser;
    private Button red;
    private Button green;
    private Button blue,encImage;
    public  ImageView imageView;
    String result="";
    File loc;
    public String signature_spec_key="Vk5mgIlDQAWS02Wm";
    public String signature_key="fcVclAggwquG8ZZj";
    public static final String enc_file_name="signature_enc";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encryption);
        brush=findViewById(R.id.brush);
        eraser=findViewById(R.id.eraser);
        green=findViewById(R.id.green);
        encImage=findViewById(R.id.encodeImage);
        red=findViewById(R.id.red);
        blue = findViewById(R.id.blue);
        imageView=findViewById(R.id.imageView);
        brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBrush();
            }
        });
        eraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eraser(v);

            }
        });
        loc=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/saved_images");

        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                red();
            }
        });
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                green();
            }
        });
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blue();
            }
        });
        encImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageBitmap(bitmap);

                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                InputStream is=new ByteArrayInputStream(stream.toByteArray());
                //file
                File outputFile=new File(getApplicationContext().getFilesDir(),enc_file_name);

                try{
                    EncryptionAlgo.encrypt(signature_key,signature_spec_key,is,new FileOutputStream(outputFile));
                    DownloadManager downloadManager = (DownloadManager) getApplicationContext().getSystemService(DOWNLOAD_SERVICE);

                    downloadManager.addCompletedDownload(outputFile.getName(), outputFile.getName(), true, "*/*",outputFile.getAbsolutePath(),outputFile.length(),true);
                    Context context=getApplicationContext();
                    Uri filePath= FileProvider.getUriForFile(context,"com.example.securesign.fileprovider",outputFile);
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.setType("vnd.android.cursor.dir/email");
                    sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    sendIntent .putExtra(Intent.EXTRA_STREAM, filePath);
                    startActivity(Intent.createChooser(sendIntent,"Share Encrypted File with "));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

       // Toast.makeText(getApplicationContext(),getApplicationContext().getFilesDir().toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onBrush(){
        paint.setColor(Color.BLACK);
        currentColor(paint.getColor());
    }

    public void red(){
        paint.setColor(Color.RED);
        currentColor(paint.getColor());
    }
    public void green(){
        paint.setColor(Color.GREEN);
        currentColor(paint.getColor());
    }
    public void blue(){
        paint.setColor(Color.BLUE);
        currentColor(paint.getColor());
    }

    public void currentColor(int c){
      currentDraw=c;
      path=new Path();

    }
    public void eraser(View v){
        pathList.clear();
        colorList.clear();
        path.reset();
        bitmapPaint.setColor(getResources().getColor(android.R.color.transparent));
        bitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        bitmap=null;
        mCanvas=null;
        Intent start=new Intent(EncryptionActivity.this,StartActivity.class);
        startActivity(start);
    }
}