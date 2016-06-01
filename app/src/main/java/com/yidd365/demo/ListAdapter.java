package com.yidd365.demo;

import android.content.Context;

import com.yidd365.utility.recyclerView.CommonRecyclerViewHolder;
import com.yidd365.utility.recyclerView.SimpleRVBaseAdapter;

/**
 * Created by orinchen on 16/5/30.
 */
public class ListAdapter extends SimpleRVBaseAdapter<ItemData> {
    public ListAdapter(Context context) {
        super(context);
    }

    @Override
    protected void onBindDataToView(CommonRecyclerViewHolder viewHolder, ItemData cookInfo, int position) {
        viewHolder.setText(R.id.index, cookInfo.getIndex() + "");
        viewHolder.setText(R.id.name, cookInfo.getName() + "");
    }

    @Override
    public int getItemLayoutID(int i) {
        return R.layout.item_layout;
    }
}
