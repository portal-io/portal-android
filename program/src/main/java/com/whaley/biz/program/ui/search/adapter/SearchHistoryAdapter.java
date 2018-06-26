package com.whaley.biz.program.ui.search.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.core.widget.adapter.RecyclerViewAdapter;
import com.whaley.core.widget.adapter.ViewHolder;

/**
 * Created by dell on 2017/8/24.
 */

public class SearchHistoryAdapter extends RecyclerViewAdapter<String, ViewHolder> {

    @Override
    public ViewHolder onCreateNewViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, String data, int position) {
        TextView name = holder.getView(R.id.tv_name);
        name.setText(data);
    }

}
