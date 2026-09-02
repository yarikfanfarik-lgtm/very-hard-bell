package com.yarikfanfarik.veryhardbell;
import android.content.*;import android.graphics.*;import android.view.*;import java.util.*;
public class FlappyView extends View{
 Paint p=new Paint(1);float birdY,vel;ArrayList<Float>gaps=new ArrayList<>(),xs=new ArrayList<>();int passed=0;long last;Runnable win;Random r=new Random();boolean over=false,started=false,won=false;
 public FlappyView(Context c){super(c);reset();}
 void reset(){xs.clear();gaps.clear();for(int i=0;i<10;i++){xs.add(700+i*320f);gaps.add(120f+r.nextInt(260));}birdY=350;vel=0;passed=0;over=false;won=false;started=false;last=System.currentTimeMillis();}
 public void setOnWin(Runnable r){win=r;}
 @Override protected void onDraw(Canvas c){super.onDraw(c);p.setColor(Color.rgb(180,235,255));c.drawRect(0,0,getWidth(),getHeight(),p);long now=System.currentTimeMillis();float dt=Math.min(.03f,(now-last)/1000f);last=now;if(started&&!over&&!won){birdY+=vel*dt;vel+=900*dt;float speed=190*dt;for(int i=0;i<xs.size();i++)xs.set(i,xs.get(i)-speed);}
  p.setColor(Color.YELLOW);c.drawCircle(100,birdY,18,p);int done=0;for(int i=0;i<10;i++){float x=xs.get(i),gap=gaps.get(i);p.setColor(Color.rgb(35,150,55));c.drawRect(x,0,x+70,gap-65,p);c.drawRect(x,gap+65,x+70,getHeight(),p);if(x+70<100)done++;if(started&&!over&&!won&&x<118&&x+70>82&&(birdY<gap-65||birdY>gap+65))over=true;}
  if(birdY<0||birdY>getHeight())over=true;if(done>=10&&!over&&!won){won=true;if(win!=null)win.run();return;}
  p.setColor(Color.BLACK);p.setTextSize(22);c.drawText("Трубы: "+done+"/10",20,35,p);if(over){p.setColor(Color.RED);p.setTextSize(30);c.drawText("Столкновение — тап для рестарта",20,getHeight()/2f,p);}else if(!started){c.drawText("Тапни, чтобы начать",20,getHeight()/2f,p);}if(!won)postInvalidateDelayed(16);
 }
 @Override public boolean onTouchEvent(MotionEvent e){if(e.getAction()==MotionEvent.ACTION_DOWN){if(over){reset();started=true;}else{started=true;vel=-350;}postInvalidate();return true;}return true;}
}
