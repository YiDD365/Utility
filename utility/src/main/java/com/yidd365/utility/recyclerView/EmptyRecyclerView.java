package com.yidd365.utility.recyclerView;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by orinchen on 16/6/1.
 */
public class EmptyRecyclerView extends RecyclerView {
    private View emptyView;
    final private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }
    };

    public EmptyRecyclerView(Context context) {
        super(context);
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    void checkIfEmpty() {

        Adapter adapter = getAdapter();
        if (emptyView != null && getAdapter() != null) {
            boolean emptyViewVisible = false;
            if(adapter instanceof HeaderAndFooterRecyclerViewAdapter){
                HeaderAndFooterRecyclerViewAdapter hAdapter =
                        (HeaderAndFooterRecyclerViewAdapter) adapter;
                emptyViewVisible =
                        (hAdapter.getItemCount() - hAdapter.getFooterViewsCount()- hAdapter.getHeaderViewsCount()) == 0;
            }else{
                emptyViewVisible = getAdapter().getItemCount() == 0;
            }

            emptyView.setVisibility(emptyViewVisible ? VISIBLE : GONE);
            setVisibility(emptyViewVisible ? GONE : VISIBLE);

            if(this.getParent() instanceof SwipeRefreshLayout){
                if(emptyViewVisible) {
                    ((SwipeRefreshLayout) this.getParent()).setRefreshing(false);
                }
                ((SwipeRefreshLayout) this.getParent()).setVisibility(emptyViewVisible ? GONE : VISIBLE);
            }
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }

        checkIfEmpty();
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        checkIfEmpty();
    }
}
