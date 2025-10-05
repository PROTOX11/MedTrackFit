package com.example.medtrackfit.services;

import com.example.medtrackfit.entities.Doctor;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface DoctorService extends UserDetailsService {
    Doctor saveDoctor(Doctor doctor);
    Doctor getDoctorByEmail(String email);
    boolean isDoctorExistByEmail(String email);
}
