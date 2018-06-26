package com.whaley.biz.common.model.bi;

import com.google.gson.reflect.TypeToken;
import com.whaley.core.utils.GsonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: qxw
 * Date:2017/9/22
 * Introduction:
 */

public class LogInfoParam {

    final String currentPageId;
    final Map<String, Object> currentPageProp;
    final String eventId;
    final Map<String, Object> eventProp;
    final String nextPageId;


    public static Builder createBuilder() {
        return new Builder();
    }

    public LogInfoParam(Builder builder) {
        this.currentPageId = builder.currentPageId;
        this.currentPageProp = builder.currentPageProp;
        this.eventId = builder.eventId;
        this.eventProp = builder.eventProp;
        this.nextPageId = builder.nextPageId;
    }


    public static class Builder {
        String currentPageId;
        Map<String, Object> currentPageProp;
        String eventId;
        Map<String, Object> eventProp;
        String nextPageId;

        public String getCurrentPageId() {
            return currentPageId;
        }


        public Builder putCurrentPagePropKeyValue(String key, Object value) {
            checkCurrentPageProp();
            currentPageProp.put(key, value);
            return this;
        }

        public Builder setCurrentPageProp(Object object) {
            String json = GsonUtil.getGson().toJson(object);
            Map<String, Object> map = GsonUtil.getGson().fromJson(json, new TypeToken<Map<String, Object>>() {
            }.getType());
            currentPageProp = map;
            return this;
        }

        private Builder checkCurrentPageProp() {
            if (currentPageProp == null)
                currentPageProp = new HashMap<>();
            return this;
        }

        public Builder setCurrentPageId(String currentPageId) {
            this.currentPageId = currentPageId;
            return this;
        }

        public Builder setEventId(String eventId) {
            this.eventId = eventId;
            return this;
        }


        public Builder putEventPropKeyValue(String key, Object value) {
            checkEventProp();
            eventProp.put(key, value);
            return this;
        }

        public Builder setEventProp(Object object) {
            String json = GsonUtil.getGson().toJson(object);
            Map<String, Object> map = GsonUtil.getGson().fromJson(json, new TypeToken<Map<String, Object>>() {
            }.getType());
            eventProp = map;
            return this;
        }

        private Builder checkEventProp() {
            if (eventProp == null) {
                eventProp = new HashMap<>();
            }
            return this;
        }

        public Builder setNextPageId(String nextPageId) {
            this.nextPageId = nextPageId;
            return this;
        }

        public LogInfoParam build() {
            return new LogInfoParam(this);
        }

        @Override
        public String toString() {
            return "Builder{" +
                    "currentPageId='" + currentPageId + '\'' +
                    ", currentPageProp=" + currentPageProp +
                    ", eventId='" + eventId + '\'' +
                    ", eventProp=" + eventProp +
                    ", nextPageId='" + nextPageId + '\'' +
                    '}';
        }
    }
}
