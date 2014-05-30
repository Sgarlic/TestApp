package com.poding.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

import com.poding.constants.Constants;
import com.poding.testapp.R;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class Util {
	/**
	 * PageNumber refers to left, middle, and right page. Use 0,1,2 to represent
	 * @param activity
	 * @param pageNumber
	 * @return
	 */
	public static void setActivityBackground(Activity activity,int pageNumber,View view){
		Resources resources = activity.getResources();
		Bitmap bitmap=BitmapFactory.decodeResource(resources,R.drawable.main_background_2x);
		
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
	    
	    Drawable imagebakground = new BitmapDrawable(activity.getResources(), backgroudBitmap);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//		 	Log.i("Poding", "Background");
		 	view.setBackground(imagebakground);
		} else {
//		   	Log.i("Poding", "Drawable");
		   	view.setBackgroundDrawable(imagebakground);
		}
	}
	
	public static void setFullScreen(Activity activity){
		// 使屏幕不显示标题栏(必须要在setContentView方法执行前执行)
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 隐藏状态栏，使内容全屏显示(必须要在setContentView方法执行前执行)
		activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	
	public static String getFormatedDate(int year,int month,int dayOfMonth){
		month+=1;
		String selectedDate = "%s-%s-%s";
		return String.format(
				selectedDate, String.valueOf(year),String.valueOf(month),String.valueOf(dayOfMonth));
	}
	
	public static String getFormatedDate(Calendar calendar){
		return getFormatedDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
	}
}
