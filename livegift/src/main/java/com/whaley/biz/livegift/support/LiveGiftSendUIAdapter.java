package com.whaley.biz.livegift.support;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whaley.biz.common.widget.NoAlphaItemAnimator;
import com.whaley.biz.livegift.GiftMemoryManager;
import com.whaley.biz.livegift.R;
import com.whaley.biz.livegift.ViewPagerIndicator;
import com.whaley.biz.livegift.model.GiftModel;
import com.whaley.biz.livegift.model.MemberModel;
import com.whaley.biz.playerui.component.BaseUIAdapter;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.utils.DisplayUtil;
import com.whaley.core.utils.StrUtil;
import com.whaley.core.widget.adapter.RecyclerViewAdapter;
import com.whaley.core.widget.adapter.ViewHolder;
import com.whaley.core.widget.viewholder.AbsViewHolder;
import com.whaley.core.widget.viewholder.IViewHolder;
import com.whaley.core.widget.viewholder.OnItemClickListener;
import com.whaley.core.widget.viewpager.BaseRecyclerPagerAdapter;
import com.whaley.core.widget.viewpager.ViewPager;

import java.util.List;

import at.wirecube.additiveanimations.additive_animator.AdditiveAnimator;
import butterknife.internal.DebouncingOnClickListener;

/**
 * Author: qxw
 * Date:2017/10/12
 * Introduction:
 */

public class LiveGiftSendUIAdapter extends BaseUIAdapter<LiveGiftSendController> {
    ViewStub viewStub;
    ViewGroup layoutLiveGift;
    RelativeLayout viewSend;
    RecyclerView listMember;
    TextView tvWhaleyMoney;
    ViewPager viewPager;
    TextView tvRecharge;
    TextView tvSend;
    FrameLayout viewMember;
    View viewGiftTemp;
    LinearLayout viewIndicator;
    static ImageRequest.RequestManager requestManager;
    static int giftColumn;
    GiftTempAdapter giftTempAdapter;
    MemberAdapter memberAdapter;
    ViewPagerIndicator viewPagerIndicator;
    int oldPosition = -1;

