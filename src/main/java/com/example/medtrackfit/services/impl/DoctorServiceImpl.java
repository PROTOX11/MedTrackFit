package com.example.medtrackfit.services.impl;

import com.example.medtrackfit.entities.Doctor;
import com.example.medtrackfit.entities.DoctorPerformance;
import com.medtrackfit.helper.AppConstants;
import com.example.medtrackfit.repositories.DoctorRepository;
import com.example.medtrackfit.services.CloudinaryService;
import com.example.medtrackfit.services.DoctorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CloudinaryService cloudinaryService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    @Transactional
    public Doctor saveDoctor(Doctor doctor) {
        if (doctorRepository.existsByEmail(doctor.getEmail())) {
            throw new IllegalStateException("Doctor with email " + doctor.getEmail() + " already exists");
        }
        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        doctor.setRoleList(List.of(AppConstants.ROLE_DOCTOR));
        logger.info("Encoded password for doctor: {}", doctor.getEmail());

        if (doctor.getDoctorPerformance() == null) {
            DoctorPerformance performance = DoctorPerformance.builder()
                    .doctor(doctor)
                    .patientsHelped(0)
                    .consultationsGiven(0)
                    .satisfactionRating(0.0)
                    .yearsActive(0)
                    .specializationScore(0)
                    .totalPatients(0)
                    .activeCases(0)
                    .recovering(0)
                    .critical(0)
                    .rating(0.0)
                    .build();
            doctor.setDoctorPerformance(performance);
        }
        Doctor savedDoctor = doctorRepository.save(doctor);
        logger.info("Doctor saved with doctorId: {}, has performance: {}",
                savedDoctor.getDoctorId(), savedDoctor.getDoctorPerformance() != null);
        return savedDoctor;
    }

    @Override
    public Doctor getDoctorByEmail(String email) {
        return doctorRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Doctor not found with email: " + email));
    }

    @Override
    public boolean isDoctorExistByEmail(String email) {
        return doctorRepository.existsByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return getDoctorByEmail(email);
    }

    @Override
    @Transactional
    public Doctor updateProfilePicture(String doctorId, MultipartFile file) throws IOException {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        String imageUrl = cloudinaryService.uploadImage(file);
        doctor.setProfilePicture(imageUrl);
        return doctorRepository.save(doctor);
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }
}
