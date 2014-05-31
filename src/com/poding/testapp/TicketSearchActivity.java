package com.poding.testapp;

import java.util.Calendar;

import com.droid.Activity01;
import com.poding.constants.Constants;
import com.poding.util.Util;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Build;

public class TicketSearchActivity extends ActionBarActivity {
	private TextView flyFromDateTextView;
	private TextView leftpageFlyFromTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Display display = getWindowManager().getDefaultDisplay();
		Point screenSize = new Point();
		display.getSize(screenSize);
		Constants.ScreenHeight = screenSize.y;
		Constants.ScreenWidth = screenSize.x;
		
		Util.setFullScreen(this);
		setContentView(R.layout.activity_ticket_search);
		LinearLayout linearlayout = (LinearLayout)findViewById(R.id.ticket_search_linearlayout);
		Util.setActivityBackground(this, 0, linearlayout);
		
		initView();
	}
	
	private void initView(){
		LinearLayout leftPageSinglewayDateLinearLayout = (LinearLayout)findViewById(R.id.leftpage_singleway_date_linearlayout);
		leftPageSinglewayDateLinearLayout.setClickable(true);
		leftPageSinglewayDateLinearLayout.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(TicketSearchActivity.this, DateSelectActivity.class);
				startActivityForResult(intent,1);
			}
		});
		
		flyFromDateTextView = (TextView)findViewById(R.id.fly_from_date_textView);
		Calendar calendar = Calendar.getInstance();
		String flyFromDate = Util.getFormatedDate(calendar);
		flyFromDateTextView.setText(flyFromDate);
		Constants.Fly_From_Date = flyFromDate;
		
		leftpageFlyFromTextView = (TextView)findViewById(R.id.leftpage_fly_from_textView);
		leftpageFlyFromTextView.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(TicketSearchActivity.this, Activity01.class);
				startActivityForResult(intent,0);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ticket_search, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_ticket_search,
					container, false);
			return rootView;
		}
	}

	@Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  if(data == null)
		  return;
	  if(data.getExtras() == null)
		  return;
	  if(requestCode==1 && data.getExtras().containsKey("selectedDate")){
		  String flyFromDate = data.getExtras().get("selectedDate").toString();
		  flyFromDateTextView.setText(flyFromDate);
		  Constants.Fly_From_Date = flyFromDate;
	  }
	 }
}
