package com.whaley.biz.setting.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.whaley.core.widget.viewholder.ListAdapter;

public class BaseHeadFootAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> implements ListAdapter {
    private static final int ITEM_TYPE_HEADER = -1000;
    private static final int ITEM_TYPE_FOOTER = 1000;
    private RecyclerView.Adapter adapter;

    private List<MoreViewHolder> headViews = new ArrayList<>();
    private List<MoreViewHolder> footViews = new ArrayList<>();

    private List<Integer> headTypes = new ArrayList<>();

    private List<Integer> footTypes = new ArrayList<>();


    private int count;

    private int adapterCount;

    public BaseHeadFootAdapter(RecyclerView.Adapter adapter) {
        setWrapAdapter(adapter);
        adapterCount=adapter.getItemCount();
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if(!(holder instanceof MoreViewHolder)){
            adapter.onViewRecycled(holder);
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if(!(holder instanceof MoreViewHolder)){
            adapter.onViewDetachedFromWindow(holder);
        }
    }

    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    @Override
    public void setData(List datas) {
        if (adapter instanceof ListAdapter) {
            ListAdapter listAdapter = (ListAdapter) adapter;
            listAdapter.setData(datas);
        }
    }

    @Override
    public void updates() {
        notifyDataSetChanged();
    }

    public void addHeader(View headView) {
        headViews.add(new MoreViewHolder(headView));
        updateCount();
    }

    private void updateCount(){
        count=adapter.getItemCount() + headViews.size()
                + footViews.size();
    }

    public void removeAllHeader() {
        headViews.removeAll(headViews);
        updateCount();
    }

    public void addFooter(View footView) {
        footViews.add(new MoreViewHolder(footView));
        updateCount();
    }

    public void removeAllFooter() {
        footViews.removeAll(footViews);
        updateCount();
    }

    private static class MoreViewHolder extends RecyclerView.ViewHolder {

        public MoreViewHolder(View arg0) {
            super(arg0);
            // TODO Auto-generated constructor stub
        }
    }

    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        if (position < headViews.size()) {
            int type = position + 10000;
            headTypes.add(type);
            return type;
        } else if (position < adapter.getItemCount() + headViews.size()) {
            if(adapter instanceof BaseHeadFootAdapter){
//                Logger.i("nestAdapter type="+adapter.getItemViewType(position - headViews.size()));
            }
            return adapter.getItemViewType(position - headViews.size());
        } else {
            int type = position + 10000;
            footTypes.add(type);
//            Logger.i("type="+type);
            return type;
        }
    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return count;
    }

    public int getFooterSize() {
        return footViews.size();
    }

    public int getHeaderSize() {
        return headViews.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        // TODO Auto-generated method stub
        if (!(viewHolder instanceof MoreViewHolder)) {
            adapter.onBindViewHolder(viewHolder, position - getHeaderSize());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        // TODO Auto-generated method stub
        RecyclerView.ViewHolder viewHolder;
//        Logger.i("itemType="+itemType+",headersize="+getHeaderSize()+",footersize="+getFooterSize()+",adapter itemcount="+adapterCount);

        if (headTypes.contains(itemType)) {
            int position = itemType - 10000;
            viewHolder = headViews.get(position);
        } else if (footTypes.contains(itemType)) {
            int position = itemType - 10000;
            viewHolder = footViews.get(position - getHeaderSize() - adapterCount);
        } else {
            viewHolder = adapter.onCreateViewHolder(viewGroup, itemType);
        }

        return viewHolder;
    }

    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        BaseHeadFootAdapter headfootAdapter = BaseHeadFootAdapter.this;

        @Override
        public void onChanged() {
            updateCount();
            adapterCount=adapter.getItemCount();
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            updateCount();
            adapterCount=adapter.getItemCount();
            headfootAdapter.notifyItemRangeChanged(positionStart+headViews.size(), itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            updateCount();
            adapterCount=adapter.getItemCount();
            headfootAdapter.notifyItemRangeInserted(positionStart+headViews.size(), itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            updateCount();
            adapterCount=adapter.getItemCount();
            headfootAdapter.notifyItemRangeRemoved(positionStart+headViews.size(), itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition,
                                     int itemCount) {
            updateCount();
            adapterCount=adapter.getItemCount();
            headfootAdapter.notifyItemMoved(fromPosition+headViews.size(), itemCount);
        }
    };

    public void setWrapAdapter(RecyclerView.Adapter adapter) {
        if (adapter == null)
            return;
        if (this.adapter != null)
            this.adapter.unregisterAdapterDataObserver(observer);
        this.adapter = adapter;
        this.adapter.registerAdapterDataObserver(observer);

    }

}

