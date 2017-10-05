package com.demo.autoviewpager.activity;

/*
 *  @项目名：  AutoViewPager 
 *  @包名：    com.demo.autoviewpager.activity
 *  @文件名:   GlideImageLoader
 *  @创建者:   xuerui
 *  @创建时间:  2017/10/5 13:47
 *  @描述：    TODO
 */

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.demo.autoviewpager.loader.ImageLoader;

public class GlideImageLoader implements ImageLoader {
	@Override
	public void displayImage(Context context, String url, ImageView imageView) {
		Glide.with(context).load(url).into(imageView);
	}

	@Override
	public void clearMemoryCache() {

	}
}
