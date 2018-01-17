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
	//����һ������
	private ArrayList<String>mlist,mlist_cut,mListDuration;
	private TextView mTextView,mTextView2,mTextView3;
	//����һ�����α���
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
				//�趨�����������ֵ
				currentTime.setMax(mPlayer.getDuration());
				//�ý�������ȡ����ǰ����ʱ��
				currentTime.setProgress(mPlayer.getCurrentPosition());
				//���������Ѿ�������ʱ�򣬿�ʼ������һ�׸�
				if(currentTime.getMax()-currentTime.getProgress() < 800 )
				{
					mPlayer.stop();
					mPlayer.release();
				    mPlayer=null;
					if((mId+1)<mlist.size()){mId++;}
					else if((mId+1)==mlist.size()){mId=0;}
					//��ȡ����
					//textView1.setText(mlist_cut.get(mId));
					//���»�ȡ��ʱ��
					int time = Integer.parseInt(mListDuration.get(mId));
					int min = time/1000/60; //msת���ɷ�
					int sec = time/1000%60;//msת������
					//�½�һ��������
					mPlayer = new MediaPlayer();
					try {
						//��ȡ����·��
						mPlayer.setDataSource(mlist.get(mId));
					} catch (IllegalArgumentException | SecurityException
							| IllegalStateException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						//׼������
						mPlayer.prepare();
					} catch (IllegalStateException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
							
			//�½�һ��message,�Ѹ���������ʱ�䴫��������߳�1��ʾ
					Message msg = new Message();
					String lo = mlist_cut.get(mId);
					msg.obj = lo;
					msg.arg1 = mPlayer.getDuration();
					mThreadHandler1.sendMessage(msg);
					
					//����������
					mPlayer.start();
				}
			//ÿ��һ��ʱ�䣬���½�һ��message���ѵ�ǰʱ�䴫��������߳�0��ʾ
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
		//��ȡ��ǰ������id
	     mTextView = (TextView) findViewById(R.id.textView1); 

		//��ȡ�ϸ�ҳ��������ļ�Ŀ¼list��id�š���ʱ��duration���и��ĸ���
		Intent intent=getIntent();
		mlist=intent.getStringArrayListExtra("list");
		mId=intent.getIntExtra("id", 7);
		duration = intent.getStringExtra("duration"); 
		mListDuration = intent.getStringArrayListExtra("listDuration");
		mlist_cut=intent.getStringArrayListExtra("list_cut");
	
		
		//��ȡ��ǰʱ���id����������id
		mTextView2 = (TextView)findViewById(R.id.textView4);
		currentTime = (SeekBar)findViewById(R.id.seekBar1);
		//��ʾ����
		mTextView.setText(mlist_cut.get(mId));
		//������ʱ��
		mTextView3 = (TextView)findViewById(R.id.textView6);
		//���»�ȡ��ʱ��
		int time = Integer.parseInt(mListDuration.get(mId));
		int min = time/1000/60; //msת���ɷ�
		int sec = time/1000%60;//msת������
		mTextView3.setText(min+":"+sec);
		//�½�һ��������
		mPlayer = new MediaPlayer();
		
		//�������߳�
		mThread = new MyThread();
		mThread1 = new MyThread1();
		mThread.start();
		mThread1.start();
		
		

		//������ʱ�����½�����
		if(mTimer == null){
			mTimer = new Timer();
			mTimer.schedule(new MyTimerTask(), 300,300);	
		}
		
		//��ȡ����·��
		try {
			mPlayer.setDataSource(mlist.get(mId));
		} catch (IllegalArgumentException | SecurityException
				| IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//׼������
		try {
			mPlayer.prepare();
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//��ʼ����
		mPlayer.start();
		
		//����һ��
		mButton_pre = (Button)findViewById(R.id.button1);
		mButton_pre.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//�ȹرն�ʱ��
				mTimer.cancel();
				//��ֹͣ��ǰ������
				mPlayer.stop();
				mPlayer.release();
			    mPlayer=null;
			    //ѭ������
				if(mId>0){mId--;}
				else if(mId==0){mId=mlist.size()-1;}
				//��ȡ����
				mTextView.setText(mlist_cut.get(mId));
				
				//���»�ȡ��ʱ��
				int time = Integer.parseInt(mListDuration.get(mId));
				int min = time/1000/60; //msת���ɷ�
				int sec = time/1000%60;//msת������
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
				//�ٴο�����ʱ��
				mTimer = new Timer();
				mTimer.schedule(new MyTimerTask(), 300,300);
			}
		});
		
		//����һ��
		mButton_next = (Button)findViewById(R.id.button2);
		mButton_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//�ȹرն�ʱ��
				mTimer.cancel();
				//��ֹͣ��ǰ������
				mPlayer.stop();
				mPlayer.release();
			    mPlayer=null;
				if((mId+1)<mlist.size()){mId++;}
				else if((mId+1)==mlist.size()){mId=0;}
				//��ȡ����
				mTextView.setText(mlist_cut.get(mId));
				//���»�ȡ��ʱ��
				int time = Integer.parseInt(mListDuration.get(mId));
				int min = time/1000/60; //msת���ɷ�
				int sec = time/1000%60;//msת������
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
				//����������
				mPlayer.start();
				//�ٴο�����ʱ��
				mTimer = new Timer();
				mTimer.schedule(new MyTimerTask(), 300,300);	
			}
		});
		
		//��ͣ����
		mButton_stop = (Button)findViewById(R.id.button3);
		mButton_stop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//�ȹرն�ʱ��
				mTimer.cancel();
				//����һ�θı�һ��״̬
				STOPFLAG = !STOPFLAG;
				if(STOPFLAG == true){
					mPlayer.pause();
					mButton_stop.setText("����");
				}
				else {
					mPlayer.start();
				mButton_stop.setText("��ͣ");
				}
				//�ٴο�����ʱ��
				mTimer = new Timer();
				mTimer.schedule(new MyTimerTask(), 300,300);	
			}
		});
		
		//�϶�������
		currentTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			//�ſ�SeekBarʱ����
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				//��ȡ��ǰ���������ȣ��ò��������ŵ�ǰ����
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
				//����������
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
	//�߳�ֹͣ
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//�ر��߳�
		mThreadHandler.getLooper().quit();
		mThreadHandler = null;
		mThread.interrupt();
		mThread = null;
		mThreadHandler1.getLooper().quit();
		mThreadHandler1 = null;
		mThread1.interrupt();
		mThread1 = null;
		//�رն�ʱ��
		mTimer.cancel();
		mTimer=null;
		//�رյ�ǰ������
		mPlayer.stop();
		mPlayer=null;
	}
	//�趨��ǰʱ������̵߳�Handler���������̵߳�����MyThread
	private  Handler mUIHandler = new Handler() {
		public void handleMessage(Message msg){
			int type = msg.what;
			int min = type/1000/60;
			int sec = type/1000%60;
			mTextView2.setText(min+":"+sec);
			//mTextView.setText(name+"");
		};
	};
	//�趨��ǰ���������̵߳�Handler���������̵߳�����MyThread1
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
	
	//�½�һ����ȡ��ǰʱ�����߳���
		private class MyThread extends Thread{
			public void run() {
				super.run();
				Looper.prepare();//�¼���ѭ��
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
		
		//�½���ǰ����һ�����߳���
		private class MyThread1 extends Thread{
				public void run() {
					super.run();
					Looper.prepare();//�¼���ѭ��
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
		
	//��дbutton ����¼�
//	private abstract class NoDoubleClickListener implements OnClickListener {  
//			  
//			  
//	        public static final int MIN_CLICK_DELAY_TIME = 1000;//�������ò��ܳ����೤ʱ��  
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
