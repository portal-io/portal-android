package com.whaleyvr.biz.download;

import com.whaleyvr.biz.download.db.DownloadBean;
import com.whaleyvr.biz.download.db.DownloadDatabseManager;
import com.whaleyvr.biz.download.util.DownloadUtil;

import java.util.ArrayList;
import java.util.List;

public class DownloadDataManager implements DownloadStatus{

    private static volatile DownloadDataManager instance;

    private volatile List<DownloadBean> downloadBeanList;

    Object lock=new Object();

    private DownloadDataManager(){
        initAllDownloadDatas();
    }


    public static DownloadDataManager getInstance(){
        if(instance==null){
            synchronized (DownloadDataManager.class){
                if(instance==null){
                    instance=new DownloadDataManager();
                }
            }
        }
        return instance;
    }

    private void initAllDownloadDatas(){
        synchronized (lock) {
            downloadBeanList = DownloadDatabseManager.getInstance().queryAllList();
            checkState();
        }
    }

    private void checkState(){
        for (DownloadBean bean:downloadBeanList){
            int status=bean.getStatus();
            switch (status){
                case DOWNLOAD_STATUS_NOTSTARTED:
                case DOWNLOAD_STATUS_PREPARED:
                case DOWNLOAD_STATUS_DOWNLOADING:
                case DOWNLOAD_STATUS_ERROR:
                case DOWNLOAD_STATUS_PAUSE:
                    bean.setStatus(DOWNLOAD_STATUS_PAUSE);
                    updateNewDownload(bean);
                    break;
                case DOWNLOAD_STATUS_COMPLETED:
                    break;
                default:
                    break;
            }
        }
    }

    public List<DownloadBean> getDownloadBeanList(){
        synchronized (lock) {
            downloadBeanList = DownloadDatabseManager.getInstance().queryAllList();
            return downloadBeanList;
        }
    }

    public int getStatus(String itemId){
        synchronized (lock) {
            DownloadBean bean=new DownloadBean();
            bean.setItemid(itemId);
            if(downloadBeanList.contains(bean)){
                return downloadBeanList.get(downloadBeanList.indexOf(bean)).getStatus();
            }
            return DOWNLOAD_STATUS_NOTDOWNLOAD;
        }
    }

    public DownloadBean getDownloadBeanByID(String itemId){
        synchronized (lock) {
            DownloadBean bean=new DownloadBean();
            bean.setItemid(itemId);
            if(downloadBeanList.contains(bean)){
                return downloadBeanList.get(downloadBeanList.indexOf(bean));
            }
            return null;
        }
    }

    public void addNewDownload(DownloadBean bean){
        synchronized (lock) {
            bean.setStatus(DOWNLOAD_STATUS_PREPARED);
            DownloadDatabseManager.getInstance().insertOrReplace(bean);
            addOrUpdateBean(bean);
        }
    }

    private void addOrUpdateBean(DownloadBean bean){
        if(downloadBeanList.contains(bean)){
            int position=downloadBeanList.indexOf(bean);
            downloadBeanList.set(position,bean);
        }else {
            downloadBeanList.add(0, bean);
        }
    }

    public void updateNewDownload(DownloadBean bean){
        synchronized (lock) {
            DownloadDatabseManager.getInstance().insertOrReplace(bean);
            addOrUpdateBean(bean);
        }
    }

    public void removeDownload(DownloadBean bean){
        synchronized (lock) {
            DownloadDatabseManager.getInstance().delete(bean);
            if(downloadBeanList.contains(bean)){
                downloadBeanList.remove(bean);
            }
        }
    }

    public void removeDownloadList(List<DownloadBean> deletes){
        synchronized (lock) {
            DownloadDatabseManager.getInstance().deleteList(deletes);
            downloadBeanList = DownloadDatabseManager.getInstance().queryAllList();
        }
    }

    public boolean removeDownloadListByIds(List<String> ids){
        synchronized (lock) {
            ArrayList<String> deletes=new ArrayList<>();
            for (String id:ids){
                int status=getStatus(id);
                DownloadBean bean=getDownloadBeanByID(id);
                deletes.add(id);
                if(bean != null && !DownloadUtil.isEmpty(bean.getSavePath())){
                    DownloadUtil.deleteFile(bean.getSavePath()+".temp");
                    DownloadUtil.deleteFile(bean.getSavePath());
                }
            }
            boolean ret=DownloadDatabseManager.getInstance().deleteListByIds(deletes);
            if(ret)
                downloadBeanList = DownloadDatabseManager.getInstance().queryAllList();
            return ret;
        }
    }

}
