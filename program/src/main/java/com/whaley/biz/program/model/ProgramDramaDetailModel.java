package com.whaley.biz.program.model;

import com.whaley.biz.program.constants.VideoType;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dell on 2017/11/14.
 */

public class ProgramDramaDetailModel extends BaseModel implements Serializable {
    /**
     * stat : {"playCount":350,"viewCount":299}
     * code : aaabbb
     * centerPic : 中心底图url
     * displayName : 互动剧-测试
     * videoType : 3D
     * superscript : 角标图url
     * typeName : 美女故事
     * description : 描述信息
     * contentPrivider : {"cpCode":"defaultCP","isFollow":1,"name":"发布者名字","fansCount":2552,"headPic":"发布者图像url"}
     * tags : 美女-喜剧
     * isChargeable : 0
     * score : 9
     * nodes : [{"childrenCode":"subcode3-subcode4","cCode":"subcode1","mediaDtos":[{"isAnalysis":1,"source":"vr","renderType":"360_2D_OCTAHEDRAL","resolution":"4k","playUrl":"http://vr.moguv.com/play/5178fb6082e04b94940d5655a348bdc9&flag=.ganalyze","status":1},{"isAnalysis":1,"source":"vr_share","renderType":"RENDER_TYPE_360_2D","resolution":"2k","playUrl":"http://vr.moguv.com/play/5178fb6082e04b94940d5655a348bdc9&flag=.ganalyze","status":1}],"defaultVisible":1,"xyz":{"x":"1.25","y":"3.75","z":"0.80"},"tipTime":1000,"tip":"请选择...","position":1,"smallPic":"小图","defaultItem":"subcode3","title":"子剧1"},{"childrenCode":null,"cCode":"subcode2","mediaDtos":[{"isAnalysis":1,"source":"vr","renderType":"360_2D_OCTAHEDRAL","resolution":"4k","playUrl":"http://vr.moguv.com/play/5178fb6082e04b94940d5655a348bdc9&flag=.ganalyze","status":1},{"isAnalysis":1,"source":"vr_share","renderType":"RENDER_TYPE_360_2D","resolution":"2k","playUrl":"http://vr.moguv.com/play/5178fb6082e04b94940d5655a348bdc9&flag=.ganalyze","status":1}],"defaultVisible":null,"xyz":{"x":"1.25","y":"3.75","z":"0.80"},"tipTime":null,"tip":"请选择...","position":2,"smallPic":"小图","defaultItem":null,"title":"子剧2"},{"childrenCode":null,"cCode":"subcode3","mediaDtos":[{"isAnalysis":1,"source":"vr","renderType":"360_2D_OCTAHEDRAL","resolution":"4k","playUrl":"http://vr.moguv.com/play/5178fb6082e04b94940d5655a348bdc9&flag=.ganalyze","status":1},{"isAnalysis":1,"source":"vr_share","renderType":"RENDER_TYPE_360_2D","resolution":"2k","playUrl":"http://vr.moguv.com/play/5178fb6082e04b94940d5655a348bdc9&flag=.ganalyze","status":1}],"defaultVisible":null,"xyz":{"x":"1.25","y":"3.75","z":"0.80"},"tipTime":null,"tip":"请选择...","position":1,"smallPic":"小图","defaultItem":null,"title":"子剧3"},{"childrenCode":null,"cCode":"subcode4","mediaDtos":[{"isAnalysis":1,"source":"vr","renderType":"360_2D_OCTAHEDRAL","resolution":"4k","playUrl":"http://vr.moguv.com/play/5178fb6082e04b94940d5655a348bdc9&flag=.ganalyze","status":1},{"isAnalysis":1,"source":"vr_share","renderType":"RENDER_TYPE_360_2D","resolution":"2k","playUrl":"http://vr.moguv.com/play/5178fb6082e04b94940d5655a348bdc9&flag=.ganalyze","status":1}],"defaultVisible":null,"xyz":{"x":"1.25","y":"3.75","z":"0.80"},"tipTime":null,"tip":"请选择...","position":2,"smallPic":"小图","defaultItem":null,"title":"子剧4"}]
     * price : 99
     * startNode : {"childrenCode":"subcode1-subcode2","cCode":"subcode0","mediaDtos":[{"isAnalysis":1,"source":"vr","renderType":"360_2D_OCTAHEDRAL","resolution":"4k","playUrl":"http://vr.moguv.com/play/5178fb6082e04b94940d5655a348bdc9&flag=.ganalyze","status":1},{"isAnalysis":1,"source":"vr_share","renderType":"RENDER_TYPE_360_2D","resolution":"2k","playUrl":"http://vr.moguv.com/play/5178fb6082e04b94940d5655a348bdc9&flag=.ganalyze","status":1}],"defaultVisible":1,"xyz":{"x":"1.25","y":"3.75","z":"0.80"},"tipTime":1200,"tip":"请选择...","position":1,"smallPic":"小图","defaultItem":"subcode1","title":"子剧0"}
     * subtitle : 互动剧副标题
     * bigPic : 详情图
     * smallPic : 列表图url
     * interactMode : 0
     * radius : 0.5
     * status : 1
     */

    private StatQueryModel stat;
    private String code;
    private String centerPic;
    private String displayName;
    private String videoType;
    private String superscript;
    private String typeName;
    private String description;
    private String type;
    private ContentPrividerModel contentProvider;
    private String tags;
    private int isChargeable;
    private String score;
    private String price;
    private NodeModel startNode;
    private String subtitle;
    private String bigPic;
    private String smallPic;
    private int interactMode;
    private String radius;
    private int status;
    private int duration;
    private List<NodeModel> nodes;
    private int appInteractMode;

    public String getVideoFormat(){
        if(videoType!=null&&(videoType.equals(VideoType.VIDEO_TYPE_MORETV_2D)||videoType.equals(VideoType.VIDEO_TYPE_MORETV_TV))){
            return "2d";
        }
        return videoType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCenterPic() {
        return centerPic;
    }

    public void setCenterPic(String centerPic) {
        this.centerPic = centerPic;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getSuperscript() {
        return superscript;
    }

    public void setSuperscript(String superscript) {
        this.superscript = superscript;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getIsChargeable() {
        return isChargeable;
    }

    public void setIsChargeable(int isChargeable) {
        this.isChargeable = isChargeable;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getBigPic() {
        return bigPic;
    }

    public void setBigPic(String bigPic) {
        this.bigPic = bigPic;
    }

    public String getSmallPic() {
        return smallPic;
    }

    public void setSmallPic(String smallPic) {
        this.smallPic = smallPic;
    }

    public int getInteractMode() {
        return interactMode;
    }

    public void setInteractMode(int interactMode) {
        this.interactMode = interactMode;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public StatQueryModel getStat() {
        return stat;
    }

    public void setStat(StatQueryModel stat) {
        this.stat = stat;
    }

    public ContentPrividerModel getContentPrivider() {
        return contentProvider;
    }

    public void setContentPrivider(ContentPrividerModel contentPrivider) {
        this.contentProvider = contentPrivider;
    }

    public NodeModel getStartNode() {
        return startNode;
    }

    public void setStartNode(NodeModel startNode) {
        this.startNode = startNode;
    }

    public List<NodeModel> getNodes() {
        return nodes;
    }

    public void setNodes(List<NodeModel> nodes) {
        this.nodes = nodes;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getAppInteractMode() {
        return appInteractMode;
    }

    public void setAppInteractMode(int appInteractMode) {
        this.appInteractMode = appInteractMode;
    }
}
