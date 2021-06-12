package com.example.securesign;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionAlgo {
    private final static String ALGO_DEF="AES/CBC/PKCS5Padding";
    private final static int BUFFER_LEN=1024;
    private final static String ALGO_SECRET_KEY="AES";

    public static void encrypt(String secretKey, String specKey, InputStream is, OutputStream os) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, IOException {
        try{
            IvParameterSpec ivs=new IvParameterSpec(specKey.getBytes("UTF-8"));
            SecretKeySpec keySpec=new SecretKeySpec(secretKey.getBytes("UTF-8"),ALGO_SECRET_KEY);
            Cipher cipher=Cipher.getInstance(ALGO_DEF);
            cipher.init(Cipher.ENCRYPT_MODE,keySpec,ivs);
            os=new CipherOutputStream(os,cipher);
            int count;
            byte [] buffer=new byte[BUFFER_LEN];
            while((count=is.read(buffer))>0)
                os.write(buffer,0,count);


        }
        finally{
            os.close();
        }


    }


    public static void decryptToFile(String secretKey,String specKey,InputStream is,OutputStream os) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, IOException{
        try{
            IvParameterSpec ivs=new IvParameterSpec(specKey.getBytes("UTF-8"));
            SecretKeySpec keySpec=new SecretKeySpec(secretKey.getBytes("UTF-8"),ALGO_SECRET_KEY);
            Cipher cipher=Cipher.getInstance(ALGO_DEF);
            cipher.init(Cipher.DECRYPT_MODE,keySpec,ivs);
            os=new CipherOutputStream(os,cipher);
            int count;
            byte [] buffer=new byte[BUFFER_LEN];
            while((count=is.read(buffer))>0)
                os.write(buffer,0,count);
        }
        finally{
            os.close();
        }


    }

}
