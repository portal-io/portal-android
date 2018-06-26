package com.whaleyvr.biz.parser.support;

import android.support.v4.util.LruCache;

import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.PrepareStartPlayEvent;
import com.whaley.biz.playerui.exception.PlayerException;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.core.utils.GsonUtil;
import com.whaley.core.utils.StrUtil;
import com.whaleyvr.biz.parser.model.VideoParserBean;
import com.whaleyvr.biz.parser.videoparser.VideoParser;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by YangZhi on 2017/8/4 19:29.
 */

public class PlayerParserController extends BaseController {

    private static final LruCache<String, String> PARSERED_URLS = new LruCache<>(10);

    private Disposable disposable;

    private final int prepapreStartPlayPriority;

    public PlayerParserController(int prepapreStartPlayPriority) {
        this.prepapreStartPlayPriority = prepapreStartPlayPriority;
    }

    @Subscribe(priority = 80)
    public void onPrepareStartPlayEvent(PrepareStartPlayEvent prepareStartPlayEvent) {
        if (prepareStartPlayEvent.getMaxPriority() < 80) {
            return;
        }
        PlayData playData = prepareStartPlayEvent.getPlayData();
        String orginUrl = playData.getOrginUrl();
        if (StrUtil.isEmpty(orginUrl)) {
            getEventBus().cancelEventDelivery(prepareStartPlayEvent);
            getPlayerController().setError(new PlayerException(ErrorConstants.PARSER_ERROR_ORIGIN_URL, "获取播放地址错误..."));
            return;
        }
        String parserBeanJson = PARSERED_URLS.get(orginUrl);
        if (StrUtil.isEmpty(parserBeanJson)) {
            getEventBus().cancelEventDelivery(prepareStartPlayEvent);
            parserUrl(playData);
            return;
        }
//        emitEvent("/parser/event/parsesuccess",parserBeanJson);
        playData.putCustomData("key_parsesuccess_json", parserBeanJson);
    }

    private void parserUrl(final PlayData playData) {
        dispose();
        final String orginUrl = playData.getOrginUrl();
        disposable = Observable.just(playData)
                .flatMap(new Function<PlayData, ObservableSource<List<VideoParserBean>>>() {
                    @Override
                    public ObservableSource<List<VideoParserBean>> apply(@NonNull final PlayData playData) throws Exception {
                        String url = orginUrl;
                        if (url.contains("hls.snailvr.com")) {
                            url = removeFlag(url);
                        }
                        return VideoParser.parserUrl(url);
                    }
                })
                .map(new Function<List<VideoParserBean>, String>() {
                    @Override
                    public String apply(@NonNull List<VideoParserBean> videoParserBeen) throws Exception {
                        return GsonUtil.getGson().toJson(videoParserBeen);
                    }
                })
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(@NonNull String videoParserBeen) {
                        PARSERED_URLS.put(orginUrl, videoParserBeen);
                        playData.putCustomData("key_parsesuccess_json", videoParserBeen);
                        PrepareStartPlayEvent prepareStartPlayEvent = new PrepareStartPlayEvent();
                        prepareStartPlayEvent.setPlayData(getPlayerController().getRepository().getCurrentPlayData());
                        prepareStartPlayEvent.setMaxPriority(79);
                        emitStickyEvent(prepareStartPlayEvent);
                        disposable = null;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (isDisposed())
                            return;
                        getPlayerController().setError(new PlayerException(ErrorConstants.PARSER_ERROR, "内容源解析失败"));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private String removeFlag(String path) {
        if (path.contains("&flag=")) {
            path = path.substring(0, path.indexOf("&flag="));
        }
        return path;
    }

    @Override
    protected void onDispose() {
        super.onDispose();
        dispose();
    }

    private void dispose() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }

    @Override
    protected void onDestory() {
        super.onDestory();
        dispose();
    }
}
