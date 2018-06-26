package com.whaley.biz.launcher.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.ArrayMap;

import com.snailvr.manager.R;
import com.whaley.core.utils.StrUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by yangzhi on 16/10/22.
 */
public abstract class FragmentSwitchTabAdapter {

    private String mCurrentTag;

    private FragmentManager fm;

    private Map<String, Fragment.SavedState> savedStateMap;


    public FragmentSwitchTabAdapter(FragmentManager fm) {
        setFragmentManager(fm);
        savedStateMap = new ArrayMap<>();
    }


    public void setFragmentManager(FragmentManager fm) {
        this.fm = fm;
    }

    protected abstract Fragment createFragment(String tag);


    protected abstract void setArguments(String tag,Fragment fragment);


    public void switchPage(String tag){

        final String currentTag=mCurrentTag;

        Fragment newPage=fm.findFragmentByTag(tag);

        FragmentTransaction ft = null;

        if(!StrUtil.isEmpty(currentTag)) {
            if(currentTag.equals(tag)){
                repeatTagClick(newPage);
                return;
            }
            ft = fm.beginTransaction();
            Fragment lastPage = fm.findFragmentByTag(currentTag);
            if (lastPage!=null) {
                if(lastPage.isAdded()&&!lastPage.isHidden()) {
                    ft.hide(lastPage);
                }
//                savedStateMap.put(currentTag,fm.saveFragmentInstanceState(lastPage));
            }
        }
        if(ft == null){
            ft = fm.beginTransaction();
        }

        if(newPage==null){
            newPage=createFragment(tag);
//            Fragment.SavedState savedState=savedStateMap.get(tag);
//            if(savedState!=null){
//                newPage.setInitialSavedState(savedState);
//            }
            ft.add(R.id.content,newPage,tag);
        }else if(newPage.isAdded()&&newPage.isHidden()){
            setArguments(tag,newPage);
            ft.show(newPage);
        }

        mCurrentTag=tag;
        ft.commitNowAllowingStateLoss();

    }


    protected FragmentManager getFragmentManager(){
        return fm;
    }

    public void onTrimMemory(int level){

    }


    public void onSaveInstanceState(Bundle outState) {
        outState.putString("mCurrentTag", mCurrentTag);
    }


    public void onRestoreInstanceState(Bundle saveInstanceState) {
        if (saveInstanceState != null) {
            mCurrentTag = saveInstanceState.getString("mCurrentTag", "");
        }
    }

    public Fragment getCurrentFragment() {
        return fm.findFragmentByTag(mCurrentTag);
    }

    public String getmCurrentTag() {
            return mCurrentTag;
    }


    protected void repeatTagClick(Fragment fragment) {

    }
}
