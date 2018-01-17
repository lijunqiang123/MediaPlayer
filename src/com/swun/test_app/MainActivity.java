package com.swun.test_app;

import java.util.ArrayList;

import com.swun.test_app.R;

import android.support.v7.app.ActionBarActivity;
import android.R.string;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Camera.Size;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends ActionBarActivity {
	private ListView mlistView;
	private ArrayAdapter<String>mAdapter;
	private ArrayList<String>mList,mList_cut,mListDuration;
	private String duration;
	private String data_cut;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initMediaList();
		mAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mList_cut);
		mlistView=(ListView) findViewById(R.id.listView1);
		mlistView.setAdapter(mAdapter);
		mlistView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				//��ȡ��ǰ����������ִ�
				String str=mList.get(arg2);
				//�½�intent
				
				//����һ��intent
				Intent intent=new Intent();
				//���ݲ�������һ��ҳ���intent
				intent.putExtra("list",mList);
				intent.putExtra("id", arg2);
				intent.putExtra("list_cut", mList_cut);
				intent.putExtra("duration", duration);
				intent.putExtra("listDuration", mListDuration);
				
				
				//��ת����һ��ҳ��
				intent.setClass(MainActivity.this, NextActivity.class);
				startActivity(intent);
				
			}
		});

	}

	private void initMediaList(){
		mList = new ArrayList<String>();
		mList_cut = new ArrayList<String>();
		mListDuration = new ArrayList<String>();
		//��ȡContentResolver
		ContentResolver resolver = getContentResolver();
		//��ַ���ⲿ������ý���
		Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		//Ҫ��ѯ����λ���������б�
		String[] projection = new String[]{
				MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media.ALBUM,
				MediaStore.Audio.Media.DURATION
		};
		//��ѯ
		Cursor cursor= resolver.query(uri, projection, null, null, null);
		//Cursor cursor1= resolver.query(projection., projection, null, null, null);
		//ѭ����ȡcursor��ÿһ������
		while(cursor.moveToNext()){
			//��ȡ��λ��id
			int id = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
			int id_album = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
			int id_duration=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
			//������λid��ȡ��λ����
			String data = cursor.getString(id);
			duration =cursor.getString(id_duration);
//			Log.d("First", "data"+data);
//			Log.d("First", "album"+album);
//			Log.d("First", ""+);
			//�ӵ�mList��
			//��data������Ƭ
			data_cut  = data.substring(37,data.length());
			mList_cut.add(data_cut);
			mList.add(data);
			mListDuration.add(duration);
			//mList.add(id_duration);
		}
	}
	
	
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
