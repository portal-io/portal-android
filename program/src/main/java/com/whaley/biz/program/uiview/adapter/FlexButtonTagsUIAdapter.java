//package com.whaley.biz.program.uiview.adapter;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.whaley.core.utils.DisplayUtil;
//import com.whaleyvr.view.ClickSetter;
//import com.whaleyvr.view.ClickableSimpleViewHolder;
//import com.whaleyvr.view.UIAdapter;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by YangZhi on 2017/3/30 11:24.
// */
//
//public class FlexButtonTagsUIAdapter extends BaseUIAdapter<FlexButtonTagsUIAdapter.FlexViewHolder,FlexButtonTagsUIViewModel> {
//
//
//
//    @Override
//    public FlexViewHolder onCreateViewHolder(ViewGroup parent, int type) {
//        return new FlexViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_flexbox,parent,false));
//    }
//
//    @Override
//    public void onBindViewHolder(FlexViewHolder ClickableUIViewHolder, FlexButtonTagsUIViewModel uiViewModel, int position) {
//        List<ButtonTagUIViewModel> tagUIViewModels=uiViewModel.getTagUIViewModels();
//        int i=0;
//        for (ButtonTagUIViewModel viewModel:tagUIViewModels){
//            UIAdapter uiAdapter=ClickableUIViewHolder.uiAdapters.get(i);
//            if(uiAdapter instanceof ClickSetter){
//                ClickSetter clickSetter=(ClickSetter) uiAdapter;
//                clickSetter.setOnViewClickListener(getOnViewClickListener());
//            }
//            uiAdapter.onBindView(uiAdapter.getViewHolder(),viewModel,i);
//            i++;
//            if(i>3){
//                break;
//            }
//        }
//    }
//
//    public static class FlexViewHolder extends ClickableSimpleViewHolder {
//
//        FlexboxLayout flexboxLayout;
//
//        List<UIAdapter> uiAdapters;
//
//        public FlexViewHolder(View view){
//            super(view);
//            flexboxLayout=getRootView();
//            uiAdapters=new ArrayList<>();
//            ButtonTagUIAdapter buttonTagUIAdapter=new ButtonTagUIAdapter();
//            ButtonTagUIAdapter.ButtonTagViewHolder ClickableUIViewHolder=buttonTagUIAdapter.onCreateView(flexboxLayout,0);
//            ClickableUIViewHolder.getRootView().setLayoutParams(createDefaultLayoutParams());
//            ClickableUIViewHolder.getRootView().setPadding(DisplayUtil.convertDIP2PX(5),0, DisplayUtil.convertDIP2PX(5),0);
//            flexboxLayout.addView(ClickableUIViewHolder.getRootView());
//            uiAdapters.add(buttonTagUIAdapter);
//
//            buttonTagUIAdapter=new ButtonTagUIAdapter();
//            ClickableUIViewHolder=buttonTagUIAdapter.onCreateView(flexboxLayout,0);
//            ClickableUIViewHolder.getRootView().setLayoutParams(createDefaultLayoutParams());
//            ClickableUIViewHolder.getRootView().setPadding(DisplayUtil.convertDIP2PX(5),0, DisplayUtil.convertDIP2PX(5),0);
//            flexboxLayout.addView(ClickableUIViewHolder.getRootView());
//            uiAdapters.add(buttonTagUIAdapter);
//
//            buttonTagUIAdapter=new ButtonTagUIAdapter();
//            ClickableUIViewHolder=buttonTagUIAdapter.onCreateView(flexboxLayout,0);
//            ClickableUIViewHolder.getRootView().setLayoutParams(createDefaultLayoutParams());
//            ClickableUIViewHolder.getRootView().setPadding(DisplayUtil.convertDIP2PX(5),0, DisplayUtil.convertDIP2PX(5),0);
//            flexboxLayout.addView(ClickableUIViewHolder.getRootView());
//            uiAdapters.add(buttonTagUIAdapter);
//
//            buttonTagUIAdapter=new ButtonTagUIAdapter();
//            ClickableUIViewHolder=buttonTagUIAdapter.onCreateView(flexboxLayout,0);
//            ClickableUIViewHolder.getRootView().setLayoutParams(createDefaultLayoutParams());
//            ClickableUIViewHolder.getRootView().setPadding(DisplayUtil.convertDIP2PX(5),0, DisplayUtil.convertDIP2PX(5),0);
//            flexboxLayout.addView(ClickableUIViewHolder.getRootView());
//            uiAdapters.add(buttonTagUIAdapter);
//            flexboxLayout.setFlexWrap(FlexboxLayout.FLEX_WRAP_WRAP);
//            flexboxLayout.setAlignItems(FlexboxLayout.ALIGN_ITEMS_CENTER);
//            flexboxLayout.setJustifyContent(FlexboxLayout.JUSTIFY_CONTENT_CENTER);
//        }
//
//
//
//        private FlexboxLayout.LayoutParams createDefaultLayoutParams() {
//            FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(
//                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
//                    DisplayUtil.convertDIP2PX(62));
////            lp.order = 1;
//            lp.flexGrow =0;
//            lp.flexShrink = 0;
//            return lp;
//        }
//    }
//}
