package com.whaley.biz.program.playersupport.component.normalplayer.tv;

import com.whaley.biz.common.interactor.observer.ErrorHandleObserver;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.PrepareStartPlayEvent;
import com.whaley.biz.playerui.exception.PlayerException;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerConstants;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.constants.PlayerType;
import com.whaley.biz.program.interactor.GetDetailInfo;
import com.whaley.biz.program.model.ProgramDetailModel;
import com.whaley.biz.program.model.SeriesModel;
import com.whaley.biz.program.playersupport.event.ChangeSerieEvent;
import com.whaley.biz.program.model.response.ProgramDetailResponse;
import com.whaley.biz.program.playersupport.exception.ProgramErrorConstants;
import com.whaley.core.repository.RepositoryManager;
import com.whaley.core.utils.StrUtil;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by YangZhi on 2017/8/23 16:59.
 */

public class TVController extends BaseController {

    // 详情模型
    private ProgramDetailModel detailModel;

    // 剧集模型
    private ProgramDetailModel seriesModel;

    // 最终的详情和剧集组装模型
    private ProgramDetailModel finalModel;

    GetDetailInfo getDetailInfo;

    private Disposable disposable;

    // 上一次选择的剧集
    private SeriesModel lastSelectedModel;

    public TVController() {
        getDetailInfo = new GetDetailInfo();
        getDetailInfo.setRepositoryManager(RepositoryManager.create(null));
    }

    @Subscribe(priority = PlayerConstants.PREPARE_START_PLAY_PRIORITY_TV)
    public void onPrepareStartPlay(PrepareStartPlayEvent prepareStartPlayEvent){
        if(prepareStartPlayEvent.getMaxPriority()<PlayerConstants.PREPARE_START_PLAY_PRIORITY_TV){
            return;
        }
        PlayData playData = prepareStartPlayEvent.getPlayData();
        // 获取播放集数
        int serieIndex = playData.getIntegerCustomData(PlayerDataConstants.TV_SERIES_INDEX);
        if(serieIndex == 0){
            serieIndex = 1;
        }
        // 如果已存在最终的组装模型 并且 和需要播放的集数一致 则不再获取数据
        if (finalModel != null && finalModel.getCurEpisode() == serieIndex)
            return;
        ProgramDetailModel programDetailModel = playData.getCustomData(PlayerDataConstants.PROGRAM_INFO);
        if (playData.getType() == PlayerType.TYPE_MORETV_TV) {
            String code;
            if (isSeries(programDetailModel)||seriesModel!=null) {
                // 剧集
                int index = serieIndex-1;
                if(index>programDetailModel.getSeries().size()-1||programDetailModel.getSeries().get(index).getCurEpisode()!=serieIndex){
                    index = 0;
                }
                code = programDetailModel.getSeries().get(index).getCode();
                seriesModel = programDetailModel;
            } else {
                // 单集
                code = programDetailModel.getParentCode();
                detailModel = programDetailModel;
            }
            getDetailInfo(code);
            getEventBus().cancelEventDelivery(prepareStartPlayEvent);
        }

    }


    @Subscribe
    public void onChangeSerieEvent(ChangeSerieEvent changeSerieEvent){
        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
        SeriesModel seriesModel = changeSerieEvent.isChangeToNext()? (SeriesModel)playData.getCustomData(PlayerDataConstants.TV_NEXT_PLAY_SERIES):finalModel.getSeries().get(changeSerieEvent.getSerie());
        int seriesIndex = seriesModel.getCurEpisode();
        playData.putCustomData(PlayerDataConstants.TV_SERIES_INDEX, seriesIndex);
        playData.setTitle(seriesModel.getDisplayName());
        playData.setId(seriesModel.getCode());
        getPlayerController().reStartPlay(true);
    }

    private boolean isSeries(ProgramDetailModel programDetailModel) {
        return programDetailModel.getSeries() != null || StrUtil.isEmpty(programDetailModel.getParentCode());
    }

