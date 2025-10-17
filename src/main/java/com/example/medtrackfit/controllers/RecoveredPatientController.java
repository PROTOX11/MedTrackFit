package com.example.medtrackfit.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.PathVariable;
import com.example.medtrackfit.services.DoctorService;
import com.example.medtrackfit.services.SufferingPatientService;
import com.example.medtrackfit.services.HealthMentorService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.example.medtrackfit.entities.Doctor;
import com.example.medtrackfit.entities.SufferingPatient;
import com.example.medtrackfit.entities.HealthMentor;

@Controller
@RequestMapping("/recoveredpatient")
public class RecoveredPatientController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private SufferingPatientService sufferingPatientService;

    @Autowired
    private HealthMentorService healthMentorService;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "recoveredpatient/dashboard";
    }

    @GetMapping("/connect_suffering")
    public String connectSuffering(Model model) {
        List<SufferingPatient> sufferingPatients = sufferingPatientService.getAllSufferingPatients();
        model.addAttribute("sufferingPatients", sufferingPatients);
        return "recoveredpatient/connect_suffering";
    }

    @GetMapping("/connect_doctor")
    public String connectDoctor(Model model) {
        List<Doctor> doctors = doctorService.getAllDoctors();
        model.addAttribute("doctors", doctors);
        return "recoveredpatient/connect_doctor";
    }

    @GetMapping("/connect_mentor")
    public String connectMentor(Model model) {
        List<HealthMentor> healthMentors = healthMentorService.getAllHealthMentors();
        model.addAttribute("healthMentors", healthMentors);
        return "recoveredpatient/connect_mentor";
    }

    @GetMapping("/connect_doctor/request/{doctorId}")
    public String requestConnectDoctor(@PathVariable String doctorId, Model model, Authentication authentication) {
        // Minimal implementation: in a full app we would create a connection request record and notify the doctor.
        // For now, redirect back to the connect page with a success flag.
        return "redirect:/recoveredpatient/connect_doctor?requested=" + doctorId;
    }

    @GetMapping("/profile")
    public String profile() {
        return "recoveredpatient/profile";
    }

    @GetMapping("/edit-profile")
    public String editProfile() {
        return "recoveredpatient/edit-profile";
    }
}
