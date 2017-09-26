package com.example.clock;

import java.util.Calendar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

public class ClockDemo extends Activity {
	private Button mSetOff;
	private Button mSetOn;
	private Button mSetGpioOff;
	private Button mSetGpioOn;
	private Button mSetOffOn;

	Calendar mCalendar = Calendar.getInstance();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ObjectPool.mAlarmHelper = new AlarmHelper(this);

		mSetOff = (Button) findViewById(R.id.mSetOff);
		mSetOn = (Button) findViewById(R.id.mSetOn);
		mSetGpioOff = (Button) findViewById(R.id.mSetGpioOff);
		mSetGpioOn = (Button) findViewById(R.id.mSetGpioOn);
		mSetOffOn = (Button) findViewById(R.id.mSetOffOn);
		setListener();
	}

	public void setListener() {
		mSetOff.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mCalendar.setTimeInMillis(System.currentTimeMillis());
				int mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
				int mMinute = mCalendar.get(Calendar.MINUTE);
				new TimePickerDialog(ClockDemo.this,
						new TimePickerDialog.OnTimeSetListener() {
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								mCalendar.setTimeInMillis(System
										.currentTimeMillis());
								mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
								mCalendar.set(Calendar.MINUTE, minute);
								mCalendar.set(Calendar.SECOND, 0);
								mCalendar.set(Calendar.MILLISECOND, 0);
								ObjectPool.mAlarmHelper.openAlarm(12, "off",
										"哈哈-哈哈-哈哈", mCalendar.getTimeInMillis());
							}
						}, mHour, mMinute, true).show();
			}
		});

		mSetOn.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				mCalendar.setTimeInMillis(System.currentTimeMillis());
				int mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
				int mMinute = mCalendar.get(Calendar.MINUTE);
				new TimePickerDialog(ClockDemo.this,
						new TimePickerDialog.OnTimeSetListener() {
							public void onTimeSet(TimePicker view,
												  int hourOfDay, int minute) {
								mCalendar.setTimeInMillis(System
										.currentTimeMillis());
								mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
								mCalendar.set(Calendar.MINUTE, minute);
								mCalendar.set(Calendar.SECOND, 0);
								mCalendar.set(Calendar.MILLISECOND, 0);
								ObjectPool.mAlarmHelper.openAlarm(13, "on",
										"哈哈-哈哈-哈哈", mCalendar.getTimeInMillis());
							}
						}, mHour, mMinute, true).show();
			}
		});

		mSetGpioOff.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent("android.intent.action.GPIOSET_BROADCAST");
				intent.putExtra("gpio", "one");  //gpio只可能是one和two
				intent.putExtra("value", 0);         //gpio的值只可能是0或者1
				sendBroadcast(intent);
			}
		});

		mSetGpioOn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent("android.intent.action.GPIOSET_BROADCAST");
				intent.putExtra("gpio", "one");  //gpio只可能是one和two
				intent.putExtra("value", 1);         //gpio的值只可能是0或者1
				sendBroadcast(intent);
			}
		});

		mSetOffOn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent("android.56iq.intent.action.setpoweronoff");
				int[] timeonArray = {2017,9,25,21,50};
				int[] timeoffArray = {2017,9,25,21,47};
				intent.putExtra("timeon", timeonArray);
				intent.putExtra("timeoff", timeoffArray);
				intent.putExtra("enable", true);
				sendBroadcast(intent);
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showBackDialog();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/** Give the tip when exit the application. */
	public void showBackDialog() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("提示")
				.setIcon(R.drawable.icon)
				.setMessage("是否退出?")
				.setPositiveButton("sure",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								System.exit(0);
								android.os.Process
										.killProcess(android.os.Process
												.myPid());
								dialog.dismiss();
							}
						})
				.setNegativeButton("cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
		AlertDialog ad = builder.create();
		ad.show();
	}
}