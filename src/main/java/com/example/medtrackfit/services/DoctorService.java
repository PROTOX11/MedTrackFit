package com.example.medtrackfit.services;

import com.example.medtrackfit.entities.Doctor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface DoctorService extends UserDetailsService {
    Doctor saveDoctor(Doctor doctor);
    Doctor getDoctorByEmail(String email);
    boolean isDoctorExistByEmail(String email);
    Doctor updateProfilePicture(String doctorId, MultipartFile file) throws IOException;
}
