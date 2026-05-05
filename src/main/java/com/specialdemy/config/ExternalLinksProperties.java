package com.specialdemy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "special-demy.external")
public class ExternalLinksProperties {

    private String blogUrl = "";
    private String instagramUrl = "";
    private String youtubeUrl = "";
    private String contactEmail = "";
    private String kakaoChannelUrl = "https://pf.kakao.com/_KxgiaG";

    public String getBlogUrl() {
        return blogUrl;
    }

    public void setBlogUrl(String blogUrl) {
        this.blogUrl = blogUrl;
    }

    public String getInstagramUrl() {
        return instagramUrl;
    }

    public void setInstagramUrl(String instagramUrl) {
        this.instagramUrl = instagramUrl;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getKakaoChannelUrl() {
        return kakaoChannelUrl;
    }

    public void setKakaoChannelUrl(String kakaoChannelUrl) {
        this.kakaoChannelUrl = kakaoChannelUrl;
    }
}
