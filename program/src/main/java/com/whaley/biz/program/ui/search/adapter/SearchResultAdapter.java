package com.whaley.biz.program.ui.search.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.ui.uimodel.SearchResultViewModel;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.widget.adapter.RecyclerViewAdapter;
import com.whaley.core.widget.adapter.ViewHolder;

/**
 * Created by dell on 2017/8/25.
 */

public class SearchResultAdapter extends RecyclerViewAdapter<SearchResultViewModel, ViewHolder> implements ProgramConstants {

    ImageRequest.RequestManager requestManager;

    public SearchResultAdapter(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public ViewHolder onCreateNewViewHolder(ViewGroup viewGroup, int position) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search_result, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, SearchResultViewModel searchResultViewModel, int position) {
        bindViewHolder(searchResultViewModel, viewHolder, position);
    }

    private void bindViewHolder(SearchResultViewModel searchResultViewModel, ViewHolder holder, int position) {
        ImageView iv_img = holder.getView(R.id.pic);
        ImageView iv_tag = holder.getView(R.id.tag);
        TextView tv_name = holder.getView(R.id.name);
        TextView subTitle = holder.getView(R.id.subTitle);
        tv_name.setText(searchResultViewModel.getName());
        subTitle.setText(searchResultViewModel.getSubTitle());
        if(searchResultViewModel.isDrama()){
            iv_tag.setVisibility(View.VISIBLE);
        }else{
            iv_tag.setVisibility(View.GONE);
        }
        requestManager.load(searchResultViewModel.getPic()).fitCenter().small().placeholder(R.mipmap.default_6).into(iv_img);
    }

}
