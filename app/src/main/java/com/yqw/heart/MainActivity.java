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

        for (int i = 0;i<300;i++){
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

//        heartFrameLayout.setOnDoubleClickListener(new OnDoubleClickListener() {
//            @Override
//            public void onDoubleClick(View view) {
//                showToast("双击了");
//
//            }
//        });

//        heartFrameLayout.setOnSimpleClickListener(new OnSimpleClickListener() {
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
    }
}
