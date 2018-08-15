package com.whaley.biz.portal.ui.viewmodel;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by dell on 2018/8/8.
 */

public class BubbleViewModel {

    private float num;
    private String code;
    private Button button;
    private LinearLayout linearLayout;

    public BubbleViewModel(String code, float num, LinearLayout linearLayout) {
        this.num = num;
        this.code = code;
        this.linearLayout = linearLayout;
        this.button = (Button) linearLayout.getChildAt(0);
        if(button!=null){
            button.setText(String.valueOf(num));
        }
        if(linearLayout.getVisibility()!= View.VISIBLE){
            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    public float getNum() {
        return num;
    }

    public void setNum(float num) {
        this.num = num;
        if(button!=null){
            button.setText(String.valueOf(num));
        }
        if(linearLayout!=null&&linearLayout.getVisibility()!= View.VISIBLE){
            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public void setLinearLayout(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