    private void getDetailInfo(String code) {
        GetDetailInfo.Param param = new GetDetailInfo.Param();
        param.setCode(code);
        dispose();
        disposable = getDetailInfo.buildUseCaseObservable(param)
                .doOnNext(new Consumer<ProgramDetailResponse>() {
                    @Override
                    public void accept(@NonNull ProgramDetailResponse programDetailResponse) throws Exception {
                        ProgramDetailModel programDetailModel = programDetailResponse.getData();
                        if (isSeries(programDetailModel)) {
                            seriesModel = programDetailModel;
                        } else {
                            detailModel = programDetailModel;
                        }
                    }
                })
                .map(new Function<ProgramDetailResponse, ProgramDetailModel>() {
                    @Override
                    public ProgramDetailModel apply(@NonNull ProgramDetailResponse programDetailResponse) throws Exception {
                        // 必须剧集模型和详情模型都获取成功才算获取电视剧数据成功
                        if (seriesModel == null || detailModel == null) {
                            throw new PlayerException(0, "获取剧集数据错误");
                        } else {
                            // 组装最终的详情和剧集模型
                            ProgramDetailModel finalProgramDetailModel = new ProgramDetailModel();
                            finalProgramDetailModel.setCode(detailModel.getCode());
                            finalProgramDetailModel.setParentCode(detailModel.getParentCode());
                            finalProgramDetailModel.setActors(detailModel.getActors());
                            finalProgramDetailModel.setArea(detailModel.getArea());
                            finalProgramDetailModel.setCurEpisode(detailModel.getCurEpisode());
                            String description = !StrUtil.isEmpty(detailModel.getDescription()) ? detailModel.getDescription() : seriesModel.getDescription();
                            finalProgramDetailModel.setDescription(description);
                            finalProgramDetailModel.setDescription(detailModel.getDescription());
                            finalProgramDetailModel.setDirector(detailModel.getDirector());
                            finalProgramDetailModel.setDisplayName(detailModel.getDisplayName());
                            finalProgramDetailModel.setDuration(detailModel.getDuration());
                            finalProgramDetailModel.setScore(detailModel.getScore());
                            finalProgramDetailModel.setStatus(detailModel.getStatus());
                            int totalEpiscode = detailModel.getTotalEpisode()>0?detailModel.getTotalEpisode():seriesModel.getTotalEpisode();
                            finalProgramDetailModel.setTotalEpisode(totalEpiscode);
                            finalProgramDetailModel.setType(detailModel.getType());
                            finalProgramDetailModel.setVideoType(detailModel.getVideoType());
                            finalProgramDetailModel.setMedias(detailModel.getMedias());
                            finalProgramDetailModel.setStat(detailModel.getStat());
                            finalProgramDetailModel.setLunboPic(seriesModel.getLunboPic());

                            List<SeriesModel> seriesModels = new ArrayList<SeriesModel>();

                            Iterator<SeriesModel> iterator = seriesModel.getSeries().iterator();

                            SeriesModel nextSeriesModel = iterator.next();

                            if(lastSelectedModel!=null){
                                lastSelectedModel.setSelected(false);
                            }


                            // 遍历总集数 组装 UI 数据 判断哪些集数是不存在的 哪些集数是可以播的
                            SeriesModel nextPlaySeriesModel = null;
                            for (int i = 1, j = finalProgramDetailModel.getTotalEpisode(); i <= j; i++) {
                                if (nextSeriesModel == null || nextSeriesModel.getCurEpisode() > i) {
                                    SeriesModel model = new SeriesModel();
                                    model.setReal(false);
                                    model.setCurEpisode(i);
                                    seriesModels.add(model);
                                    continue;
                                }
                                int curEpisode = nextSeriesModel.getCurEpisode();
                                if(curEpisode == finalProgramDetailModel.getCurEpisode()){
                                    nextSeriesModel.setSelected(true);
                                    lastSelectedModel = nextSeriesModel;
                                }else if(curEpisode > finalProgramDetailModel.getCurEpisode() && nextPlaySeriesModel == null){
                                    nextPlaySeriesModel = nextSeriesModel;
                                }
                                seriesModels.add(nextSeriesModel);
                                if(iterator.hasNext()) {
                                    nextSeriesModel = iterator.next();
                                }else {
                                    nextSeriesModel = null;
                                }
                            }


                            finalProgramDetailModel.setSeries(seriesModels);
                            PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
                            playData.setId(finalProgramDetailModel.getCode());
                            playData.putCustomData(PlayerDataConstants.PROGRAM_INFO, finalProgramDetailModel);
                            playData.putCustomData(PlayerDataConstants.TV_NEXT_PLAY_SERIES,nextPlaySeriesModel);
                            playData.setTitle(lastSelectedModel.getDisplayName());
                            return finalProgramDetailModel;

                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ErrorHandleObserver<ProgramDetailModel>() {
                    @Override
                    public void onFinalError(Throwable e) {
                        getPlayerController().setError(new PlayerException(ProgramErrorConstants.STR_ERROR_TV_SERIES, "获取剧集数据错误"));
                    }

                    @Override
                    public void onStatusError(int status, String message) {
                        getPlayerController().setError(new PlayerException(ProgramErrorConstants.STR_ERROR_TV_SERIES, "获取剧集数据错误"));
                    }

                    @Override
                    public void onNoDataError() {
                        getPlayerController().setError(new PlayerException(ProgramErrorConstants.STR_ERROR_TV_SERIES, "获取剧集数据错误"));
                    }

                    @Override
                    public void onNext(@NonNull ProgramDetailModel programDetailModel) {
                        finalModel = programDetailModel;

                        PrepareStartPlayEvent prepareStartPlayEvent = new PrepareStartPlayEvent();
                        prepareStartPlayEvent.setPlayData(getPlayerController().getRepository().getCurrentPlayData());
                        prepareStartPlayEvent.setMaxPriority(PlayerConstants.PREPARE_START_PLAY_PRIORITY_TV-1);
                        emitEvent(prepareStartPlayEvent);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private void dispose() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }

    @Override
    protected void onDispose() {
        super.onDispose();
        dispose();
    }

    @Override
    protected void onDestory() {
        super.onDestory();
        onDispose();
    }
}
