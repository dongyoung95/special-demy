package com.specialdemy.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "special-demy.media-spotlight")
public class MediaSpotlightProperties {

    private String sectionTitle = "미디어에 소개된 특수데미";
    private String sectionLead = "언론·방송 등에서 소개된 유튜브 영상입니다. 아래 재생창에서 바로 볼 수 있습니다.";
    private String emptyHint = "등록된 미디어 영상이 없습니다. `application.yml`의 `special-demy.media-spotlight.videos`에 YouTube 동영상 ID를 추가하세요.";
    private List<MediaSpotlightVideo> videos = new ArrayList<>();

    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    public String getSectionLead() {
        return sectionLead;
    }

    public void setSectionLead(String sectionLead) {
        this.sectionLead = sectionLead;
    }

    public String getEmptyHint() {
        return emptyHint;
    }

    public void setEmptyHint(String emptyHint) {
        this.emptyHint = emptyHint;
    }

    public List<MediaSpotlightVideo> getVideos() {
        return videos;
    }

    public void setVideos(List<MediaSpotlightVideo> videos) {
        this.videos = videos != null ? videos : new ArrayList<>();
    }

    public static class MediaSpotlightVideo {

        private String videoId = "";
        private String title = "";
        private String caption = "";

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId != null ? videoId.trim() : "";
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title != null ? title.trim() : "";
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption != null ? caption.trim() : "";
        }
    }
}
