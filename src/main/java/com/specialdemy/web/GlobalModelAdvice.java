package com.specialdemy.web;

import com.specialdemy.config.ExternalLinksProperties;
import com.specialdemy.config.MediaSpotlightProperties;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAdvice {

    private final ExternalLinksProperties externalLinksProperties;
    private final MediaSpotlightProperties mediaSpotlightProperties;

    public GlobalModelAdvice(
            ExternalLinksProperties externalLinksProperties,
            MediaSpotlightProperties mediaSpotlightProperties) {
        this.externalLinksProperties = externalLinksProperties;
        this.mediaSpotlightProperties = mediaSpotlightProperties;
    }

    @ModelAttribute("links")
    public ExternalLinksProperties externalLinks() {
        return externalLinksProperties;
    }

    @ModelAttribute("mediaSpotlight")
    public MediaSpotlightProperties mediaSpotlight() {
        return mediaSpotlightProperties;
    }

    /**
     * Thymeleaf 3.1+ 에서는 #request 가 기본 제공되지 않아, 앵커 링크는 서버에서 조립합니다.
     */
    @ModelAttribute("homeGalleryUrl")
    public String homeGalleryUrl(HttpServletRequest request) {
        return request.getContextPath() + "/#photo-gallery";
    }
}
