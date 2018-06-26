package com.whaley.biz.program.ui.detail.viewholder;

import android.view.View;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.core.appcontext.AppContextProvider;

/**
 * Created by dell on 2017/11/13.
 */

public class DramaViewHolder extends VRViewHolder {

    public DramaViewHolder(View view) {
        super(view);
        layoutDownlod.getChildAt(0).setEnabled(false);
        tvHaveVoucher.setVisibility(View.GONE);
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
        TextView textView = (TextView) layoutDownlod.getChildAt(1);
        textView.setText("缓存");
        if (data.isDownloadEnable()) {
            textView.setTextColor(AppContextProvider.getInstance().getContext().getResources().getColor(R.color.color6));
        } else {
            textView.setTextColor(AppContextProvider.getInstance().getContext().getResources().getColor(R.color.color9));
        }
    }

    public void updateDownloaded(boolean isDownloaded) {
        //
    }

}
