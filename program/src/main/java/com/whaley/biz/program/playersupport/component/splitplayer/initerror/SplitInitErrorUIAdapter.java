package com.whaley.biz.program.playersupport.component.splitplayer.initerror;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.whaley.biz.playerui.component.simpleplayer.initError.InitErrorUIAdapter;
import com.whaley.biz.program.R;

/**
 * Created by dell on 2017/10/30.
 */

public class SplitInitErrorUIAdapter extends InitErrorUIAdapter {

    protected TextView tvErrorText2;
    protected Button btnRetry2;

    protected void show() {
        checkInflatedView();
        inflatedView.setVisibility(View.VISIBLE);
    }

    protected void hide() {
        if (inflatedView == null)
            return;
        inflatedView.setVisibility(View.GONE);
    }

    public void updateErrorText(String text) {
        super.updateErrorText(text);
        tvErrorText2.setText(text);
    }

    protected int getLayoutResource(){
        return R.layout.layout_split_init_error;
    }

    protected void checkInflatedView() {
        if (inflatedView == null) {
            inflatedView = viewStub.inflate();
            tvErrorText = (TextView) inflatedView.findViewById(R.id.tv_error_text);
            tvErrorText2 = (TextView) inflatedView.findViewById(R.id.tv_error_text2);
            btnRetry = (Button) inflatedView.findViewById(R.id.btn_retry);
            btnRetry2 = (Button) inflatedView.findViewById(R.id.btn_retry2);
            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    retry();
                }
            });
            btnRetry2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    retry();
                }
            });
        }
    }

}
