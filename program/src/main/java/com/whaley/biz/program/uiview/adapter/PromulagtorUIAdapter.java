package com.whaley.biz.program.uiview.adapter;

import android.content.Context;
import android.content.MutableContextWrapper;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.common.utils.DialogUtil;
import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.model.user.UserModel;
import com.whaley.biz.program.uiview.ClickableSimpleViewHolder;
import com.whaley.biz.program.uiview.ClickableUIViewHolder;
import com.whaley.biz.program.uiview.ImageloaderUser;
import com.whaley.biz.program.uiview.OnClickableUIViewClickListener;
import com.whaley.biz.program.uiview.viewmodel.PromulagtorUIViewModel;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: qxw
 * Date: 2017/3/20
 */

public class PromulagtorUIAdapter extends BaseUIAdapter<PromulagtorUIAdapter.PromulagtorViewHolder, PromulagtorUIViewModel> implements ImageloaderUser {

    ImageRequest.RequestManager requestManager;


    @Override
    public void setRequestManager(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public void onRecycled() {
        super.onRecycled();
        cancelImageLoader(getViewHolder().ivAvatar);
        getViewHolder().onRecycled();
        setRequestManager(null);
    }

    @Override
    public PromulagtorViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        return new PromulagtorViewHolder(getLayoutInfalterFormParent(parent).inflate(R.layout.item_promulagtor, parent, false));
    }

    @Override
    public void onBindViewHolder(final PromulagtorViewHolder UIViewHolder, final PromulagtorUIViewModel uiViewModel, int position) {
        UIViewHolder.uiAdapter.setRealContext(getRealContext());
        requestManager.load(uiViewModel.getImgUrl()).centerCrop().circle().placeholder(R.mipmap.default_4).into(UIViewHolder.ivAvatar);
        followView(UIViewHolder, uiViewModel.isFollow());
        UIViewHolder.viewFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (getOnUIViewClickListener() != null) {
                    Router.getInstance().buildExecutor("/user/service/checklogin").callback(new Executor.Callback<UserModel>() {
                        @Override
                        public void onCall(UserModel userModel) {
                            uiViewModel.setFollow(!uiViewModel.isFollow());
                            followView(UIViewHolder, uiViewModel.isFollow());
                            ((OnFollowViewClickListener) getOnUIViewClickListener()).onFollowClick(UIViewHolder);
                        }

                        @Override
                        public void onFail(Executor.ExecutionError executionError) {
                            DialogUtil.showDialog(v.getContext(),
                                    v.getContext().getString(R.string.dialog_follow), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            TitleBarActivity.goPage(getStarter(v.getContext()), 0, "/user/ui/login");
                                        }
                                    });
                        }
                    }).excute();
                }
            }
        });
        UIViewHolder.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnFollowViewClickListener) getOnUIViewClickListener()).onPromulagtorClick(UIViewHolder);
            }
        });
        UIViewHolder.uiAdapter.setRequestManager(requestManager);
        UIViewHolder.uiAdapter.setOnUIViewClickListener(
                getOnUIViewClickListener());
        UIViewHolder.uiAdapter.onBindView(UIViewHolder.recyclerHolder, uiViewModel.getRecyclerViewModel(), 0);
    }


    private Starter getStarter(Context context) {
        MutableContextWrapper contextWrapper = (MutableContextWrapper) context;
        return (Starter) contextWrapper.getBaseContext();
    }

    private void followView(PromulagtorViewHolder UIViewHolder, boolean isFollow) {
        int drawableID;
        if (isFollow) {
            UIViewHolder.tvFollow.setText("已关注");
            UIViewHolder.tvFollow.setSelected(true);
            drawableID = R.mipmap.ic_followed;
            UIViewHolder.viewFollow.setSelected(true);
        } else {
            UIViewHolder.tvFollow.setText("关注");
            UIViewHolder.tvFollow.setSelected(false);
            drawableID = R.mipmap.ic_follow;
            UIViewHolder.viewFollow.setSelected(false);
        }
        Drawable drawable = getViewHolder().getRootView().getContext().getResources().getDrawable(drawableID);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        UIViewHolder.tvFollow.setCompoundDrawables(drawable, null, null, null);
    }

    static class PromulagtorViewHolder extends ClickableSimpleViewHolder {
        @BindView(R2.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R2.id.tv_follow)
        TextView tvFollow;
        @BindView(R2.id.view_follow)
        FrameLayout viewFollow;
        @BindView(R2.id.view_content)
        RecyclerView viewContent;

//        SafeViewPool safeViewPool;


        RecyclerViewUIAdapter uiAdapter;
        RecyclerViewUIAdapter.RecyclerHolder recyclerHolder;

        public PromulagtorViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            uiAdapter = new RecyclerViewUIAdapter();
            recyclerHolder = new RecyclerViewUIAdapter.RecyclerHolder(viewContent);
            uiAdapter.setRecyclerHolder(recyclerHolder);
            uiAdapter.onCreateView((ViewGroup) getRootView(), 0);
        }

        private void onRecycled() {
            uiAdapter.onBindView(recyclerHolder, null, 0);
            uiAdapter.onRecycled();
            viewFollow.setOnClickListener(null);
        }
    }


    public interface OnFollowViewClickListener extends OnClickableUIViewClickListener {
        void onFollowClick(ClickableUIViewHolder UIViewHolder);

        void onPromulagtorClick(ClickableUIViewHolder UIViewHolder);
    }
}
