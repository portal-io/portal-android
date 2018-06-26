package com.whaley.biz.user.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whaley.biz.common.widget.SimpleTextChangedListener;
import com.whaley.biz.user.R;
import com.whaley.core.utils.DisplayUtil;


/**
 * Created by dell on 2016/11/7.
 */

public class EditLayout extends RelativeLayout implements View.OnFocusChangeListener {

    private String inputType;
    private String hint;
    private EditText et_input;
    private ImageButton btn_clear;
    private View bottom_line;
    private TextWatcher listener;
    int lineBg = -1;

    public EditLayout(Context context) {
        this(context, null);
    }

    public EditLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.edit_layout_style);
    }

    public EditLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.EditLayout, defStyleAttr, 0);
        inputType = typedArray.getString(R.styleable.EditLayout_inputType);
        hint = typedArray.getString(R.styleable.EditLayout_hint);
        lineBg = typedArray.getResourceId(com.whaley.biz.common.R.styleable.SettingViewLayout_lineBg, R.color.color2);
        typedArray.recycle();
        initView();
    }

    public EditText getEditTextView() {
        return et_input;
    }

    public void addTextChangedListener(TextWatcher listener) {
        this.listener = listener;
    }

    public void setError(boolean isError) {
        if (isError) {
            bottom_line.setBackgroundResource(R.color.color15);
        } else {
            bottom_line.setBackgroundResource(R.color.color3);
        }
    }

    public void requestInputFocus() {
        et_input.requestFocus();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_edit_input, this);
        et_input = (EditText) findViewById(R.id.et_input);
        btn_clear = (ImageButton) findViewById(R.id.btn_clear);
        bottom_line = findViewById(R.id.bottom_line);
        bottom_line.setBackgroundResource(lineBg);
        if (!TextUtils.isEmpty(hint)) {
            et_input.setHint(hint);
        }
        et_input.setOnFocusChangeListener(this);
        if (inputType != null) {
            switch (inputType) {
                case "phone":
                    et_input.setInputType(InputType.TYPE_CLASS_PHONE);
                    break;
                case "password":
                case "textPassword":
                case "imgCode":
                    et_input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    break;
                case "number":
                    et_input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    break;
                default:
                    et_input.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
            }
        }
        et_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });
        et_input.addTextChangedListener(new SimpleTextChangedListener() {
            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    btn_clear.setVisibility(GONE);
                } else {
                    btn_clear.setVisibility(VISIBLE);
                }
                if (listener != null) {
                    listener.afterTextChanged(s);
                }
            }
        });
        btn_clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                et_input.setText("");
            }
        });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == R.id.et_input) {
            LayoutParams layoutParams = (LayoutParams) bottom_line.getLayoutParams();
            if (hasFocus) {
                layoutParams.height = DisplayUtil.convertDIP2PX(2);
            } else {
                layoutParams.height = DisplayUtil.convertDIP2PX(1);
            }
            bottom_line.setLayoutParams(layoutParams);
        }
    }
}
