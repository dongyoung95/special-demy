package com.specialdemy.repo;

import com.specialdemy.model.Inquiry;
import com.specialdemy.model.InquiryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    List<Inquiry> findAllByOrderByCreatedAtDesc();

    List<Inquiry> findAllByStatusOrderByCreatedAtDesc(InquiryStatus status);
}
