package com.poding.util;

import java.io.File;
import java.io.FileOutputStream;

import com.poding.constants.Constants;
import com.poding.testapp.R;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;

public class Util {
	/**
	 * PageNumber refers to left, middle, and right page. Use 0,1,2 to represent
	 * @param activity
	 * @param pageNumber
	 * @return
	 */
	public static Bitmap getBackground(Activity activity,int pageNumber){
		Resources resources = activity.getResources();
		Bitmap bitmap=BitmapFactory.decodeResource(resources,R.drawable.main_background);
		
		int newWidth = bitmap.getWidth()/3;
		int newHeight = bitmap.getHeight();
		
		Bitmap newBitmap = Bitmap.createBitmap(bitmap, newWidth*pageNumber, 0, newWidth, newHeight);
		
		float scaleWidth = ((float) Constants.ScreenWidth ) / newWidth; 
	    float scaleHeight = ((float) Constants.ScreenHeight ) / newHeight;
	    
		
	    Matrix matrix = new Matrix();
	    matrix.postScale(scaleWidth, scaleHeight); 
	    
	    Bitmap backgroudBitmap = Bitmap.createBitmap(newBitmap, 0, 0,newWidth, newHeight, matrix, true);
	    
//	    FileOutputStream out = null;
//		try {
//		       out = new FileOutputStream("/mnt/sdcard2/backgroudBitmap.png");
//		       backgroudBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
//		       out.flush();
//		} catch (Exception e) {
//		    e.printStackTrace();
//		} finally {
//		       try{
//		           out.close();
//		       } catch(Throwable ignore) {}
//		}
//		
//		Log.d("Poding", "success");
	    
	    return backgroudBitmap;
	}
}
