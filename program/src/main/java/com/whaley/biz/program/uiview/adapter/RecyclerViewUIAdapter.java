package com.whaley.biz.program.uiview.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whaley.biz.program.R;
import com.whaley.biz.program.uiview.BannerPlayerProviderSetter;
import com.whaley.biz.program.uiview.ClickableSimpleViewHolder;
import com.whaley.biz.program.uiview.ClickableUIViewHolder;
import com.whaley.biz.program.uiview.ImageloaderUser;
import com.whaley.biz.program.uiview.OnClickableUIViewClickListener;
import com.whaley.biz.program.uiview.UIAdapterCreator;
import com.whaley.biz.program.uiview.VisibleSwitcher;
import com.whaley.biz.program.uiview.viewmodel.ClickableUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.OutRectUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.RecyclerViewModel;
import com.whaley.biz.program.uiview.viewpool.CommonViewPool;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.widget.adapter.RecyclerViewAdapter;
import com.whaley.core.widget.uiview.UIViewClickSetter;


import java.util.ArrayList;
import java.util.List;

/**
 * Author: qxw
 * Date: 2017/3/10
 */

public class RecyclerViewUIAdapter extends BaseUIAdapter<RecyclerViewUIAdapter.RecyclerHolder, RecyclerViewModel> implements ImageloaderUser, VisibleSwitcher {

    private ImageRequest.RequestManager requestManager;


    private RecyclerHolder recyclerHolder;

    private boolean isVisible;


