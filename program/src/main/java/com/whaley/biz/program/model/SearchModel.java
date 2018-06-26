package com.whaley.biz.program.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.whaley.biz.playerui.model.DataBuilder;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.ProgramConstants;

import java.util.List;

/**
 * Created by dell on 2017/8/25.
 */

public class SearchModel implements Parcelable, ProgramConstants {
    private int status;
    private String message;
    private String useTime;
    private List<ArrangeBean> arrange;
    /**
     * code : 94934a155158419a90e12279016c3055
     * display_name : 测试 1101-1
     * type : MOVIE
     * director : 7
     * actors : 5
     * tags : 精彩动作
     * area : 10
     * big_pic : http://test-image.tvmore.com.cn/image/get-image/10000004/14779872283776586421.jpg
     * description : 11
     * subtitle : 3
     */

    private List<ProgramBean> program;

    protected SearchModel(Parcel in) {
        status = in.readInt();
        message = in.readString();
        useTime = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(status);
        dest.writeString(message);
        dest.writeString(useTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SearchModel> CREATOR = new Creator<SearchModel>() {
        @Override
        public SearchModel createFromParcel(Parcel in) {
            return new SearchModel(in);
        }

        @Override
        public SearchModel[] newArray(int size) {
            return new SearchModel[size];
        }
    };

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public List<ArrangeBean> getArrange() {
        return arrange;
    }

    public void setArrange(List<ArrangeBean> arrange) {
        this.arrange = arrange;
    }

    public List<ProgramBean> getProgram() {
        return program;
    }

    public void setProgram(List<ProgramBean> program) {
        this.program = program;
    }

    public static class ArrangeBean implements Parcelable{
        private String code;
        private String name;
        private String big_image_url;
        private String is_leaf;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBig_image_url() {
            return big_image_url;
        }

        public void setBig_image_url(String big_image_url) {
            this.big_image_url = big_image_url;
        }

        public String getIs_leaf() {
            return is_leaf;
        }

        public void setIs_leaf(String is_leaf) {
            this.is_leaf = is_leaf;
        }

        protected ArrangeBean(Parcel in) {
            code = in.readString();
            name = in.readString();
            big_image_url = in.readString();
            is_leaf = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(code);
            dest.writeString(name);
            dest.writeString(big_image_url);
            dest.writeString(is_leaf);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<ArrangeBean> CREATOR = new Creator<ArrangeBean>() {
            @Override
            public ArrangeBean createFromParcel(Parcel in) {
                return new ArrangeBean(in);
            }

            @Override
            public ArrangeBean[] newArray(int size) {
                return new ArrangeBean[size];
            }
        };

    }

    public static class ProgramBean implements Parcelable{
        private String code;
        private String display_name;
        private String type;
        private String director;
        private String actors;
        private String tags;
        private String area;
        private String big_pic;
        private String video_type;
        private String small_pic;
        private String description;
        private String subtitle;
        private String program_type;

        public String getVideo_type() {
            return video_type;
        }

        public void setVideo_type(String video_type) {
            this.video_type = video_type;
        }

        public String getSmall_pic() {
            return small_pic;
        }

        public void setSmall_pic(String small_pic) {
            this.small_pic = small_pic;
        }

        protected ProgramBean(Parcel in) {
            code = in.readString();
            display_name = in.readString();
            type = in.readString();
            director = in.readString();
            actors = in.readString();
            tags = in.readString();
            area = in.readString();
            big_pic = in.readString();
            description = in.readString();
            subtitle = in.readString();
            small_pic = in.readString();
            video_type = in.readString();
            program_type = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(code);
            dest.writeString(display_name);
            dest.writeString(type);
            dest.writeString(director);
            dest.writeString(actors);
            dest.writeString(tags);
            dest.writeString(area);
            dest.writeString(big_pic);
            dest.writeString(description);
            dest.writeString(subtitle);
            dest.writeString(small_pic);
            dest.writeString(video_type);
            dest.writeString(program_type);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<ProgramBean> CREATOR = new Creator<ProgramBean>() {
            @Override
            public ProgramBean createFromParcel(Parcel in) {
                return new ProgramBean(in);
            }

            @Override
            public ProgramBean[] newArray(int size) {
                return new ProgramBean[size];
            }
        };

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDisplay_name() {
            return display_name;
        }

        public void setDisplay_name(String display_name) {
            this.display_name = display_name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDirector() {
            return director;
        }

        public void setDirector(String director) {
            this.director = director;
        }

        public String getActors() {
            return actors;
        }

        public void setActors(String actors) {
            this.actors = actors;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getBig_pic() {
            return big_pic;
        }

        public void setBig_pic(String big_pic) {
            this.big_pic = big_pic;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public PlayData getPlayData() {
            int type;
            if (VIDEO_TYPE_VR.equals(video_type)) {
                type = TYPE_PLAY_PANO;
            } else if (VIDEO_TYPE_3D.equals(video_type)) {
                type = TYPE_PLAY_3D;
            } else if (VIDEO_TYPE_MORETV_TV.equals(video_type)) {
                type = TYPE_PLAY_MORETV_TV;
            } else if (VIDEO_TYPE_MORETV_2D.equals(video_type)) {
                type = TYPE_PLAY_MORETV_2D;
            } else {
                type = TYPE_PLAY_PANO;
            }
            return DataBuilder.createBuilder("", type)
                    .setId(code)
                    .setTitle(display_name)
                    .setMonocular(true)
                    .build();
        }

        public String getProgram_type() {
            return program_type;
        }

        public void setProgram_type(String program_type) {
            this.program_type = program_type;
        }
    }

}
