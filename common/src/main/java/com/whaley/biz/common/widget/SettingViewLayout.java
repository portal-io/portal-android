package com.whaley.biz.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.whaley.biz.common.R;
import com.whaley.core.utils.DisplayUtil;
import com.whaley.core.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: qxw
 * Date:2017/7/25
 * Introduction:
 */

public class SettingViewLayout extends FrameLayout {

    TextView tvName;
    TextView tvText;
    ImageView ivLeftPic;
    ImageView ivRightPic;
    FrameLayout viewContent;
    View viewTopLine;
    View viewBottomLine;
    int nameColor;
    int textColor;
    int nameSize;
    int textSize;
    int rightPaddingContext = 0;
    int leftPaddingName = 0;
    int leftPaddingPic = 0;
    int rightPaddingPic = 0;
    int rightPaddingCheckbox = 0;
    int rightPaddingSegment = 0;
    CheckBox checkBox;
    SegmentedGroup segmentedGroup;
    int segmentSize;
    int segmentCheckedColor;
    int segmentUnCheckedColor;
    int segmentTextCheckedColor;
    int segmentTextUnCheckedColor;
    float segmentRadius;
    String[] segmentTexts;
    List<Integer> segmentIdList;
    int customizeViewId = -1;
    View customizeView;

    public void setOnItemClickListener(OnClickListener onClickListener) {
        if (onClickListener == null) {
            setClickable(false);
        } else {
            setOnClickListener(onClickListener);
        }
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        if (checkBox != null) {
            checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
        }
    }

    public void clearChecked() {
        if (checkBox != null) {
            checkBox.setVisibility(GONE);
            if (checkBox.getParent() != null) {
                ((ViewGroup) checkBox.getParent()).removeView(checkBox);
            }
        }
    }

    public void setChecked(boolean checked) {
        if (checkBox != null) {
            checkBox.setChecked(checked);
        }
    }

    public boolean isChecked() {
        if (checkBox != null) {
            checkBox.isChecked();
        }
        return false;
    }

    public interface OnSegmentCheckedChangeListener {
        void onCheckedChanged(int index);
    }

