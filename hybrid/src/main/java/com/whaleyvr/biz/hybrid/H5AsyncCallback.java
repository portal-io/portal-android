package com.whaleyvr.biz.hybrid;

import com.whaley.core.debug.logger.Log;
import com.whaley.core.utils.GsonUtil;
import com.whaleyvr.biz.hybrid.model.CallbackPayload;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by YangZhi on 2016/10/27 11:10.
 */
public abstract class H5AsyncCallback<T> implements H5Callback<CallbackPayload<T>> {
    private JSComplete jsComplete;

    @Override
    public final Map<Object, Object> doPerform(CallbackPayload<T> payload) {

        Object data=payload.getData();
        data=getCallbackDataFromObj(this,data);
        doPerform((T) data,getJsComplete());
        return null;
    }


    /**
     * 将Map 对象化  获取对象模型
     * @param callback
     * @param data
     * @return
     */
    private Object getCallbackDataFromObj(H5Callback callback,Object data){
        Object object=null;
        try {
            String json= GsonUtil.getGson().toJson(data);

            Type type=null;
            if(callback instanceof H5AsyncCallback) {
                type=getAsyncH5CallbackDataType(callback.getClass());
                object=GsonUtil.getGson().fromJson(json,type);
            }
        }catch (Exception e){
            Log.e(e,"H5AsyncCallback getCallbackDataFromObj");
        }
        return object;
    }

    private Type getAsyncH5CallbackDataType(Class clazz) {
        Type type = clazz.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type actualType1 = ((ParameterizedType) type).getActualTypeArguments()[0];
            return actualType1;
        }
        return null;
    }



    public abstract void doPerform(T payload,JSComplete jsComplete);


    public void setJsComplete(JSComplete jsComplete) {
        this.jsComplete = jsComplete;
    }

    public JSComplete getJsComplete() {
        return jsComplete;
    }
}
