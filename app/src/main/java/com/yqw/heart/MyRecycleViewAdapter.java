package com.yqw.heart;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yqw.hotheart.HeartFrameLayout;
import com.yqw.hotheart.minterface.OnDoubleClickListener;
import com.yqw.hotheart.minterface.OnSimpleClickListener;

import java.util.List;

/**
 * Created by Administrator on 2016/11/2.
 */
public class MyRecycleViewAdapter extends RecyclerView.Adapter<MyRecycleViewAdapter.ViewHolder> {
    private List<String> mData;
    MyRecycleViewAdapter(List<String> data) {
        mData = data ;
    }
    private OnItemClickListener itemClickListener;
    void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.setIsRecyclable(true);
        return viewHolder;
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        HeartFrameLayout mHeartFrameLayout;
        Button mButton;
        ViewHolder(View itemView) {
            super(itemView);
            mHeartFrameLayout = itemView.findViewById(R.id.heart);

            mHeartFrameLayout.setOnDoubleClickListener(new OnDoubleClickListener() {
                @Override
                public void onDoubleClick(View view) {
                    Log.d("yqw","onDoubleClick");
                }
            });
            mHeartFrameLayout.setOnSimpleClickListener(new OnSimpleClickListener() {
                @Override
                public void onSimpleClick(View view) {
                    Log.d("yqw","onSimpleClick");
                }
            });
            mButton = itemView.findViewById(R.id.bt_start);
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("yqw","点赞");
                }
            });
        }
        @Override
        public void onClick(View view) {
            if (itemClickListener != null){
                itemClickListener.onItemClick(view,getPosition());
            }
        }
    }
}