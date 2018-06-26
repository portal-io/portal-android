package com.whaley.biz.program.playersupport.widget.camerasource;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.whaley.biz.program.R;
import com.whaley.biz.program.playersupport.model.CameraModel;
import com.whaley.core.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/7/17.
 */

public class SourceLayout extends LinearLayout implements SourceBtn.OnSelectListener{

    private OnSelectListener listener;

    private List<SourceBtn> btnList = new ArrayList<>();

    public SourceLayout(Context context) {
        this(context, null);
    }

    public SourceLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SourceLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_source, this, true);
    }

    public void setData(Map<String,CameraModel> sourceList){
        btnList.clear();
        removeAllViews();
        Iterator iter = sourceList.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String source= (String)entry.getKey();
            SourceBtn sourceBtn = new SourceBtn(getContext());
            sourceBtn.setListener(this);
            sourceBtn.setSource(source);
            LinearLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 0, DisplayUtil.convertDIP2PX(8));
            addView(sourceBtn, lp);
            btnList.add(sourceBtn);
            if(source.equals("Public")){
                sourceBtn.select();
            }
        }
    }

    public void select(String source){
        for(SourceBtn btn : btnList){
            if(source.equals(btn.getSource())){
                btn.select();
            }else{
                btn.unSelect();
            }
        }
    }

    @Override
    public void onSelected(String source, SourceBtn sourceBtn) {
        for(SourceBtn btn : btnList){
            if(btn!=sourceBtn){
                btn.unSelect();
            }
        }
        if(listener!=null){
            listener.onSelected(source);
        }
    }

    public void setListener(OnSelectListener listener) {
        this.listener = listener;
    }

    public interface OnSelectListener{
        void onSelected(String source);
    }

    public List<SourceBtn> getBtnList() {
        return btnList;
    }

}
