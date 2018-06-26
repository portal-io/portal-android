package com.whaley.biz.program.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.whaley.core.utils.StrUtil;


/**
 * Author: qxw
 * Date: 2016/11/30
 */

public class JustifyTextView extends TextView {

    String oldText = "";
    /**
     * TextView的总宽度
     */
    private int mViewWidth;

    /**
     * 右边控件的宽度
     */
    private int moreWidth;
    /**
     * 行高
     */
    private int mLineY;
    private int trimLines;
    private boolean isMore = true;

    public JustifyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public JustifyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JustifyTextView(Context context) {
        super(context);
    }

    public void setTrimLines(int maxLine) {
        this.trimLines = maxLine;
    }

    public void setMore(boolean isMore) {
        this.isMore = isMore;
    }

    public void setMoreWidth(int width) {
        this.moreWidth = width;
    }

    public boolean isOldText(String text) {
        if (StrUtil.isEmpty(text) || text.equals(oldText)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            TextPaint paint = getPaint();
            paint.setColor(getCurrentTextColor());
            paint.drawableState = getDrawableState();
            mViewWidth = getMeasuredWidth();//拿到textview的实际宽度
            String text = getText().toString();
            mLineY = 0;
            mLineY += getTextSize();
            Layout layout = getLayout();
            int lines = layout.getLineCount();
            int height = getMeasuredHeight();
//            if (isMore) {
//                lines = trimLines;
//            }
            for (int i = 0; i < lines; i++) {//每行循环
                int lineStart = layout.getLineStart(i);
                int lineEnd = layout.getLineEnd(i);
                String line = text.substring(lineStart, lineEnd);//获取到TextView每行中的内容
                float width = StaticLayout.getDesiredWidth(text, lineStart, lineEnd, getPaint());
                if (needScale(line)&&(mLineY+paint.getTextSize())<height) {
                    if (i == layout.getLineCount() - 1) {//最后一行不需要重绘
                        if (isMore) {
                            float characterWidth = width / (lineEnd - lineStart);
                            int num = (int) (((width - (mViewWidth - moreWidth)) + 0.5) / characterWidth) + 2;
                            line = line.substring(0, line.length() - num) + "...";
                            canvas.drawText(line, 0, mLineY, paint);
                        } else {
                            canvas.drawText(line, 0, mLineY, paint);
                        }
                    } else {
                        drawScaleText(canvas, mViewWidth, lineStart, line, width);
                    }
                }
                mLineY += getLineHeight();//写完一行以后，高度增加一行的高度
            }
        } catch (Exception e) {
        }
    }

    private void drawMoreText(Canvas canvas, int lineStart, String line, float width) {
    }

    /**
     * 重绘此行
     *
     * @param canvas    画布
     * @param lineStart 行头
     * @param line      该行所有的文字
     * @param lineWidth 该行每个文字的宽度的总和
     */
    private void drawScaleText(Canvas canvas, int viewWidth, int lineStart, String line,
                               float lineWidth) {
        float x = 0;
        if (isFirstLineOfParagraph(lineStart, line)) {
            String blanks = "  ";
            canvas.drawText(blanks, x, mLineY, getPaint());// 以 (x, mLineY) 为起点，画出blanks
            float bw = StaticLayout.getDesiredWidth(blanks, getPaint());// 画出一个空格需要的宽度
            x += bw;
            line = line.substring(3);
        }
        // 比如说一共有5个字，中间隔了4个空儿，
        //	那就用整个TextView的宽度 - 这5个字的宽度，
        //然后除以4，填补到这4个空儿中
        float d = (viewWidth - lineWidth) / (line.length() - 1);

        for (int i = 0; i < line.length(); i++) {
            String c = String.valueOf(line.charAt(i));
            float cw = StaticLayout.getDesiredWidth(c, getPaint());
            canvas.drawText(c, x, mLineY, getPaint());
            x += cw + d;
        }
    }

    /**
     * 判断是不是段落的第一行。
     * 一个汉字相当于一个字符，此处判断是否为第一行的依据是：
     * 字符长度大于3且前两个字符为空格
     *
     * @param lineStart
     * @param line
     * @return
     */
    private boolean isFirstLineOfParagraph(int lineStart, String line) {
        return line.length() > 3 && line.charAt(0) == ' '
                && line.charAt(1) == ' ';
    }


    /**
     * 判断需不需要缩放
     * 该行最后一个字符不是换行符的时候返回true，
     * 该行最后一个字符是换行符的时候返回false
     *
     * @param line
     * @return
     */
    private boolean needScale(String line) {
        if (line.length() == 0) {
            return false;
        } else {
            return line.charAt(line.length() - 1) != '\n';//该行最后一个字符不是换行符的时候返回true，是换行符的时候返回false
        }
    }


}
