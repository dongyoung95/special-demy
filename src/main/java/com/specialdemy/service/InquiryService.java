package com.specialdemy.service;

import com.specialdemy.model.Inquiry;
import com.specialdemy.model.InquiryStatus;
import com.specialdemy.model.PreferredContact;
import com.specialdemy.repo.InquiryRepository;
import com.specialdemy.web.dto.InquiryForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InquiryService {

    private final InquiryRepository inquiryRepository;

    public InquiryService(InquiryRepository inquiryRepository) {
        this.inquiryRepository = inquiryRepository;
    }

    @Transactional
    public Inquiry save(InquiryForm form) {
        Inquiry inquiry = new Inquiry();
        inquiry.setName(form.getName().trim());
        inquiry.setPhone(blankToNull(form.getPhone()));
        inquiry.setEmail(blankToNull(form.getEmail()));
        inquiry.setPreferredContact(form.getPreferredContact());
        inquiry.setProgram(form.getProgram().trim());
        inquiry.setPreparationStatus(form.getPreparationStatus().trim());
        inquiry.setMessage(form.getMessage().trim());
        inquiry.setStatus(InquiryStatus.NEW);
        return inquiryRepository.save(inquiry);
    }

    public List<Inquiry> findAll(InquiryStatus filter) {
        if (filter == null) {
            return inquiryRepository.findAllByOrderByCreatedAtDesc();
        }
        return inquiryRepository.findAllByStatusOrderByCreatedAtDesc(filter);
    }

    public Optional<Inquiry> findById(Long id) {
        return inquiryRepository.findById(id);
    }

    @Transactional
    public void updateAdminFields(Long id, InquiryStatus status, String adminMemo) {
        Inquiry inquiry = inquiryRepository.findById(id).orElseThrow();
        inquiry.setStatus(status);
        inquiry.setAdminMemo(adminMemo == null ? null : adminMemo.trim());
    }

    private static String blankToNull(String value) {
        if (value == null) {
            return null;
        }
        String t = value.trim();
        return t.isEmpty() ? null : t;
    }
}
