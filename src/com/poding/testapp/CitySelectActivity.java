package com.poding.testapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.poding.constants.CityProperty;
import com.poding.testapp.R;
import com.poding.util.Util;
import com.poding.view.CitySelectLetterListView;
import com.poding.view.CitySelectLetterListView.OnTouchingLetterChangedListener;

public class CitySelectActivity extends Activity {
	private BaseAdapter adapter;  
    private ListView allCitiesListView;
//    private TextView overlay;
    private CitySelectLetterListView letterListView;
    private static final String NAME = "name", NUMBER = "number", SORT_KEY = "sort_key";
    private static final String PROPERTY="property", LOCATION="定位",HISTORY="历史",HOT="热门";
    private HashMap<String, Integer> alphaIndexer;//存放存在的汉语拼音首字母和与之对应的列表位置
    private String[] sections;//存放存在的汉语拼音首字母
    private Handler handler;
//    private OverlayThread overlayThread;
    private String locatedCity = "定位到的城市";
  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_city_select);  
        
        allCitiesListView = (ListView) findViewById(R.id.city_select_listView);
        letterListView = (CitySelectLetterListView) findViewById(R.id.city_select_letter_listView);
        letterListView.setOnTouchingLetterChangedListener(new LetterListViewListener());
        
//        asyncQuery = new MyAsyncQueryHandler(getContentResolver());
        alphaIndexer = new HashMap<String, Integer>();
        handler = new Handler();
//        overlayThread = new OverlayThread();
//        initOverlay();
    }  
  
    @Override  
    protected void onResume() {  
        super.onResume();  
        setAdapter();
    }  
    
    private void setAdapter() {
    	adapter = new ListAdapter(this, getHistoryCityList(),getHotCityList(),getCitiesList());
        allCitiesListView.setAdapter(adapter);  
  
    }
    
    private class ListAdapter extends BaseAdapter {
    	 private LayoutInflater inflater;  
         private List<ContentValues> list;
    	
    	public ListAdapter(Context context, List<ContentValues> historyCityList, List<ContentValues> hotCityList, List<ContentValues> cityList) {
    		this.inflater = LayoutInflater.from(context);
    		this.list = new ArrayList<ContentValues>();
    		alphaIndexer = new HashMap<String, Integer>();
    		int sectionSize = 0;
    		if(locatedCity!=null){
    			sectionSize+=1;
    			list.add(generateContentValues(locatedCity,CityProperty.LOCATECITY));
    		}
    		
    		list.addAll(historyCityList);
    		list.addAll(hotCityList);
    		list.addAll(cityList);
    		
    		sectionSize+=(historyCityList.size()+hotCityList.size()+list.size());
    		sections = new String[sectionSize];
    		
    		int sectionPointer = 0;
    		
    		if(locatedCity!=null){
    			sections[sectionPointer] = LOCATION;
    			alphaIndexer.put(LOCATION, sectionPointer);
    			sectionPointer++;
    		}
    		
    		for(ContentValues historyCity : historyCityList){
    			sections[sectionPointer] = HISTORY;
    			if(!alphaIndexer.containsKey(HISTORY))
    				alphaIndexer.put(HISTORY, sectionPointer);
    			sectionPointer++;
    		}
    		
    		for(ContentValues hotCity : hotCityList){
    			sections[sectionPointer] = HOT;
    			if(!alphaIndexer.containsKey(HOT))
    				alphaIndexer.put(HOT, sectionPointer);
    			sectionPointer++;
    		}
    		
    		for (int i = 0; i < cityList.size(); i++,sectionPointer++) {
    			//当前汉语拼音首字母
    			String currentStr = getAlpha(cityList.get(i).getAsString(SORT_KEY));
    			//上一个汉语拼音首字母，如果不存在为“ ”
                String previewStr = (i - 1) >= 0 ? getAlpha(cityList.get(i - 1).getAsString(SORT_KEY)) : " ";
                if (!previewStr.equals(currentStr)) {
                	String name = getAlpha(cityList.get(i).getAsString(SORT_KEY));
                	alphaIndexer.put(name, sectionPointer);  
                	sections[sectionPointer] = name; 
                }
            }
    	}
    	
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {  
                convertView = inflater.inflate(R.layout.city_list_item, null);
                holder = new ViewHolder();  
                holder.alpha = (TextView) convertView.findViewById(R.id.alpha);  
                holder.name = (TextView) convertView.findViewById(R.id.name);  
//                holder.number = (TextView) convertView.findViewById(R.id.number);  
                convertView.setTag(holder);  
            } else {  
                holder = (ViewHolder) convertView.getTag();  
            }  
            ContentValues cv = list.get(position);  
            holder.name.setText(cv.getAsString(NAME));
//            holder.number.setText(cv.getAsString(NUMBER));
//            String currentStr = getAlpha(list.get(position).getAsString(SORT_KEY));
//            String previewStr = (position - 1) >= 0 ? getAlpha(list.get(position - 1).getAsString(SORT_KEY)) : " ";
            String currentStr = getContentValuesTitle(cv);
            String previewStr = (position - 1) >= 0 ? getContentValuesTitle(list.get(position-1)) : " ";
            if (!previewStr.equals(currentStr)) {  
                holder.alpha.setVisibility(View.VISIBLE);
                holder.alpha.setText(currentStr);
            } else {  
                holder.alpha.setVisibility(View.GONE);
            }  
            return convertView;  
		}
		
		private class ViewHolder {
			TextView alpha;  
            TextView name;  
//            TextView number;
		}
    	
    }
    
    private String getContentValuesTitle(ContentValues content){
    	String title = content.getAsString(PROPERTY);
    	if(title.equals(CityProperty.CityList.getProperty())){
    		title = getAlpha(content.getAsString(SORT_KEY));
    	}
    	return title;
    }
    
