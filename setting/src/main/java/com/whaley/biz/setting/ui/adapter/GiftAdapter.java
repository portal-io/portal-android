package com.whaley.biz.setting.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whaley.biz.setting.R;
import com.whaley.biz.setting.R2;
import com.whaley.biz.setting.util.SettingUtil;
import com.whaley.biz.setting.ui.viewmodel.GiftViewModel;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.widget.adapter.RecyclerViewAdapter;
import com.whaley.core.widget.adapter.ViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dell on 2017/8/1.
 */

public class GiftAdapter extends RecyclerViewAdapter<GiftViewModel, ViewHolder> {

    private static final int NORMAL_ITEM = 0;
    private static final int GROUP_ITEM = 1;
    ImageRequest.RequestManager requestManager;

    public GiftAdapter(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return GROUP_ITEM;
        String currentDate = datas.get(position).getYear();
        int prevIndex = position - 1;
        String prevData = datas.get(prevIndex).getYear();
        boolean isDifferent = !currentDate.equals(prevData);
        return isDifferent ? GROUP_ITEM : NORMAL_ITEM;
    }

    @Override
    public ViewHolder onCreateNewViewHolder(ViewGroup parent, int viewType) {
        if (viewType == NORMAL_ITEM) {
            return new LocalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gift_without_title,
                    parent, false));
        } else {
            return new LocalViewHolderWithTitle(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gift_with_title,
                    parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, GiftViewModel giftViewModel, int position) {
        if (holder instanceof LocalViewHolderWithTitle) {
            bindGroupItem(giftViewModel, (LocalViewHolderWithTitle) holder, position);
        } else {
            bindNormalItem(giftViewModel, (LocalViewHolder) holder, position);
        }
    }

    private void bindNormalItem(GiftViewModel giftViewModel, LocalViewHolder holder, int position) {
        holder.position = position;
        holder.title.setText(giftViewModel.getDate()+"我抽中了");
        holder.content.setText(giftViewModel.getContent());
        if(giftViewModel.getType()==1){
            holder.tvRedeem.setText(giftViewModel.getInfo());
            holder.layoutRedeem.setVisibility(View.VISIBLE);
            holder.tvVirtual.setVisibility(View.GONE);
        }else if(giftViewModel.getType()==2) {
            holder.layoutRedeem.setVisibility(View.GONE);
            holder.tvVirtual.setVisibility(View.VISIBLE);
            holder.tvVirtual.setText(giftViewModel.getInfo());
        } else{
            holder.layoutRedeem.setVisibility(View.GONE);
            holder.tvVirtual.setVisibility(View.GONE);
        }
        requestManager.load(giftViewModel.getPic()).medium().centerCrop().into(holder.pic);
    }

    private void bindGroupItem(GiftViewModel giftViewModel, LocalViewHolderWithTitle holder, int position) {
        bindNormalItem(giftViewModel, holder, position);
        holder.date.setText(giftViewModel.getMonth());
    }

    class LocalViewHolder extends ViewHolder {
        @BindView(R2.id.pic)
        ImageView pic;
        @BindView(R2.id.title)
        TextView title;
        @BindView(R2.id.content)
        TextView content;

        @BindView(R2.id.tv_redeem)
        TextView tvRedeem;
        @BindView(R2.id.layout_redeem)
        LinearLayout layoutRedeem;
        @BindView(R2.id.tv_virtual)
        TextView tvVirtual;

        int position;

        @OnClick({R2.id.btn_redeem})
        public void onClick(View view) {
            if (null != onRecyclerViewListener) {
                onRecyclerViewListener.onBtnClick(position);
            }
        }

        LocalViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class LocalViewHolderWithTitle extends LocalViewHolder {

        @BindView(R2.id.date)
        TextView date;

        LocalViewHolderWithTitle(View view) {
            super(view);
        }
    }

    public interface OnRecyclerViewListener {
        void onBtnClick(int position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

}
