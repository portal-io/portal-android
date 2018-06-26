package com.whaley.biz.program.ui.detail.viewholder;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.widget.JustifyTextView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

import butterknife.BindView;

public class MovieViewHolder extends ProgramViewHolder<MovieViewHolder.MovieProgramModel> {
    @BindView(R2.id.tv_program_name)
    TextView tvProgramName;
    @BindView(R2.id.iv_play_icon)
    ImageView ivPlayIcon;
    @BindView(R2.id.tv_play_count)
    TextView tvPlayCount;
    @BindView(R2.id.layout_tag)
    TagFlowLayout layoutTag;
    @BindView(R2.id.tv_rate)
    TextView tvRate;
    @BindView(R2.id.rb_rate)
    RatingBar rbRate;
    @BindView(R2.id.layout_program_title)
    RelativeLayout layoutProgramTitle;
    @BindView(R2.id.view_line)
    View viewLine;
    @BindView(R2.id.tv_district_title)
    TextView tvDistrictTitle;
    @BindView(R2.id.tv_district)
    TextView tvDistrict;
    @BindView(R2.id.tv_year_title)
    TextView tvYearTitle;
    @BindView(R2.id.tv_year)
    TextView tvYear;
    @BindView(R2.id.tv_director_title)
    TextView tvDirectorTitle;
    @BindView(R2.id.tv_director)
    TextView tvDirector;
    @BindView(R2.id.tv_actor_title)
    TextView tvActorTitle;
    @BindView(R2.id.tv_actor)
    TextView tvActor;
    @BindView(R2.id.tv_description)
    TextView tvDescription;
    @BindView(R2.id.layout_program_des)
    RelativeLayout layoutProgramDes;
    @BindView(R2.id.view_line2)
    View viewLine2;

    TagAdapter tagAdapter;

    public MovieViewHolder(View view) {
        super(view);
        layoutDownlod.getChildAt(0).setEnabled(false);
        tvDescription.setMovementMethod(ScrollingMovementMethod.getInstance());
    }


    @Override
    protected void onBindData(MovieProgramModel data) {
        tvProgramName.setText(data.getProgramName());
        tvDescription.setText(data.getDescription());
        tagAdapter = new TagAdapter<String>(data.getTags()) {
            @Override
            public View getView(FlowLayout parent, int position, String o) {
                TextView tv = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tag,
                        parent, false);
                tv.setText(o);
                return tv;
            }
        };
        layoutTag.setAdapter(tagAdapter);
        tvPlayCount.setText(data.getPlayCount());
        tvRate.setText(data.getRateStr());
        rbRate.setRating(data.getRate());
        tvYear.setText(data.getYear());
        tvDistrict.setText(data.getDistrict());
        tvActor.setText(data.getActor());
        tvDirector.setText(data.getDirector());

    }

    public static class MovieProgramModel {
        private String programName;
        private String description;
        private List<String> tags;
        private String playCount;
        private String rateStr;
        private float rate;
        private String year;
        private String district;
        private String actor;
        private String director;


        public String getProgramName() {
            return programName;
        }

        public void setProgramName(String programName) {
            this.programName = programName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        public String getPlayCount() {
            return playCount;
        }

        public void setPlayCount(String playCount) {
            this.playCount = playCount;
        }

        public String getRateStr() {
            return rateStr;
        }

        public void setRateStr(String rateStr) {
            this.rateStr = rateStr;
        }

        public float getRate() {
            return rate;
        }

        public void setRate(float rate) {
            this.rate = rate;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getActor() {
            return actor;
        }

        public void setActor(String actor) {
            this.actor = actor;
        }

        public String getDirector() {
            return director;
        }

        public void setDirector(String director) {
            this.director = director;
        }
    }

}