package com.whaley.biz.program.ui.detail.viewholder;

import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.model.SeriesModel;
import com.whaley.biz.program.ui.detail.adapter.SeriesAdapter;
import com.whaley.biz.program.widget.JustifyTextView;
import com.whaley.core.utils.DisplayUtil;
import com.whaley.core.widget.viewholder.IViewHolder;
import com.whaley.core.widget.viewholder.OnItemClickListener;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TVViewHolder extends ProgramViewHolder<TVViewHolder.TVProgramModel> {
    static final int HEIGHT_HORIZONTAL_LINEAR = (DisplayUtil.screenW - 6) / 7;
    static final int MARGIN_BOTTOM_HORIZONTAL_LINEAR = DisplayUtil.convertDIP2PX(108);
//        int margin_bottom_horizontal_linear;

    @BindView(R2.id.tv_program_name)
    TextView tvProgramName;
    @BindView(R2.id.iv_play_icon)
    ImageView ivPlayIcon;
    @BindView(R2.id.tv_play_count)
    TextView tvPlayCount;
    @BindView(R2.id.layout_tag)
    TagFlowLayout layoutTag;
    @BindView(R2.id.layout_program_title)
    RelativeLayout layoutProgramTitle;
    @BindView(R2.id.view_line)
    View viewLine;
    @BindView(R2.id.tv_moretv_description)
    TextView tv_moretv_description;
    @BindView(R2.id.view_line2)
    View viewLine2;
    @BindView(R2.id.tv_all_anthology_name)
    TextView tvAllAnthologyName;
    @BindView(R2.id.tv_all_series_unfold)
    TextView tvAllSeriesUnfold;
    @BindView(R2.id.view_all_series_line)
    View viewAllSeriesLine;
    @BindView(R2.id.layout_series_title)
    RelativeLayout layoutSeriesTitle;
    @BindView(R2.id.view_line3)
    View viewLine3;
    @BindView(R2.id.rv_all_series)
    RecyclerView rvAllSeries;
    @BindView(R2.id.layout_series)
    LinearLayout layout_series;

    LinearLayoutManager layoutManager;

    SeriesAdapter adapter;

    OnItemClickListener onItemClickListener;

    ValueAnimator animator;

    int lastSelectedPosition = -1;

    public TVViewHolder(View view) {
        super(view);
        tv_moretv_description.setMovementMethod(ScrollingMovementMethod.getInstance());
        adapter = new SeriesAdapter();
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(IViewHolder viewHolder, int position) {

                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(viewHolder, position);
                if (layoutManager instanceof GridLayoutManager) {
                    changeToHorizontalLinearLayout();
                }
            }
        });
        changeToHorizontalLinearLayout(false);
        rvAllSeries.setAdapter(adapter);
        rvAllSeries.setHasFixedSize(true);
        rvAllSeries.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = layoutManager.getPosition(view);
                if (position > 0) {
                    outRect.left = 1;
                }
                if (layoutManager instanceof GridLayoutManager) {
                    if (position > 7) {
                        outRect.top = 1;
                    }
                } else {
                    outRect.top = 0;
                }
            }
        });
        layoutDownlod.getChildAt(0).setEnabled(false);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    void changeToGridLayout() {
        layoutSeriesTitle.setSelected(true);
        openSeries();
        tvAllSeriesUnfold.setText("收起");
        Drawable drawable = getRootView().getContext().getResources().getDrawable(R.mipmap.ic_tv_series_unfold);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvAllSeriesUnfold.setCompoundDrawables(null, null, drawable, null);
    }

    void changeToHorizontalLinearLayout() {
        changeToHorizontalLinearLayout(true);
    }

    void changeToHorizontalLinearLayout(boolean isAnim) {
        if (isAnim) {
            closeSeries();
        } else {
            layoutButtons.setVisibility(View.VISIBLE);
            tv_moretv_description.setVisibility(View.VISIBLE);
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rvAllSeries.getLayoutParams();
            layoutParams.height = HEIGHT_HORIZONTAL_LINEAR;
            layoutParams = (ViewGroup.MarginLayoutParams) layout_series.getLayoutParams();
            layoutParams.bottomMargin = MARGIN_BOTTOM_HORIZONTAL_LINEAR;
            layout_series.requestLayout();

            layoutManager = new LinearLayoutManager(rvAllSeries.getContext(), LinearLayoutManager.HORIZONTAL, false);
            rvAllSeries.setLayoutManager(layoutManager);
        }
        layoutSeriesTitle.setSelected(false);
        tvAllSeriesUnfold.setText("展开");
        Drawable drawable = getRootView().getContext().getResources().getDrawable(R.mipmap.ic_tv_series_shrink);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvAllSeriesUnfold.setCompoundDrawables(null, null, drawable, null);
    }

    public void cancelAnim() {
        if (animator != null && animator.isRunning())
            animator.cancel();
    }

    private void openSeries() {
        animFinal(getFullHeight(), 0, 0f, layoutButtons.getHeight(), new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                super.onAnimationEnd(animation);
                layoutButtons.setVisibility(View.GONE);
                tv_moretv_description.setVisibility(View.GONE);
                layoutManager = new GridLayoutManager(rvAllSeries.getContext(), 7);
                rvAllSeries.setLayoutManager(layoutManager);
            }
        });
    }

    private void closeSeries() {
        animFinal(HEIGHT_HORIZONTAL_LINEAR, MARGIN_BOTTOM_HORIZONTAL_LINEAR, 1f, 0f, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(android.animation.Animator animation) {
                super.onAnimationStart(animation);
                layoutButtons.setVisibility(View.VISIBLE);
                tv_moretv_description.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                super.onAnimationEnd(animation);
                layoutManager = new LinearLayoutManager(rvAllSeries.getContext(), LinearLayoutManager.HORIZONTAL, false);
                rvAllSeries.setLayoutManager(layoutManager);
                if (lastSelectedPosition != -1) {
                    rvAllSeries.scrollToPosition(lastSelectedPosition);
                }
            }
        });
    }

    private void animFinal(int finalHeight, int finalBottomMargin, float finalAlpha, float finalButtonsTranslateY, final android.animation.Animator.AnimatorListener listener) {
        cancelAnim();
        if (getRootView() == null)
            return;
        final int currentHeight = rvAllSeries.getHeight();

        final int currentMargin = ((ViewGroup.MarginLayoutParams) layout_series.getLayoutParams()).bottomMargin;
        final int diffMargin = currentMargin - finalBottomMargin;

        final float currentAlpha = tv_moretv_description.getAlpha();
        final float diffAlpha = currentAlpha - finalAlpha;

        final float currentTranslateY = layoutButtons.getTranslationY();
        final float diffTranslateY = currentTranslateY - finalButtonsTranslateY;


        animator = ValueAnimator.ofInt(currentHeight, finalHeight);
        animator.addListener(new android.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(android.animation.Animator animation) {
                if (listener != null)
                    listener.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                if (listener != null)
                    listener.onAnimationEnd(animation);
                animation.removeAllListeners();
            }

            @Override
            public void onAnimationCancel(android.animation.Animator animation) {
                if (listener != null)
                    listener.onAnimationCancel(animation);
                animation.removeAllListeners();
            }

            @Override
            public void onAnimationRepeat(android.animation.Animator animation) {
                if (listener != null)
                    listener.onAnimationRepeat(animation);
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            int lastValue = currentHeight;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (getRootView() == null)
                    return;
                int value = (int) animation.getAnimatedValue();
                float fraction = animation.getAnimatedFraction();
                if (value != lastValue) {
                    ViewGroup.LayoutParams layoutParams = rvAllSeries.getLayoutParams();
                    layoutParams.height = value;
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layout_series.getLayoutParams();
                    marginLayoutParams.bottomMargin = currentMargin - (int) (diffMargin * fraction);
                    layout_series.requestLayout();

                    tv_moretv_description.setAlpha(currentAlpha - diffAlpha * fraction);
                    layoutButtons.setTranslationY(currentTranslateY - diffTranslateY * fraction);
                    lastValue = value;
                }
                if (fraction == 1f) {
                    animation.removeAllUpdateListeners();
                }
            }
        });
        animator.setDuration(150);
        animator.start();
    }


    private int getFullHeight() {
        return layoutDetail.getHeight() - viewLine.getTop() - viewLine.getHeight() - viewLine2.getHeight() - layoutSeriesTitle.getHeight();
    }


    @OnClick(R2.id.layout_series_title)
    public void onSeriesSwitchButtonClick() {
        layoutSeriesTitle.setSelected(!layoutSeriesTitle.isSelected());
        boolean isShowGrid = layoutSeriesTitle.isSelected();
        if (isShowGrid) {
            changeToGridLayout();
            return;
        }
        changeToHorizontalLinearLayout();
    }

    @Override
    public void destory() {
        cancelAnim();
        super.destory();
    }

    public boolean onBackPressed() {
        if (layoutSeriesTitle != null && layoutSeriesTitle.isSelected()) {
            changeToHorizontalLinearLayout();
            return true;
        }
        return false;
    }

    @Override
    protected void onBindData(TVProgramModel data) {
        tvProgramName.setText(data.getProgramName());
        tv_moretv_description.setText(data.getDescription());
        tvPlayCount.setText(data.getPlayCount());
        layoutTag.setAdapter(new TagAdapter<String>(data.getTags()) {
            @Override
            public View getView(FlowLayout parent, int position, String tag) {
                TextView tv = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tag,
                        parent, false);
                tv.setText(tag);
                return tv;
            }
        });
        adapter.setData(data.getSeriesModels());
        lastSelectedPosition = data.getSelectedPosition();
        rvAllSeries.post(new Runnable() {
            @Override
            public void run() {
                if (rvAllSeries != null && lastSelectedPosition != -1) {
                    rvAllSeries.scrollToPosition(lastSelectedPosition);
                }
            }
        });
    }


    public static class TVProgramModel {
        private String programName;
        private String description;
        private List<String> tags;
        private String playCount;
        private List<SeriesModel> seriesModels;
        private int selectedPosition = -1;

        public String getProgramName() {
            return programName;
        }

        public void setProgramName(String programName) {
            this.programName = programName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        public String getPlayCount() {
            return playCount;
        }

        public void setPlayCount(String playCount) {
            this.playCount = playCount;
        }

        public List<SeriesModel> getSeriesModels() {
            return seriesModels;
        }

        public void setSeriesModels(List<SeriesModel> seriesModels) {
            this.seriesModels = seriesModels;
        }

        public void setSelectedPosition(int selectedPosition) {
            this.selectedPosition = selectedPosition;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }
    }
}