package com.specialdemy.model;

public enum InquiryStatus {
    NEW,
    READ,
    REPLIED,
    ON_HOLD;

    public String getLabelKo() {
        return switch (this) {
            case NEW -> "신규";
            case READ -> "확인";
            case REPLIED -> "답변 완료";
            case ON_HOLD -> "보류";
        };
    }
}
