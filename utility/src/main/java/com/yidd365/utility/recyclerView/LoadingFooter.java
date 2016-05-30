package com.yidd365.utility.recyclerView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.widget.RelativeLayout;

import com.yidd365.utility.R;

/**
 * Created by orinchen on 16/5/25.
 */

public class LoadingFooter extends RelativeLayout implements RecyclerViewStateManager.IStateFooterView {

    protected RecyclerViewStateManager.State mState = RecyclerViewStateManager.State.Normal;

    private View loadingView;
    private View networkErrorView;
    private View endView;
    private View emptyView;
    private View emptyWithErrorView;

    private OnClickListener networkErrViewClickListener;

    private int loadingViewId = R.layout.layout_simple_footer_loading;
    private int empytViewId  = R.layout.layout_simple_footer_empty;
    private int netErrorViewId = R.layout.layout_simple_footer_network_error;
    private int endViewId = R.layout.layout_simple_footer_end;
    private int emptyAndErrorId = R.layout.layout_simple_footer_empty_error;


    public int getEndViewId() {
        return endViewId;
    }

    public int getLoadingViewId() {
        return loadingViewId;
    }

    public void setLoadingViewId(int loadingViewId) {
        this.loadingViewId = loadingViewId;
    }

    public int getEmpytViewId() {
        return empytViewId;
    }

    public void setEmpytViewId(int empytViewId) {
        this.empytViewId = empytViewId;
    }

    public int getNetErrorViewId() {
        return netErrorViewId;
    }

    public void setNetErrorViewId(int netErrorViewId) {
        this.netErrorViewId = netErrorViewId;
    }

    public void setEndViewId(int endViewId) {
        this.endViewId = endViewId;
    }

    public int getEmptyAndErrorId() {
        return emptyAndErrorId;
    }

    public void setEmptyAndErrorId(int emptyAndErrorId) {
        this.emptyAndErrorId = emptyAndErrorId;
    }


    public OnClickListener getNetworkErrViewClickListener() {
        return networkErrViewClickListener;
    }

    public void setNetworkErrViewClickListener(OnClickListener networkErrViewClickListener) {
        this.networkErrViewClickListener = networkErrViewClickListener;
    }

    public LoadingFooter(Context context) {
        super(context);
        init(context);
    }

    public LoadingFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadingFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.layout_simple_footer, this);
        setOnClickListener(null);
        setState(RecyclerViewStateManager.State.Normal);
    }

    @Override
    public RecyclerViewStateManager.State getState() {
        return mState;
    }

    /**
     * 设置状态
     *
     * @param status
     */
    @Override
    public void setState(RecyclerViewStateManager.State status) {
        if (mState == status) {
            return;
        }
        mState = status;

        if (loadingView != null) {
            loadingView.setVisibility(GONE);
        }

        if (endView != null) {
            endView.setVisibility(GONE);
        }

        if (networkErrorView != null) {
            networkErrorView.setVisibility(GONE);
        }

        if (emptyView != null){
            emptyView.setVisibility(GONE);
        }

        if (emptyWithErrorView != null){
            emptyWithErrorView.setVisibility(GONE);
        }

        switch (status) {
            case Loading:

                if (loadingView == null) {
                    ViewStub viewStub = (ViewStub) findViewById(R.id.loading_viewstub);
                    viewStub.setLayoutResource(getLoadingViewId());
                    loadingView = viewStub.inflate();
                }
                loadingView.setVisibility(VISIBLE);

                break;
            case TheEnd:

                if (endView == null) {
                    ViewStub viewStub = (ViewStub) findViewById(R.id.end_viewstub);
                    viewStub.setLayoutResource(getEndViewId());
                    endView = viewStub.inflate();
                }
                endView.setVisibility(VISIBLE);

                break;
            case NetWorkError:

                if (networkErrorView == null) {
                    ViewStub viewStub = (ViewStub) findViewById(R.id.network_error_viewstub);
                    viewStub.setLayoutResource(this.getNetErrorViewId());
                    networkErrorView = viewStub.inflate();
                    networkErrorView.setOnClickListener(this.networkErrViewClickListener);
                }
                networkErrorView.setVisibility(VISIBLE);

                break;

            case NoData:

                if (emptyView == null){
                    ViewStub viewStub = (ViewStub) findViewById(R.id.empty_viewstub);
                    viewStub.setLayoutResource(this.getEmpytViewId());
                    emptyView = viewStub.inflate();
                }
                emptyView.setVisibility(VISIBLE);

                break;

            case NoDataWithError:

                if(emptyWithErrorView == null){
                    ViewStub viewStub = (ViewStub) findViewById(R.id.empty_err_viewstub);
                    viewStub.setLayoutResource(this.getEmpytViewId());
                    emptyWithErrorView = viewStub.inflate();
                }
                emptyWithErrorView.setVisibility(getVisibility());

            default: // Normal

                break;
        }
    }
}
