package com.whaleyvr.biz.download;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.router.Executor;
import com.whaley.core.utils.GsonUtil;
import com.whaleyvr.biz.download.db.DownloadBean;
import com.whaleyvr.biz.download.event.DownloadEvent;
import com.whaleyvr.biz.download.event.DownloadTaskFinishEvent;
import com.whaleyvr.biz.download.util.DownloadUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

@Route(path = "/download/service/downloadservicemanager")
public class DownloadServiceManager implements ServiceManager, DownloadStatus, Executor {

    DownloadDataManager dataManager;

    LinkedBlockingQueue<IDownloadTask> taskQueue=new LinkedBlockingQueue<>();

    IDownloadTask currentTask;

    private long mLastClick = 0;
    private final static int MIN_CLICK_INTERVAL = 150;

    public final static int EVENT_ADD_DOWNLOAD = 1;
    public final static int EVENT_RESUME_DOWNLOAD = 2;
    public final static int EVENT_PAUSE_DOWNLOAD = 3;
    public final static int EVENT_REMOVE_DOWNLOAD = 4;

    @Override
    public void init() {
        dataManager= DownloadDataManager.getInstance();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onDesotry() {
        EventBus.getDefault().unregister(this);
    }

    private void executeNextTask(){
            currentTask = taskQueue.poll();
            if (currentTask != null) {
                currentTask.execute();
            }
    }

    @Subscribe(threadMode  = ThreadMode.MAIN)
    public void onEvent(DownloadTaskFinishEvent event){
        currentTask=null;
        executeNextTask();
    }

    public void addNewDownloadTask(DownloadBean bean){
        if(bean == null){
            return;
        } else if(currentTask!=null&&currentTask.equals(bean)){
            return;
        }else if (taskQueue.contains(bean)){
            return;
        }
        if(!DownloadUtil.checkAvailableSpace(bean)){
            return;
        }
        dataManager.addNewDownload(bean);
        bean.setStatus(DOWNLOAD_STATUS_PREPARED);
        DownloadDataManager.getInstance().updateNewDownload(bean);
        EventBus.getDefault().post(new DownloadEvent(bean));
        DownloadTask task=new DownloadTask(bean);
        taskQueue.add(task);
        if(currentTask==null){
            executeNextTask();
        }
    }


    public boolean resumeDownloadTask(String itemId){
        DownloadBean bean =dataManager.getDownloadBeanByID(itemId);
        if (bean!=null){
            if(!DownloadUtil.checkAvailableSpace(bean)){
                return false;
            }
            if(currentTask!=null&&currentTask.equals(bean)){
                return false;
            }else if (taskQueue.contains(bean)){
                return false;
            }
            bean.setStatus(DOWNLOAD_STATUS_PREPARED);
            DownloadDataManager.getInstance().updateNewDownload(bean);
            EventBus.getDefault().post(new DownloadEvent(bean));
            DownloadTask task=new DownloadTask(bean);
            taskQueue.add(task);
            if(currentTask==null){
                executeNextTask();
            }
            return true;
        }else {
            return false;
        }
    }


    public boolean pauseDownloadTask(String itemId){
        boolean ret = false;
        if(currentTask!=null){
            if(currentTask.equals(itemId)){
                currentTask.cancel();
                currentTask = null;
                ret=true;
            }
        }
        Iterator<IDownloadTask> iterator = taskQueue.iterator();
            while (iterator.hasNext()) {
                IDownloadTask task = iterator.next();
                if (task.equals(itemId)) {
                    task.cancel();
                    taskQueue.remove(task);
                    ret = true;
                }
            }
        DownloadBean bean=dataManager.getDownloadBeanByID(itemId);
        bean.setStatus(DOWNLOAD_STATUS_PAUSE);
        dataManager.updateNewDownload(bean);
        EventBus.getDefault().post(new DownloadEvent(bean));
        return ret;
    }


    public boolean removeDownloadTask(List<String> itemIds){
            for (String itemId:itemIds){
                Iterator<IDownloadTask> iterator=taskQueue.iterator();
                while (iterator.hasNext()) {
                    IDownloadTask task = iterator.next();
                    if (task.equals(itemId)) {
                        task.cancel();
                        taskQueue.remove(task);
                    }
                }
                if(currentTask!=null){
                    if(currentTask.equals(itemId)){
                        currentTask.cancel();
                        currentTask = null;
                    }
                }
            }
        boolean ret=dataManager.removeDownloadListByIds(itemIds);
        return ret;
    }

    @Override
    public void excute(Object o, Callback callback) {
        if (System.currentTimeMillis() - mLastClick > MIN_CLICK_INTERVAL) {
            mLastClick = System.currentTimeMillis();
            Map<String, Object> param = (Map<String, Object>) o;
            int eventId = (int) param.get("eventId");
            switch (eventId) {
                case EVENT_ADD_DOWNLOAD:
                    DownloadBean downloadBean = GsonUtil.getGson().fromJson((String) param.get("object"), DownloadBean.class);
                    addNewDownloadTask(downloadBean);
                    break;
                case EVENT_RESUME_DOWNLOAD:
                    String itemId = (String) param.get("object");
                    resumeDownloadTask(itemId);
                    break;
                case EVENT_PAUSE_DOWNLOAD:
                    itemId = (String) param.get("object");
                    pauseDownloadTask(itemId);
                    break;
                case EVENT_REMOVE_DOWNLOAD:
                    List<String> itemIds = (List<String>) param.get("object");
                    removeDownloadTask(itemIds);
                    break;
            }
        }
    }

    @Override
    public void init(Context context) {

    }
}
