package com.specialdemy.model;

public enum PreferredContact {
    PHONE,
    EMAIL,
    KAKAO;

    public String getLabel() {
        return switch (this) {
            case PHONE -> "전화";
            case EMAIL -> "이메일";
            case KAKAO -> "카카오톡";
        };
    }
}
