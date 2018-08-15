package com.whaleyvr.biz.unity.vrone;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.snailvr.vrone.R;
import com.whaley.core.utils.DisplayUtil;
import com.whaleyvr.biz.unity.model.MediaInfo;

/**
 * Created by dell on 2017/12/5.
 */

public class SplitLayout extends FrameLayout implements SplitView {

    protected ImageButton btnBack;
    protected TextView tvTitle;
    protected ImageButton btnStartPause;
    protected TextView tvCurrent;
    protected Button btnDefinition;
    protected ImageButton btnSplit;
    protected TextView tvTotal;
    protected SeekBar skProgress;
    protected FrameLayout layoutTouch;
    protected FrameLayout layoutControl;
    protected TextView tvToast;
    protected FrameLayout hotSpot;
    protected RelativeLayout layoutTitle;
    protected RelativeLayout layoutBottomControls;
    protected View line;

    private Context mContext;

    boolean clickable;

    SplitController splitController;

    public SplitLayout(Context context) {
        this(context, null);
    }

    public SplitLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SplitLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void setData(MediaInfo mediaInfo) {
        if (splitController != null) {
            splitController.setData(mediaInfo);
        }
    }

    private void initView(Context context) {
        this.mContext = context;
        this.splitController = new SplitController(mContext, this, isLive());
        onViewCreated(LayoutInflater.from(mContext).inflate(getLayoutId(), this, true));
    }

    protected boolean isLive() {
        return false;
    }

    protected int getLayoutId() {
        return R.layout.layout_split;
    }

    protected void onViewCreated(View view) {
        btnBack = (ImageButton) view.findViewById(R.id.btn_back);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvToast = (TextView) view.findViewById(R.id.tv_toast);
        btnStartPause = (ImageButton) view.findViewById(R.id.btn_start_pause);
        tvCurrent = (TextView) view.findViewById(R.id.tv_current);
        btnDefinition = (Button) view.findViewById(R.id.btn_definition);
        btnSplit = (ImageButton) view.findViewById(R.id.btn_split);
        tvTotal = (TextView) view.findViewById(R.id.tv_total);
        skProgress = (SeekBar) view.findViewById(R.id.sk_progress);
        layoutTouch = (FrameLayout) view.findViewById(R.id.layout_touch);
        layoutControl = (FrameLayout) view.findViewById(R.id.layout_control);
        hotSpot = (FrameLayout)view.findViewById(R.id.hot_spot);
        layoutTitle = (RelativeLayout)view.findViewById(R.id.layout_title);
        layoutBottomControls = (RelativeLayout)view.findViewById(R.id.layout_bottom_controls);
        line = view.findViewById(R.id.line);
        initView();
    }

    protected void initView() {
        int height = (int)(0.11f * DisplayUtil.getScreenHeight());
        FrameLayout.LayoutParams layoutParamsTitle = (FrameLayout.LayoutParams)layoutTitle.getLayoutParams();
        layoutParamsTitle.height = height;
        layoutTitle.setLayoutParams(layoutParamsTitle);
        FrameLayout.LayoutParams layoutParamsBottom = (FrameLayout.LayoutParams)layoutBottomControls.getLayoutParams();
        layoutParamsBottom.height = height;
        layoutBottomControls.setLayoutParams(layoutParamsBottom);
        FrameLayout.LayoutParams layoutParamsLine = (FrameLayout.LayoutParams)line.getLayoutParams();
        layoutParamsLine.height = height;
        line.setLayoutParams(layoutParamsLine);

        if (btnBack != null) {
            btnBack.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (splitController != null) {
                        splitController.onBackClick();
                    }
                }
            });
        }
        if (btnSplit != null) {
            btnSplit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (splitController != null) {
                        splitController.onSplitClick();
                    }
                }
            });
        }
        if (layoutTouch != null) {
            layoutTouch.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (splitController != null) {
                        splitController.onBlankClick();
                    }
                }
            });
        }
        if (btnDefinition != null) {
            btnDefinition.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (splitController != null && isClickable()) {
                        splitController.onSwitchDefinitionClick();
                    }
                }
            });
        }
        if (hotSpot != null) {
            hotSpot.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            if (splitController != null) {
                                splitController.onHotSpotClick(0);
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            if (splitController != null) {
                                splitController.onHotSpotClick(1);
                            }
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            if (splitController != null) {
                                splitController.onHotSpotClick(2);
                            }
                            break;
                    }
                    return true;
                }
            });
        }
        if (btnStartPause != null) {
            btnStartPause.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (splitController != null && isClickable()) {
                        if (btnStartPause.getTag() instanceof Integer && (Integer) btnStartPause.getTag() == 1) {
                            splitController.onRetryClick();
                        } else {
                            splitController.onStartPauseClick();
                        }
                    }
                }
            });
        }
        if (skProgress != null) {
            skProgress.setEnabled(false);
            skProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (splitController != null) {
                        splitController.onSeekChanging(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    if (splitController != null) {
                        splitController.onStartSeekTouch();
                    }
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    if (splitController != null) {
                        splitController.onSeekChanged(seekBar.getProgress());
                    }
                }
            });
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (splitController != null) {
            splitController.onViewCreated();
        }
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    @Override
    public void show() {
        layoutControl.setVisibility(VISIBLE);
        hotSpot.setVisibility(GONE);
        line.setVisibility(GONE);
    }

    @Override
    public void hide() {
        layoutControl.setVisibility(GONE);
        hotSpot.setVisibility(VISIBLE);
        line.setVisibility(VISIBLE);
    }

    @Override
    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public void setPlayCount(String playCount) {

    }

    @Override
    public void updateCurrentTimeText(String text) {
        tvCurrent.setText(text);
    }

    @Override
    public void updateTotalTimeText(String text) {
        tvTotal.setText(text);
    }

    @Override
    public void changeSeekMax(int duration) {
        skProgress.setMax(duration);
    }

    @Override
    public void changeSeekProgress(int duration) {
        skProgress.setProgress(duration);
    }

    @Override
    public void updateDefinitionText(String text) {
        btnDefinition.setText(text);
    }

    @Override
    public void changeWidgetClickable(boolean clickable) {
        skProgress.setEnabled(clickable);
        setClickable(clickable);
    }

    @Override
    public void changeStartPauseBtn(boolean isStarted) {
        int imageRes = isStarted ? com.whaley.biz.playerui.R.drawable.bg_btn_player_pause_selector : com.whaley.biz.playerui.R.drawable.bg_btn_player_start_selector;
        btnStartPause.setImageResource(imageRes);
        btnStartPause.setTag(0);
    }

    @Override
    public void changeSeekSecondProgress(int secondProgress) {
        skProgress.setSecondaryProgress((int) secondProgress);
    }

    @Override
    public void showToast(String text, String color) {
        if(TextUtils.isEmpty(text))
            return;
        if (TextUtils.isEmpty(color)) {
            tvToast.setTextColor(getResources().getColor(R.color.color12));
        } else {
            tvToast.setTextColor(Color.parseColor(color));
        }
        tvTitle.setVisibility(GONE);
        tvToast.setVisibility(VISIBLE);
        tvToast.setText(Html.fromHtml(text));
    }

    @Override
    public void clearToast() {
        tvTitle.setVisibility(VISIBLE);
        tvToast.setVisibility(GONE);
    }

    @Override
    public void onCompleted() {
        btnStartPause.setImageResource(R.mipmap.split_retry);
        btnStartPause.setTag(1);
    }

}
