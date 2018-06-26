package com.whaley.biz.program.ui.search.presenter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.program.ui.search.SearchHistoryView;
import com.whaley.biz.program.ui.search.repository.SearchHistoryRepository;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.inject.annotation.Repository;

/**
 * Created by dell on 2017/8/24.
 */

public class SearchHistoryPresenter extends BasePagePresenter<SearchHistoryView> {

    private static final String KEY_SEARCH_HISTORY_SIZE = "pre_search_history_size";
    private static final String KEY_SEARCH_HISTORY = "key_search_history_";

    @Repository
    SearchHistoryRepository repository;

    public SearchHistoryPresenter(SearchHistoryView view) {
        super(view);
    }

    public SearchHistoryRepository getRepository(){
        return repository;
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        loadHistory();
        if(getUIView() != null){
            getUIView().onUpdate();
        }
    }

    private void loadHistory() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(AppContextProvider.getInstance().getContext());
        getRepository().getHistoryList().clear();
        int size = sp.getInt(KEY_SEARCH_HISTORY_SIZE, 0);
        for(int i=0;i<size;i++) {
            getRepository().getHistoryList().add(sp.getString(KEY_SEARCH_HISTORY + i, ""));
        }
    }

    private void clearHistory(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(AppContextProvider.getInstance().getContext());
        SharedPreferences.Editor mEdit = sp.edit();
        mEdit.remove(KEY_SEARCH_HISTORY_SIZE);
        for(int i=0;i<getRepository().getHistoryList().size();i++) {
            mEdit.remove(KEY_SEARCH_HISTORY + i);
        }
        mEdit.commit();
    }

    public void clear(){
        clearHistory();
        getRepository().getHistoryList().clear();
        if(getUIView() != null){
            getUIView().onUpdate();
        }
    }

}
