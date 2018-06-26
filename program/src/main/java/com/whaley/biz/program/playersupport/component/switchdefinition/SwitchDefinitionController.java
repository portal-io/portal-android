package com.whaley.biz.program.playersupport.component.switchdefinition;

import android.support.v4.util.SimpleArrayMap;
import android.widget.Toast;

import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.BufferingEvent;
import com.whaley.biz.playerui.event.ErrorEvent;
import com.whaley.biz.playerui.event.PrepareStartPlayEvent;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.playerui.exception.PlayerException;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerConstants;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.constants.VideoBitType;
import com.whaley.biz.program.playersupport.event.DefinitionChangedEvent;
import com.whaley.biz.program.playersupport.event.SwitchDefinitionEvent;
import com.whaley.biz.program.playersupport.exception.ProgramErrorConstants;
import com.whaley.biz.program.playersupport.model.DefinitionModel;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;

import org.greenrobot.eventbus.Subscribe;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by YangZhi on 2017/8/4 20:39.
 */

public class SwitchDefinitionController extends BaseController {

    private SimpleArrayMap<Integer, DefinitionModel> currentDefinitionMap;

    private DefinitionModel currentDefinitionModel;

    private Disposable disposable;

    private int tryCount;

    private boolean isUserSwitchNext;

//    @Subscribe
//    public void onDefinitionResolvedEvent(DefinitionResolvedEvent definitionResolvedEvent) {
//        tryCount = 0;
//        currentDefinitionMap = definitionResolvedEvent.getDefinitionModelMap();
//    }

    @Subscribe(priority = PlayerConstants.PREPARE_START_PLAY_PRIORITY_SWITCH_DEFINITION)
    public void onPrepareStartPlay(PrepareStartPlayEvent prepareStartPlayEvent) {
        if (prepareStartPlayEvent.getMaxPriority() < PlayerConstants.PREPARE_START_PLAY_PRIORITY_SWITCH_DEFINITION) {
            return;
        }
        tryCount = 0;
        currentDefinitionMap = prepareStartPlayEvent.getPlayData().getCustomData(PlayerDataConstants.RESOLVED_DEFINITION_MAP);
        checkDefualtDefinition(currentDefinitionMap);
        getEventBus().cancelEventDelivery(prepareStartPlayEvent);
    }

    @Subscribe
    public void onVideoPrepared(VideoPreparedEvent videoPreparedEvent) {
        tryCount = 0;
    }

    @Subscribe
    public void onSwitchDifinitionEvent(SwitchDefinitionEvent switchDefinitionEvent) {
        if(switchDefinitionEvent.getDefinition()>0){
            isUserSwitchNext = false;
            playNextDefinition(switchDefinitionEvent.getDefinition());
        }else {
            isUserSwitchNext = true;
            playNext();
        }
        getPlayerController().getRepository().setOnBuffering(true);
        emitEvent(new BufferingEvent());
    }

    @Subscribe(priority = PlayerConstants.ERROR_EVENT_PRIORITY_SWITCH_DEFINITION)
    public void onError(ErrorEvent errorEvent) {
        PlayerException e = errorEvent.getPlayerException();
        if (e.getErrorCode() == ProgramErrorConstants.STR_ERROR_PLAYER) {
            if (isUserSwitchNext) {
                Toast.makeText(AppContextProvider.getInstance().getContext(), "该清晰度不可播，正在切换可用清晰度", Toast.LENGTH_SHORT).show();
            }
            getEventBus().cancelEventDelivery(errorEvent);
            if (tryCount < currentDefinitionMap.size() - 1 && hasNext()) {
                isUserSwitchNext = false;
                playNext();
                tryCount++;
            } else {
                getPlayerController().setError(new PlayerException(ProgramErrorConstants.ERROR_ALL_DEFINITION_ERROR, "播放数据源错误"));
            }
        }
    }