//    //初始化汉语拼音首字母弹出提示框
//    private void initOverlay() {
//    	LayoutInflater inflater = LayoutInflater.from(this);
//    	overlay = (TextView) inflater.inflate(R.layout.city_list_letter_select_overlay, null);
//    	overlay.setVisibility(View.INVISIBLE);
//		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
//				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
//				WindowManager.LayoutParams.TYPE_APPLICATION,
//				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//				PixelFormat.TRANSLUCENT);
//		WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
//		windowManager.addView(overlay, lp);
//    }
    
    private class LetterListViewListener implements OnTouchingLetterChangedListener{

		@Override
		public void onTouchingLetterChanged(final String s) {
			if(alphaIndexer.get(s) != null) {
				int position = alphaIndexer.get(s);
				allCitiesListView.setSelection(position);
//				overlay.setText(sections[position]);
//				overlay.setVisibility(View.VISIBLE);
//				handler.removeCallbacks(overlayThread);
				//延迟一秒后执行，让overlay为不可见
//				handler.postDelayed(overlayThread, 1500);
			} 
		}
    	
    }
    
//    //设置overlay不可见
//    private class OverlayThread implements Runnable {
//
//		@Override
//		public void run() {
//			overlay.setVisibility(View.GONE);
//		}
//    	
//    }
    
    
	//获得汉语拼音首字母
    private String getAlpha(String str) {  
        if (str == null) {  
            return "#";  
        }  
  
        if (str.trim().length() == 0) {  
            return "#";  
        }  
  
        char c = str.trim().substring(0, 1).charAt(0);  
        // 正则表达式，判断首字母是否是英文字母  
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");  
        if (pattern.matcher(c + "").matches()) {  
            return (c + "").toUpperCase();  
        } else {  
            return "#";  
        }  
    }  
    
    class SortBySortKey implements Comparator<ContentValues>{
		@Override
		public int compare(ContentValues value0, ContentValues value1) {
			String key0 = value0.getAsString(SORT_KEY);
    		String key1 = value1.getAsString(SORT_KEY);
    		return key0.compareTo(key1);
		}
    	
    }
    
    private List<ContentValues> getCitiesList(){
        List<ContentValues> list = new ArrayList<ContentValues>();  
        String[] cities = {
         	"北京","上海","广州","天津","重庆",
           	"哈尔滨","长春","沈阳","大连","济南","青岛","西安","成都","武汉","南京","杭州","宁波","厦门","深圳",
           	"连云港","徐州","宿迁","淮安","盐城","泰州","扬州","镇江","南通","常州","无锡","苏州"
        };
        for(int i=0;i<cities.length;i++){
           	list.add(generateContentValues(cities[i],CityProperty.CityList));
        }
        if (list.size() > 0) {  
           	Collections.sort(list,new SortBySortKey());
//            setAdapter(list);  
        }  
        return list;
    }
    
    private List<ContentValues> getHistoryCityList(){
    	String[] historyCities = {"历史1","历史2","历史3"};
    	List<ContentValues> list = new ArrayList<ContentValues>(); 
    	for(int i=0;i<historyCities.length;i++){
           	list.add(generateContentValues(historyCities[i],CityProperty.HISTORY));
        }
        return list;
    }
    
    private List<ContentValues> getHotCityList(){
    	String[] hotCities = {"热门1","热门2","热门3"};
    	List<ContentValues> list = new ArrayList<ContentValues>(); 
    	for(int i=0;i<hotCities.length;i++){
           	list.add(generateContentValues(hotCities[i],CityProperty.HOT));
        }
        return list;
    }
    
    private ContentValues generateContentValues(String cityName, CityProperty cityProperty){
    	ContentValues cv = new ContentValues();
       	cv.put(NAME, cityName);
       	cv.put(NUMBER, Util.getPinYin(cityName));
       	cv.put(SORT_KEY, Util.getPinYin(cityName));
       	cv.put(PROPERTY, cityProperty.getProperty());
       	return cv;
    }
    
}  

