# AutoViewPager
- 1.在项目build.gradle目录下添加

		allprojects {
		    repositories {
		    jcenter()
		    maven {
			url 'https://jitpack.io' 
		    }
		}

- 2.在app的build.gradle目录下添加

		dependencies {
        		compile 'com.github.xuerui1993:AutoViewPagerLibrary:v1.3'
    		}

- 3.在布局中添加

		<com.demo.autoviewpager.widget.AutoViewpager
		    android:id="@+id/auto_viewpager"
		    android:layout_width="match_parent"
		    android:layout_height="180dp"/>

- 布局支持自定义属性

		<com.demo.autoviewpager.widget.AutoViewpager
		    app:dotPosition="center" //设置小圆点位置
		    app:dotSrc="@drawable/dot_selector" // 设置小圆点图片
		    app:isAuto="true"  //是否轮播
		    app:duration="5000"  //图片切换时间
		    app:dotSize="8dp"    //原点大小
		    android:id="@+id/auto_viewpager"
		    android:layout_width="match_parent"
		    android:layout_height="180dp"/>

- 4.创建ImageLoader实现类

		public class GlideImageLoader implements ImageLoader {
			@Override
			public void displayImage(Context context, String url, ImageView imageView) {
				Glide.with(context).load(url).into(imageView);
			}

			@Override
			public void clearMemoryCache() {

			}
		}

- 5.在代码中使用
		
		autoViewpager.setImageloader(new GlideImageLoader()); //外部设置图片加载类,必须设置,否则会抛出异常
		List<String> urlList = new ArrayList<>();  //图片地址集合
		List<String> labelList = new ArrayList<>();  //标题集合
		autoViewpager.setLaBelData(urlList); //设置标题
		autoViewpager.setIsAuto(true);  //设置是否轮播
		autoViewpager.setDotImageResourse(R.drawable.dot_selector);  //设置圆点图片
		autoViewpager.setDotSize(getResources().getDimensionPixelOffset(R.dimen.top_dot_size));  //设置圆点大小
		autoViewpager.setData(urlList);  //设置图片数据集合,需在所有设置最后使用

