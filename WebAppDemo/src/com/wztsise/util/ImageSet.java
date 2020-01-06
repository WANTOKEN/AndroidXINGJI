package com.wztsise.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.DisplayMetrics;

public class ImageSet {
    public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {  
        InputStream input = ac.getContentResolver().openInputStream(uri);  
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();  
        onlyBoundsOptions.inJustDecodeBounds = true;  
        onlyBoundsOptions.inDither = true;//optional  
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional  
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);  
        input.close();  
        int originalWidth = onlyBoundsOptions.outWidth;  
        int originalHeight = onlyBoundsOptions.outHeight;  
        if ((originalWidth == -1) || (originalHeight == -1))  
            return null;  
        float hh = 800f;  
        float ww = 480f; 
         
        int be = 1; 
        if (originalWidth > originalHeight && originalWidth > ww) { 
            be = (int) (originalWidth / ww);  
        } else if (originalWidth < originalHeight && originalHeight > hh) { 
            be = (int) (originalHeight / hh);  
        }  
        if (be <= 0)  
            be = 1;  
         
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();  
        bitmapOptions.inSampleSize = be; 
        bitmapOptions.inDither = true;//optional  
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional  
        input = ac.getContentResolver().openInputStream(uri);  
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);  
        input.close();  
   
        return compressImage(bitmap); 
    }  
     
    public static Bitmap compressImage(Bitmap image) {  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos); 
        int options = 100;  
        while (baos.toByteArray().length / 1024 > 100) {  
            baos.reset(); 
             
            image.compress(Bitmap.CompressFormat.JPEG, options, baos); 
            options -= 10; 
        }  
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray()); 
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null); 
        return bitmap;  
    }
}
