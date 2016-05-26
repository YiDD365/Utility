package com.yidd365.utility.recyclerView;

import android.view.View;

/**
 * Created by orinchen on 16/5/25.
 */

public interface OnListLoadNextPageListener {

    /**
     * 开始加载下一页
     *
     * @param view 当前RecyclerView/ListView/GridView
     */
    void onLoadNextPage(View view);
}
