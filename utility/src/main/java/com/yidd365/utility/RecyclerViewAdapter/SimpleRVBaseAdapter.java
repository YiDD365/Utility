package com.yidd365.utility.RecyclerViewAdapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.yidd365.utility.ViewUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by orinchen on 16/5/19.
 */
public abstract class SimpleRVBaseAdapter<T, VT extends CommonRecyclerViewHolder> extends RecyclerView.Adapter<VT> {
    protected List<T> items;
    protected Context context;
    protected boolean enableAnimateItems = false;
    protected int lastAnimatedPosition = -1;
    protected OnItemClickListener<T> onItemClickListener;

    public SimpleRVBaseAdapter(Context context){
        this.context = context;
        this.items = new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener){
        this.onItemClickListener = listener;
    }

    @Override
    public VT onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(getItemLayoutID(viewType), parent, false);
        CommonRecyclerViewHolder.OnItemViewClickListener listener = position -> {
            if(SimpleRVBaseAdapter.this.onItemClickListener == null)
                return;
            T item = getItem(position);
            SimpleRVBaseAdapter.this.onItemClickListener.onItemClicked(item);
        };

        return this.createViewHolder(view, listener);
    }
    @Override
    public void onBindViewHolder(VT holder, int position) {
        runEnterAnimation(holder.getItemView(), position);
        onBindDataToView(holder, items.get(position));
    }

    /**
     * 绑定数据到Item的控件中去
     * @param holder
     * @param bean
     */
    protected abstract void onBindDataToView(VT holder, T bean);

    protected abstract VT createViewHolder(View view, CommonRecyclerViewHolder.OnItemViewClickListener listener);

    /**
     * 取得ItemView的布局文件
     * @param viewType
     * @return
     */
    public abstract int getItemLayoutID(int viewType);

    public final T getItem(int position){
        return this.items.get(position);
    }

    public void cleanItems(){
        if(this.items == null)
            return;

        this.items.clear();
        this.notifyDataSetChangedEx();
    }

    public int addItems(Collection<T> items){
        if(items == null || items.size()<1)
            return 0;

        if(this.items == null)
            this.items = new ArrayList<>(items);

        this.items.addAll(items);
        this.notifyDataSetChangedEx();
        return items.size();
    }

    public int bindData(Collection<T> items){
        if(items == null || items.size()<1) {
            cleanItems();
            return 0;
        }

        if(this.items == null)
            this.items = new ArrayList<>(items);
        else {
            this.items.clear();
            this.items.addAll(items);
        }

        this.notifyDataSetChangedEx();
        return items.size();
    }

    @Override
    public int getItemCount() {
        return this.items != null ? this.items.size() : 0;
    }

    /**
     * item的加载动画
     * @param view
     * @param position
     */
    private void runEnterAnimation(View view, int position) {
        if (!enableAnimateItems) {
            return;
        }
        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(ViewUtils.getScreenHeight(context));
            view.animate()
                    .translationY(50)
                    .setStartDelay(100)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(300)
                    .start();
        }
    }

    public interface OnItemClickListener<T>
    {
        void onItemClicked(T item);
    }

    private void notifyDataSetChangedEx(){
        Handler h = new Handler(Looper.getMainLooper());
        h.post(SimpleRVBaseAdapter.this::notifyDataSetChanged);
    }
}
