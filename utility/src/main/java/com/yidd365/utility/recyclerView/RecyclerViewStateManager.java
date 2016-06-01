package com.yidd365.utility.recyclerView;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by orinchen on 16/5/26.
 */

public class RecyclerViewStateManager {

    private Activity activity;
    private RecyclerView recyclerView;
    private IStateFooterView footerView;
    private View.OnClickListener onClickListener;

    public RecyclerViewStateManager( @NonNull Activity activity,
                                     @NonNull RecyclerView recyclerView){
        this(activity, recyclerView, null);
    }

    public RecyclerViewStateManager(@NonNull Activity activity,
                                    @NonNull RecyclerView recyclerView,
                                    @Nullable IStateFooterView footerView){
        this.recyclerView = recyclerView;
        this.activity = activity;

        if(!(footerView instanceof View) || footerView == null){
            this.footerView = new LoadingFooter(this.activity);
        }else{
            this.footerView = footerView;
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener){
        this.onClickListener = onClickListener;
        if(this.footerView != null){
            ((View) this.footerView).setOnClickListener(this.onClickListener);
        }
    }

    /**
     * 设置headerAndFooterAdapter的FooterView State
     *
     * @param state         FooterView State
     */
    public void setFooterViewState(State state) {

        if (this.activity == null || this.activity.isFinishing()) {
            return;
        }

        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();

        if (outerAdapter == null || !(outerAdapter instanceof HeaderAndFooterRecyclerViewAdapter)) {
            return;
        }

        HeaderAndFooterRecyclerViewAdapter headerAndFooterAdapter = (HeaderAndFooterRecyclerViewAdapter) outerAdapter;

        //只有一页的时候，就别加什么FooterView了
//        if (headerAndFooterAdapter.getInnerAdapter().getItemCount() < pageSize) {
//            return;
//        }

        if (headerAndFooterAdapter.getFooterViewsCount() < 1) {
            if (footerView == null) {
                this.footerView = new LoadingFooter(this.activity);
                ((View)this.footerView).setOnClickListener(this.onClickListener);
            }

            headerAndFooterAdapter.addFooterView((View) footerView);
        }

        footerView.setState(state);
        //recyclerView.scrollToPosition(headerAndFooterAdapter.getItemCount() - 1);
    }

    /**
     * 获取当前RecyclerView.FooterView的状态
     *
     */
    public State getFooterViewState() {
        return  this.footerView.getState();
    }

    public enum State {
        Normal/**正常*/,
        TheEnd/**加载到最底了*/,
        Loading/**加载中..*/,
        NetWorkError/**网络异常*/
    }

    interface IStateFooterView {
        void setState(State state);
        State getState();
    }
}
