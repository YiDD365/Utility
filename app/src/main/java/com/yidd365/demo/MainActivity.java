package com.yidd365.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.yidd365.utility.recyclerView.EmptyRecyclerView;
import com.yidd365.utility.recyclerView.EndlessRecyclerOnScrollListener;
import com.yidd365.utility.recyclerView.HeaderAndFooterRecyclerViewAdapter;
import com.yidd365.utility.recyclerView.RecyclerViewStateManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.yidd365.utility.recyclerView.RecyclerViewStateManager.State.Normal;
import static com.yidd365.utility.recyclerView.RecyclerViewStateManager.State.TheEnd;

/**
 * Created by Neo on 16/5/24.
 */
public class MainActivity  extends AppCompatActivity {

    @BindView(R.id.cook_list_view)
    protected EmptyRecyclerView recyclerView;

    @BindView(R.id.empty_text_view)
    protected TextView emptyView;

    private RecyclerViewStateManager viewStateManager;
    private ListAdapter adapter = null;
    private HeaderAndFooterRecyclerViewAdapter headerAndFooterRecyclerViewAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        this.recyclerView.setEmptyView(emptyView);

        this.adapter = new ListAdapter(this);

        this.headerAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(this.adapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(this.headerAndFooterRecyclerViewAdapter);
        this.recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(){
            @Override
            public void onLoadNextPage(View view) {
                super.onLoadNextPage(view);
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<ItemData> datas = new ArrayList<>();
                        for (int i =0; i < 20; i++){
                            ItemData newData = new ItemData();
                            newData.setIndex(i);
                            newData.setName("Name:"+i);
                            datas.add(newData);
                        }
                        adapter.addItems(datas);
                        viewStateManager.setFooterViewState(TheEnd);
                    }
                }, 1000);
            }
        });
        this.viewStateManager = new RecyclerViewStateManager(this, this.recyclerView);
    }

    @OnClick(R.id.load_data)
    protected void load(){
        ArrayList<ItemData> datas = new ArrayList<>();
        for (int i =0; i < 20; i++){
            ItemData newData = new ItemData();
            newData.setIndex(i);
            newData.setName("Name:"+i);
            datas.add(newData);
        }

        adapter.bindData(datas);
        viewStateManager.setFooterViewState(Normal);
    }

    @OnClick(R.id.no_data)
    protected void clean(){
        adapter.cleanItems();
    }
}
