package com.yidd365.utility.RecyclerViewAdapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
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
    public void setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
    }

    /**
     * 加载drawable中的图片
     * @param viewId
     * @param resId
     */
    public void setImage(int viewId, int resId) {
        ImageView iv = getView(viewId);
        iv.setImageResource(resId);
    }

    public interface OnItemViewClickListener
    {
        void onItemViewClecked(int position);
    }
}
