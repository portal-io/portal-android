package com.whaley.biz.program.uiview.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.uiview.ClickableSimpleViewHolder;
import com.whaley.biz.program.uiview.OnClickableUIViewClickListener;
import com.whaley.biz.program.uiview.viewmodel.ShareBottomUIViewModel;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.share.ShareConstants;
import com.whaley.core.utils.DisplayUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: qxw
 * Date:2017/8/22
 * Introduction:
 */

public class ShareBottomUIAdapter extends BaseUIAdapter<ShareBottomUIAdapter.ViewHolder, ShareBottomUIViewModel> {


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(getLayoutInfalterFormParent(viewGroup).inflate(R.layout.item_share_bottom, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, ShareBottomUIViewModel model, int i) {
        if (model.isPay()) {
            holder.getRootView().setPadding(0, 0, 0, DisplayUtil.convertDIP2PX(78));
        } else {
            holder.getRootView().setPadding(0, 0, 0, DisplayUtil.convertDIP2PX(33));
        }
        int stringId = R.string.text_share_topic_title;
        if (model.getContentType() == ShareBottomUIViewModel.PROGRAM_SET_TYPE) {
            stringId = R.string.text_share_program_set_title;
        } else if (model.getContentType() == ShareBottomUIViewModel.PROGRAM_PACKAGE_TYPE) {
            stringId = R.string.text_share_program_package_title;
        }
        holder.tvShare.setText(AppContextProvider.getInstance().getContext().getString(stringId));
        holder.layoutSina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getOnUIViewClickListener() != null) {
                    ((ShareClickListener) getOnUIViewClickListener()).onShareClick(ShareConstants.TYPE_SINA);
                }
            }
        });
        holder.layoutWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getOnUIViewClickListener() != null) {
                    ((ShareClickListener) getOnUIViewClickListener()).onShareClick(ShareConstants.TYPE_WEIXIN);
                }
            }
        });

        holder.layoutWxfrends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getOnUIViewClickListener() != null) {
                    ((ShareClickListener) getOnUIViewClickListener()).onShareClick(ShareConstants.TYPE_WEIXIN_CIRCLE);
                }
            }
        });

        holder.layoutQq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getOnUIViewClickListener() != null) {
                    ((ShareClickListener) getOnUIViewClickListener()).onShareClick(ShareConstants.TYPE_QQ);
                }
            }
        });

        holder.layoutQzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getOnUIViewClickListener() != null) {
                    ((ShareClickListener) getOnUIViewClickListener()).onShareClick(ShareConstants.TYPE_QZONE);
                }
            }
        });
    }

    public static class ViewHolder extends ClickableSimpleViewHolder {
        @BindView(R2.id.layout_sina)
        LinearLayout layoutSina;
        @BindView(R2.id.layout_wx)
        LinearLayout layoutWx;
        @BindView(R2.id.layout_wxfrends)
        LinearLayout layoutWxfrends;
        @BindView(R2.id.layout_qq)
        LinearLayout layoutQq;
        @BindView(R2.id.layout_qzone)
        LinearLayout layoutQzone;
        @BindView(R2.id.tv_share)
        TextView tvShare;

        public ViewHolder(View rootView) {
            super(rootView);
            ButterKnife.bind(this, rootView);
        }
    }

    public interface ShareClickListener extends OnClickableUIViewClickListener {
        void onShareClick(int type);
    }
}
