package com.whaley.biz.program.playersupport.component.normalplayer.resolvedefinition;

import android.support.v4.util.LruCache;
import android.support.v4.util.SimpleArrayMap;

import com.google.gson.reflect.TypeToken;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.PrepareStartPlayEvent;
import com.whaley.biz.playerui.exception.PlayerException;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerConstants;
import com.whaley.biz.program.constants.VideoBitType;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.constants.ServerRenderType;
import com.whaley.biz.program.playersupport.exception.ProgramErrorConstants;
import com.whaley.biz.program.playersupport.model.DefinitionModel;
import com.whaley.biz.program.playersupport.model.VideoParserBean;
import com.whaley.core.utils.GsonUtil;
import com.whaley.core.utils.StrUtil;
import com.whaley.wvrplayer.vrplayer.external.event.VideoConstantValue;

import org.greenrobot.eventbus.Subscribe;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by YangZhi on 2017/8/4 20:42.
 */

public class ResolveDefinitionController extends BaseController {

    private static final LruCache<String, SimpleArrayMap<Integer, DefinitionModel>> RESOLVED_DEFINITIONS
            = new LruCache<>(10);

    private Disposable disposable;


    @Subscribe(priority = PlayerConstants.PREPARE_START_PLAY_PRIORITY_RESOLVE_DEFINITION)
    public void onPrepareStartPlay(PrepareStartPlayEvent prepareStartPlayEvent){
        if(prepareStartPlayEvent.getMaxPriority()<PlayerConstants.PREPARE_START_PLAY_PRIORITY_RESOLVE_DEFINITION){
            return;
        }
        PlayData playData = prepareStartPlayEvent.getPlayData();
        String penndingResolveJson = playData.getCustomData("key_parsesuccess_json");
        String serverRenderType = playData.getCustomData(PlayerDataConstants.SERVER_RENDER_TYPE);
        if (!StrUtil.isEmpty(penndingResolveJson)) {
            SimpleArrayMap<Integer, DefinitionModel> resolvedDefinitionModel = RESOLVED_DEFINITIONS.get(penndingResolveJson+serverRenderType);
            if (resolvedDefinitionModel == null) {
                getEventBus().cancelEventDelivery(prepareStartPlayEvent);
                resolveDefinition(penndingResolveJson, serverRenderType);
                return;
            }
            playData.putCustomData(PlayerDataConstants.RESOLVED_DEFINITION_MAP,resolvedDefinitionModel);
        }
    }


