package com.whaley.biz.program.ui.search.presenter;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.program.ui.search.SearchView;
import com.whaley.core.appcontext.AppContextProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/8/24.
 */

public class SearchPresenter extends BasePagePresenter<SearchView> {

    private static final String KEY_SEARCH_HISTORY_SIZE = "pre_search_history_size";
    private static final String KEY_SEARCH_HISTORY = "key_search_history_";

    public SearchPresenter(SearchView view) {
        super(view);
    }

    public void searchHistory(String str) {
        search(str);
    }

    public void onSearchClick(String str){
        search(str);
    }

    public void search(String str) {
        saveHistory(str);
        if (getUIView() != null) {
            getUIView().onSearch(str);
        }
    }

    private void saveHistory(String str) {
        List<String> historyList = new ArrayList<String>();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(AppContextProvider.getInstance().getContext());
        SharedPreferences.Editor mEdit = sp.edit();
        int size = sp.getInt(KEY_SEARCH_HISTORY_SIZE, 0);
        for (int i = 0; i < size; i++) {
            historyList.add(sp.getString(KEY_SEARCH_HISTORY + i, ""));
        }
        int index = historyList.indexOf(str);
        if (index > 0) {
            historyList.remove(index);
        } else if (index == 0) {
            return;
        }
        if (historyList.size() < 10) {
            historyList.add(0, str);
        } else {
            historyList.remove(historyList.size() - 1);
            historyList.add(0, str);
        }
        mEdit.putInt(KEY_SEARCH_HISTORY_SIZE, historyList.size());
        for (int i = 0; i < historyList.size(); i++) {
            mEdit.remove(KEY_SEARCH_HISTORY + i);
            mEdit.putString(KEY_SEARCH_HISTORY + i, historyList.get(i));
        }
        mEdit.commit();
    }

}
