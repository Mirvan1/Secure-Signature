package com.example.securesign;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class StartActivity extends AppCompatActivity {
    Button encrypt,decrypt;
    public int REQUEST_DECRYPTION=1001;
    public InputStream encryptedFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        encrypt=findViewById(R.id.encryptImageS);
        decrypt=findViewById(R.id.decryptImage);

        encrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent encryption=new Intent(StartActivity.this, EncryptionActivity.class);
                encryption.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(encryption);

            }
        });

                decrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           checkPermission();

            }
        });

        }
public void code(){
    Intent decryptIntent = new Intent(StartActivity.this, DecryptionActivity.class);
    startActivity(decryptIntent);
}
    private void decryptButtonDefine() {
        File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()+File.separator+ EncryptionActivity.enc_file_name);
        Uri path= Uri.fromFile(file);

        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent,REQUEST_DECRYPTION);



    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_DECRYPTION) {
            Uri uri = null;
            String path = "";
            if (data != null && resultCode == RESULT_OK) {
                uri = data.getData();
                path = data.getData().getPath();
    //            Toast.makeText(getApplicationContext(), uri.toString(), Toast.LENGTH_LONG).show();
  //              Log.e("path:", uri.toString());

            try {

                encryptedFile = getApplicationContext().getContentResolver().openInputStream(uri);
                Toast.makeText(getApplicationContext(), String.valueOf(encryptedFile.available()),Toast.LENGTH_LONG).show();
                DecryptionActivity.is=encryptedFile;
//                Log.e("enc av: ", String.valueOf(encryptedFile.available()));
                code();
                } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            }
        }
    }
    private void checkPermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(StartActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    StartActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        } else {
            decryptButtonDefine();

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                decryptButtonDefine();

            }
        }
    }

}