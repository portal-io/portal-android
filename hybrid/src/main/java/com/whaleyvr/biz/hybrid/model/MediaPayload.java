package com.whaleyvr.biz.hybrid.model;

import com.whaleyvr.biz.hybrid.model.player.DataBuilder;
import com.whaleyvr.biz.hybrid.model.player.PlayData;
import com.whaleyvr.biz.hybrid.router.ProgramConstants;

/**
 * Created by YangZhi on 2016/10/12 23:10.
 */
public class MediaPayload implements ProgramConstants{

    private MediaModel mediaModel;


    public MediaModel getMediaModel() {
        return mediaModel;
    }

    public void setMediaModel(MediaModel mediaModel) {
        this.mediaModel = mediaModel;
    }

    public static class MediaModel {
        private String mediaUrl;

        private String title;

        private Integer type;

        private String id;

        private String imgUrl;

        private long duration;

        private String resourceCode;

        public int getIsChargeable() {
            return isChargeable;
        }

        public void setIsChargeable(int isChargeable) {
            this.isChargeable = isChargeable;
        }

        private int isChargeable;

        private String price;

        private String renderType;

        public String getMediaUrl() {
            return mediaUrl;
        }

        public void setMediaUrl(String mediaUrl) {
            this.mediaUrl = mediaUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public long getDuration() {
            return duration;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }

        public String getResourceCode() {
            return resourceCode;
        }

        public void setResourceCode(String resourceCode) {
            this.resourceCode = resourceCode;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getRenderType() {
            return renderType;
        }

        public void setRenderType(String renderType) {
            this.renderType = renderType;
        }

        public PlayData getPlayData() {
            int type = TYPE_PLAY_PANO;
            if (VIDEO_TYPE_VR.equals(type)) {
                type = TYPE_PLAY_PANO;
            } else if (VIDEO_TYPE_3D.equals(type)) {
                type = TYPE_PLAY_3D;
            } else if (VIDEO_TYPE_MORETV_TV.equals(type)) {
                type = TYPE_PLAY_MORETV_TV;
            } else if (VIDEO_TYPE_MORETV_2D.equals(type)) {
                type = TYPE_PLAY_MORETV_2D;
            }else if (TYPE_LIVE.equals(type)) {
                type = TYPE_PLAY_LIVE;;
            } else {
                type = TYPE_PLAY_PANO;
            }
            return DataBuilder.createBuilder(mediaUrl, type)
                    .setId(id)
                    .setTitle(title)
                    .setMonocular(true)
                    .putCustomData("live_init_bg_img", imgUrl)
                    .build();
            }
    }

}
