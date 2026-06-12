package com.example.medtrackfit.controllers;

import com.example.medtrackfit.entities.Appointment;
import com.example.medtrackfit.repositories.AppointmentRepository;
import com.example.medtrackfit.services.UniversalUserService;
import com.medtrackfit.helper.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UniversalUserService universalUserService;

    @PostMapping("/create")
    public ResponseEntity<?> createAppointment(@RequestBody Map<String, String> body,
                                               Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
        String doctorEmail = Helper.getEmailOfLoggedInUser(authentication);
        String doctorId = universalUserService.getUserId(doctorEmail);
        String doctorName = universalUserService.getUserName(doctorEmail);

        String patientId = body.get("patientId");
        String patientName = body.get("patientName");
        String title = body.get("title");
        String time = body.get("time");
        String date = body.get("date");

        if (patientId == null || title == null || time == null || date == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "All fields are required"));
        }

        Appointment appt = Appointment.builder()
                .patientId(patientId)
                .patientName(patientName != null ? patientName : "Patient")
                .doctorId(doctorId)
                .doctorName(doctorName)
                .title(title)
                .time(time)
                .date(date)
                .build();

        Appointment saved = appointmentRepository.save(appt);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyAppointments(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
        String email = Helper.getEmailOfLoggedInUser(authentication);
        String userId = universalUserService.getUserId(email);
        String role = universalUserService.getUserRole(email);

        List<Appointment> list;
        if ("Doctor".equals(role)) {
            list = appointmentRepository.findByDoctorId(userId);
        } else {
            list = appointmentRepository.findByPatientId(userId);
        }

        return ResponseEntity.ok(list);
    }
}