    public void setOnSegmentCheckedChangeListener(final OnSegmentCheckedChangeListener listener) {
        if (segmentedGroup != null) {
            segmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int index = segmentIdList.indexOf(checkedId);
                    if (index >= 0) {
                        if (listener != null) {
                            listener.onCheckedChanged(index);
                        }
                    }
                }
            });
        }
    }

    public void clearSegment() {
        if (segmentedGroup != null) {
            segmentedGroup.setVisibility(View.GONE);
            if (segmentedGroup.getParent() != null) {
                ((ViewGroup) segmentedGroup.getParent()).removeView(segmentedGroup);
            }
        }
    }

    public void selectSegment(int index) {
        if (segmentedGroup != null && segmentedGroup.getChildAt(index) != null) {
            segmentedGroup.getChildAt(index).performClick();
        }
    }

    /**
     * 给右边内容赋值
     *
     * @param charSequence
     */
    public void setText(CharSequence charSequence) {
        updateText(charSequence);
    }

    public void setText(@StringRes int resid) {
        setText(getContext().getResources().getText(resid));
    }

    /**
     * 给左边边内容赋值
     *
     * @param charSequence
     */
    public void setName(CharSequence charSequence) {
        updateName(charSequence);
    }

    public void setName(@StringRes int resid) {
        setName(getContext().getResources().getText(resid));
    }

    public ImageView getIvRightPic() {
        if (ivRightPic == null) {
            changeToRightPic();
        }
        return ivRightPic;
    }

    /**
     * 左图赋值
     *
     * @param leftPic
     */
    public void setLeftPic(int leftPic) {
        updateLeftPic(leftPic);
    }

    /**
     * 右图赋值
     *
     * @param rightPic
     */
    public void setRightPic(int rightPic) {
        updateRightPic(rightPic);
    }

    /**
     * 赋值CheckboxPic
     *
     * @param checkboxPic
     */
    public void setCheckboxPic(int checkboxPic) {
        updateCheckboxPic(checkboxPic);
    }

    public void setSegment(int segmentSize, String... segmentTextList) {
        this.segmentSize = segmentSize;
        if (segmentTextList != null && segmentTextList.length >= segmentSize) {
            if (segmentTexts == null) {
                segmentTexts = new String[3];
            }
            for (int i = 0; i < segmentSize; i++) {
                segmentTexts[i] = segmentTextList[i];
            }
        }
        updateSegment();
    }

    public SettingViewLayout(@NonNull Context context) {
        this(context, null);
    }

    public SettingViewLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingViewLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private int getColor(int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(getContext(), id);
        } else {
            return getContext().getResources().getColor(id);
        }
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        String name = null;
        String text = null;
        int leftPic = 0;
        int rightPic = 0;
        int checkboxPic = 0;
        int height = 0;
        int topLine = 0;
        int bottomLine = 0;

        //    int lineBg;
        int topLineBg = 0;
        int bottomBg = 0;
        int viewBg = 0;
        int leftPaddingView = 0;
        int rightPaddingView = 0;

        boolean canClick = true;

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.SettingViewLayout);
            name = a.getString(R.styleable.SettingViewLayout_nameText);
            text = a.getString(R.styleable.SettingViewLayout_contentText);
            leftPic = a.getResourceId(R.styleable.SettingViewLayout_leftPic, -1);
            rightPic = a.getResourceId(R.styleable.SettingViewLayout_rightPic, -1);
            checkboxPic = a.getResourceId(R.styleable.SettingViewLayout_checkboxPic, -1);
            height = (int) a.getDimension(R.styleable.SettingViewLayout_viewHeight, DisplayUtil.convertDIP2PX(56));
            topLine = (int) a.getDimension(R.styleable.SettingViewLayout_viewTopLine, 0);
            bottomLine = (int) a.getDimension(R.styleable.SettingViewLayout_viewBottomLine, 1);
            int lineBg = a.getResourceId(R.styleable.SettingViewLayout_lineBg, R.color.color9);
            topLineBg = a.getResourceId(R.styleable.SettingViewLayout_topLineBg, lineBg);
            bottomBg = a.getResourceId(R.styleable.SettingViewLayout_bottomLineBg, lineBg);
            viewBg = a.getResourceId(R.styleable.SettingViewLayout_viewBg, R.drawable.setting_item_bg);
            nameColor = a.getColor(R.styleable.SettingViewLayout_nameTextColor, getColor(R.color.color4));
            textColor = a.getColor(R.styleable.SettingViewLayout_contentTextColor, getColor(R.color.color5));
            nameSize = (int) a.getDimension(R.styleable.SettingViewLayout_nameTextSize, DisplayUtil.convertDIP2PX(15));
            textSize = (int) a.getDimension(R.styleable.SettingViewLayout_contentTextSize, DisplayUtil.convertDIP2PX(12));
            leftPaddingView = (int) a.getDimension(R.styleable.SettingViewLayout_leftPaddingView, DisplayUtil.convertDIP2PX(12));
            rightPaddingView = (int) a.getDimension(R.styleable.SettingViewLayout_rightPaddingView, DisplayUtil.convertDIP2PX(6.7f));
            leftPaddingPic = (int) a.getDimension(R.styleable.SettingViewLayout_leftPaddingPic, DisplayUtil.convertDIP2PX(2));
            leftPaddingName = (int) a.getDimension(R.styleable.SettingViewLayout_leftPaddingName, DisplayUtil.convertDIP2PX(13));
            rightPaddingPic = (int) a.getDimension(R.styleable.SettingViewLayout_rightPaddingPic, DisplayUtil.convertDIP2PX(10));
            rightPaddingContext = (int) a.getDimension(R.styleable.SettingViewLayout_rightPaddingContent, DisplayUtil.convertDIP2PX(10));
            rightPaddingCheckbox = (int) a.getDimension(R.styleable.SettingViewLayout_rightPaddingCheckbox, DisplayUtil.convertDIP2PX(10));
            rightPaddingSegment = (int) a.getDimension(R.styleable.SettingViewLayout_rightPaddingSegment, DisplayUtil.convertDIP2PX(10));
            canClick = a.getBoolean(R.styleable.SettingViewLayout_canClick, true);
            customizeViewId = a.getResourceId(R.styleable.SettingViewLayout_customizeViewId, -1);
            segmentSize = a.getInt(R.styleable.SettingViewLayout_segmentSize, 0);
            segmentCheckedColor = a.getColor(R.styleable.SettingViewLayout_segmentCheckedColor, getColor(R.color.color1));
            segmentUnCheckedColor = a.getColor(R.styleable.SettingViewLayout_segmentUnCheckedColor, getColor(R.color.color9));
            segmentTextCheckedColor = a.getColor(R.styleable.SettingViewLayout_segmentTextCheckedColor, getColor(R.color.color12));
            segmentTextUnCheckedColor = a.getColor(R.styleable.SettingViewLayout_segmentTextUnCheckedColor, getColor(R.color.color12));
            segmentRadius = a.getDimension(R.styleable.SettingViewLayout_segmentRadius, 0);
            String segmentText1 = a.getString(R.styleable.SettingViewLayout_segmentText1);
            String segmentText2 = a.getString(R.styleable.SettingViewLayout_segmentText2);
            String segmentText3 = a.getString(R.styleable.SettingViewLayout_segmentText3);
            if (!StrUtil.isEmpty(segmentText1) || !StrUtil.isEmpty(segmentText2) || !StrUtil.isEmpty(segmentText3)) {
                segmentTexts = new String[3];
                segmentTexts[0] = segmentText1;
                segmentTexts[1] = segmentText2;
                segmentTexts[2] = segmentText3;
            }
            a.recycle();
        }
        LayoutInflater.from(context).inflate(R.layout.layout_setting_view, this, true);
        setClickable(canClick);
        setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
        viewContent = (FrameLayout) this.findViewById(R.id.view_content);
        viewContent.setPadding(leftPaddingView, 0, rightPaddingView, 0);
        ViewGroup.LayoutParams layoutParams = viewContent.getLayoutParams();
        layoutParams.height = height;
        viewContent.setBackgroundResource(viewBg);
        viewContent.requestLayout();
        if (topLine != 0) {
            changeToTopLine();
            viewTopLine.getLayoutParams().height = topLine;
            viewTopLine.setBackgroundResource(topLineBg);
            viewTopLine.requestLayout();
        }
        if (bottomLine != 0) {
            changeToBottomLine();
            viewBottomLine.getLayoutParams().height = bottomLine;
            viewBottomLine.setBackgroundResource(bottomBg);
            viewBottomLine.requestFocus();
        }
        updateLeftPic(leftPic);
        updateName(name);
        updateText(text);
        updateRightPic(rightPic);
        updateCheckboxPic(checkboxPic);
        updateSegment();

    }

