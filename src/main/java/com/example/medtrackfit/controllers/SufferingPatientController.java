package com.example.medtrackfit.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.example.medtrackfit.services.AllBlogPostService;
import com.example.medtrackfit.entities.AllBlogPost;
import com.example.medtrackfit.services.DoctorService;
import com.example.medtrackfit.services.UniversalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.example.medtrackfit.entities.Doctor;
import com.medtrackfit.helper.Helper;

@Controller
@RequestMapping("/suff-pat")
public class SufferingPatientController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private UniversalUserService universalUserService;

    @Autowired
    private AllBlogPostService allBlogPostService;

    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication) {
        if (authentication != null) {
            String username = Helper.getEmailOfLoggedInUser(authentication);
            String userRole = universalUserService.getUserRole(username);
            model.addAttribute("userRole", userRole);
            model.addAttribute("loggedInUser", universalUserService.getUserByEmail(username));
        }
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "suff-pat/dashboard";
    }

    @GetMapping("/connect_recovered")
    public String connectRecovered() {
        return "suff-pat/connect_recovered";
    }

    @GetMapping("/connect_doctor")
    public String connectDoctor(Model model) {
        List<Doctor> doctors = doctorService.getAllDoctors();
        model.addAttribute("doctors", doctors);
        return "suff-pat/connect_doctor";
    }

    @GetMapping("/connect_mentor")
    public String connectMentor() {
        return "suff-pat/connect_mentor";
    }

    @GetMapping("/connect_doctor/request/{doctorId}")
    public String requestConnectDoctor(@PathVariable String doctorId, Model model, Authentication authentication) {
        // Minimal implementation: in a full app we would create a connection request record and notify the doctor.
        // For now, redirect back to the connect page with a success flag.
        return "redirect:/suff-pat/connect_doctor?requested=" + doctorId;
    }

    @GetMapping("/blog")
    public String blog(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // Fetch all public blogs from all user types
        Pageable pageable = PageRequest.of(page, size);
        Page<AllBlogPost> allBlogs = allBlogPostService.findAllPublicBlogs(pageable);

        // Get blog statistics
        long totalPosts = allBlogPostService.countByStatus(AllBlogPost.PostStatus.PUBLISHED);
        long doctorPosts = allBlogPostService.countByAuthorTypeAndStatus(AllBlogPost.AuthorType.DOCTOR, AllBlogPost.PostStatus.PUBLISHED);
        long mentorPosts = allBlogPostService.countByAuthorTypeAndStatus(AllBlogPost.AuthorType.MENTOR, AllBlogPost.PostStatus.PUBLISHED);
        long patientPosts = allBlogPostService.countByAuthorTypeAndStatus(AllBlogPost.AuthorType.RECOVERED_PATIENT, AllBlogPost.PostStatus.PUBLISHED);

        model.addAttribute("allBlogs", allBlogs.getContent());
        model.addAttribute("currentPage", allBlogs.getNumber());
        model.addAttribute("totalPages", allBlogs.getTotalPages());
        model.addAttribute("totalElements", allBlogs.getTotalElements());
        model.addAttribute("totalPosts", totalPosts);
        model.addAttribute("doctorPosts", doctorPosts);
        model.addAttribute("mentorPosts", mentorPosts);
        model.addAttribute("patientPosts", patientPosts);
        model.addAttribute("baseUrl", "/suff-pat/blog");

        return "suff-pat/blog";
    }
}