    public void show() {
        isOnShow = true;
        if (layoutLiveGift == null) {
            if(viewStub==null || viewStub.getParent() ==null) {
                return;
            }
                layoutLiveGift = (ViewGroup) viewStub.inflate();
                layoutLiveGift.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getController().giftHide();
                        hide();
                    }
                });
                viewSend = (RelativeLayout) layoutLiveGift.findViewById(R.id.view_send);
                viewSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                tvWhaleyMoney = (TextView) layoutLiveGift.findViewById(R.id.tv_whaley_money);
                tvRecharge = (TextView) layoutLiveGift.findViewById(R.id.tv_recharge);
                tvRecharge.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getController().onRechargeClick();
                    }
                });
                tvSend = (TextView) layoutLiveGift.findViewById(R.id.tv_send);
                tvSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getController().onSendGift();
                    }
                });
                viewGiftTemp = layoutLiveGift.findViewById(R.id.view_gift_temp);
                viewPager = (ViewPager) layoutLiveGift.findViewById(R.id.view_gift);
                ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
                if (getController().isLandScape()) {
                    getController().setLandscapeGift();
                    layoutParams.height = DisplayUtil.convertDIP2PX(112);
                } else {
                    if (getController().giftModels.size() > 4) {
                        layoutParams.height = DisplayUtil.convertDIP2PX(220);
                    } else {
                        layoutParams.height = DisplayUtil.convertDIP2PX(110);
                    }
                }
                viewPager.setLayoutParams(layoutParams);
                giftColumn = getController().getGiftColumn();
                giftTempAdapter = new GiftTempAdapter(new GiftTempAdapter.OnItemClickViewPageListener() {
                    @Override
                    public void onItemClick(GiftModel giftModel) {
                        getController().onGiftClick(giftModel);
                    }
                });
                viewPager.setAdapter(giftTempAdapter);
                int size = getController().getGiftModels().size();
                if (size > 1) {
                    viewIndicator = (LinearLayout) layoutLiveGift.findViewById(R.id.view_indicator);
                    viewIndicator.setVisibility(View.VISIBLE);
                    viewPagerIndicator = new ViewPagerIndicator(getContext(), viewPager, viewIndicator, getController().getGiftModels().size());
                    viewPager.setOnPageChangeListener(viewPagerIndicator);
                }
                viewMember = (FrameLayout) layoutLiveGift.findViewById(R.id.view_member);
                viewMember.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                if (getController().isHaveMember()) {
                    listMember = (RecyclerView) layoutLiveGift.findViewById(R.id.list_member);
                    listMember.setItemAnimator(new NoAlphaItemAnimator());
                    LinearLayoutManager manager = new LinearLayoutManager(listMember.getContext(), LinearLayoutManager.HORIZONTAL, false);
                    listMember.setLayoutManager(manager);
                    MemberModel memberModel = getController().getOldMemberSelected();
                    String memberCode = null;
                    if (memberModel != null) {
                        memberCode = memberModel.getMemberCode();
                        updateSendText(String.format(getActivity().getString(R.string.reward_gift), memberModel.getMemberName()));
                    }
                    memberAdapter = new MemberAdapter(memberCode, manager);
                    listMember.setAdapter(memberAdapter);
                    memberAdapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(IViewHolder iViewHolder, int pos) {
                            getController().onSetectMemberClick(iViewHolder, pos);
//                    updateMember(pos);
                        }
                    });
                }
        }
        if (requestManager == null) {
            requestManager = ImageLoader.with(getActivity());
        }
        tvWhaleyMoney.setText(getController().getWhaleyMoney());
        showGift();
        showMember();
        AdditiveAnimator.cancelAnimations(layoutLiveGift);
        AdditiveAnimator.animate(layoutLiveGift)
                .alpha(1f)
                .addStartAction(new Runnable() {
                    @Override
                    public void run() {
                        layoutLiveGift.setVisibility(View.VISIBLE);
                    }
                })
                .addEndAction(new AdditiveAnimator.AnimationEndListener() {
                    @Override
                    public void onAnimationEnd(boolean wasCancelled) {
                        getController().emitGiftHeight();
                    }
                })
                .start();
    }

    public void updateSend(boolean isClick) {
        if(tvSend!=null) {
            tvSend.setSelected(isClick);
        }
    }

    public void updateSendText(String text) {
        if(tvSend!=null) {
            tvSend.setText(text);
        }
    }

    /**
     * 礼物模板显示
     */
    private void showGift() {
        if(giftTempAdapter!=null) {
            giftTempAdapter.setData(getController().getGiftModels());
        }
    }

    /**
     * 成员显示
     */
    private void showMember() {
        if (layoutLiveGift == null)
            return;
        if (getController().isHaveMember()) {
            viewMember.setVisibility(View.VISIBLE);
            if (memberAdapter.getData() == null) {
                memberAdapter.setData(getController().getMemberModels());
            }
            MemberModel memberModel = getController().getOldMemberSelected();
            if (memberModel != null) {
                memberAdapter.updatePosition(memberModel.getPos());
            }
        } else {
            viewMember.setVisibility(View.GONE);
        }
    }

    public void updateMember(int posititon, boolean isSelected) {
        if (layoutLiveGift == null)
            return;
        ViewHolder viewHolder = (ViewHolder)listMember.findViewHolderForAdapterPosition(posititon);
        if(viewHolder==null) {
            return;
        }
        ImageView ivSelect = viewHolder.getView(R.id.iv_selected);
        MemberModel memberModel = viewHolder.getBindModel();
        if (isSelected) {
            memberModel.setSelect(true);
            ivSelect.setVisibility(View.VISIBLE);
            if (oldPosition >= 0 && oldPosition != posititon) {
                ViewHolder oldViewHolder = (ViewHolder)listMember.findViewHolderForAdapterPosition(oldPosition);
                if(oldViewHolder!=null){
                    updateMember(oldPosition, false);
                }else if(memberAdapter != null && memberAdapter.getData()!=null
                        && memberAdapter.getData().size()>oldPosition){
                    memberAdapter.getData().get(oldPosition).setSelect(false);
                    memberAdapter.notifyItemChanged(oldPosition);
                }
            }
            oldPosition = posititon;
        } else {
            memberModel.setSelect(false);
            ivSelect.setVisibility(View.INVISIBLE);
        }
    }

    public void hide() {
        isOnShow = false;
        if (layoutLiveGift == null)
            return;
        AdditiveAnimator.cancelAnimations(layoutLiveGift);
        AdditiveAnimator.animate(layoutLiveGift)
                .alpha(0f)
                .addEndAction(new AdditiveAnimator.AnimationEndListener() {
                    @Override
                    public void onAnimationEnd(boolean wasCancelled) {
                        layoutLiveGift.setVisibility(View.GONE);
                    }
                })
                .start();
    }

    @Override
    protected View initView(ViewGroup parent) {
        ViewStub viewStub = new ViewStub(parent.getContext(), R.layout.layout_live_gift);
        viewStub.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return viewStub;
    }

    @Override
    public View getRootView() {
        return layoutLiveGift == null ? super.getRootView() : layoutLiveGift;
    }


    @Override
    protected void onViewCreated(View view) {
        viewStub = (ViewStub) view;
    }

    public void clearViewHolderSelected() {
        if(giftTempAdapter!=null) {
            giftTempAdapter.clearViewHolderSelected();
        }
    }

    @Override
    public void destroy() {
        if (layoutLiveGift != null) {
            AdditiveAnimator.cancelAnimations(layoutLiveGift);
        }
        requestManager = null;
    }

    public int getGiftLayoutHeight() {
        int viewMemberHeight = (viewMember != null && viewMember.getVisibility() == View.VISIBLE) ? DisplayUtil.convertDIP2PX(70) : 0;
        int viewIndicatorHeight = (viewIndicator != null && viewIndicator.getVisibility() == View.VISIBLE) ? DisplayUtil.convertDIP2PX(25) : 0;
        int height = viewMemberHeight + viewIndicatorHeight + viewPager.getHeight() + DisplayUtil.convertDIP2PX(50);
        return height;
    }

    public void updateWhaleyMoney(String whaleyMoney) {
        if(tvWhaleyMoney!=null) {
            tvWhaleyMoney.setText(whaleyMoney);
        }
    }


    static class GiftTempAdapter extends BaseRecyclerPagerAdapter<List<GiftModel>, ViewPageHolder> {
        OnItemClickViewPageListener onItemClickViewPageListener;
        IViewHolder oldHolder;

        public GiftTempAdapter(OnItemClickViewPageListener onItemClickViewPageListener) {
            this.onItemClickViewPageListener = onItemClickViewPageListener;
        }

        @Override
        public ViewPageHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recyclerview_gift, parent, false);
            return new ViewPageHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewPageHolder giftTempViewHolder, final List<GiftModel> lists, final int pos) {
            giftTempViewHolder.adapter.setData(lists);
            giftTempViewHolder.adapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(IViewHolder iViewHolder, int i) {
                    GiftModel giftModel = null;
                    if (iViewHolder != oldHolder) {
                        iViewHolder.getItemView().setSelected(true);
                        giftModel = lists.get(i);
                        if (oldHolder != null) {
                            oldHolder.getItemView().setSelected(false);
                        }
                        oldHolder = iViewHolder;
                    } else {
                        if (oldHolder != null) {
                            oldHolder.getItemView().setSelected(false);
                            oldHolder = null;
                        }
                    }

                    if (onItemClickViewPageListener != null) {
                        onItemClickViewPageListener.onItemClick(giftModel);
                    }
                }
            });

        }

        public void clearViewHolderSelected() {
            if (oldHolder != null) {
                oldHolder.getItemView().setSelected(false);
                oldHolder = null;
            }
        }

        public interface OnItemClickViewPageListener {
            void onItemClick(GiftModel giftModel);
        }
    }


    static class ViewPageHolder extends AbsViewHolder {
        Adapter adapter;

        public ViewPageHolder(View view) {
            super(view);
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setItemAnimator(new NoAlphaItemAnimator());
            recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), giftColumn));
            adapter = new Adapter();
            recyclerView.setAdapter(adapter);
        }
    }

    static class Adapter extends RecyclerViewAdapter<GiftModel, ViewHolder> {

        @Override
        public ViewHolder onCreateNewViewHolder(ViewGroup parent, int i) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gift, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, GiftModel giftModel, int i) {
            viewHolder.getItemView().setSelected(false);
            ImageView ivGift = viewHolder.getView(R.id.iv_gift);
            requestManager.load(giftModel.getIcon()).centerCrop().into(ivGift);
            TextView giftName = viewHolder.getView(R.id.tv_gift_name);
            TextView giftWorth = viewHolder.getView(R.id.tv_gift_worth);
            giftName.setText(giftModel.getTitle());
            giftWorth.setText(giftModel.getPrice() + AppContextProvider.getInstance().getContext().getString(R.string.whaley_money));
        }
    }


    static class MemberAdapter extends RecyclerViewAdapter<MemberModel, ViewHolder> {

        private String code;
        IViewHolder oldViewHolder;
        LinearLayoutManager manager;

        public MemberAdapter(String code, LinearLayoutManager manager) {
            this.code = code;
            this.manager = manager;
        }

        @Override
        public ViewHolder onCreateNewViewHolder(ViewGroup parent, int i) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, MemberModel memberModel, int i) {
            ImageView ivPic = viewHolder.getView(R.id.iv_pic);
            TextView tvName = viewHolder.getView(R.id.tv_name);
            requestManager.load(memberModel.getSmallPic()).error(R.drawable.bg_placeholeder_circle_shape).placeholder(R.drawable.bg_placeholeder_circle_shape).centerCrop().circle().into(ivPic);
            tvName.setText(memberModel.getMemberName());
            ImageView ivSelect = viewHolder.getView(R.id.iv_selected);
            if (memberModel.isSelect()) {
                ivSelect.setVisibility(View.VISIBLE);
            } else {
                ivSelect.setVisibility(View.GONE);
            }
        }

        public void updatePosition(int i) {
            manager.scrollToPosition(i);
        }

    }


    boolean isOnShow;

    public boolean isOnShow() {
        return isOnShow;
    }
}
