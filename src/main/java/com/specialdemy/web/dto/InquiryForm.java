package com.specialdemy.web.dto;

import com.specialdemy.model.PreferredContact;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class InquiryForm {

    @NotBlank(message = "이름을 입력해 주세요.")
    @Size(min = 2, max = 100, message = "이름은 2자 이상 100자 이하로 입력해 주세요.")
    private String name;

    @Size(max = 30)
    @Pattern(
            regexp = "^$|^[0-9+\\-\\s()]{8,30}$",
            message = "전화번호는 숫자·하이픈·공백·+·()만 사용하고, 8자 이상 입력해 주세요.")
    private String phone;

    @Size(max = 200)
    @Email(message = "이메일 형식을 확인해 주세요.")
    private String email;

    @NotNull(message = "선호 답변 방식을 선택해 주세요.")
    private PreferredContact preferredContact = PreferredContact.PHONE;

    @NotBlank(message = "희망 과정을 선택해 주세요.")
    @Size(max = 120)
    private String program;

    @NotBlank(message = "현재 준비 상태를 입력해 주세요.")
    @Size(min = 10, max = 4000, message = "현재 준비 상태를 10자 이상으로 적어 주세요. (운동 횟수·기록·일정 등)")
    private String preparationStatus;

    @NotBlank(message = "문의 내용을 입력해 주세요.")
    @Size(min = 15, max = 8000, message = "문의 내용을 15자 이상으로 적어 주세요.")
    private String message;

    @AssertTrue(message = "개인정보 수집에 동의해 주세요.")
    private boolean privacyAgreed;

    @AssertTrue(message = "전화번호 또는 이메일 중 하나는 필수입니다.")
    public boolean isPhoneOrEmailPresent() {
        boolean hasPhone = phone != null && !phone.isBlank();
        boolean hasEmail = email != null && !email.isBlank();
        return hasPhone || hasEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PreferredContact getPreferredContact() {
        return preferredContact;
    }

    public void setPreferredContact(PreferredContact preferredContact) {
        this.preferredContact = preferredContact;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getPreparationStatus() {
        return preparationStatus;
    }

    public void setPreparationStatus(String preparationStatus) {
        this.preparationStatus = preparationStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isPrivacyAgreed() {
        return privacyAgreed;
    }

    public void setPrivacyAgreed(boolean privacyAgreed) {
        this.privacyAgreed = privacyAgreed;
    }
}
