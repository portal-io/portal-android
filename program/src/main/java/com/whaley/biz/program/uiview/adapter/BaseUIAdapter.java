package com.whaley.biz.program.uiview.adapter;


import android.content.Context;
import android.content.MutableContextWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.request.Request;
import com.whaley.biz.program.R;
import com.whaley.biz.program.uiview.OnClickableUIViewClickListener;
import com.whaley.biz.program.uiview.ClickableUIViewHolder;
import com.whaley.biz.program.uiview.viewmodel.ClickableUIViewModel;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.image.ImageLoader;

/**
 * Author: qxw
 * Date:2017/8/21
 * Introduction:
 */

public abstract class BaseUIAdapter<T extends ClickableUIViewHolder, H extends ClickableUIViewModel> extends com.whaley.core.widget.uiview.BaseUIAdapter<T, H> implements UIAdapter<T, H> {


    private Context realContext;

    public void setOnUIViewClickListener(OnClickableUIViewClickListener onUIViewClickListener) {
        super.setOnUIViewClickListener(onUIViewClickListener);
    }

    public OnClickableUIViewClickListener getOnUIViewClickListener() {
        return (OnClickableUIViewClickListener) super.getOnUIViewClickListener();
    }

    public void setRealContext(Context realContext) {
        this.realContext = realContext;
    }


    public Context getRealContext() {
        return realContext==null?AppContextProvider.getInstance().getContext():realContext;
    }

    @Override
    public void onRecycled() {
        super.onRecycled();
        setRealContext(null);
        Context context = getViewHolder().getRootView().getContext();
        if(context instanceof MutableContextWrapper){
            MutableContextWrapper contextWrapper = (MutableContextWrapper) context;
            contextWrapper.setBaseContext(AppContextProvider.getInstance().getContext());
        }
        setOnUIViewClickListener(null);
    }

    @Override
    public final void onBindView(T viewHolder, H uiViewModel, int position) {
        View rootView = viewHolder.getRootView();
        Context context = rootView.getContext();
        if(context instanceof MutableContextWrapper){
            MutableContextWrapper contextWrapper = (MutableContextWrapper) context;
            contextWrapper.setBaseContext(realContext);
        }
        super.onBindView(viewHolder, uiViewModel, position);
    }

    @Override
    protected void setViewClick() {
        if(getOnUIViewClickListener()!=null&&getViewHolder().getData()!=null) {
            super.setViewClick();
        }
    }

    protected Context getContextFormParent(ViewGroup parent){
        return new MutableContextWrapper(parent.getContext());
    }

    protected LayoutInflater getLayoutInfalterFormParent(ViewGroup parent){
        Context context = parent==null?AppContextProvider.getInstance().getContext():parent.getContext();
        MutableContextWrapper contextWrapper = new MutableContextWrapper(context);
        return LayoutInflater.from(contextWrapper).cloneInContext(contextWrapper);
    }

    protected void cancelImageLoader(ImageView imageView){
//        ImageLoader.cancelRequests(imageView);
//        Request request = (Request) imageView.getTag();
//        if(request!=null){
//            imageView.setTag(null);
//            request.recycle();
//        }
//        ImageLoader.with(AppContextProvider.getInstance().getContext())
//                .load(R.mipmap.default_4)
//                .into(imageView);
//        imageView.setImageResource(0);
        ImageLoader.clearView(imageView);
        imageView.setImageResource(0);
    }


}
