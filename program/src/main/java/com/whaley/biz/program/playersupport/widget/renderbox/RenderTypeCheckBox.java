package com.whaley.biz.program.playersupport.widget.renderbox;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


import com.whaley.biz.program.R;


/**
 * Created by YangZhi on 2017/1/4 20:53.
 */

public class RenderTypeCheckBox extends LinearLayout implements View.OnClickListener{

    Button btnPlane;
    Button btn360;
    Button btn180;


    Button btn2d;
    Button btn3dLr;
    Button btn3dUd;

    View lastSelectedLeft;
    View lastSelectedRight;

    OnRenderTypeSelectListener listener;

    public RenderTypeCheckBox(Context context) {
        this(context, null);
    }

    public RenderTypeCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RenderTypeCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_player_renderbox, this, true);
        btnPlane=findView(R.id.btn_plane);
        btn360=findView(R.id.btn_360);
        btn180=findView(R.id.btn_180);
        btnPlane.setTag(R.id.btn_plane,"平面");
        btn360.setTag(R.id.btn_360,"球面");
        btn180.setTag(R.id.btn_180,"半球");

        btn2d=findView(R.id.btn_2d);
        btn3dLr=findView(R.id.btn_3d_lr);
        btn3dUd=findView(R.id.btn_3d_ud);

        btn2d.setTag(R.id.btn_2d,"2D");
        btn2d.setTag(R.id.btn_plane,"2D");
        btn2d.setTag(R.id.btn_180,"PLANE");
        btn2d.setTag(R.id.btn_360,"2D");


        btn3dLr.setTag(R.id.btn_3d_lr,"3D左右");
        btn3dLr.setTag(R.id.btn_plane,"3D_LR");
        btn3dLr.setTag(R.id.btn_180,"3D_LF");
        btn3dLr.setTag(R.id.btn_360,"3D_LF");

        btn3dUd.setTag(R.id.btn_3d_ud,"3D上下");
        btn3dUd.setTag(R.id.btn_plane,"3D_UD");
        btn3dUd.setTag(R.id.btn_180,"3D_UD");
        btn3dUd.setTag(R.id.btn_360,"3D_UD");

        btn360.setSelected(true);
        btn2d.setSelected(true);
        lastSelectedLeft=btn360;
        lastSelectedRight=btn2d;
        btnPlane.setOnClickListener(this);
        btn360.setOnClickListener(this);
        btn180.setOnClickListener(this);
        btn2d.setOnClickListener(this);
        btn3dLr.setOnClickListener(this);
        btn3dUd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.btn_plane || id == R.id.btn_360 || id == R.id.btn_180){
            if(v!=lastSelectedLeft) {
                lastSelectedLeft.setSelected(false);
                v.setSelected(true);
                lastSelectedLeft = v;
            }
        }else {
            if(v!=lastSelectedRight) {
                lastSelectedRight.setSelected(false);
                v.setSelected(true);
                lastSelectedRight = v;
            }
        }
        callback();
    }


    private void callback(){
        if(listener!=null){
            Object rightTag=lastSelectedRight.getTag(lastSelectedLeft.getId());
            String renderType=lastSelectedLeft.getTag()+"_"+rightTag;
            String formatRenderType=lastSelectedLeft.getTag(lastSelectedLeft.getId())+" | "+lastSelectedRight.getTag(lastSelectedRight.getId());
            listener.onSelected(renderType,formatRenderType);
        }
    }

    private <R extends View> R findView(int id){
        return (R) findViewById(id);
    }

    public void setListener(OnRenderTypeSelectListener listener) {
        this.listener = listener;
        callback();
    }

    public interface OnRenderTypeSelectListener{
        void onSelected(String renderType,String formatRenderType);
    }

}
