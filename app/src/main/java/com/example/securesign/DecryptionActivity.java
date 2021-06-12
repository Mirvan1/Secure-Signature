package com.example.securesign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

public class DecryptionActivity extends AppCompatActivity {
    ImageView decryptView;
    Button saveImageButton;
    public static InputStream is;
    //InputStream encryptedFile;
    OutputStream outputStream;
    public String dec_signature="Decrypted_signature";
    private static int REQUEST_CODE = 120;
    Bitmap bitmap2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decryption);
        decryptView=findViewById(R.id.decryptView);
        saveImageButton=findViewById(R.id.saveImageButton);
        showImage();
        saveImageButton.setClickable(false);
//        saveImageButton.setOnClickListener(new View.OnClickListener() {
//            //@RequiresApi(api = Build.VERSION_CODES.R)
//            @Override
//            public void onClick(View v) {
//                if (ContextCompat.checkSelfPermission(DecryptionActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
//                    try {
//                        saveImage();
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                }else {
//                    askPermission();
//                }
//
//            }
//        });
    }
    public void showImage(){

        File decFile=new File(getApplicationContext().getFilesDir(),dec_signature);
        try{
            EncryptionActivity ma=new EncryptionActivity();
            EncryptionAlgo.decryptToFile(ma.signature_key,ma.signature_spec_key,is,new FileOutputStream(decFile));
            decryptView.setImageURI(Uri.fromFile(decFile));

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

    }

    private void saveImage() throws FileNotFoundException {

        decryptView.setImageBitmap(bitmap2);
        File dir = new File(Environment.getExternalStorageDirectory(), "com.example.securesign");

        if (!dir.exists()) {
            dir.mkdirs();
        }

        final File img = new File(dir, "image" + ".png");
        if (img.exists()) {
            img.delete();
        }
        final OutputStream outStream = new FileOutputStream(img);
        BitmapDrawable drawable = (BitmapDrawable) decryptView.getDrawable();
        bitmap2=drawable.getBitmap();
        bitmap2.compress(Bitmap.CompressFormat.PNG, 100, outStream);
        try {
            outStream.flush();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + "com.example.securesign.dec", img);
        share.putExtra(Intent.EXTRA_STREAM, photoURI);
        startActivity(Intent.createChooser(share, "Share image"));
    }
    private void askPermission() {

        ActivityCompat.requestPermissions(DecryptionActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                try {
                    saveImage();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(DecryptionActivity.this,"Please provide the required permissions",Toast.LENGTH_SHORT).show();
            }
        }
    }
}