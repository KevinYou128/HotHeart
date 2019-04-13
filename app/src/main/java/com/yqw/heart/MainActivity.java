package com.yqw.heart;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        HeartFrameLayout heartFrameLayout = findViewById(R.id.heart);

//        //点击监听方案一：解决单击和双击冲突的点击方案（解除注释就可以测试效果了）
//        heartFrameLayout.setOnTouchListener(new MyClickListener
//                (new MyClickListener.MyClickCallBack() {
//
//                    @Override
//                    public void onSimpleClick() {
//                        showToast("单击了");
//                    }
//
//                    @Override
//                    public void onDoubleClick() {
//                        showToast("双击了");
//                    }
//                }));

        //============== 华丽的分割线 ==================

//        //点击监听方案二：普通单击双击，单击会一直被调用（解除注释就可以测试效果了）
//        heartFrameLayout.setOnDoubleClickListener(new DoubleClickListener() {
//            @Override
//            public void onDoubleClick(View view) {
//                showToast("双击了");
//
//            }
//        });
//        heartFrameLayout.setOnSimpleClickListener(new SimpleClickListener() {
//            @Override
//            public void onSimpleClick(View view) {
//                showToast("单击了");
//            }
//        });
    }

    private void showToast(String content){
        Toast.makeText(this, content,Toast.LENGTH_SHORT).show();
    }
}
