package com.yidd365.utility.recyclerView;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by orinchen on 16/5/24.
 */

public class CommonRecyclerViewHolder extends RecyclerView.ViewHolder {
    private OnItemViewClickListener itemViewClickListener;
    private final SparseArray<View> mViews;
    private View itemView;

    public View getItemView(){
        return this.itemView;
    }

    public CommonRecyclerViewHolder(View itemView){
        this(itemView, null);
    }

    public CommonRecyclerViewHolder(View itemView, OnItemViewClickListener listener) {
        super(itemView);
        this.mViews = new SparseArray<>();
        this.itemView = itemView;
        //添加Item的点击事件
        if(listener!=null){
            this.itemViewClickListener = listener;
        }

        itemView.setOnClickListener(view -> {
            if(itemViewClickListener == null)
                return;
            itemViewClickListener.onItemViewClecked(getAdapterPosition());
        });
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public void setvisibility(int viewId, int visibility){
        View v= this.getView(viewId);
        v.setVisibility(visibility);
    }

    /**
     * 加载drawable中的图片
     * @param viewId
     * @param resId
     */
    public void setImageResource(int viewId, int resId) {
        ImageView iv = getView(viewId);
        iv.setImageResource(resId);
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param bm
     */
    public void setImageBitmap(int viewId, Bitmap bm)
    {
        ImageView iv = getView(viewId);
        iv.setImageBitmap(bm);
    }

    public void setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
    }

    public void setSubViewOnClick(int viewId, View.OnClickListener listener){
        View v = this.getItemView();
        v.setOnClickListener(listener);
    }

    public void setButtonOnClick(int viewId, View.OnClickListener listener){
        Button bt = this.getView(viewId);
        bt.setOnClickListener(listener);
    }

    public interface OnItemViewClickListener
    {
        void onItemViewClecked(int position);
    }
}
