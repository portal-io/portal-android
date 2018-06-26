package com.whaley.biz.program.ui.detail.viewholder;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.utils.StringUtil;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.image.ImageRequest;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class VRViewHolder extends ProgramViewHolder<VRViewHolder.VRProgramModel> {
    @BindView(R2.id.tv_program_name)
    TextView tvProgramName;
    @BindView(R2.id.iv_play_icon)
    ImageView ivPlayIcon;
    @BindView(R2.id.tv_play_count)
    TextView tvPlayCount;
    @BindView(R2.id.tv_have_voucher)
    public TextView tvHaveVoucher;

    //        @BindView(R2.id.view_vertical_line)
//        View viewVerticalLine;
//        @BindView(R2.id.tv_duration)
//        TextView tvDuration;
    @BindView(R2.id.layout_program_title)
    RelativeLayout layoutProgramTitle;
    @BindView(R2.id.view_line)
    View viewLine;
    @BindView(R2.id.view_line2)
    View viewLine2;
    @BindView(R2.id.iv_poster_img)
    ImageView ivPosterImg;
    @BindView(R2.id.tv_poster_name)
    TextView tvPosterName;
    @BindView(R2.id.tv_poster_fans)
    TextView tvPosterFans;
    @BindView(R2.id.tv_follow)
    TextView tvFollow;
    @BindView(R2.id.layout_follow)
    FrameLayout layoutFollow;
    @BindView(R2.id.layout_poster_info)
    RelativeLayout layoutPosterInfo;

    //    View.OnClickListener onFollowClick;
//
//    View.OnClickListener onPosterClick;
    OnPosterClickListener onPosterClickListener;

    ImageRequest.RequestManager requestManager;

    public VRViewHolder(View view) {
        super(view);
    }

    public void setRequestManager(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    //    public void setOnFollowClick(View.OnClickListener onFollowClick) {
//        this.onFollowClick = onFollowClick;
//    }
//
    public void setOnPosterClick(OnPosterClickListener onPosterClickListener) {
        this.onPosterClickListener = onPosterClickListener;
    }

    @OnClick(R2.id.layout_follow)
    public void onFollowClick() {
        if (this.onPosterClickListener != null)
            this.onPosterClickListener.onFollowClick(layoutFollow);
    }

    @OnClick(R2.id.layout_poster_info)
    public void onPosterClick() {
        if (this.onPosterClickListener != null)
            this.onPosterClickListener.onPosterClick(layoutFollow);
    }

    @Override
    protected void onBindData(VRProgramModel data) {
        tvProgramName.setText(data.getProgramName());
        tvPosterName.setText(data.getPosterName());
        setFollowData(false, data.isPosterFollow());
        requestManager.load(data.getPosterImage())
                .centerCrop()
                .circle()
                .into(ivPosterImg);
        tvPlayCount.setText(data.getPlayCount());
        layoutDownlod.getChildAt(0).setEnabled(data.isDownloadEnable());
        TextView textView = (TextView) layoutDownlod.getChildAt(1);
        textView.setText("缓存");
        if (data.isDownloadEnable()) {
            textView.setTextColor(AppContextProvider.getInstance().getContext().getResources().getColor(R.color.color6));
        } else {
            textView.setTextColor(AppContextProvider.getInstance().getContext().getResources().getColor(R.color.color9));
        }

    }


    public void setFollowData(boolean isUpdate, boolean isFollow) {
        data.setPosterFollow(isFollow);
        if (isFollow) {
            tvFollow.setText("已关注");
            data.setPosterFans(data.getPosterFans() + (isUpdate ? 1 : 0));
        } else {
            tvFollow.setText("关注");
            data.setPosterFans(data.getPosterFans() - (isUpdate ? 1 : 0));
        }
        tvPosterFans.setText(String.format(Locale.CHINESE, "%s粉丝", StringUtil.getCuttingCount(data.getPosterFans())));
        tvFollow.setSelected(isFollow);
        layoutFollow.setSelected(isFollow);
    }

    public void updateDownloaded(boolean isDownloaded) {
        layoutDownlod.getChildAt(0).setSelected(isDownloaded);
        TextView textView = (TextView) layoutDownlod.getChildAt(1);
        if (isDownloaded) {
            textView.setText("已缓存");
            textView.setTextColor(AppContextProvider.getInstance().getContext().getResources().getColor(R.color.color1));
        } else {
            textView.setText("缓存");
            textView.setTextColor(AppContextProvider.getInstance().getContext().getResources().getColor(R.color.color6));
        }
    }

    public static class VRProgramModel {
        private String programName;
        private String posterName;
        private int posterFans;
        private String posterImage;
        private String posterCode;
        private boolean isPosterFollow;
        private String playCount;
        private boolean isDownloadEnable;

        public boolean isPosterFollow() {
            return isPosterFollow;
        }

        public void setPosterFollow(boolean posterFollow) {
            isPosterFollow = posterFollow;
        }

        public String getProgramName() {
            return programName;
        }

        public void setProgramName(String programName) {
            this.programName = programName;
        }

        public String getPosterName() {
            return posterName;
        }

        public void setPosterName(String posterName) {
            this.posterName = posterName;
        }

        public int getPosterFans() {
            return posterFans;
        }

        public void setPosterFans(int posterFans) {
            this.posterFans = posterFans;
        }

        public String getPosterImage() {
            return posterImage;
        }

        public void setPosterImage(String posterImage) {
            this.posterImage = posterImage;
        }

        public String getPlayCount() {
            return playCount;
        }

        public void setPlayCount(String playCount) {
            this.playCount = playCount;
        }

        public boolean isDownloadEnable() {
            return isDownloadEnable;
        }

        public void setDownloadEnable(boolean downloadEnable) {
            isDownloadEnable = downloadEnable;
        }

        public void setPosterCode(String posterCode) {
            this.posterCode = posterCode;
        }

        public String getPosterCode() {
            return posterCode;
        }
    }

    public interface OnPosterClickListener {
        void onFollowClick(View view);

        void onPosterClick(View view);

    }

}