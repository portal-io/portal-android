package com.whaley.biz.program.playersupport.widget.camerasource;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whaley.biz.program.R;


/**
 * Created by dell on 2017/7/17.
 */

public class SourceBtn extends LinearLayout implements View.OnClickListener{

    private ImageView iv_narrow;
    private TextView tv_text;
    private boolean isSelected = false;
    private OnSelectListener listener;
    private String source;

    public SourceBtn(Context context) {
        this(context, null);
    }

    public SourceBtn(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SourceBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_btn_source, this, true);
        view.setOnClickListener(this);
        iv_narrow = findView(R.id.iv_narrow);
        tv_text = findView(R.id.tv_text);
    }

    private <R extends View> R findView(int id){
        return (R) findViewById(id);
    }

    @Override
    public void onClick(View v) {
        if(!isSelected){
            isSelected = true;
            iv_narrow.setVisibility(VISIBLE);
            if(listener!=null){
                listener.onSelected(source, this);
            }
        }
    }

    public void select(){
        if(!isSelected){
            isSelected = true;
            iv_narrow.setVisibility(VISIBLE);
        }
    }

    public void unSelect(){
        if(isSelected){
            isSelected = false;
            iv_narrow.setVisibility(INVISIBLE);
        }
    }

    public void setListener(OnSelectListener listener) {
        this.listener = listener;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
        tv_text.setText(CameraTransfer.covert(source));
    }

    public interface OnSelectListener{
        void onSelected(String source, SourceBtn sourceBtn);
    }

}
