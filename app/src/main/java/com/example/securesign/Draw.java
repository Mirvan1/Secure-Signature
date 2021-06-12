package com.example.securesign;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import static com.example.securesign.EncryptionActivity.paint;
import static com.example.securesign.EncryptionActivity.path;

public class Draw extends View {
    public static ArrayList<Path> pathList=new ArrayList<>();
    public static ArrayList<Integer>colorList=new ArrayList<>();
    public ViewGroup.LayoutParams params;
    public static int currentDraw= Color.BLACK;
    public static Bitmap bitmap;
    public static Canvas mCanvas;
    public static Paint bitmapPaint;

    public Draw(Context context) {
        super(context);
        define(context);

    }

    public Draw(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        define(context);
    }

    public Draw(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        define(context);

    }
    public void define(Context context){

        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(10f);
        params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        bitmapPaint = new Paint(Paint.DITHER_FLAG);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x=event.getX();
        float y=event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x,y);
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x,y);
                pathList.add(path);
                colorList.add(currentDraw);
                return  true;
            default:
                return false;

        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(bitmap==null && mCanvas==null){
            bitmap=Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);;
            mCanvas=new Canvas(bitmap);
          //  Toast.makeText(getContext(),bitmap.toString(),Toast.LENGTH_LONG).show();
        }
            mCanvas.setBitmap(bitmap);


        for(int i=0;i<pathList.size();i++){
        paint.setColor(colorList.get(i));
        //canvas.drawPath(pathList.get(i),paint);
        mCanvas.drawPath(pathList.get(i),paint);

        invalidate();
        }
        canvas.drawBitmap(bitmap, 100, 100, paint);

        }
    public Bitmap getBitmap()
    {
        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache();
        Bitmap bmp = Bitmap.createBitmap(this.getDrawingCache());
        this.setDrawingCacheEnabled(false);


        return bmp;
    }
}
