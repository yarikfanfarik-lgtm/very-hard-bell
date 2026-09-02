package com.yarikfanfarik.veryhardbell;

import android.app.*;import android.content.*;import android.os.*;import android.provider.Settings;import android.net.Uri;import android.graphics.Color;import android.view.*;import android.widget.*;import java.util.*;

public class MainActivity extends Activity {
    static final String[] TASKS={"FLAPPY — 10 зелёных труб","Математика — 5 примеров","Фото предмета","Память — повтори 6 плиток","Реакция — успей нажать","Порядок — нажми 1→10","Цвет — выбери нужный цвет","Тряска — потряси телефон"};
    static final String KEY_ORDER="order", KEY_ENABLED="enabled", KEY_DIFF="diff";
    android.content.SharedPreferences p;
    LinearLayout root;
    @Override public void onCreate(Bundle b){super.onCreate(b);p=getSharedPreferences("bell",0);showHome();}
    TextView tv(String s,int z){TextView t=new TextView(this);t.setText(s);t.setTextSize(z);t.setPadding(20,16,20,16);return t;}
    Button btn(String s){Button b=new Button(this);b.setText(s);return b;}
    void base(String title){root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(18,18,18,18);TextView h=tv(title,26);root.addView(h);ScrollView sv=new ScrollView(this);sv.addView(root);setContentView(sv);}
    void showHome(){base("⏰ Very Hard Bell");root.addView(tv("Будильник, который реально заставит проснуться.",18));
        Button set=btn("Настроить будильник");root.addView(set);set.setOnClickListener(v->alarmSettings());
        Button tasks=btn("🎯 Задания и порядок");root.addView(tasks);tasks.setOnClickListener(v->taskSettings());
        Button test=btn("▶ Запустить испытание сейчас");root.addView(test);test.setOnClickListener(v->startActivity(new Intent(this,AlarmActivity.class)));
        if(Build.VERSION.SDK_INT>=31){Button exact=btn("Разрешение на точные будильники");root.addView(exact);exact.setOnClickListener(v->{try{startActivity(new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM));}catch(Exception e){}});}
    }
    void alarmSettings(){base("⏰ Настройка будильника");TimePicker tp=new TimePicker(this);tp.setIs24HourView(true);root.addView(tp);EditText label=new EditText(this);label.setHint("Название, например: В школу");root.addView(label);
        CheckBox repeat=new CheckBox(this);repeat.setText("Повторять каждый день");repeat.setChecked(p.getBoolean("repeat",true));root.addView(repeat);
        CheckBox vib=new CheckBox(this);vib.setText("Вибрация");vib.setChecked(p.getBoolean("vib",true));root.addView(vib);
        SeekBar volume=new SeekBar(this);volume.setMax(100);volume.setProgress(p.getInt("volume",80));root.addView(tv("Громкость",16));root.addView(volume);
        Button save=btn("💾 Сохранить и поставить");root.addView(save);save.setOnClickListener(v->{p.edit().putInt("hour",tp.getHour()).putInt("minute",tp.getMinute()).putString("label",label.getText().toString()).putBoolean("repeat",repeat.isChecked()).putBoolean("vib",vib.isChecked()).putInt("volume",volume.getProgress()).apply();AlarmReceiver.schedule(this);Toast.makeText(this,"Будильник установлен",Toast.LENGTH_SHORT).show();showHome();});
        Button cancel=btn("Назад");root.addView(cancel);cancel.setOnClickListener(v->showHome());
    }
    ArrayList<Integer> order(){String s=p.getString(KEY_ORDER,"0,1,2,3,4,5,6,7");ArrayList<Integer>a=new ArrayList<>();for(String x:s.split(","))try{a.add(Integer.parseInt(x));}catch(Exception e){}return a;}
    void saveOrder(ArrayList<Integer>a){StringBuilder s=new StringBuilder();for(int x:a){if(s.length()>0)s.append(',');s.append(x);}p.edit().putString(KEY_ORDER,s.toString()).apply();}
    void taskSettings(){base("🎯 Задания");root.addView(tv("Отметь нужные задания. Стрелками меняется порядок. Сложность влияет на примеры и мини-игры.",16));
        ArrayList<Integer> a=order();boolean[] en=new boolean[8];String es=p.getString(KEY_ENABLED,"11111111");for(int i=0;i<8;i++)en[i]=i<es.length()&&es.charAt(i)=='1';
        LinearLayout list=new LinearLayout(this);list.setOrientation(LinearLayout.VERTICAL);root.addView(list);refreshTaskList(list,a,en);
        Spinner diff=new Spinner(this);String[] ds={"Легко","Нормально","Сложнее"};diff.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,ds));diff.setSelection(p.getInt(KEY_DIFF,0));root.addView(tv("Сложность",16));root.addView(diff);
        Button save=btn("💾 Сохранить настройки");root.addView(save);save.setOnClickListener(v->{StringBuilder s=new StringBuilder();for(boolean q:en)s.append(q?'1':'0');p.edit().putString(KEY_ENABLED,s.toString()).putInt(KEY_DIFF,diff.getSelectedItemPosition()).apply();saveOrder(a);Toast.makeText(this,"Настройки сохранены",Toast.LENGTH_SHORT).show();showHome();});
        Button back=btn("Назад");root.addView(back);back.setOnClickListener(v->showHome());
    }
    void refreshTaskList(LinearLayout list,ArrayList<Integer>a,boolean[]en){list.removeAllViews();for(int pos=0;pos<a.size();pos++){final int idx=pos, task=a.get(pos);LinearLayout row=new LinearLayout(this);row.setGravity(Gravity.CENTER_VERTICAL);CheckBox c=new CheckBox(this);c.setText(TASKS[task]);c.setChecked(en[task]);c.setLayoutParams(new LinearLayout.LayoutParams(0,-2,1));c.setOnCheckedChangeListener((b,x)->en[task]=x);row.addView(c);Button up=btn("↑");Button dn=btn("↓");row.addView(up,new LinearLayout.LayoutParams(55,55));row.addView(dn,new LinearLayout.LayoutParams(55,55));up.setOnClickListener(v->{if(idx>0){Collections.swap(a,idx,idx-1);refreshTaskList(list,a,en);}});dn.setOnClickListener(v->{if(idx<a.size()-1){Collections.swap(a,idx,idx+1);refreshTaskList(list,a,en);}});list.addView(row);}}
}
