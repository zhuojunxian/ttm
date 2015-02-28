package com.cd7d.ttm.ui;

import com.cd7d.ttm.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Task extends RelativeLayout  {
	public float currentX = 40;
	public float currentY = 50;
	public String ID="0";
	public String Name="";
	public String Desc="";
	private ImageView imageView;  
	private int screenWidth;  
    private int screenHeight;  
    Animation translateAnimation;
	// 定义、创建画笔
	Paint p = new Paint();

	public Task(Context context) {  
        super(context);  
		 LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	        inflater.inflate(R.layout.itemtask, this);  
	        imageView=(ImageView) findViewById(R.id.jiantou); 
	        DisplayMetrics dm = getResources().getDisplayMetrics();  
	        screenWidth = dm.widthPixels;  
	        screenHeight = dm.heightPixels - 50;  
        // TODO Auto-generated constructor stub  
    } 
	public Task(Context context, AttributeSet attrs) {
		super(context, attrs);
		 LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	        inflater.inflate(R.layout.itemtask, this);  
	        imageView=(ImageView) findViewById(R.id.jiantou); 
	        DisplayMetrics dm = getResources().getDisplayMetrics();  
	        screenWidth = dm.widthPixels;  
	        screenHeight = dm.heightPixels - 50;  
	        imageView.setOnTouchListener(movingEventListener);  
	        
		// TODO Auto-generated constructor stub
	}

	private OnTouchListener movingEventListener = new OnTouchListener() {  
        int lastX, lastY;  
  
        @SuppressLint("ClickableViewAccessibility") @Override  
        public boolean onTouch(View v, MotionEvent event) {  
            switch (event.getAction()) {  
            case MotionEvent.ACTION_DOWN:  
                lastX = (int) event.getRawX();  
                lastY = (int) event.getRawY();  
                break;  
            case MotionEvent.ACTION_MOVE:  
                int dx = (int) event.getRawX() - lastX;  
                int dy = (int) event.getRawY() - lastY;  
  
                int left = v.getLeft() + dx;  
                int top = v.getTop() + dy;  
                int right = v.getRight() + dx;  
                int bottom = v.getBottom() + dy;  
                // 设置不能出界  
                if (left < 0) {  
                    left = 0;  
                    right = left + v.getWidth();  
                }  
  
                if (right > screenWidth) {  
                    right = screenWidth;  
                    left = right - v.getWidth();  
                }  
  
                if (top < 0) {  
                    top = 0;  
                    bottom = top + v.getHeight();  
                }  
  
                if (bottom > screenHeight) {  
                    bottom = screenHeight;  
                    top = bottom - v.getHeight();  
                }  
  
                v.layout(left, v.getTop(), right, v.getBottom());  
  
                lastX = (int) event.getRawX();  
                lastY = (int) event.getRawY();  
  
                break;  
            case MotionEvent.ACTION_UP:  
            	 v.layout(0, v.getTop(), v.getWidth(), v.getBottom());
//            	translateAnimation=new TranslateAnimation(0,-v.getLeft(),0,0);
//            	translateAnimation.setDuration(1000);               //设置动画持续时间  
//            	//translateAnimation.setFillAfter (true);
//                v.setAnimation(translateAnimation);             //设置动画效果  
//                translateAnimation.startNow();                      //启动动画  
                break;  
            }  
            return true;  
        }  
    };  

//@Override
//public void onDraw(Canvas canvas){
//	super.onDraw(canvas);
//	p.setColor(Color.RED);
//	// 绘制一个小圆（作为小球）
//	canvas.drawCircle(currentX, currentY, 15, p);
//}
//@Override
//public boolean onTouchEvent(MotionEvent event)
//{
//	// 当前组件的currentX、currentY两个属性
//	this.currentX = event.getX();
//	this.currentY = event.getY();
//	
// 
//	// 通知改组件重绘
//	this.invalidate();
//	// 返回true表明处理方法已经处理该事件
//	return true;
//}

public void Set(String tID,String tname,String tdesc){
	ID=tID;
	Name=tname;
	Desc=tdesc;
	TextView mname=(TextView) findViewById(R.id.name); 
	TextView mdesc=(TextView) findViewById(R.id.description); 
	mname.setText(Name);
	mdesc.setText(Desc);
	
}
 
}
