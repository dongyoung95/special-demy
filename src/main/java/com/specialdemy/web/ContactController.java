package com.specialdemy.web;

import com.specialdemy.model.PreferredContact;
import com.specialdemy.service.InquiryService;
import com.specialdemy.web.dto.InquiryForm;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContactController {

    private static final String SESSION_CONTACT_THANKS = "contactThanks";

    private final InquiryService inquiryService;

    public ContactController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @InitBinder("inquiryForm")
    public void initInquiryFormBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @ModelAttribute("preferredContacts")
    public PreferredContact[] preferredContacts() {
        return PreferredContact.values();
    }

    @GetMapping("/contact")
    public String contactForm(@ModelAttribute("inquiryForm") InquiryForm inquiryForm) {
        return "contact";
    }

    @PostMapping("/contact")
    public String submit(
            @Valid @ModelAttribute("inquiryForm") InquiryForm inquiryForm,
            BindingResult bindingResult,
            @RequestParam(value = "_trap", required = false) String trap,
            Model model,
            HttpSession session) {
        if (trap != null && !trap.isBlank()) {
            session.setAttribute(SESSION_CONTACT_THANKS, Boolean.TRUE);
            return "redirect:/contact/thanks";
        }
        if (bindingResult.hasErrors()) {
            return "contact";
        }
        inquiryService.save(inquiryForm);
        session.setAttribute(SESSION_CONTACT_THANKS, Boolean.TRUE);
        return "redirect:/contact/thanks";
    }

    @GetMapping("/contact/thanks")
    public String thanks(HttpSession session, Model model) {
        Object flag = session.getAttribute(SESSION_CONTACT_THANKS);
        if (!(flag instanceof Boolean) || !((Boolean) flag)) {
            return "redirect:/contact";
        }
        session.removeAttribute(SESSION_CONTACT_THANKS);
        model.addAttribute("submitted", true);
        return "contact-thanks";
    }
}
