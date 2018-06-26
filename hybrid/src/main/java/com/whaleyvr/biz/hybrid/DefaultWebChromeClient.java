package com.whaleyvr.biz.hybrid;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.view.View;

import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

/**
 * Created by yangzhi on 17/1/11.
 */

public class DefaultWebChromeClient extends WebChromeClient{


    private Activity getActivity(View view){
        Context context = view.getContext();
        if (context instanceof Activity) {
            return((Activity) context);
        } else if (context instanceof ContextWrapper) {
            Context context2 = ((ContextWrapper) context).getBaseContext();
            if (context2 instanceof Activity) {
                return ((Activity) context2);
            }
        }
        return null;
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {

        return super.onShowFileChooser(webView, valueCallback, fileChooserParams);
    }
}
