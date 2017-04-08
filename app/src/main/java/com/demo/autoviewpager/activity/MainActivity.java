package com.demo.autoviewpager.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.demo.autoviewpager.R;
import com.demo.autoviewpager.widget.AutoViewpager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		AutoViewpager autoViewpager = (AutoViewpager) findViewById(R.id.auto_viewpager);
		List<String> urlList = new ArrayList<>();
		String url1 = "http://img.hboffice.cn/newsletter/maKVdr1491015223771.jpg";
		String url2 = "http://img.hboffice.cn/newsletter/maKVdr1491015223771.jpg";
		urlList.add(url1);
		urlList.add(url2);
		autoViewpager.setLabelColor("#000000");
		autoViewpager.setLaBelData(urlList);
		autoViewpager.setIsAuto(true);
		autoViewpager.setDotImageResourse(R.drawable.dot_selector);
		autoViewpager.setDotSize(getResources().getDimensionPixelOffset(R.dimen.top_dot_size));
		autoViewpager.setPicList(urlList);
	}
}