    private void checkDefualtDefinition(final SimpleArrayMap<Integer, DefinitionModel> currentDefinitionMap) {
        dispose();
        disposable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<Integer> e) throws Exception {
                Router.getInstance().buildExecutor("/setting/service/getprioritydefinition")
                        .callback(new Executor.Callback<Integer>() {
                            @Override
                            public void onCall(Integer definition) {
                                if(!e.isDisposed()) {
                                    e.onNext(definition);
                                }
                            }

                            @Override
                            public void onFail(Executor.ExecutionError executionError) {
                                if(!e.isDisposed()) {
                                    e.onNext(0);
                                }
                            }
                        }).excute();
            }
        }).map(new Function<Integer, Integer>() {
            @Override
            public Integer apply(@NonNull Integer level) throws Exception {
                boolean isHigher = false;
                if (level == 1) {
                    isHigher = true;
                }
                int definition = -1;

                for (int i = 0, j = currentDefinitionMap.size() - 1; i <= j; i++) {
                    DefinitionModel definitionModel = currentDefinitionMap.valueAt(i);
                    int itemDefinition = definitionModel.getDefinition();
                    if (definition == -1) {
                        definition = itemDefinition;
                        continue;
                    }
                    boolean isApply = false;
                    if (isHigher) {
                        if (itemDefinition > definition && itemDefinition != VideoBitType.HD) {
                            isApply = true;
                        }
                    } else {
                        if (itemDefinition < definition) {
                            isApply = true;
                        }
                    }
                    if (isApply) {
                        definition = itemDefinition;
                    }
                }
                return definition;
            }
        }).subscribeWith(new DisposableObserver<Integer>() {
            @Override
            public void onNext(@NonNull Integer definition) {
                switchDefinition(definition);
                PrepareStartPlayEvent prepareStartPlayEvent = new PrepareStartPlayEvent();
                prepareStartPlayEvent.setPlayData(getPlayerController().getRepository().getCurrentPlayData());
                prepareStartPlayEvent.setMaxPriority(PlayerConstants.PREPARE_START_PLAY_PRIORITY_SWITCH_DEFINITION - 1);
                getEventBus().postSticky(prepareStartPlayEvent);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                getPlayerController().setError(new PlayerException(ProgramErrorConstants.ERROR_SWITCH_DEFAULT_DEFINITION_ERROR, "切换默认清晰度错误"));
            }

            @Override
            public void onComplete() {

            }
        });
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

    private void playNext() {
        nextDefinition();
        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
        if(playData.getType() == ProgramConstants.TYPE_PLAY_LIVE){
            playData.setProgress(0);
        }else{
            playData.setProgress(getPlayerController().getCurrentProgress());
        }
        getPlayerController().pause();
        PrepareStartPlayEvent prepareStartPlayEvent = new PrepareStartPlayEvent();
        prepareStartPlayEvent.setPlayData(playData);
        prepareStartPlayEvent.setMaxPriority(1);
        getPlayerController().getEventBus().post(prepareStartPlayEvent);
//        getPlayerController().startPlay(playData);
    }

    private void playNextDefinition(int definition){
        switchDefinition(definition);
        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
        if(playData.getType() == ProgramConstants.TYPE_PLAY_LIVE){
            playData.setProgress(0);
        }else{
            playData.setProgress(playData.getProgress());
        }
        getPlayerController().pause();
        PrepareStartPlayEvent prepareStartPlayEvent = new PrepareStartPlayEvent();
        prepareStartPlayEvent.setPlayData(playData);
        prepareStartPlayEvent.setMaxPriority(1);
        getPlayerController().getEventBus().post(prepareStartPlayEvent);
    }


    public void switchDefinition(int definition) {
        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
        SimpleArrayMap<Integer, DefinitionModel> definitionMap = currentDefinitionMap;
        DefinitionModel definitionModel = definitionMap.get(definition);
        playData.setRenderType(definitionModel.getRenderType());
        playData.setRealUrl(definitionModel.getUrl());
        currentDefinitionModel = definitionModel;
        getPlayerController().getRepository().getCurrentPlayData().putCustomData(PlayerDataConstants.CURRENT_DEFINITION_MODEL, currentDefinitionModel);
        DefinitionChangedEvent definitionChangedEvent = new DefinitionChangedEvent();
        definitionChangedEvent.setDefinitionModel(getCurrentDefinition());
        emitStickyEvent(definitionChangedEvent);
//        playData.setRenderType(definitionModel.getRenderType());
//        definitionRepository.setCurrentDefinition(definition);
    }

    private boolean hasNext() {
        return currentDefinitionMap != null && currentDefinitionMap.size() > 1;
    }

    public void nextDefinition() {
        SimpleArrayMap<Integer, DefinitionModel> definitionMap = currentDefinitionMap;
        DefinitionModel definitionModel = getCurrentDefinition();
        final int size = definitionMap.size();
        int nextIndex = 0;
        if (definitionModel != null) {
            int definition = definitionModel.getDefinition();
            nextIndex = definitionMap.indexOfKey(definition) + 1;
        }
        if (nextIndex == size) {
            nextIndex = 0;
        }
        int nextDefinition = definitionMap.keyAt(nextIndex);
        switchDefinition(nextDefinition);
    }

    public DefinitionModel getCurrentDefinition() {
        return currentDefinitionModel;
    }

    @Override
    protected void onDestory() {
        super.onDestory();
        dispose();
    }
}