    @Override
    public void setRequestManager(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    public void setRecyclerHolder(RecyclerHolder recyclerHolder) {
        this.recyclerHolder = recyclerHolder;
    }


    @Override
    public void changeVisible(boolean isVisible) {
        if (isVisible == this.isVisible) {
            return;
        }
        this.isVisible = isVisible;
        getViewHolder().adapter.changeVisible(isVisible);
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int type) {
        if (recyclerHolder != null)
            return recyclerHolder;
        return new RecyclerHolder(getLayoutInfalterFormParent(parent).inflate(R.layout.layout_recycler_uiview, parent, false));
    }

    @Override
    public void onAttach() {
        super.onAttach();
        getViewHolder().adapter.onAttach();
    }

    @Override
    public void onDettatch() {
        super.onDettatch();
        getViewHolder().adapter.onDettach();
    }

    @Override
    public void onRecycled() {
        super.onRecycled();
        setRequestManager(null);
        getViewHolder().adapter.onRecycled();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (recyclerHolder != null && recyclerHolder.adapter != null) {
            recyclerHolder.adapter.visibleSwitchers.clear();
        }
    }

    public UIAdapter findChildAdapter(int position) {
        RecyclerView recyclerView = recyclerHolder.getRootView();
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
        UIAdapter uiAdapter = null;
        if (viewHolder != null) {
            Holder holder = (Holder) viewHolder;
            uiAdapter = holder.uiAdapter;
        }
        return uiAdapter;
    }

    @Override
    public boolean onInitViewHolder(final RecyclerHolder viewHolder, final RecyclerViewModel viewModel, final int position) {
        super.onInitViewHolder(viewHolder, viewModel, position);
        if (viewModel == null)
            return false;
        final RecyclerView recyclerView = viewHolder.getRootView();
        recyclerView.setHasFixedSize(true);
        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
        layoutParams.height = viewModel.getHight();
        layoutParams.width = viewModel.getWidth();
        recyclerView.setLayoutParams(layoutParams);
        RecyclerView.LayoutManager layoutManager;
        if (viewModel.isGrid()) {
            layoutManager = new GridLayoutManager(recyclerView.getContext(), viewModel.getColumnCount());
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return viewModel.getSpanSize(position);
                }
            });
        } else {
            if (viewModel.isHorizontal()) {
                layoutManager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false);
            } else {
                layoutManager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
            }
        }
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = recyclerView.getLayoutManager().getPosition(view);
                OutRectUIViewModel outRectUIViewModel = viewModel.getOutRectModel(position);
                if (outRectUIViewModel != null) {
                    outRect.top = outRectUIViewModel.getOutTop();
                    outRect.bottom = outRectUIViewModel.getOutBottom();
                    outRect.left = outRectUIViewModel.getOutLeft();
                    outRect.right = outRectUIViewModel.getOutRight();
                }
            }
        });
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
        linearLayoutManager.setRecycleChildrenOnDetach(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setRecycledViewPool(CommonViewPool.getInstance());
        return true;
    }

    @Override
    public void onBindViewHolder(RecyclerHolder viewHolder, RecyclerViewModel uiViewModel, int position) {
        viewHolder.adapter.setRealContext(getRealContext());
        viewHolder.adapter.setRequestManager(requestManager);
        viewHolder.adapter.setOnUIViewClickListener(getOnUIViewClickListener());
        if (uiViewModel == null) {
            viewHolder.adapter.setData(null);
            return;
        }
        viewHolder.adapter.setData(uiViewModel.getClickableUiViewModels());
    }


    public static class Adapter extends RecyclerViewAdapter<ClickableUIViewModel, Holder> implements ImageloaderUser {
        ImageRequest.RequestManager requestManager;

        OnClickableUIViewClickListener onUIViewClickListener;

        List<VisibleSwitcher> visibleSwitchers = new ArrayList<>();

        List<UIAdapter> uiAdapters = new ArrayList<>();


        boolean isVisible;

        Context realContext;

        public int getUIAdaptersSize() {
            return uiAdapters.size();
        }


        public void setRealContext(Context realContext) {
            this.realContext = realContext;
        }

        public Context getRealContext() {
            return realContext;
        }

        @Override
        public void setRequestManager(ImageRequest.RequestManager requestManager) {
            this.requestManager = requestManager;
        }

        public void setOnUIViewClickListener(OnClickableUIViewClickListener onUIViewClickListener) {
            this.onUIViewClickListener = onUIViewClickListener;
        }

        @Override
        public void setData(List<ClickableUIViewModel> datas) {
            visibleSwitchers.clear();
            uiAdapters.clear();
            super.setData(datas);
        }

        public void changeVisible(boolean isVisible) {
            this.isVisible = isVisible;
            Log.d("BannerPlayerContainer", "recyclerviewa adapter isVisible= " + isVisible);
            if (visibleSwitchers.size() > 0) {
                for (VisibleSwitcher switcher : visibleSwitchers) {
                    switcher.changeVisible(isVisible);
                }
            }
        }

        public int getItemViewType(int position) {
            if (getData() != null && getData().size() > position) {
                return getData().get(position).getType();
            }
            return super.getItemViewType(position);
        }

        @Override
        public void onViewDetachedFromWindow(Holder viewHolder) {
            super.onViewDetachedFromWindow(viewHolder);
            viewHolder.uiAdapter.onDettatch();
            if (viewHolder.uiAdapter instanceof VisibleSwitcher) {
                ((VisibleSwitcher) viewHolder.uiAdapter).changeVisible(false);
                visibleSwitchers.remove(viewHolder.uiAdapter);
            }
        }

        @Override
        public void onViewAttachedToWindow(Holder viewHolder) {
            super.onViewAttachedToWindow(viewHolder);
            viewHolder.uiAdapter.onAttach();
            if (viewHolder.uiAdapter instanceof VisibleSwitcher) {
                ((VisibleSwitcher) viewHolder.uiAdapter).changeVisible(isVisible);
            }
        }

        @Override
        public void onViewRecycled(Holder viewHolder) {
            super.onViewRecycled(viewHolder);
            viewHolder.uiAdapter.onRecycled();
            uiAdapters.remove(viewHolder.uiAdapter);
        }

        public void onAttach() {

        }

        public void onDettach() {

        }

        public void onRecycled() {
            setRealContext(null);
            setRequestManager(null);
            setOnUIViewClickListener(null);
            for (UIAdapter uiAdapter : uiAdapters) {
                uiAdapter.onRecycled();
            }
        }

        @Override
        public Holder onCreateNewViewHolder(ViewGroup parent, int viewType) {
            if(CommonViewPool.isDebug) {
                Log.e(CommonViewPool.TAG, "创建ViewHolder type = " + viewType);
            }
            UIAdapter uiAdapter = UIAdapterCreator.createUIAdapterByType(parent, viewType);
            ClickableUIViewHolder UIViewHolder = uiAdapter.onCreateView(parent, viewType);
            return new Holder(UIViewHolder.getRootView(), uiAdapter, UIViewHolder);
        }

        @Override
        public void onBindViewHolder(Holder holder, ClickableUIViewModel clickableUiViewModel, int position) {
            if (holder.uiAdapter instanceof ImageloaderUser) {
                ((ImageloaderUser) holder.uiAdapter).setRequestManager(requestManager);
            }
            if (holder.uiAdapter instanceof UIViewClickSetter && onUIViewClickListener != null) {
                ((UIViewClickSetter) holder.uiAdapter).setOnUIViewClickListener(onUIViewClickListener);
            }
            if (holder.uiAdapter instanceof BaseUIAdapter) {
                BaseUIAdapter baseUIAdapter = (BaseUIAdapter) holder.uiAdapter;
                baseUIAdapter.setRealContext(realContext);
            }
            uiAdapters.add(holder.uiAdapter);
            holder.uiAdapter.onBindView(holder.UIViewHolder, clickableUiViewModel, position);
            if (holder.uiAdapter instanceof VisibleSwitcher) {
                VisibleSwitcher visibleSwitcher = (VisibleSwitcher) holder.uiAdapter;
                visibleSwitchers.add(visibleSwitcher);
            }
        }


//        @Override
//        public void onViewRecycled(ClickableUIViewHolder holder) {
//            super.onViewRecycled(holder);
//            Holder ClickableUIViewHolder = (Holder) holder;
//            ClickableUIViewHolder.uiAdapter.onDettatch();
//            if (ClickableUIViewHolder.uiAdapter instanceof VisibleSwitcher) {
//                visibleSwitchers.remove(ClickableUIViewHolder.uiAdapter);
//            }
//            uiAdapters.remove(ClickableUIViewHolder.uiAdapter);
//        }
//
//        @Override
//        public ClickableUIViewHolder onCreateNewViewHolder(ViewGroup parent, int viewType) {
//            UIAdapter uiAdapter = UIAdapterCreator.createUIAdapterByType(parent, viewType);
//            com.whaleyvr.view.ClickableUIViewHolder ClickableUIViewHolder = uiAdapter.onCreateView(parent, viewType);
//            return new Holder(ClickableUIViewHolder.getRootView(), uiAdapter, ClickableUIViewHolder);
//        }
//
//        @Override
//        public void onBindViewHolder(com.whaley.core.widget.adapter.ClickableUIViewHolder ClickableUIViewHolder, Object o, int i) {
//
//        }
//
//        @Override
//        public void onBindViewHolder(ClickableUIViewHolder ClickableUIViewHolder, ClickableUIViewModel data, int position) {
//            Holder holder = (Holder) ClickableUIViewHolder;
//            if (holder.uiAdapter instanceof ImageloaderUser) {
//                ((ImageloaderUser) holder.uiAdapter).setRequestManager(requestManager);
//            }
//            if (holder.uiAdapter instanceof ClickSetter && onViewClickListener != null) {
//                ((ClickSetter) holder.uiAdapter).setOnViewClickListener(onViewClickListener);
//            }
//            if (holder.uiAdapter instanceof VisibleSwitcher) {
//                visibleSwitchers.add((VisibleSwitcher) holder.uiAdapter);
//            }
//            uiAdapters.add(holder.uiAdapter);
//            holder.uiAdapter.onBindView(holder.ClickableUIViewHolder, data, position);
//        }
    }

    public static class Holder extends com.whaley.core.widget.adapter.ViewHolder {

        public UIAdapter uiAdapter;

        ClickableUIViewHolder UIViewHolder;

        public Holder(View view, UIAdapter uiAdapter, ClickableUIViewHolder UIViewHolder) {
            super(view);
            this.uiAdapter = uiAdapter;
            this.UIViewHolder = UIViewHolder;
        }
    }


    public static class RecyclerHolder extends ClickableSimpleViewHolder {
        Adapter adapter;


        public RecyclerHolder(View view) {
            super(view);
            RecyclerView recyclerView = getRootView();
            adapter = new Adapter();
            recyclerView.setAdapter(adapter);
        }

        public Adapter getAdapter() {
            return adapter;
        }
    }


}
