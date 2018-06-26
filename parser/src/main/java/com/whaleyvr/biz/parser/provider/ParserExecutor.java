package com.whaleyvr.biz.parser.provider;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.core.router.Executor;
import com.whaley.core.utils.GsonUtil;
import com.whaleyvr.biz.parser.model.VideoParserBean;
import com.whaleyvr.biz.parser.videoparser.VideoParser;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by dell on 2017/8/24.
 */

@Route(path = "/parser/service/parser")
public class ParserExecutor implements Executor<String> {

    @Override
    public void init(Context context) {

    }

    @Override
    public void excute(String s, final Callback callback) {
        VideoParser.parserUrl(s).subscribe(new DisposableObserver<List<VideoParserBean>>() {
            @Override
            public void onNext(@NonNull List<VideoParserBean> videoParserBeen) {
                callback.onCall(GsonUtil.getGson().toJson(videoParserBeen));
            }

            @Override
            public void onError(@NonNull Throwable e) {
                callback.onFail(new ExecutionError(e.getMessage(),e) {
                });
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