    private void resolveDefinition(final String resolveJson, final String serverRenderType) {
        dispose();
        disposable = Observable.just(resolveJson)
                .map(new Function<String, List<VideoParserBean>>() {
                    @Override
                    public List<VideoParserBean> apply(@NonNull String s) throws Exception {
                        return GsonUtil.getGson().fromJson(s, new TypeToken<List<VideoParserBean>>() {
                        }.getType());
                    }
                })
                .doOnNext(new Consumer<List<VideoParserBean>>() {
                    @Override
                    public void accept(@NonNull List<VideoParserBean> videoParserBeans) throws Exception {
                        sortResult(videoParserBeans);
                    }
                })
                .map(new Function<List<VideoParserBean>, SimpleArrayMap<Integer, DefinitionModel>>() {
                    @Override
                    public SimpleArrayMap<Integer, DefinitionModel> apply(@NonNull List<VideoParserBean> videoParserBeen) throws Exception {
                        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
                        boolean isFootball = playData.getBooleanCustomData(PlayerDataConstants.FOOTBALL_TYPE);
                        if(isFootball){
                            return getResultByFootBall(videoParserBeen);
                        }
                        SimpleArrayMap<Integer, DefinitionModel> definitionModelMap = filterResult(videoParserBeen);
                        return definitionModelMap;
                    }
                })
                .subscribeWith(new DisposableObserver<SimpleArrayMap<Integer, DefinitionModel>>() {
                    @Override
                    public void onNext(@NonNull SimpleArrayMap<Integer, DefinitionModel> definitionModelMap) {
                        if(definitionModelMap.size()>0) {
                            RESOLVED_DEFINITIONS.put(resolveJson+serverRenderType, definitionModelMap);

                            PlayData playData = getPlayerController().getRepository().getCurrentPlayData();

                            playData.putCustomData(PlayerDataConstants.RESOLVED_DEFINITION_MAP, definitionModelMap);

//                        DefinitionResolvedEvent definitionResolvedEvent = new DefinitionResolvedEvent();
//                        definitionResolvedEvent.setDefinitionModelMap(definitionModelMap);
//                        emitEvent(definitionResolvedEvent);

                            PrepareStartPlayEvent prepareStartPlayEvent = new PrepareStartPlayEvent();
                            prepareStartPlayEvent.setPlayData(playData);
                            prepareStartPlayEvent.setMaxPriority(PlayerConstants.PREPARE_START_PLAY_PRIORITY_RESOLVE_DEFINITION - 1);
                            emitEvent(prepareStartPlayEvent);
                        }else {
                            getPlayerController().setError(new PlayerException(ProgramErrorConstants.STR_ERROR_NONE_ENABLE_DEFINITION_ERROR,"获取可用清晰度地址错误"));
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getPlayerController().setError(new PlayerException(ProgramErrorConstants.STR_ERROR_RESOLVE_DEFINITION_ERROR,"播放器异常"));
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

    private void dispose(){
        if(disposable!=null){
            disposable.dispose();
            disposable = null;
        }
    }


    /**
     * 排序清晰度
     *
     * @param beanlist
     */
    private void sortResult(List<VideoParserBean> beanlist) {
        Collections.sort(beanlist, new Comparator<VideoParserBean>() {
            @Override
            public int compare(VideoParserBean o1, VideoParserBean o2) {
                if (o1 == null) {
                    if (o2 == null)
                        return 0;
                    else
                        return -1;
                } else {
                    if (o2 == null) {
                        return 1;
                    } else {
                        int value = Integer.compare(o1.videoBitType, o2.videoBitType);
                        return value;
                    }
                }
            }
        });
    }

    /**
     * 过滤清晰度
     *
     * @param beanlist
     * @return
     */
    private SimpleArrayMap<Integer, DefinitionModel> filterResult(List<VideoParserBean> beanlist) {
        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
        SimpleArrayMap<Integer, DefinitionModel> definitionMap = new SimpleArrayMap<>();
        final int type = playData.getType();
//        boolean isFilter4K = type != PlayerType.TYPE_MORETV_2D && type != PlayerType.TYPE_MORETV_TV;
        boolean isFilterSD = false;
        final String serverRenderType = playData.getCustomData(PlayerDataConstants.SERVER_RENDER_TYPE);
        if (!StrUtil.isEmpty(serverRenderType)) {
            switch (serverRenderType) {
                case ServerRenderType.RENDER_TYPE_PLANE_2D:
                case ServerRenderType.RENDER_TYPE_PLANE_3D_LR:
                case ServerRenderType.RENDER_TYPE_PLANE_3D_UD:
                case ServerRenderType.RENDER_TYPE_360_3D_LF:
                case ServerRenderType.RENDER_TYPE_360_3D_UD:
                case ServerRenderType.RENDER_TYPE_180_PLANE:
                case ServerRenderType.RENDER_TYPE_180_3D_UD:
                case ServerRenderType.RENDER_TYPE_180_3D_LF:
                case ServerRenderType.RENDER_TYPE_360_OCT_3D:
                case ServerRenderType.RENDER_TYPE_180_OCT_3D:
//                    isFilter4K = false;
                    isFilterSD = true;
                    break;
                default:
                    break;
            }
        }

        boolean hasSDAOrSDB = false;
        if(!isFilterSD){
            for (VideoParserBean videoParserBean : beanlist) {
                if(videoParserBean.videoBitType == VideoBitType.SDA||videoParserBean.videoBitType==VideoBitType.SDB){
                    hasSDAOrSDB=true;
                    break;
                }
            }
        }


        for (VideoParserBean videoParserBean : beanlist) {
            boolean isAdd = false;
            switch (videoParserBean.videoBitType){
                case VideoBitType.ST:
                    isAdd = !hasSDAOrSDB;
                    break;
                case VideoBitType.SD:
                    isAdd = !isFilterSD && !hasSDAOrSDB;
                    break;
                case VideoBitType.HD:
                    isAdd = !hasSDAOrSDB;
                    break;
                case VideoBitType.SDA:
                    isAdd = !isFilterSD && hasSDAOrSDB;
                    break;
                case VideoBitType.SDB:
                    isAdd = !isFilterSD && hasSDAOrSDB;
                    break;
                case VideoBitType.TDA:
                    isAdd = true;
                    break;
                case VideoBitType.TDB:
                    isAdd = true;
                    break;
                default:
                    break;
            }

            if(isAdd){
                DefinitionModel definitionModel = createDefinitionModel(type, serverRenderType, videoParserBean);
                definitionMap.put(definitionModel.getDefinition(), definitionModel);
            }

        }

//        if (definitionMap.isEmpty()) {
//            VideoParserBean videoParserBean = beanlist.get(0);
//            DefinitionModel definitionModel = createDefinitionModel(type, serverRenderType, videoParserBean);
//            definitionMap.put(definitionModel.getDefinition(), definitionModel);
//        }

        return definitionMap;
    }


    public  SimpleArrayMap<Integer, DefinitionModel> getResultByFootBall(List<VideoParserBean> beanlist) {
        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
        boolean isPublic = playData.getBooleanCustomData(PlayerDataConstants.CURRENT_CAMERA_IS_PUBLIC);
        final String serverRenderType = playData.getCustomData(PlayerDataConstants.SERVER_RENDER_TYPE);
        DefinitionModel definitionModel = new DefinitionModel();
        definitionModel.setUrl(beanlist.get(0).url);
        int renderType;
        if(isPublic){
            renderType = RenderTypeUtil.getRenderTypeByRenderTypeStr(serverRenderType);
        }else {
            renderType = VideoConstantValue.MODE_HALF_SPHERE_VIP;
        }
        definitionModel.setRenderType(renderType);
        definitionModel.setDefinition(VideoBitType.HD);
        SimpleArrayMap<Integer,DefinitionModel> definitionMap = new SimpleArrayMap<>();
        definitionMap.put(definitionModel.getDefinition(),definitionModel);
        return definitionMap;
    }

    /**
     * 创建单个清晰度模型
     *
     * @param type
     * @param videoParserBean
     * @return
     */
    private DefinitionModel createDefinitionModel(int type, String serverRenderType, VideoParserBean videoParserBean) {
        DefinitionModel definitionModel = new DefinitionModel();
        definitionModel.setUrl(videoParserBean.url);
        definitionModel.setDefinition(videoParserBean.videoBitType);
        int renderType = RenderTypeUtil.getRenderType(serverRenderType, type, definitionModel.getDefinition());
        definitionModel.setRenderType(renderType);
        return definitionModel;
    }

    @Override
    protected void onDestory() {
        super.onDestory();
        dispose();
    }
}
