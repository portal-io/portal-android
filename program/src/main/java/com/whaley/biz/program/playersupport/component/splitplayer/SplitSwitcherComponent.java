//package com.whaley.biz.program.playersupport.component.splitplayer;
//
//import com.whaley.biz.playerui.PlayerView;
//import com.whaley.biz.playerui.component.BaseComponent;
//import com.whaley.biz.playerui.component.BaseController;
//import com.whaley.biz.playerui.component.BaseUIAdapter;
//import com.whaley.biz.playerui.component.Component;
//
//import java.util.List;
//
///**
// * Created by dell on 2017/10/30.
// */
//
//public class SplitSwitcherComponent extends BaseComponent {
//
//    PlayerView playerView;
//    List<Component> components;
//    boolean isLive;
//
//    public SplitSwitcherComponent(PlayerView playerView, List<Component> components, boolean isLive){
//        this.playerView = playerView;
//        this.components = components;
//        this.isLive = isLive;
//    }
//
//    @Override
//    protected BaseController onCreateController() {
//        return new SplitSwitcherControl(playerView, components, isLive);
//    }
//
//    @Override
//    protected BaseUIAdapter onCreateUIAdapter() {
//        return null;
//    }
//
//}