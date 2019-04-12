# HotHeart
抖音APP点赞效果实现，模仿抖音APP双击屏幕蹦出心图，特点：   1. 可以自定义图片   2. 可以自定义旋转角度   3. 超级简洁，占用内存小
# 预览
![预览图片](https://github.com/KevinYou128/HotHeart/blob/master/app/src/main/res/drawable/douyin2.gif)
# 使用
 第一步：
 在项目根目录的build.gradle文件中加入

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
 第二步：
 添加依赖项

	dependencies {
		implementation 'com.github.KevinYou128:HotHeart:v1.0'
	}
第三步：直接在布局文件里调用

    <com.yqw.hotheart.HeartFrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    	xmlns:app="http://schemas.android.com/apk/res-auto"
    	xmlns:tools="http://schemas.android.com/tools"
    	android:layout_width="match_parent"
    	android:layout_height="match_parent"
    	app:degrees_interval_max="20"
    	app:degrees_interval_min="-20"
    	app:swipe_image="@drawable/ic_heart"
    	tools:context=".MainActivity">
    <ImageView
         android:layout_width="match_parent"
         android:layout_height="match_parent"
         android:src="@drawable/douyin" />
    </com.yqw.hotheart.HeartFrameLayout>
直接在你的父布局外层包裹上hotheart布局就可以了，另外，考虑到缩减布局层数，我特意添加了HeartLinearLayout、HeartConstraintLayout、HeartRelativeLayout、HeartFrameLayout等父容器，你可以直接使用它们来替换你的原生布局，以达到缩减布局层数的效果。

<h3>XML属性说明</h3>

<p> swipe_image：点击时需要显示的图片<br />
 <br />
 refresh_rate：设置动画刷新频率，默认为16，数值越大动画表现越慢，建议使用默认就好了<br />
 <br />
 degrees_interval_min：图片最小旋转角度，默认-30，取值范围为-360到360（注意取值小于或等于max）<br />
 <br />
 degrees_interval_max:图片最大旋转角度，默认30，取值范围为-360到360（注意取值大于或等于min）</p>
<h3>java代码属性说明</h3>

<p> setOnDoubleClickListener：双击事件监听<br />
<br />
 示例：
		
	heartViewGroup.setOnDoubleClickListener(new HeartViewGroup.DoubleClickListener() {
            @Override
            public void onDoubleClick(View view) {
                //双击事件处理
            }
        });
	
 <br />
 setSwipeImage(int id)：设置点击时需要显示的图片<br />
 <br />
 setRefreshRate(int refreshRate)：设置动画刷新频率，默认为16，数值越大动画表现越慢，建议使用默认就好了<br />
 <br />
 setDegreesInterval(int min,int max)：设置图片旋转角度区间，默认-30到30<br />
min取值范围为-360到360<br />
max取值范围为-360到360</p>

[博客地址](https://blog.csdn.net/you943047219/article/details/89239562)
