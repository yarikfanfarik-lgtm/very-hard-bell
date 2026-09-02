package com.yarikfanfarik.veryhardbell;
import android.app.*;import android.content.*;import android.os.*;import java.util.*;
public class AlarmReceiver extends BroadcastReceiver{
 public static final int REQ=4242;
 public static void schedule(Context c){
  AlarmManager am=(AlarmManager)c.getSystemService(Context.ALARM_SERVICE);if(am==null)return;
  int targetH=c.getSharedPreferences("bell",0).getInt("systemHour",7),targetM=c.getSharedPreferences("bell",0).getInt("systemMinute",0);
  AlarmManager.AlarmClockInfo info=am.getNextAlarmClock();
  Calendar next=Calendar.getInstance();
  if(info!=null)next.setTimeInMillis(info.getTriggerTime());
  else {next.set(Calendar.HOUR_OF_DAY,targetH);next.set(Calendar.MINUTE,targetM);next.set(Calendar.SECOND,0);next.set(Calendar.MILLISECOND,0);if(next.getTimeInMillis()<=System.currentTimeMillis())next.add(Calendar.DAY_OF_YEAR,1);}
  if(info!=null){Calendar chosen=Calendar.getInstance();chosen.setTimeInMillis(info.getTriggerTime());if(chosen.get(Calendar.HOUR_OF_DAY)!=targetH||chosen.get(Calendar.MINUTE)!=targetM)return;}
  PendingIntent pi=PendingIntent.getBroadcast(c,REQ,new Intent(c,AlarmReceiver.class),PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
  if(Build.VERSION.SDK_INT>=31&&!am.canScheduleExactAlarms())return;
  if(Build.VERSION.SDK_INT>=23)am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,next.getTimeInMillis(),pi);else am.setExact(AlarmManager.RTC_WAKEUP,next.getTimeInMillis(),pi);
 }
 @Override public void onReceive(Context c,Intent i){
  Intent a=new Intent(c,AlarmActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
  a.putExtra("from_alarm",true);c.startActivity(a);
  schedule(c);
 }
}
