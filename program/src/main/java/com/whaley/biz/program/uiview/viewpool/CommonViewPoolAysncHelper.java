package com.whaley.biz.program.uiview.viewpool;

import android.content.MutableContextWrapper;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.whaley.biz.program.uiview.ClickableUIViewHolder;
import com.whaley.biz.program.uiview.UIAdapterCreator;
import com.whaley.biz.program.uiview.adapter.LiveTopicUIAdapter;
import com.whaley.biz.program.uiview.adapter.RecyclerViewUIAdapter;
import com.whaley.biz.program.uiview.adapter.UIAdapter;
import com.whaley.biz.program.uiview.viewmodel.ClickableUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.NestedParentUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.RecyclerViewModel;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.utils.ReflexUtil;

import java.util.List;

/**
 * Created by yangzhi on 2017/9/30.
 */

public class CommonViewPoolAysncHelper {

    public static void putViewHoldersByRecyclerViewModel(ViewGroup parent, RecyclerViewModel recyclerViewModel) {
        if (parent == null) {
            return;
        }
        if (recyclerViewModel == null) {
            return;
        }
        List<ClickableUIViewModel> clickableUIViewModels = recyclerViewModel.getClickableUiViewModels();
        if (clickableUIViewModels == null || clickableUIViewModels.size() == 0) {
            return;
        }
        for (ClickableUIViewModel clickableUIViewModel : clickableUIViewModels) {
            int type = clickableUIViewModel.getType();
            int count = CommonViewPool.getInstance().getRecycledViewCount(type);
            int maxCount = CommonViewPool.getInstance().getMaxRecycledViewsByType(type);
            if (count < maxCount) {
                UIAdapter uiAdapter = UIAdapterCreator.createUIAdapterByType(parent, type);
                ClickableUIViewHolder uiViewHolder = uiAdapter.onCreateView(parent, type);
                RecyclerView.ViewHolder viewHolder = new RecyclerViewUIAdapter.Holder(uiViewHolder.getRootView(), uiAdapter, uiViewHolder);
                ReflexUtil.setField(RecyclerView.ViewHolder.class.getName(), viewHolder, "mItemViewType", type);
                MutableContextWrapper contextWrapper = (MutableContextWrapper) uiViewHolder.getRootView().getContext();
                contextWrapper.setBaseContext(AppContextProvider.getInstance().getContext());
                CommonViewPool.getInstance().putRecycledView(viewHolder);
                if (clickableUIViewModel instanceof NestedParentUIViewModel) {
                    NestedParentUIViewModel nestedParentUIViewModel = (NestedParentUIViewModel) clickableUIViewModel;
                    RecyclerViewModel childRecyclerViewModel = nestedParentUIViewModel.getChildRecyclerViewModel();
                    ViewGroup childParent = uiViewHolder.getRootView();
                    putViewHoldersByRecyclerViewModel(childParent, childRecyclerViewModel);
                }
            }
        }
    }
}
