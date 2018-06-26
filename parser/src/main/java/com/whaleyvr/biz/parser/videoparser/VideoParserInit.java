package com.whaleyvr.biz.parser.videoparser;

import com.peersless.security.Security;
import com.whaley.core.appcontext.AppContextProvider;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import com.peersless.videoParser.VideoParser;

public class VideoParserInit {

    public static void init() {
		String userId = "c11274c332867aefc3e4c20cf7138681";
		Security.GetInstance().SetUserID(userId);
		Security.GetInstance().InitModule(AppContextProvider.getInstance().getContext(),
				Security.SNAIL);
        startHttpAgent();
        initVideoParser().subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Boolean aBoolean) {

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
	}

    public static boolean isAvailable(){
        return VideoParser.GetInstance().isAvailable();
    }

    public static void startHttpAgent(){
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                if (!HttpAgent.getInstance().IsOpen()) {
                    HttpAgent.getInstance().Start(12580);
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Boolean aBoolean) {

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

	public static Observable<Boolean> initVideoParser(){
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                VideoParser vp = VideoParser.GetInstance();
                vp.SetContext(AppContextProvider.getInstance().getContext());
                vp.SetUpdateHost("u.tvmore.com.cn");
                emitter.onNext(vp.Init());
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
	}

}
