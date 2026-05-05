package com.specialdemy.web;

import com.specialdemy.model.Inquiry;
import com.specialdemy.model.InquiryStatus;
import com.specialdemy.service.InquiryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminInquiryController {

    private final InquiryService inquiryService;

    public AdminInquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @GetMapping({"", "/"})
    public String adminRoot() {
        return "redirect:/admin/inquiries";
    }

    @GetMapping("/inquiries")
    public String list(@RequestParam(value = "status", required = false) InquiryStatus status, Model model) {
        model.addAttribute("inquiries", inquiryService.findAll(status));
        model.addAttribute("filterStatus", status);
        model.addAttribute("inquiryStatuses", InquiryStatus.values());
        return "admin/inquiries";
    }

    @GetMapping("/inquiries/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Inquiry inquiry = inquiryService.findById(id).orElseThrow();
        model.addAttribute("inquiry", inquiry);
        model.addAttribute("inquiryStatuses", InquiryStatus.values());
        return "admin/inquiry-detail";
    }

    @PostMapping("/inquiries/{id}")
    public String update(
            @PathVariable Long id,
            @RequestParam InquiryStatus status,
            @RequestParam(value = "adminMemo", required = false) String adminMemo,
            RedirectAttributes redirectAttributes) {
        inquiryService.updateAdminFields(id, status, adminMemo);
        redirectAttributes.addFlashAttribute("saved", Boolean.TRUE);
        return "redirect:/admin/inquiries/" + id;
    }
}
