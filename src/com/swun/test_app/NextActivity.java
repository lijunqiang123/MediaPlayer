package com.swun.test_app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.datatype.Duration;
import android.R.integer;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class NextActivity extends ActionBarActivity {
	private MediaPlayer mPlayer;
	//创建一个数组
	private ArrayList<String>mlist,mlist_cut,mListDuration;
	private TextView mTextView,mTextView2,mTextView3;
	//创建一个整形变量
	private int mId;
	private String duration;
	private boolean STOPFLAG=false;
	private Button mButton_pre,mButton_next,mButton_stop;
	private SeekBar currentTime;
	private Timer mTimer;
	private Handler mThreadHandler,mThreadHandler1;
	private MyThread mThread;
	private MyThread1 mThread1;
	private class MyTimerTask extends TimerTask{
		//private TimerTask mTask = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//设定进度条的最大值
				currentTime.setMax(mPlayer.getDuration());
				//让进度条读取到当前播放时间
				currentTime.setProgress(mPlayer.getCurrentPosition());
				//当进度条已经到最后的时候，开始播放下一首歌
				if(currentTime.getMax()-currentTime.getProgress() < 800 )
				{
					mPlayer.stop();
					mPlayer.release();
				    mPlayer=null;
					if((mId+1)<mlist.size()){mId++;}
					else if((mId+1)==mlist.size()){mId=0;}
					//获取歌名
					//textView1.setText(mlist_cut.get(mId));
					//重新获取总时间
					int time = Integer.parseInt(mListDuration.get(mId));
					int min = time/1000/60; //ms转化成分
					int sec = time/1000%60;//ms转化成秒
					//新建一个播放器
					mPlayer = new MediaPlayer();
					try {
						//获取音乐路径
						mPlayer.setDataSource(mlist.get(mId));
					} catch (IllegalArgumentException | SecurityException
							| IllegalStateException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						//准备声音
						mPlayer.prepare();
					} catch (IllegalStateException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
							
			//新建一个message,把歌曲名和总时间传给相关主线程1显示
					Message msg = new Message();
					String lo = mlist_cut.get(mId);
					msg.obj = lo;
					msg.arg1 = mPlayer.getDuration();
					mThreadHandler1.sendMessage(msg);
					
					//启动播放器
					mPlayer.start();
				}
			//每隔一段时间，就新建一个message，把当前时间传给相关主线程0显示
				Message msg = new Message();
				msg.what = mPlayer.getCurrentPosition();

				mThreadHandler.sendMessage(msg);
//				try {
//					mThread.sleep(100);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
		};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_next);
		//获取当前歌名的id
	     mTextView = (TextView) findViewById(R.id.textView1); 

		//获取上个页面的数据文件目录list、id号、总时间duration、切割后的歌名
		Intent intent=getIntent();
		mlist=intent.getStringArrayListExtra("list");
		mId=intent.getIntExtra("id", 7);
		duration = intent.getStringExtra("duration"); 
		mListDuration = intent.getStringArrayListExtra("listDuration");
		mlist_cut=intent.getStringArrayListExtra("list_cut");
	
		
		//获取当前时间的id，进度条的id
		mTextView2 = (TextView)findViewById(R.id.textView4);
		currentTime = (SeekBar)findViewById(R.id.seekBar1);
		//显示歌名
		mTextView.setText(mlist_cut.get(mId));
		//设置总时间
		mTextView3 = (TextView)findViewById(R.id.textView6);
		//重新获取总时间
		int time = Integer.parseInt(mListDuration.get(mId));
		int min = time/1000/60; //ms转化成分
		int sec = time/1000%60;//ms转化成秒
		mTextView3.setText(min+":"+sec);
		//新建一个播放器
		mPlayer = new MediaPlayer();
		
		//启动主线程
		mThread = new MyThread();
		mThread1 = new MyThread1();
		mThread.start();
		mThread1.start();
		
		

		//启动定时器更新进度条
		if(mTimer == null){
			mTimer = new Timer();
			mTimer.schedule(new MyTimerTask(), 300,300);	
		}
		
		//获取音乐路径
		try {
			mPlayer.setDataSource(mlist.get(mId));
		} catch (IllegalArgumentException | SecurityException
				| IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//准备声音
		try {
			mPlayer.prepare();
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//开始播放
		mPlayer.start();
		
		//按上一首
		mButton_pre = (Button)findViewById(R.id.button1);
		mButton_pre.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//先关闭定时器
				mTimer.cancel();
				//先停止当前播放器
				mPlayer.stop();
				mPlayer.release();
			    mPlayer=null;
			    //循环播放
				if(mId>0){mId--;}
				else if(mId==0){mId=mlist.size()-1;}
				//获取歌名
				mTextView.setText(mlist_cut.get(mId));
				
				//重新获取总时间
				int time = Integer.parseInt(mListDuration.get(mId));
				int min = time/1000/60; //ms转化成分
				int sec = time/1000%60;//ms转化成秒
				mTextView3.setText(min+":"+sec);
				
				mPlayer = new MediaPlayer();
				try {
					mPlayer.setDataSource(mlist.get(mId));
				} catch (IllegalArgumentException | SecurityException
						| IllegalStateException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					mPlayer.prepare();
				} catch (IllegalStateException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mPlayer.start();
				//再次开启定时器
				mTimer = new Timer();
				mTimer.schedule(new MyTimerTask(), 300,300);
			}
		});
		
		//按下一首
		mButton_next = (Button)findViewById(R.id.button2);
		mButton_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//先关闭定时器
				mTimer.cancel();
				//先停止当前播放器
				mPlayer.stop();
				mPlayer.release();
			    mPlayer=null;
				if((mId+1)<mlist.size()){mId++;}
				else if((mId+1)==mlist.size()){mId=0;}
				//获取歌名
				mTextView.setText(mlist_cut.get(mId));
				//重新获取总时间
				int time = Integer.parseInt(mListDuration.get(mId));
				int min = time/1000/60; //ms转化成分
				int sec = time/1000%60;//ms转化成秒
				mTextView3.setText(min+":"+sec);
				
				mPlayer = new MediaPlayer();
				try {
					mPlayer.setDataSource(mlist.get(mId));
				} catch (IllegalArgumentException | SecurityException
						| IllegalStateException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					mPlayer.prepare();
				} catch (IllegalStateException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//启动播放器
				mPlayer.start();
				//再次开启定时器
				mTimer = new Timer();
				mTimer.schedule(new MyTimerTask(), 300,300);	
			}
		});
		
		//暂停播放
		mButton_stop = (Button)findViewById(R.id.button3);
		mButton_stop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//先关闭定时器
				mTimer.cancel();
				//进来一次改变一次状态
				STOPFLAG = !STOPFLAG;
				if(STOPFLAG == true){
					mPlayer.pause();
					mButton_stop.setText("播放");
				}
				else {
					mPlayer.start();
				mButton_stop.setText("暂停");
				}
				//再次开启定时器
				mTimer = new Timer();
				mTimer.schedule(new MyTimerTask(), 300,300);	
			}
		});
		
		//拖动进度条
		currentTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			//放开SeekBar时触发
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				//获取当前进度条进度，让播放器播放当前进度
				mPlayer.seekTo(currentTime.getProgress());
				try {
					mPlayer.setDataSource(mlist.get(mId));
				} catch (IllegalArgumentException | SecurityException
						| IllegalStateException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					mPlayer.prepare();
				} catch (IllegalStateException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//启动播放器
				mPlayer.start();
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub
				//mPlayer.seekTo(arg1);
			}
		});
	}
	//线程停止
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//关闭线程
		mThreadHandler.getLooper().quit();
		mThreadHandler = null;
		mThread.interrupt();
		mThread = null;
		mThreadHandler1.getLooper().quit();
		mThreadHandler1 = null;
		mThread1.interrupt();
		mThread1 = null;
		//关闭定时器
		mTimer.cancel();
		mTimer=null;
		//关闭当前播放器
		mPlayer.stop();
		mPlayer=null;
	}
	//设定当前时间的主线程的Handler，接收子线程的数据MyThread
	private  Handler mUIHandler = new Handler() {
		public void handleMessage(Message msg){
			int type = msg.what;
			int min = type/1000/60;
			int sec = type/1000%60;
			mTextView2.setText(min+":"+sec);
			//mTextView.setText(name+"");
		};
	};
	//设定当前歌曲的主线程的Handler，接收子线程的数据MyThread1
	private  Handler mUIHandler1 = new Handler() {
		public void handleMessage(Message msg){
		    //mTextView = (TextView) findViewById(R.id.textView1); 
			String name = (String) msg.obj;
			int tin = msg.arg1;
			Log.d("test1", name+"");
			Log.d("test2", name+"");
			//mTextView.setText("");
			int min = tin/1000/60;
			int sec = tin/1000%60;
			mTextView.setText(name);
			mTextView3.setText(min+":"+sec);
		};
	};
	
	//新建一个读取当前时间子线程类
		private class MyThread extends Thread{
			public void run() {
				super.run();
				Looper.prepare();//事件的循环
				mThreadHandler = new Handler(){
					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						super.handleMessage(msg);
						int type = msg.what;
						Message msg1 = new Message();
						msg1.what=type;
						mUIHandler.sendMessage(msg1);
					}
				};
				Looper.loop();
			}
		}
		
		//新建当前歌名一个子线程类
		private class MyThread1 extends Thread{
				public void run() {
					super.run();
					Looper.prepare();//事件的循环
					mThreadHandler1 = new Handler(){
						@Override
						public void handleMessage(Message msg) {
							// TODO Auto-generated method stub
							super.handleMessage(msg);
							String name = (String) msg.obj;
							int dur = msg.arg1;
							Message msg1 = new Message();
							msg1.obj=name;
							msg1.arg1 = dur;
							mUIHandler1.sendMessage(msg1);
						}
					};
					Looper.loop();
				}
			}
		
	//重写button 点击事件
//	private abstract class NoDoubleClickListener implements OnClickListener {  
//			  
//			  
//	        public static final int MIN_CLICK_DELAY_TIME = 1000;//这里设置不能超过多长时间  
//	        private long lastClickTime = 0;  
//	          
//	        protected abstract void onNoDoubleClick(View v);  
//	        @Override  
//	        public void onClick(View v) {  
//	            long currentTime = Calendar.getInstance().getTimeInMillis();  
//	            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {  
//	                lastClickTime = currentTime;  
//	                onNoDoubleClick(v);  
//	            }   
//	        }     
//	    }  
		
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
}