//    @Override
//    protected void onFinishInflate() {
//        super.onFinishInflate();
//        if (customizeViewId != -1) {
//            int childCount = getChildCount();
//            if (childCount > 1) {
//                customizeView = getChildAt(2);
//                LinearLayout rightView = (LinearLayout) findViewById(R.id.view_right);
//                FrameLayout.LayoutParams layoutParams
//                        = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
//                        , ViewGroup.LayoutParams.MATCH_PARENT);
//                removeView(customizeView);
//                rightView.addView(customizeView, layoutParams);
//            }
//        }
//    }

    public void updateText(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            changeToText();
            tvText.setText(charSequence);
            tvText.setVisibility(View.VISIBLE);
        } else if (tvText != null) {
            tvText.setText("");
            tvText.setVisibility(View.GONE);
        }
    }

    private void updateName(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            changeToName();
            tvName.setText(charSequence);
            tvName.setVisibility(View.VISIBLE);
        } else if (tvName != null) {
            tvName.setText("");
            tvName.setVisibility(View.GONE);
        }
    }

    private void updateLeftPic(int leftPic) {
        if (leftPic != -1) {
            changeToLeftPic();
            ivLeftPic.setImageResource(leftPic);
            ivLeftPic.setVisibility(View.VISIBLE);
        } else if (ivLeftPic != null) {
            ivLeftPic.setImageResource(0);
            ivLeftPic.setVisibility(View.GONE);
        }
    }

    private void updateRightPic(int rightPic) {
        if (rightPic != -1) {
            changeToRightPic();
            ivRightPic.setImageResource(rightPic);
            ivRightPic.setVisibility(View.VISIBLE);
        } else if (ivRightPic != null) {
            ivRightPic.setImageResource(0);
            ivRightPic.setVisibility(View.GONE);
        }
    }

    private void updateCheckboxPic(int checkboxPic) {
        if (checkboxPic != -1) {
            changeToCheckboxPic();
            checkBox.setBackgroundResource(checkboxPic);
        }
    }

    private void updateSegment() {
        if (segmentSize > 1 && segmentSize < 4) {
            changeToSegment();
            if (segmentIdList == null) {
                segmentIdList = new ArrayList<>();
            }
            for (int i = 0; i < segmentSize; i++) {
                addButton(getContext(), segmentedGroup, i);
            }
            segmentedGroup.setTintColor(segmentCheckedColor);
            segmentedGroup.setUnCheckedTintColor(segmentUnCheckedColor);
            segmentedGroup.setTextColor(segmentTextCheckedColor);
            segmentedGroup.setUnCheckedTextColor(segmentTextUnCheckedColor);
            segmentedGroup.setRadius(segmentRadius);
            segmentedGroup.updateBackground();
            selectSegment(0);
        }
    }

    private void changeToBottomLine() {
        if (viewBottomLine == null) {
            ViewStub vsBottomLine = (ViewStub) findViewById(R.id.vs_bottom_line);
            viewBottomLine = vsBottomLine.inflate().findViewById(R.id.view_line);
        }
    }

    private void changeToTopLine() {
        if (viewTopLine == null) {
            ViewStub vsTopLine = (ViewStub) this.findViewById(R.id.vs_top_line);
            viewTopLine = vsTopLine.inflate().findViewById(R.id.view_line);
        }
    }

    private void changeToLeftPic() {
        if (ivLeftPic == null) {
            ViewStub vsLeftPic = (ViewStub) this.findViewById(R.id.vs_lpic);
            ivLeftPic = (ImageView) vsLeftPic.inflate().findViewById(R.id.iv_pic);
            ivLeftPic.setPadding(leftPaddingPic, 0, 0, 0);
        }
    }

    private void changeToRightPic() {
        if (ivRightPic == null) {
            ViewStub vsRightPic = (ViewStub) this.findViewById(R.id.vs_rpic);
            ivRightPic = (ImageView) vsRightPic.inflate().findViewById(R.id.iv_pic);
            ivRightPic.setPadding(0, 0, rightPaddingPic, 0);
        }
    }

    private void changeToCheckboxPic() {
        if (checkBox == null) {
            ViewStub vsCheckbox = (ViewStub) this.findViewById(R.id.vs_checkbox);
            checkBox = (CheckBox) vsCheckbox.inflate().findViewById(R.id.checkbox);
            LinearLayout.LayoutParams layoutParamsCheckbox = (LinearLayout.LayoutParams) checkBox.getLayoutParams();
            layoutParamsCheckbox.rightMargin = rightPaddingCheckbox;
            checkBox.setLayoutParams(layoutParamsCheckbox);
        }
    }

    private void changeToName() {
        if (tvName == null) {
            ViewStub vsName = (ViewStub) this.findViewById(R.id.vs_name);
            tvName = (TextView) vsName.inflate().findViewById(R.id.tv_text);
            tvName.setPadding(leftPaddingName, 0, 0, 0);
            tvName.setTextColor(nameColor);
            tvName.setTextSize(TypedValue.COMPLEX_UNIT_PX, nameSize);
        }
    }

    private void changeToText() {
        if (tvText == null) {
            ViewStub vsText = (ViewStub) this.findViewById(R.id.vs_text);
            tvText = (TextView) vsText.inflate().findViewById(R.id.tv_text);
            tvText.setPadding(0, 0, rightPaddingContext, 0);
            tvText.setTextColor(textColor);
            tvText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

        }
    }

    private void changeToSegment() {
        if (segmentedGroup == null) {
            ViewStub vsSegmentedGroup = (ViewStub) findViewById(R.id.vs_segment);
            segmentedGroup = (SegmentedGroup) vsSegmentedGroup.inflate().findViewById(R.id.segment);
            LinearLayout.LayoutParams layoutParamsCheckbox = (LinearLayout.LayoutParams) segmentedGroup.getLayoutParams();
            layoutParamsCheckbox.rightMargin = rightPaddingSegment;
            segmentedGroup.setLayoutParams(layoutParamsCheckbox);
        }
    }

    private void addButton(Context context, SegmentedGroup group, int index) {
        RadioButton radioButton = (RadioButton) LayoutInflater.from(context).inflate(R.layout.radio_button_item, null);
        radioButton.setText(segmentTexts[index]);
//        if(index==0){
//            radioButton.setChecked(true);
//        }
        int id = View.generateViewId();

        segmentIdList.add(id);
        radioButton.setId(id);
        group.addView(radioButton);
    }
}
