package com.whaley.biz.program.playersupport.component.liveplayer.liveresolvedefinition;

import android.support.v4.util.LruCache;
import android.support.v4.util.SimpleArrayMap;

import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.PrepareStartPlayEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerConstants;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.constants.PlayerType;
import com.whaley.biz.program.constants.VideoBitType;
import com.whaley.biz.program.model.LiveDetailsModel;
import com.whaley.biz.program.model.LiveMediaModel;
import com.whaley.biz.program.playersupport.component.normalplayer.resolvedefinition.RenderTypeUtil;
import com.whaley.biz.program.playersupport.model.CameraModel;
import com.whaley.biz.program.playersupport.model.DefinitionModel;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.wvrplayer.vrplayer.external.event.VideoConstantValue;

import org.greenrobot.eventbus.Subscribe;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by YangZhi on 2017/8/22 13:19.
 */

public class LiveResolveDefinitionController extends BaseController{

    private static final LruCache<String, SimpleArrayMap<Integer, DefinitionModel>> RESOLVED_DEFINITIONS
            = new LruCache<>(10);


    @Subscribe(priority = PlayerConstants.PREPARE_START_PLAY_PRIORITY_RESOLVE_DEFINITION)
    public void onPrepareStartEvent(PrepareStartPlayEvent prepareStartPlayEvent){
        PlayData playData = prepareStartPlayEvent.getPlayData();
        int isChargeable = 0;
        if(playData.getCustomData(PlayerDataConstants.IS_CHARGEABLE) != null){
            isChargeable = playData.getCustomData(PlayerDataConstants.IS_CHARGEABLE);
        }
        boolean isMultiPosition = playData.getBooleanCustomData(PlayerDataConstants.MULTI_POSITION_TYPE);
        SimpleArrayMap<Integer, DefinitionModel> definitionMap = RESOLVED_DEFINITIONS.get(playData.getOrginUrl());
        if(definitionMap == null){
            definitionMap = new SimpleArrayMap<>();
            if(!isMultiPosition){
                LiveDetailsModel liveDetailsModel = playData.getCustomData(PlayerDataConstants.LIVE_INFO);
                sort(liveDetailsModel);
                for (LiveMediaModel liveMediaModel : liveDetailsModel.getLiveMediaDtos()){
                    final DefinitionModel definitionModel = createDefinitionModel(liveMediaModel);
                    addDefinitionMap(definitionMap,definitionModel,isChargeable);
                }
            }else {
                Map<String,CameraModel> cameraModelMap = playData.getCustomData(PlayerDataConstants.KEY_MULTI_POSITION_CAMERA);
                String currentCamera = playData.getCustomData(PlayerDataConstants.CURRENT_CAMERA);
                CameraModel currentCameraModel = cameraModelMap.get(currentCamera);
                List<DefinitionModel> definitionModelList=currentCameraModel.getDefinitionModelList();
                if(definitionModelList.size()>0){
                    for (final DefinitionModel definitionModel:definitionModelList){
                        addDefinitionMap(definitionMap,definitionModel,isChargeable);
                    }
                }else {
                    boolean isFootBall = playData.getBooleanCustomData(PlayerDataConstants.FOOTBALL_TYPE);
                    DefinitionModel definitionModel = createDefinitionModel(currentCameraModel,isFootBall);
                    addDefinitionMap(definitionMap,definitionModel,isChargeable);
                }
            }
            RESOLVED_DEFINITIONS.put(playData.getId(),definitionMap);
        }
        playData.putCustomData(PlayerDataConstants.RESOLVED_DEFINITION_MAP,definitionMap);
    }

    private void addDefinitionMap(SimpleArrayMap<Integer, DefinitionModel> definitionMap, final DefinitionModel definitionModel, int isChargeable){
        if(isChargeable == 1) {
            Router.getInstance().buildExecutor("/parser/service/decrypt").putObjParam(definitionModel.getUrl()).callback(new Executor.Callback<String>() {
                @Override
                public void onCall(String s) {
                    definitionModel.setUrl(s);
                }

                @Override
                public void onFail(Executor.ExecutionError executionError) {
                    //
                }
            }).excute();
        }
        definitionMap.put(definitionModel.getDefinition(),definitionModel);
    }

    private DefinitionModel createDefinitionModel(LiveMediaModel liveMediaModel){
        DefinitionModel definitionModel = new DefinitionModel();
        definitionModel.setUrl(liveMediaModel.getPlayUrl());
        definitionModel.setDefinition(ResolutionUtil.getVideoBitTypeFromResolution(liveMediaModel.getResolution()));
        definitionModel.setRenderType(RenderTypeUtil.getRenderType(liveMediaModel.getRenderType(), PlayerType.TYPE_LIVE,definitionModel.getDefinition()));
        return definitionModel;
    }

    private DefinitionModel createDefinitionModel(CameraModel cameraModel,boolean isFootBall){
        DefinitionModel definitionModel = new DefinitionModel();
        definitionModel.setUrl(cameraModel.getUrl());
        definitionModel.setDefinition(VideoBitType.HD);
        if(cameraModel.isPublic()||!isFootBall){
            definitionModel.setRenderType(RenderTypeUtil.getRenderTypeByRenderTypeStr(cameraModel.getServerRenderType()));
        }else {
            definitionModel.setRenderType(VideoConstantValue.MODE_HALF_SPHERE_VIP);
        }
        return definitionModel;
    }


    private void sort(LiveDetailsModel liveDetailsModel){
        Collections.sort(liveDetailsModel.getLiveMediaDtos(), new Comparator<LiveMediaModel>() {
            @Override
            public int compare(LiveMediaModel o1, LiveMediaModel o2) {
                if (o1 == null) {
                    if (o2 == null)
                        return 0;
                    else
                        return -1;
                } else {
                    if (o2 == null) {
                        return 1;
                    } else {
                        int o1VideoBitType = ResolutionUtil.getVideoBitTypeFromResolution(o1.getResolution());
                        int o2VideoBitType = ResolutionUtil.getVideoBitTypeFromResolution(o2.getResolution());
                        int value = Integer.compare(o1VideoBitType, o2VideoBitType);
                        return value;
                    }
                }
            }
        });
    }
}
