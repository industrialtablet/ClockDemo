package com.example.clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
	private static String TAG="AlarmReceiver";
	public static final String ACTION_CLOSE_LVDS_SYSTEM = "android.intent.action.close.lvds.system";
	public static final String ACTION_OPEN_LVDS_SYSTEM = "android.intent.action.open.lvds.system";

	private final boolean DBG = false;

	//	private static String REBOOT = "reboot";
	//	private static String ENABLE_KEY = "1";
	//	private static String DISABLE_KEY = "0";
	//	private static String POWER_KEY = "116";
	private static String KEY_CTRL = "/sys/class/ec3/enablekey";
	private static String KEY_PATH = "/sys/class/ec3/virkey";
	private static String REBOOT_PATH = "/sys/class/ec3/reboot_cmd";

	private static boolean IsScreenOn = true;


	@Override
	public void onReceive(Context context, Intent intent) {
		Log.e("pippin", "Alarm Receive id:"+intent.getIntExtra("_id", -1)+", title:"+intent.getStringExtra("title"));
//		if(intent.getStringExtra("title").equals("on")){
//	    	//intent.setClass(context, AlarmAlert.class);
//	     	//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		   //context.startActivity(intent);
//		  set_cmd(REBOOT , REBOOT_PATH);
//
//		}else if(intent.getStringExtra("title").equals("off")){
//			//set_cmd("1", "/sys/class/ec3/enablekey"); //"/sys/class/ec3/enablekey"
//			//set_cmd("116","/sys/class/ec3/virkey");
//
//			set_cmd(ENABLE_KEY, KEY_CTRL);
//
//			set_cmd(POWER_KEY, KEY_PATH);
//
//
//			set_cmd(DISABLE_KEY, KEY_CTRL);
//
//		}
		//	PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
		//	pm.goToSleep(1000);

		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String nowTime=format.format(new Date());

		Log.d("-eeeee-","current time: "+System.currentTimeMillis());
		if(("off").equalsIgnoreCase(intent.getStringExtra("title"))){
			//
//			   if(IsScreenOn){
			if(DBG)
				write_config("power off time:"+nowTime);
			IsScreenOn = false;
			//set_cmd(KEY_CTRL ,"1"); //"/sys/class/ec3/enablekey"
			//set_cmd(KEY_PATH, "116");
			Intent intent_off=new Intent(ACTION_CLOSE_LVDS_SYSTEM);
			context.sendBroadcast(intent_off);
			Log.d("-eeeee-","/off time:" +(System.currentTimeMillis()/1000)+"("+nowTime+")");
//			   }else {
//				   write_config("go to sleep time:"+nowTime);
//				   	IsScreenOn = false;
//					set_cmd("/sys/power/state", "mem");
//					Log.d(TAG,"			System is off now, just go to Sleep!("+nowTime+")");
//					//PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
//					//pm.goToSleep(1000);
//			   }

//			   Intent myIntent = new Intent();//创建Intent对象
//               myIntent.setAction("android.intent.action.close.hdmi.system");
//               context.sendBroadcast(myIntent);//发送广播
			// set_cmd("116","/sys/class/ec3/virkey");

		}else if(("on").equalsIgnoreCase(intent.getStringExtra("title"))){
			if(!IsScreenOn){
				if(DBG)
					write_config("boot up time:"+nowTime);
				Log.d("-eeeee-","on time:"+(System.currentTimeMillis()/1000));
				// new MyAsyncTask().execute("boot up time:"+nowTime);
				//set_cmd(REBOOT_PATH, "reboot");
				Intent intent_on=new Intent(ACTION_OPEN_LVDS_SYSTEM);
				context.sendBroadcast(intent_on);
// Intent myIntent = new Intent();//创建Intent对象
//              myIntent.setAction("android.intent.action.open.hdmi.system");
//              context.sendBroadcast(myIntent);
			} else {
				Log.d(TAG,"			System is on, not need to reboot!");
			}
		}
	}

	private static void  set_cmd(String file, String value)   {
		File OutputFile = new File(file);
		if(!OutputFile.exists()) {
			Log.d(TAG, "file not exist! " );
			return;
		}
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(OutputFile), 32);
			try {
				Log.d(TAG, "set" + file + ": " + value);
				out.write(value);
			}
			finally {
				out.close();
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "IOException when write "+OutputFile);
		}
	}

	private static void write_config(String date) {
		String fileName =  "/storage/sdcard0/power_date.txt";
		//"/data/data/com.example.autoshutdown/power_date.txt";
		//"/storage/external_storage/sdcard1/power_date.txt";
		//"/storage/sdcard0/power_date.txt";

		File dir = new File(fileName);
		if (!dir.exists()) {
			try {
				//在指定的文件夹中创建文件
				dir.createNewFile();
			} catch (Exception e) {
			}
		}

		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			FileWriter writer = new FileWriter(fileName, true);
			writer.write(date+"\t\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
