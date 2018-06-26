//package com.whaley.biz.program.utils;
//
//import android.content.Context;
//import android.os.Bundle;
//
//import com.alibaba.android.arouter.facade.Postcard;
//import com.alibaba.android.arouter.facade.annotation.Interceptor;
//import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
//import com.alibaba.android.arouter.facade.enums.RouteType;
//import com.alibaba.android.arouter.facade.template.IInterceptor;
//import com.whaley.core.router.Router;
//
///**
// * Created by yangzhi on 2017/8/15.
// */
//
//@Interceptor(priority = 5)
//public class ProgramInterceptor implements IInterceptor{
//
//    Context context;
//
//    @Override
//    public void init(Context context) {
//        this.context = context;
//    }
//
//    @Override
//    public void process(Postcard postcard, InterceptorCallback callback) {
//        RouteType routeType = postcard.getType();
//        switch (routeType){
//            case FRAGMENT:
//                Bundle bundle = postcard.getExtras();
//                bundle.getInt("");
//                Router.getInstance().buildNavigation()
//                        .setTag()
//                break;
//        }
//        postcard.getExtras();
//    }
//}
