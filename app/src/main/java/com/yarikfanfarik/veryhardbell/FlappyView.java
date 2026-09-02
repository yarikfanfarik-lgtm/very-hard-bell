package com.yarikfanfarik.veryhardbell;
import android.content.*;import android.graphics.*;import android.view.*;import java.util.*;
public class FlappyView extends View{
 Paint p=new Paint(1); float birdY=0,vel=0; ArrayList<Float> gaps=new ArrayList<>(); ArrayList<Float> xs=new ArrayList<>(); int passed=0; long last; Runnable win; Random r=new Random(); boolean over=false;
 public FlappyView(Context c){super(c);for(int i=0;i<10;i++){xs.add(700+i*320f);gaps.add(120f+r.nextInt(320));}last=System.currentTimeMillis();}
 public void setOnWin(Runnable r){win=r;}
 @Override protected void onDraw(Canvas c){super.onDraw(c);p.setColor(Color.rgb(180,235,255));c.drawRect(0,0,getWidth(),getHeight(),p);float dt=Math.min(0.03f,(System.currentTimeMillis()-last)/1000f);last=System.currentTimeMillis();birdY+=vel*dt;vel+=900*dt;float worldSpeed=190*dt;
  for(int i=0;i<xs.size();i++)xs.set(i,xs.get(i)-worldSpeed);
  if(birdY==0)birdY=getHeight()/2f;float by=birdY; p.setColor(Color.YELLOW);c.drawCircle(100,by,18,p);
  for(int i=0;i<10;i++){float x=xs.get(i),gap=gaps.get(i);p.setColor(Color.rgb(35,150,55));c.drawRect(x,0,x+70,gap-75,p);c.drawRect(x,gap+75,x+70,getHeight(),p);}
  int done=0;for(int i=0;i<10;i++){if(xs.get(i)+70<100)done++;float x=xs.get(i),gap=gaps.get(i);if(x<118&&x+70>82&&(by<gap-75||by>gap+75)){over=true;}}
  if(done>=10&&!over){if(win!=null)win.run();return;}if(by<0||by>getHeight()){over=true;}
  if(over){p.setColor(Color.RED);p.setTextSize(34);c.drawText("Столкновение! Нажми для рестарта",20,getHeight()/2f,p);}else{p.setColor(Color.BLACK);p.setTextSize(22);c.drawText("Трубы: "+done+"/10",20,35,p);postInvalidateDelayed(16);}
 }
 @Override public boolean onTouchEvent(android.view.MotionEvent e){if(e.getAction()==android.view.MotionEvent.ACTION_DOWN){if(over){birdY=getHeight()/2f;vel=0;over=false;for(int i=0;i<10;i++){xs.set(i,700+i*320f);gaps.set(i,120f+r.nextInt(320));}last=System.currentTimeMillis();postInvalidate();}else vel=-350;return true;}return true;}
}
