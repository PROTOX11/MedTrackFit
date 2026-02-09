package com.example.medtrackfit.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.medtrackfit.services.DoctorService;
import com.example.medtrackfit.services.SufferingPatientService;
import com.example.medtrackfit.services.HealthMentorService;
import com.example.medtrackfit.services.UniversalUserService;
import com.example.medtrackfit.repositories.RecoveredPatientDoctorConnectionRepository;
import com.example.medtrackfit.repositories.RecoveredPatientMentorConnectionRepository;
import com.example.medtrackfit.entities.RecoveredPatientDoctorConnection;
import com.example.medtrackfit.entities.RecoveredPatientMentorConnection;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import com.example.medtrackfit.entities.Doctor;
import com.example.medtrackfit.entities.SufferingPatient;
import com.example.medtrackfit.entities.HealthMentor;
import com.example.medtrackfit.entities.RecoveredPatient;
import com.medtrackfit.helper.Helper;


@Controller
@RequestMapping("/recoveredpatient")
public class RecoveredPatientController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private SufferingPatientService sufferingPatientService;

    @Autowired
    private HealthMentorService healthMentorService;

    @Autowired
    private UniversalUserService universalUserService;

    @Autowired
    private RecoveredPatientDoctorConnectionRepository doctorConnectionRepo;

    @Autowired
    private RecoveredPatientMentorConnectionRepository mentorConnectionRepo;

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
        return "recoveredpatient/dashboard";
    }

    @GetMapping("/connect_suffering")
    public String connectSuffering(Model model) {
        List<SufferingPatient> sufferingPatients = sufferingPatientService.getAllSufferingPatients();
        model.addAttribute("sufferingPatients", sufferingPatients);
        return "recoveredpatient/connect_suffering";
    }

    @GetMapping("/connect_doctor")
    public String connectDoctor(Model model, Authentication authentication) {
        List<Doctor> doctors = doctorService.getAllDoctors();
        
        // Get current recovered patient ID
        String recoveredPatientId = null;
        if (authentication != null) {
            String email = Helper.getEmailOfLoggedInUser(authentication);
            RecoveredPatient patient = (RecoveredPatient) universalUserService.getUserByEmail(email);
            if (patient != null) {
                recoveredPatientId = patient.getPatientId();
            }
        }
        
        // Get connected doctor IDs
        List<String> connectedDoctorIds = doctorConnectionRepo.findByRecoveredPatientId(recoveredPatientId)
                .stream()
                .map(RecoveredPatientDoctorConnection::getDoctorId)
                .collect(Collectors.toList());
        
        model.addAttribute("doctors", doctors);
        model.addAttribute("connectedDoctorIds", connectedDoctorIds);
        return "recoveredpatient/connect_doctor";
    }

    @PostMapping("/connect_doctor/{doctorId}")
    public String connectDoctor(@PathVariable String doctorId, Authentication authentication) {
        String email = Helper.getEmailOfLoggedInUser(authentication);
        RecoveredPatient patient = (RecoveredPatient) universalUserService.getUserByEmail(email);
        
        if (!doctorConnectionRepo.existsByRecoveredPatientIdAndDoctorId(patient.getPatientId(), doctorId)) {
            RecoveredPatientDoctorConnection connection = RecoveredPatientDoctorConnection.builder()
                    .recoveredPatientId(patient.getPatientId())
                    .doctorId(doctorId)
                    .connectedAt(LocalDateTime.now())
                    .status("ACTIVE")
                    .build();
            doctorConnectionRepo.save(connection);
        }
        
        return "redirect:/recoveredpatient/connect_doctor";
    }

    @PostMapping("/disconnect_doctor/{doctorId}")
    public String disconnectDoctor(@PathVariable String doctorId, Authentication authentication) {
        String email = Helper.getEmailOfLoggedInUser(authentication);
        RecoveredPatient patient = (RecoveredPatient) universalUserService.getUserByEmail(email);
        
        doctorConnectionRepo.deleteByRecoveredPatientIdAndDoctorId(patient.getPatientId(), doctorId);
        
        return "redirect:/recoveredpatient/connect_doctor";
    }


    @GetMapping("/connect_mentor")
    public String connectMentor(Model model, Authentication authentication) {
        List<HealthMentor> healthMentors = healthMentorService.getAllHealthMentors();
        
        // Get current recovered patient ID
        String recoveredPatientId = null;
        if (authentication != null) {
            String email = Helper.getEmailOfLoggedInUser(authentication);
            RecoveredPatient patient = (RecoveredPatient) universalUserService.getUserByEmail(email);
            if (patient != null) {
                recoveredPatientId = patient.getPatientId();
            }
        }
        
        // Get connected mentor IDs
        List<String> connectedMentorIds = mentorConnectionRepo.findByRecoveredPatientId(recoveredPatientId)
                .stream()
                .map(RecoveredPatientMentorConnection::getMentorId)
                .collect(Collectors.toList());
        
        model.addAttribute("healthMentors", healthMentors);
        model.addAttribute("connectedMentorIds", connectedMentorIds);
        return "recoveredpatient/connect_mentor";
    }

    @PostMapping("/connect_mentor/{mentorId}")
    public String connectMentor(@PathVariable String mentorId, Authentication authentication) {
        String email = Helper.getEmailOfLoggedInUser(authentication);
        RecoveredPatient patient = (RecoveredPatient) universalUserService.getUserByEmail(email);
        
        if (!mentorConnectionRepo.existsByRecoveredPatientIdAndMentorId(patient.getPatientId(), mentorId)) {
            RecoveredPatientMentorConnection connection = RecoveredPatientMentorConnection.builder()
                    .recoveredPatientId(patient.getPatientId())
                    .mentorId(mentorId)
                    .connectedAt(LocalDateTime.now())
                    .status("ACTIVE")
                    .build();
            mentorConnectionRepo.save(connection);
        }
        
        return "redirect:/recoveredpatient/connect_mentor";
    }

    @PostMapping("/disconnect_mentor/{mentorId}")
    public String disconnectMentor(@PathVariable String mentorId, Authentication authentication) {
        String email = Helper.getEmailOfLoggedInUser(authentication);
        RecoveredPatient patient = (RecoveredPatient) universalUserService.getUserByEmail(email);
        
        mentorConnectionRepo.deleteByRecoveredPatientIdAndMentorId(patient.getPatientId(), mentorId);
        
        return "redirect:/recoveredpatient/connect_mentor";
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
