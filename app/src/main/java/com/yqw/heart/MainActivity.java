package com.yqw.heart;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
//    private HeartFrameLayout mHeartFrameLayout;
    private MyRecycleViewAdapter myRecycleViewAdapter;
    List<String> datas = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycleview);
//        mHeartFrameLayout = findViewById(R.id.heartlayout);
        mRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));

        for (int i = 0;i<30;i++){
            datas.add(""+i);
        }

        myRecycleViewAdapter = new MyRecycleViewAdapter(datas);
        mRecyclerView.setAdapter(myRecycleViewAdapter);

        //设置item的点击事件
        myRecycleViewAdapter.setOnItemClickListener(new MyRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, int position) {
//                datas.get(position);
            }
        });

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mHeartFrameLayout.destroy();
    }
}
