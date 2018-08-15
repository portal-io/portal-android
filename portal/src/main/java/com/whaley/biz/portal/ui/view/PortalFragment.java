package com.whaley.biz.portal.ui.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.ui.RecyclerLoaderFragment;
import com.whaley.biz.common.utils.StatusBarUtil;
import com.whaley.biz.portal.R;
import com.whaley.biz.portal.R2;
import com.whaley.biz.portal.model.PortalRecord;
import com.whaley.biz.portal.ui.adapter.PortalAdapter;
import com.whaley.biz.portal.ui.presenter.PortalPresenter;
import com.whaley.biz.portal.ui.viewmodel.BubbleViewModel;
import com.whaley.biz.portal.ui.viewmodel.PortalViewModel;
import com.whaley.biz.portal.widget.ArcProgress;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.widget.viewholder.ListAdapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dell on 2018/8/2.
 */

@Route(path = "/portal/ui/portal")
public class PortalFragment extends RecyclerLoaderFragment<PortalPresenter, PortalViewModel> implements PortalView {

    @BindView(R2.id.portal_progress)
    ArcProgress portalProgress;
    @BindView(R2.id.tv_remain)
    TextView tvRemain;
    @BindView(R2.id.tv_num)
    TextSwitcher tvNum;
    @BindView(R2.id.btn_bubble1)
    LinearLayout btnBubble1;
    @BindView(R2.id.btn_bubble2)
    LinearLayout btnBubble2;
    @BindView(R2.id.btn_bubble3)
    LinearLayout btnBubble3;
    @BindView(R2.id.btn_bubble4)
    LinearLayout btnBubble4;
    @BindView(R2.id.btn_bubble5)
    LinearLayout btnBubble5;
    @BindView(R2.id.btn_bubble6)
    LinearLayout btnBubble6;
    @BindView(R2.id.bg_layout)
    RelativeLayout bgLayout;
    @BindView(R2.id.btn_bind)
    Button btnBind;

    private List<BubbleViewModel> bubbleViewModelList = new ArrayList<>();

    private List<LinearLayout> buttonList;

    private LinkedList<PortalRecord> portalList = new LinkedList<>();

    private PortalAdapter adapter;

    private ImageRequest.RequestManager requestManager;

    @Override
    protected ListAdapter onCreateAdapter() {
        adapter = new PortalAdapter(requestManager);
        return adapter;
    }

    @Override
    protected boolean isShouldLoadMore() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_portal;
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        requestManager = ImageLoader.with(this);
        getRecyclerView().setNestedScrollingEnabled(false);
        getRecyclerView().setFocusableInTouchMode(false);
        buttonList = new ArrayList<LinearLayout>() {{
            add(btnBubble1);
            add(btnBubble2);
            add(btnBubble3);
            add(btnBubble4);
            add(btnBubble5);
            add(btnBubble6);
        }};
        tvNum.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                final TextView tv=new TextView(getContext());
                tv.setTextSize(48);
                tv.setTextColor(getResources().getColor(R.color.color12));
                tv.setGravity(Gravity.CENTER);
                tv.setIncludeFontPadding(false);
                return tv;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() != null && isFragmentVisible()) {
            StatusBarUtil.changeStatusBar(getActivity().getWindow(), false, true);
        }
    }

    @OnClick(R2.id.btn_bind)
    public void onClickBind(){
        getPresenter().onBind();
    }

    @OnClick({R2.id.btn_bubble1, R2.id.btn_bubble2, R2.id.btn_bubble3, R2.id.btn_bubble4, R2.id.btn_bubble5, R2.id.btn_bubble6})
    public void onClick(View view) {
        int id = view.getId();
        int index = -1;
        if (id == R.id.btn_bubble1) {
            index = 0;
        } else if (id == R.id.btn_bubble2) {
            index = 1;
        } else if (id == R.id.btn_bubble3) {
            index = 2;
        } else if (id == R.id.btn_bubble4) {
            index = 3;
        } else if (id == R.id.btn_bubble5) {
            index = 4;
        } else if (id == R.id.btn_bubble6) {
            index = 5;
        }
        if (index >= 0) {
            clickBubble(index);
        }
    }

    private void clickBubble(final int index) {
        final BubbleViewModel bubbleViewModel = bubbleViewModelList.get(index);
        final LinearLayout btnBubble = bubbleViewModel.getLinearLayout();
        float bubbleX = btnBubble.getX();
        float bubbleY = btnBubble.getY();
        float targetX = tvNum.getX();
        float targetY = tvNum.getY();
        float offsetX = targetX+tvNum.getWidth()/2-bubbleX-btnBubble.getWidth()/2;
        float offsetY = targetY+tvNum.getWidth()/2-bubbleY-btnBubble.getHeight()/2;
        ObjectAnimator animator = ObjectAnimator.ofFloat(btnBubble, "translationY", 0, offsetY);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(btnBubble, "translationX", 0, offsetX);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(btnBubble, "alpha", 1, 0);
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(btnBubble, "scaleX", 1, 0);
        ObjectAnimator animator5 = ObjectAnimator.ofFloat(btnBubble, "scaleY", 1, 0);
        ObjectAnimator animator6 = ObjectAnimator.ofFloat(portalProgress, "progress", 0, 100);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animator).with(animator2).with(animator3).with(animator4).with(animator5).with(animator6);
        animatorSet.setDuration(1000);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                resetBubble(btnBubble);
                addSum(bubbleViewModel);
                getPresenter().onCollect(bubbleViewModel.getCode());
                boolean flag = false;
                for(BubbleViewModel bubbleViewModel:bubbleViewModelList){
                    LinearLayout linearLayout = bubbleViewModel.getLinearLayout();
                    if(linearLayout.getVisibility()==View.VISIBLE){
                        flag=true;
                        break;
                    }
                }
                if(!flag){
                    nextBubble();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    private void addSum(BubbleViewModel bubbleViewModel){
        TextView current = (TextView)tvNum.getCurrentView();
        float num = Float.valueOf(current.getText().toString());
        num += bubbleViewModel.getNum();
        tvNum.setText(String.valueOf(num));
    }

    private void nextBubble(){
        bubbleViewModelList.clear();
        for(int i=0;i<portalList.size();i++){
            PortalRecord portalRecord = portalList.remove();
            if(i<6){
                bubbleViewModelList.add(new BubbleViewModel(portalRecord.getCode(), portalRecord.getPrePortal(), buttonList.get(i)));
            }else{
                break;
            }
        }
    }

    private void resetBubble(LinearLayout linearLayout){
        linearLayout.setVisibility(View.GONE);
        ((Button)linearLayout.getChildAt(0)).setText("");
        linearLayout.setTranslationX(0);
        linearLayout.setTranslationY(0);
        linearLayout.setAlpha(1);
        linearLayout.setScaleX(1);
        linearLayout.setScaleY(1);
    }

    @Override
    public void updateBalance(float balance) {
        tvNum.setText(String.valueOf(balance));
    }

    @Override
    public void updateRecode(List<PortalRecord> portalRecords) {
        bubbleViewModelList.clear();
        portalList.clear();
        for(int i=0;i<portalRecords.size();i++){
            PortalRecord portalRecord = portalRecords.get(i);
            if(i<6){
                bubbleViewModelList.add(new BubbleViewModel(portalRecord.getCode(), portalRecord.getPrePortal(), buttonList.get(i)));
            }else{
                portalList.add(portalRecord);
            }
        }
    }

    @Override
    public void showBind(boolean isBind) {
        if(isBind){
            btnBind.setText("已绑定");
        }else{
            btnBind.setText("绑定钱包地址");
        }
    }

}
